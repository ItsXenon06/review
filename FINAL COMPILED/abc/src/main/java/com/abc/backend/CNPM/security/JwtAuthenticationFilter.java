package com.abc.backend.CNPM.security;

import com.abc.backend.CNPM.security.PhanQuyenSecurity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final PhanQuyenSecurity phanQuyenSecurity;

    @Override
protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
) throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");

    // No Bearer token → let session-based auth handle it normally
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
    }

    String token = authHeader.substring(7);

    if (phanQuyenSecurity.validateToken(token)) {
        String email  = phanQuyenSecurity.layEmail(token);
        String vaiTro = phanQuyenSecurity.layVaiTro(token);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        vaiTro != null
                            ? List.of(new SimpleGrantedAuthority("ROLE_" + vaiTro))
                            : List.of()
                );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
    // If token is present but invalid, fall through — Spring Security will
    // reject the unauthenticated request via the normal 403/redirect mechanism.

    filterChain.doFilter(request, response);
}

}
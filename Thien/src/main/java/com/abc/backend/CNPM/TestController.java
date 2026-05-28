//package com.abc.backend.CNPM;
//
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class TestController {
//
//    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
//    public ResponseEntity<Resource> serveIndexHtml() {
//        // Chỉ định hệ thống đọc file trực tiếp từ thư mục templates
//        Resource resource = new ClassPathResource("static/index.html");
//        return ResponseEntity.ok(resource);
//    }
//}
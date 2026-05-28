// ── DATA ──────────────────────────────────────────────────────────
const MODULES = [
    { key: 'tong-quan', icon: 'ti-layout-dashboard', name: 'Tổng quan',    desc: 'Xem tổng quan hệ thống' },
    { key: 'luu-tru',   icon: 'ti-database',         name: 'Lưu trữ',     desc: 'Quản lý dữ liệu xe, khách hàng...' },
    { key: 'tra-cuu',   icon: 'ti-search',           name: 'Tra cứu',     desc: 'Tra cứu thông tin' },
    { key: 'thong-ke',  icon: 'ti-chart-bar',        name: 'Thống kê',    desc: 'Thống kê, báo cáo' },
    { key: 'tinh-toan', icon: 'ti-calculator',       name: 'Tính toán',   desc: 'Tính toán chi phí, khấu hao' },
    { key: 'hop-dong',  icon: 'ti-file-text',        name: 'Hợp đồng',    desc: 'Quản lý hợp đồng thuê xe' },
    { key: 'khach-hang',icon: 'ti-users',            name: 'Khách hàng',  desc: 'Quản lý khách hàng' },
    { key: 'xe',        icon: 'ti-car',              name: 'Xe',          desc: 'Quản lý xe' },
    { key: 'thanh-toan',icon: 'ti-credit-card',      name: 'Thanh toán',  desc: 'Quản lý thanh toán, hóa đơn' },
    { key: 'thong-bao', icon: 'ti-bell',             name: 'Thông báo',   desc: 'Quản lý thông báo, nhắc nhở' },
    { key: 'cai-dat',   icon: 'ti-settings',         name: 'Cài đặt hệ thống', desc: 'Cài đặt chung' },
    { key: 'phan-quyen',icon: 'ti-shield-lock',      name: 'Phân quyền',  desc: 'Quản lý vai trò, phân quyền' },
];

const ACTIONS = ['xem', 'them', 'sua', 'xoa', 'xuat', 'pq'];
const ACTION_LABELS = { xem: 'Xem', them: 'Thêm', sua: 'Sửa', xoa: 'Xóa', xuat: 'Xuất dữ liệu', pq: 'Phân quyền' };

const ROLES = [
    { id: 1, name: 'Admin',              desc: 'Toàn quyền hệ thống',        icon: 'ti-shield-check', color: '',       count: 1  },
    { id: 2, name: 'Quản lý',            desc: 'Quản lý dùng',               icon: 'ti-user-check',   color: 'orange', count: 3  },
    { id: 3, name: 'Nhân viên kinh doanh', desc: 'Quản lý khách hàng, hợp đồng', icon: 'ti-chart-arrows-vertical', color: 'green', count: 5 },
    { id: 4, name: 'Nhân viên kế toán',  desc: 'Quản lý thanh toán, hóa đơn', icon: 'ti-calculator',  color: 'purple', count: 2 },
    { id: 5, name: 'Nhân viên kỹ thuật', desc: 'Quản lý xe, bảo dưỡng',      icon: 'ti-tools',        color: 'pink',   count: 2  },
    { id: 6, name: 'Nhân viên CSKH',     desc: 'Chăm sóc khách hàng',        icon: 'ti-headset',      color: 'teal',   count: 4  },
    { id: 7, name: 'Khách hàng',         desc: 'Khách hàng thuê xe',          icon: 'ti-user',         color: 'orange', count: 0  },
];

// Permissions store: roleId -> { moduleKey -> { action -> bool } }
const permsStore = {};

// Initialize all-true for Admin, mixed for others
ROLES.forEach(role => {
    permsStore[role.id] = {};
    MODULES.forEach(m => {
        permsStore[role.id][m.key] = {};
        ACTIONS.forEach(a => {
            if (role.id === 1) {
                permsStore[role.id][m.key][a] = true;
            } else if (role.id === 2) {
                // Manager: most perms except pq for some
                permsStore[role.id][m.key][a] = a !== 'pq';
            } else if (role.id === 3) {
                // Sales: limited
                const allowed = ['tong-quan','khach-hang','hop-dong','tra-cuu','thong-bao'];
                permsStore[role.id][m.key][a] = allowed.includes(m.key) && a !== 'xoa' && a !== 'pq' && a !== 'xuat';
            } else if (role.id === 4) {
                // Accountant
                const allowed = ['tong-quan','thanh-toan','tinh-toan','luu-tru','thong-ke','tra-cuu'];
                permsStore[role.id][m.key][a] = allowed.includes(m.key) && a !== 'pq';
            } else if (role.id === 5) {
                // Technician
                const allowed = ['tong-quan','xe','luu-tru','tra-cuu'];
                permsStore[role.id][m.key][a] = allowed.includes(m.key) && (a === 'xem' || a === 'sua');
            } else if (role.id === 6) {
                // CSKH
                const allowed = ['tong-quan','khach-hang','hop-dong','thong-bao','tra-cuu'];
                permsStore[role.id][m.key][a] = allowed.includes(m.key) && a === 'xem';
            } else {
                permsStore[role.id][m.key][a] = m.key === 'tong-quan' && a === 'xem';
            }
        });
    });
});

// ── STATE ──────────────────────────────────────────────────────────
let selectedRoleId = 1;
let currentPage    = 1;
const PAGE_SIZE    = 7;
let filteredRoles  = [...ROLES];
let pendingPerms   = null; // deep copy of current role perms being edited
let ctxRoleId      = null;

// ── INIT ───────────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    renderRoleList();
    selectRole(1);
    document.addEventListener('click', closeCtxMenu);
});

// ── SIDEBAR TOGGLE ─────────────────────────────────────────────────
function toggleSidebar() {
    document.querySelector('.sidebar').classList.toggle('collapsed');
    document.querySelector('.main-wrap').classList.toggle('expanded');
}

// ── TABS ───────────────────────────────────────────────────────────
function switchTab(tab) {
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    document.getElementById('tab-' + tab).classList.add('active');
    document.querySelectorAll('.tab-panel').forEach(p => p.classList.add('hidden'));
    document.getElementById('panel-' + tab).classList.remove('hidden');
}

// ── ROLE LIST RENDER ───────────────────────────────────────────────
function renderRoleList() {
    const start = (currentPage - 1) * PAGE_SIZE;
    const page  = filteredRoles.slice(start, start + PAGE_SIZE);
    const list  = document.getElementById('role-list');

    list.innerHTML = page.map(role => `
        <div class="role-item ${role.id === selectedRoleId ? 'selected' : ''}"
             id="role-item-${role.id}"
             onclick="selectRole(${role.id})">
            <div class="role-avatar ${role.color}">
                <i class="ti ${role.icon}"></i>
            </div>
            <div class="role-info">
                <div class="role-name">${escHtml(role.name)}</div>
                <div class="role-desc">${escHtml(role.desc)}</div>
            </div>
            <span class="role-count">${role.count}</span>
            <button class="role-menu" onclick="openCtxMenu(event, ${role.id})" title="Tùy chọn">
                <i class="ti ti-dots-vertical"></i>
            </button>
        </div>
    `).join('');

    document.getElementById('pg-label').textContent = currentPage;
}

// ── SEARCH ─────────────────────────────────────────────────────────
function filterRoles(query) {
    const q = query.toLowerCase();
    filteredRoles = ROLES.filter(r =>
        r.name.toLowerCase().includes(q) || r.desc.toLowerCase().includes(q)
    );
    currentPage = 1;
    renderRoleList();
}

// ── PAGINATION ─────────────────────────────────────────────────────
function changePage(dir) {
    const maxPage = Math.ceil(filteredRoles.length / PAGE_SIZE);
    currentPage = Math.min(Math.max(1, currentPage + dir), maxPage);
    renderRoleList();
}

// ── SELECT ROLE ────────────────────────────────────────────────────
function selectRole(id) {
    selectedRoleId = id;
    const role = ROLES.find(r => r.id === id);
    pendingPerms = deepClone(permsStore[id]);

    document.getElementById('selected-role-name').textContent = role.name;

    // Show/hide admin alert
    const alert = document.getElementById('admin-alert');
    alert.style.display = id === 1 ? 'flex' : 'none';

    renderPermTable();
    renderRoleList();
    syncColHeaders();
}

// ── PERMISSION TABLE ───────────────────────────────────────────────
function renderPermTable() {
    const tbody = document.getElementById('perm-tbody');
    tbody.innerHTML = MODULES.map(m => `
        <tr>
            <td>
                <div class="module-name">
                    <i class="ti ${m.icon}" style="color:#64748b;font-size:16px"></i>
                    ${escHtml(m.name)}
                </div>
                <div class="module-desc">${escHtml(m.desc)}</div>
            </td>
            ${ACTIONS.map(a => `
                <td>
                    <input type="checkbox"
                           id="cb-${m.key}-${a}"
                           data-module="${m.key}"
                           data-action="${a}"
                           ${pendingPerms[m.key][a] ? 'checked' : ''}
                           onchange="onCellChange('${m.key}','${a}',this.checked)"/>
                </td>
            `).join('')}
        </tr>
    `).join('');

    syncColHeaders();
    syncRowHeaders();
}

function onCellChange(moduleKey, action, checked) {
    pendingPerms[moduleKey][action] = checked;
    syncColHeaders();
    syncRowHeaders();
}

// ── SYNC HEADER CHECKBOXES ─────────────────────────────────────────
function syncColHeaders() {
    ACTIONS.forEach(a => {
        const allChecked = MODULES.every(m => pendingPerms[m.key][a]);
        const el = document.getElementById('col-' + a);
        if (el) el.checked = allChecked;
    });
}

function syncRowHeaders() {
    // No row header checkboxes in current design; reserved for future
}

function toggleCol(action, checked) {
    MODULES.forEach(m => {
        pendingPerms[m.key][action] = checked;
        const cb = document.getElementById(`cb-${m.key}-${action}`);
        if (cb) cb.checked = checked;
    });
}

// ── SAVE / RESET ───────────────────────────────────────────────────
function savePerms() {
    permsStore[selectedRoleId] = deepClone(pendingPerms);
    showToast('Đã lưu phân quyền thành công!', 'success');
}

function resetPerms() {
    pendingPerms = deepClone(permsStore[selectedRoleId]);
    renderPermTable();
    showToast('Đã đặt lại về trạng thái đã lưu.', '');
}

// ── ADD ROLE MODAL ─────────────────────────────────────────────────
function openAddRoleModal() {
    document.getElementById('new-role-name').value = '';
    document.getElementById('new-role-desc').value = '';
    openModal('modal-add-role');
}

function addRole() {
    const name = document.getElementById('new-role-name').value.trim();
    const desc = document.getElementById('new-role-desc').value.trim();
    if (!name) { showToast('Vui lòng nhập tên vai trò.', 'error'); return; }

    const newId = ROLES.length + 1;
    const colors = ['orange', 'green', 'purple', 'pink', 'teal', ''];
    ROLES.push({ id: newId, name, desc: desc || 'Vai trò mới', icon: 'ti-user', color: colors[ROLES.length % colors.length], count: 0 });

    permsStore[newId] = {};
    MODULES.forEach(m => {
        permsStore[newId][m.key] = {};
        ACTIONS.forEach(a => { permsStore[newId][m.key][a] = false; });
    });

    filteredRoles = [...ROLES];
    renderRoleList();
    closeModal('modal-add-role');
    selectRole(newId);
    showToast(`Đã tạo vai trò "${name}".`, 'success');
}

// ── IMPORT MODAL ───────────────────────────────────────────────────
function openImportModal() {
    const sel = document.getElementById('import-source-role');
    sel.innerHTML = ROLES
        .filter(r => r.id !== selectedRoleId)
        .map(r => `<option value="${r.id}">${escHtml(r.name)}</option>`)
        .join('');
    openModal('modal-import');
}

function importPerms() {
    const srcId = parseInt(document.getElementById('import-source-role').value);
    pendingPerms = deepClone(permsStore[srcId]);
    renderPermTable();
    closeModal('modal-import');
    showToast('Đã nhập quyền từ vai trò khác.', 'success');
}

// ── CONTEXT MENU ───────────────────────────────────────────────────
let ctxMenu = null;

function openCtxMenu(e, roleId) {
    e.stopPropagation();
    ctxRoleId = roleId;

    closeCtxMenu();

    ctxMenu = document.createElement('div');
    ctxMenu.className = 'ctx-menu show';
    ctxMenu.innerHTML = `
        <div class="ctx-item" onclick="ctxEdit()"><i class="ti ti-edit"></i> Sửa vai trò</div>
        <div class="ctx-item" onclick="ctxDuplicate()"><i class="ti ti-copy"></i> Nhân bản</div>
        <div class="ctx-item danger" onclick="ctxDelete()"><i class="ti ti-trash"></i> Xóa vai trò</div>
    `;
    document.body.appendChild(ctxMenu);

    const rect = e.currentTarget.getBoundingClientRect();
    ctxMenu.style.top  = rect.bottom + 4 + 'px';
    ctxMenu.style.left = rect.left - 140 + 'px';
}

function closeCtxMenu() {
    if (ctxMenu) { ctxMenu.remove(); ctxMenu = null; }
}

function ctxEdit() {
    closeCtxMenu();
    const role = ROLES.find(r => r.id === ctxRoleId);
    document.getElementById('new-role-name').value = role.name;
    document.getElementById('new-role-desc').value = role.desc;
    openModal('modal-add-role');
    // Override save to update existing
    document.querySelector('#modal-add-role .btn-save').onclick = () => {
        role.name = document.getElementById('new-role-name').value.trim() || role.name;
        role.desc = document.getElementById('new-role-desc').value.trim() || role.desc;
        renderRoleList();
        if (selectedRoleId === ctxRoleId) {
            document.getElementById('selected-role-name').textContent = role.name;
        }
        closeModal('modal-add-role');
        document.querySelector('#modal-add-role .btn-save').onclick = addRole;
        showToast('Đã cập nhật vai trò.', 'success');
    };
}

function ctxDuplicate() {
    closeCtxMenu();
    const src = ROLES.find(r => r.id === ctxRoleId);
    const newId = ROLES.length + 1;
    ROLES.push({ ...src, id: newId, name: src.name + ' (bản sao)', count: 0 });
    permsStore[newId] = deepClone(permsStore[ctxRoleId]);
    filteredRoles = [...ROLES];
    renderRoleList();
    showToast(`Đã nhân bản vai trò "${src.name}".`, 'success');
}

function ctxDelete() {
    closeCtxMenu();
    const role = ROLES.find(r => r.id === ctxRoleId);
    if (role.id === 1) { showToast('Không thể xóa vai trò Admin.', 'error'); return; }
    const idx = ROLES.findIndex(r => r.id === ctxRoleId);
    ROLES.splice(idx, 1);
    delete permsStore[ctxRoleId];
    filteredRoles = ROLES.filter(r => r.name.toLowerCase().includes(document.getElementById('roleSearch').value.toLowerCase()));
    if (selectedRoleId === ctxRoleId) selectRole(ROLES[0].id);
    else renderRoleList();
    showToast(`Đã xóa vai trò "${role.name}".`, 'success');
}

// ── MODAL HELPERS ──────────────────────────────────────────────────
function openModal(id) { document.getElementById(id).classList.remove('hidden'); }
function closeModal(id) { document.getElementById(id).classList.add('hidden'); }

// ── TOAST ──────────────────────────────────────────────────────────
let toastTimer = null;
function showToast(msg, type) {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.className = 'toast' + (type ? ' ' + type : '');
    if (toastTimer) clearTimeout(toastTimer);
    toastTimer = setTimeout(() => t.classList.add('hidden'), 3000);
}

// ── UTILS ──────────────────────────────────────────────────────────
function deepClone(obj) { return JSON.parse(JSON.stringify(obj)); }
function escHtml(str) {
    return String(str)
        .replace(/&/g,'&amp;')
        .replace(/</g,'&lt;')
        .replace(/>/g,'&gt;')
        .replace(/"/g,'&quot;');
}
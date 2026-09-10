if (!localStorage.getItem('sms_token')) {
    window.location.href = 'index.html';
}

UI.renderShell('departments.html', 'Departments');

const isAdmin = localStorage.getItem('sms_role') === 'ADMIN';

if (!isAdmin) {
    $('#add-btn').remove();
}

let allDepartments = [];

const columns = [
    { key: 'code', label: 'Code' },
    { key: 'name', label: 'Name' },
    { key: 'description', label: 'Description' }
];

function rowActions(row) {

    if (!isAdmin) {
        return '';
    }

    return `
<button class="btn btn-small" data-edit="${row.id}">Edit</button>
<button class="btn btn-small btn-danger" data-delete="${row.id}">Delete</button>
    `;
}

function renderList(rows) {

    UI.renderTable('#table-container', columns, rows, rowActions);

    $('[data-edit]').on('click', function () {
        openEditForm($(this).data('edit'));
    });

    $('[data-delete]').on('click', function () {
        handleDelete($(this).data('delete'));
    });
}

function load() {

    DepartmentsApi.getAll()

        .then(rows => {
            allDepartments = rows;
            renderList(rows);
        })

        .catch(err => {
            console.error('Failed to load departments:', err);
            UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to load departments.', 'error');
        });
}

function formHtml(department) {

    department = department || {};

    return `
<div class="form-field">
    <label>Code</label>
    <input type="text" data-field="code" value="${UI.escapeHtml(department.code || '')}" maxlength="20" required>
</div>

<div class="form-field">
    <label>Name</label>
    <input type="text" data-field="name" value="${UI.escapeHtml(department.name || '')}" maxlength="150" required>
</div>

<div class="form-field">
    <label>Description</label>
    <textarea data-field="description">${UI.escapeHtml(department.description || '')}</textarea>
</div>
`;
}

function openCreateForm() {

    UI.openDrawer(
        'New Department',
        formHtml(),
        function (values) {
            return DepartmentsApi.create(values)
                .then(() => {
                    UI.toast('Department created.', 'success');
                    load();
                })
                .catch(err => {
                    throw new Error((err.responseJSON && err.responseJSON.message) || 'Failed to create department.');
                });
        },
        'Create'
    );
}

function openEditForm(id) {

    const department = allDepartments.find(d => d.id === id);

    if (!department) {
        UI.toast('Department not found.', 'error');
        return;
    }

    UI.openDrawer(
        'Edit Department',
        formHtml(department),
        function (values) {
            return DepartmentsApi.update(id, values)
                .then(() => {
                    UI.toast('Department updated.', 'success');
                    load();
                })
                .catch(err => {
                    throw new Error((err.responseJSON && err.responseJSON.message) || 'Failed to update department.');
                });
        },
        'Save changes'
    );
}

function handleDelete(id) {

    const department = allDepartments.find(d => d.id === id);
    const departmentName = department ? department.name : id;

    UI.confirmAction(`Delete department "${departmentName}"? This cannot be undone.`)

        .then(ok => {

            if (!ok) {
                return;
            }

            DepartmentsApi.delete(id)
                .then(() => {
                    UI.toast('Department deleted.', 'success');
                    load();
                })
                .catch(err => {
                    console.error('Failed to delete department:', err);
                    UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to delete department.', 'error');
                });
        });
}

$('#add-btn').on('click', openCreateForm);

$('#search-input').on('input', function () {

    const q = $(this).val().toLowerCase().trim();

    const filtered = allDepartments.filter(d =>
        (d.code || '').toLowerCase().includes(q) ||
        (d.name || '').toLowerCase().includes(q) ||
        (d.description || '').toLowerCase().includes(q)
    );

    renderList(filtered);
});

load();

if (!localStorage.getItem('sms_token')) {
    window.location.href = 'index.html';
}

UI.renderShell('students.html', 'Students');

const isAdmin = localStorage.getItem('sms_role') === 'ADMIN';

if (!isAdmin) {
    $('#add-btn').remove();
}

let allStudents = [];
let allBatches = [];

const columns = [
    { key: 'studentId', label: 'Student ID' },
    { key: 'firstName', label: 'First name' },
    { key: 'lastName', label: 'Last name' },
    { key: 'userEmail', label: 'Email' },
    { key: 'batchName', label: 'Batch' },
    { key: 'phone', label: 'Phone' }
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

    Promise.all([StudentsApi.getAll(), BatchesApi.getAll()])

        .then(([students, batches]) => {
            allStudents = students;
            allBatches = batches;
            renderList(students);
        })

        .catch(err => {
            console.error('Failed to load students:', err);
            UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to load students.', 'error');
        });
}

function batchOptions(selectedId) {

    return allBatches.map(b =>
        `<option value="${b.id}" ${b.id === selectedId ? 'selected' : ''}>${UI.escapeHtml(b.name)}</option>`
    ).join('');
}

function createFormHtml() {

    return `
<div class="form-field">
    <label>First name</label>
    <input type="text" data-field="firstName" required>
</div>

<div class="form-field">
    <label>Last name</label>
    <input type="text" data-field="lastName" required>
</div>

<div class="form-field">
    <label>Email</label>
    <input type="email" data-field="email" required>
    <span class="hint">Used to log in to the student's own account.</span>
</div>

<div class="form-field">
    <label>Password</label>
    <input type="password" data-field="password" minlength="6" required>
    <span class="hint">At least 6 characters.</span>
</div>

<div class="form-field">
    <label>Student ID</label>
    <input type="text" data-field="studentId" required>
</div>

<div class="form-field">
    <label>Batch</label>
    <select data-field="batchId" required>
        <option value="">Select a batch</option>
        ${batchOptions()}
    </select>
</div>

<div class="form-field">
    <label>Enrollment date</label>
    <input type="date" data-field="enrollmentDate" required>
</div>

<div class="form-field">
    <label>Date of birth</label>
    <input type="date" data-field="dateOfBirth">
</div>

<div class="form-field">
    <label>Phone</label>
    <input type="tel" data-field="phone">
</div>

<div class="form-field">
    <label>Address</label>
    <textarea data-field="address"></textarea>
</div>
`;
}

function editFormHtml(student) {

    return `
<div class="form-field">
    <label>Name</label>
    <input type="text" value="${UI.escapeHtml(student.firstName + ' ' + student.lastName)}" disabled>
</div>

<div class="form-field">
    <label>Email</label>
    <input type="text" value="${UI.escapeHtml(student.userEmail || '')}" disabled>
</div>

<div class="form-field">
    <label>Student ID</label>
    <input type="text" data-field="studentId" value="${UI.escapeHtml(student.studentId || '')}" required>
</div>

<div class="form-field">
    <label>Batch</label>
    <select data-field="batchId" required>
        <option value="">Select a batch</option>
        ${batchOptions(student.batchId)}
    </select>
</div>

<div class="form-field">
    <label>Enrollment date</label>
    <input type="date" data-field="enrollmentDate" value="${student.enrollmentDate || ''}" required>
</div>

<div class="form-field">
    <label>Date of birth</label>
    <input type="date" data-field="dateOfBirth" value="${student.dateOfBirth || ''}">
</div>

<div class="form-field">
    <label>Phone</label>
    <input type="tel" data-field="phone" value="${UI.escapeHtml(student.phone || '')}">
</div>

<div class="form-field">
    <label>Address</label>
    <textarea data-field="address">${UI.escapeHtml(student.address || '')}</textarea>
</div>
`;
}

function openCreateForm() {

    if (allBatches.length === 0) {
        UI.toast('Create a batch first.', 'error');
        return;
    }

    UI.openDrawer(
        'New Student',
        createFormHtml(),
        function (values) {

            return AuthApi.register({
                firstName: values.firstName,
                lastName: values.lastName,
                email: values.email,
                password: values.password,
                role: 'STUDENT'
            })

                .then(user => {

                    return StudentsApi.create({
                        studentId: values.studentId,
                        userId: user.id,
                        batchId: values.batchId,
                        enrollmentDate: values.enrollmentDate,
                        dateOfBirth: values.dateOfBirth || null,
                        phone: values.phone,
                        address: values.address
                    });
                })

                .then(() => {
                    UI.toast('Student created.', 'success');
                    load();
                })

                .catch(err => {
                    throw new Error((err.responseJSON && err.responseJSON.message) || 'Failed to create student.');
                });
        },
        'Create'
    );
}

function openEditForm(id) {

    const student = allStudents.find(s => s.id === id);

    if (!student) {
        UI.toast('Student not found.', 'error');
        return;
    }

    UI.openDrawer(
        'Edit Student',
        editFormHtml(student),
        function (values) {

            values.userId = student.userId;
            if (!values.dateOfBirth) values.dateOfBirth = null;

            return StudentsApi.update(id, values)
                .then(() => {
                    UI.toast('Student updated.', 'success');
                    load();
                })
                .catch(err => {
                    throw new Error((err.responseJSON && err.responseJSON.message) || 'Failed to update student.');
                });
        },
        'Save changes'
    );
}

function handleDelete(id) {

    const student = allStudents.find(s => s.id === id);
    const label = student ? student.studentId : id;

    UI.confirmAction(`Delete student "${label}"? This cannot be undone.`)

        .then(ok => {

            if (!ok) {
                return;
            }

            StudentsApi.delete(id)
                .then(() => {
                    UI.toast('Student deleted.', 'success');
                    load();
                })
                .catch(err => {
                    console.error('Failed to delete student:', err);
                    UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to delete student.', 'error');
                });
        });
}

$('#add-btn').on('click', openCreateForm);

$('#search-input').on('input', function () {

    const q = $(this).val().toLowerCase().trim();

    const filtered = allStudents.filter(s =>
        (s.studentId || '').toLowerCase().includes(q) ||
        (s.firstName || '').toLowerCase().includes(q) ||
        (s.lastName || '').toLowerCase().includes(q) ||
        (s.userEmail || '').toLowerCase().includes(q) ||
        (s.batchName || '').toLowerCase().includes(q)
    );

    renderList(filtered);
});

load();

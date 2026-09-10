if (!localStorage.getItem('sms_token')) {
    window.location.href = 'index.html';
}

UI.renderShell('courses.html', 'Courses');

const isAdmin = localStorage.getItem('sms_role') === 'ADMIN';

if (!isAdmin) {
    $('#add-btn').remove();
}

let allCourses = [];
let allDepartments = [];

const columns = [
    { key: 'code', label: 'Code' },
    { key: 'title', label: 'Title' },
    { key: 'departmentName', label: 'Department' },
    { key: 'credits', label: 'Credits' }
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

    Promise.all([CoursesApi.getAll(), DepartmentsApi.getAll()])

        .then(([courses, departments]) => {
            allCourses = courses;
            allDepartments = departments;
            renderList(courses);
        })

        .catch(err => {
            console.error('Failed to load courses:', err);
            UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to load courses.', 'error');
        });
}

function departmentOptions(selectedId) {

    return allDepartments.map(dep =>
        `<option value="${dep.id}" ${dep.id === selectedId ? 'selected' : ''}>${UI.escapeHtml(dep.name)}</option>`
    ).join('');
}

function formHtml(course) {

    course = course || {};

    return `
<div class="form-field">
    <label>Code</label>
    <input type="text" data-field="code" value="${UI.escapeHtml(course.code || '')}" maxlength="20" required>
</div>

<div class="form-field">
    <label>Title</label>
    <input type="text" data-field="title" value="${UI.escapeHtml(course.title || '')}" maxlength="200" required>
</div>

<div class="form-field">
    <label>Description</label>
    <textarea data-field="description">${UI.escapeHtml(course.description || '')}</textarea>
</div>

<div class="form-field">
    <label>Credits</label>
    <input type="number" data-field="credits" min="1" value="${course.credits || ''}" required>
</div>

<div class="form-field">
    <label>Department</label>
    <select data-field="departmentId" required>
        <option value="">Select a department</option>
        ${departmentOptions(course.departmentId)}
    </select>
</div>
`;
}

function openCreateForm() {

    if (allDepartments.length === 0) {
        UI.toast('Create a department first.', 'error');
        return;
    }

    UI.openDrawer(
        'New Course',
        formHtml(),
        function (values) {
            values.credits = Number(values.credits);
            return CoursesApi.create(values)
                .then(() => {
                    UI.toast('Course created.', 'success');
                    load();
                })
                .catch(err => {
                    throw new Error((err.responseJSON && err.responseJSON.message) || 'Failed to create course.');
                });
        },
        'Create'
    );
}

function openEditForm(id) {

    const course = allCourses.find(c => c.id === id);

    if (!course) {
        UI.toast('Course not found.', 'error');
        return;
    }

    UI.openDrawer(
        'Edit Course',
        formHtml(course),
        function (values) {
            values.credits = Number(values.credits);
            return CoursesApi.update(id, values)
                .then(() => {
                    UI.toast('Course updated.', 'success');
                    load();
                })
                .catch(err => {
                    throw new Error((err.responseJSON && err.responseJSON.message) || 'Failed to update course.');
                });
        },
        'Save changes'
    );
}

function handleDelete(id) {

    const course = allCourses.find(c => c.id === id);
    const courseTitle = course ? course.title : id;

    UI.confirmAction(`Delete course "${courseTitle}"? This cannot be undone.`)

        .then(ok => {

            if (!ok) {
                return;
            }

            CoursesApi.delete(id)
                .then(() => {
                    UI.toast('Course deleted.', 'success');
                    load();
                })
                .catch(err => {
                    console.error('Failed to delete course:', err);
                    UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to delete course.', 'error');
                });
        });
}

$('#add-btn').on('click', openCreateForm);

$('#search-input').on('input', function () {

    const q = $(this).val().toLowerCase().trim();

    const filtered = allCourses.filter(c =>
        (c.code || '').toLowerCase().includes(q) ||
        (c.title || '').toLowerCase().includes(q) ||
        (c.departmentName || '').toLowerCase().includes(q)
    );

    renderList(filtered);
});

load();

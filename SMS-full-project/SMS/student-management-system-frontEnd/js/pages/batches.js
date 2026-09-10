if (!localStorage.getItem('sms_token')) {
    window.location.href = 'index.html';
}

UI.renderShell('batches.html', 'Batches');

const isAdmin = localStorage.getItem('sms_role') === 'ADMIN';

if (!isAdmin) {
    $('#add-btn').remove();
}

let allBatches = [];
let allCourses = [];

const columns = [
    { key: 'name', label: 'Name' },
    { key: 'courseTitle', label: 'Course' },
    { key: 'startDate', label: 'Start date' },
    { key: 'endDate', label: 'End date' },
    { key: 'capacity', label: 'Capacity' }
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

    Promise.all([BatchesApi.getAll(), CoursesApi.getAll()])

        .then(([batches, courses]) => {
            allBatches = batches;
            allCourses = courses;
            renderList(batches);
        })

        .catch(err => {
            console.error('Failed to load batches:', err);
            UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to load batches.', 'error');
        });
}

function courseOptions(selectedId) {

    return allCourses.map(c =>
        `<option value="${c.id}" ${c.id === selectedId ? 'selected' : ''}>${UI.escapeHtml(c.title)}</option>`
    ).join('');
}

function formHtml(batch) {

    batch = batch || {};

    return `
<div class="form-field">
    <label>Name</label>
    <input type="text" data-field="name" value="${UI.escapeHtml(batch.name || '')}" required>
</div>

<div class="form-field">
    <label>Start date</label>
    <input type="date" data-field="startDate" value="${batch.startDate || ''}" required>
</div>

<div class="form-field">
    <label>End date</label>
    <input type="date" data-field="endDate" value="${batch.endDate || ''}">
</div>

<div class="form-field">
    <label>Capacity</label>
    <input type="number" data-field="capacity" min="1" value="${batch.capacity || ''}" required>
</div>

<div class="form-field">
    <label>Course</label>
    <select data-field="courseId" required>
        <option value="">Select a course</option>
        ${courseOptions(batch.courseId)}
    </select>
</div>
`;
}

function openCreateForm() {

    if (allCourses.length === 0) {
        UI.toast('Create a course first.', 'error');
        return;
    }

    UI.openDrawer(
        'New Batch',
        formHtml(),
        function (values) {
            values.capacity = Number(values.capacity);
            if (!values.endDate) delete values.endDate;
            return BatchesApi.create(values)
                .then(() => {
                    UI.toast('Batch created.', 'success');
                    load();
                })
                .catch(err => {
                    throw new Error((err.responseJSON && err.responseJSON.message) || 'Failed to create batch.');
                });
        },
        'Create'
    );
}

function openEditForm(id) {

    const batch = allBatches.find(b => b.id === id);

    if (!batch) {
        UI.toast('Batch not found.', 'error');
        return;
    }

    UI.openDrawer(
        'Edit Batch',
        formHtml(batch),
        function (values) {
            values.capacity = Number(values.capacity);
            if (!values.endDate) delete values.endDate;
            return BatchesApi.update(id, values)
                .then(() => {
                    UI.toast('Batch updated.', 'success');
                    load();
                })
                .catch(err => {
                    throw new Error((err.responseJSON && err.responseJSON.message) || 'Failed to update batch.');
                });
        },
        'Save changes'
    );
}

function handleDelete(id) {

    const batch = allBatches.find(b => b.id === id);
    const batchName = batch ? batch.name : id;

    UI.confirmAction(`Delete batch "${batchName}"? This cannot be undone.`)

        .then(ok => {

            if (!ok) {
                return;
            }

            BatchesApi.delete(id)
                .then(() => {
                    UI.toast('Batch deleted.', 'success');
                    load();
                })
                .catch(err => {
                    console.error('Failed to delete batch:', err);
                    UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to delete batch.', 'error');
                });
        });
}

$('#add-btn').on('click', openCreateForm);

$('#search-input').on('input', function () {

    const q = $(this).val().toLowerCase().trim();

    const filtered = allBatches.filter(b =>
        (b.name || '').toLowerCase().includes(q) ||
        (b.courseTitle || '').toLowerCase().includes(q)
    );

    renderList(filtered);
});

load();

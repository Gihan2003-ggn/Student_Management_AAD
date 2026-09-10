if (!localStorage.getItem('sms_token')) {
    window.location.href = 'index.html';
}

UI.renderShell('payments.html', 'Payments');

const isAdmin = localStorage.getItem('sms_role') === 'ADMIN';

if (!isAdmin) {
    $('#add-btn').remove();
}

const STATUS_OPTIONS = ['PENDING', 'PAID', 'OVERDUE', 'CANCELLED'];

let allPayments = [];
let allStudents = [];

const columns = [
    { key: 'receiptNumber', label: 'Receipt #' },
    { key: 'studentIdCode', label: 'Student' },
    { key: 'amount', label: 'Amount', render: row => UI.money(row.amount) },
    { key: 'status', label: 'Status', render: row => UI.badge(row.status) },
    { key: 'paymentDate', label: 'Date' },
    { key: 'paymentMethod', label: 'Method' }
];

function rowActions(row) {

    if (!isAdmin) {
        return '';
    }

    const statusButtons = STATUS_OPTIONS
        .filter(s => s !== row.status)
        .map(s => `<option value="${s}">${s}</option>`)
        .join('');

    return `
<select class="status-select" data-status-for="${row.id}">
    <option value="">Set status...</option>
    ${statusButtons}
</select>
<button class="btn btn-small btn-danger" data-delete="${row.id}">Delete</button>
    `;
}

function renderList(rows) {

    UI.renderTable('#table-container', columns, rows, rowActions);

    $('[data-status-for]').on('change', function () {

        const id = $(this).data('status-for');
        const status = $(this).val();

        if (!status) {
            return;
        }

        PaymentsApi.updateStatus(id, status)
            .then(() => {
                UI.toast('Payment status updated.', 'success');
                load();
            })
            .catch(err => {
                UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to update status.', 'error');
            });
    });

    $('[data-delete]').on('click', function () {
        handleDelete($(this).data('delete'));
    });
}

function load() {

    Promise.all([PaymentsApi.getAll(), StudentsApi.getAll()])

        .then(([payments, students]) => {
            allPayments = payments;
            allStudents = students;
            renderList(payments);
        })

        .catch(err => {
            console.error('Failed to load payments:', err);
            UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to load payments.', 'error');
        });
}

function studentOptions() {

    return allStudents.map(s =>
        `<option value="${s.id}">${UI.escapeHtml(s.studentId + ' - ' + s.firstName + ' ' + s.lastName)}</option>`
    ).join('');
}

function formHtml() {

    return `
<div class="form-field">
    <label>Receipt number</label>
    <input type="text" data-field="receiptNumber" required>
</div>

<div class="form-field">
    <label>Student</label>
    <select data-field="studentId" required>
        <option value="">Select a student</option>
        ${studentOptions()}
    </select>
</div>

<div class="form-field">
    <label>Amount</label>
    <input type="number" data-field="amount" min="0.01" step="0.01" required>
</div>

<div class="form-field">
    <label>Payment method</label>
    <input type="text" data-field="paymentMethod" placeholder="e.g. Cash, Card, Bank Transfer">
</div>
`;
}

function openCreateForm() {

    if (allStudents.length === 0) {
        UI.toast('Create a student first.', 'error');
        return;
    }

    UI.openDrawer(
        'New Payment',
        formHtml(),
        function (values) {
            values.amount = Number(values.amount);
            return PaymentsApi.create(values)
                .then(() => {
                    UI.toast('Payment recorded.', 'success');
                    load();
                })
                .catch(err => {
                    throw new Error((err.responseJSON && err.responseJSON.message) || 'Failed to record payment.');
                });
        },
        'Create'
    );
}

function handleDelete(id) {

    const payment = allPayments.find(p => p.id === id);
    const label = payment ? payment.receiptNumber : id;

    UI.confirmAction(`Delete payment "${label}"? This cannot be undone.`)

        .then(ok => {

            if (!ok) {
                return;
            }

            PaymentsApi.delete(id)
                .then(() => {
                    UI.toast('Payment deleted.', 'success');
                    load();
                })
                .catch(err => {
                    console.error('Failed to delete payment:', err);
                    UI.toast((err.responseJSON && err.responseJSON.message) || 'Failed to delete payment.', 'error');
                });
        });
}

$('#add-btn').on('click', openCreateForm);

$('#search-input').on('input', function () {

    const q = $(this).val().toLowerCase().trim();

    const filtered = allPayments.filter(p =>
        (p.receiptNumber || '').toLowerCase().includes(q) ||
        (p.studentIdCode || '').toLowerCase().includes(q) ||
        (p.status || '').toLowerCase().includes(q)
    );

    renderList(filtered);
});

load();

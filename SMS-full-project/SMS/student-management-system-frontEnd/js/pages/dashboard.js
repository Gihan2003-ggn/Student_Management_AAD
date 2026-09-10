if (!localStorage.getItem('sms_token')) {
    window.location.href = 'index.html';
    throw new Error('Not authenticated');
}

UI.renderShell('dashboard.html', 'Dashboard');

const name = localStorage.getItem('sms_name');
const role = localStorage.getItem('sms_role');

$('#welcome-line').text(
    `Signed in as ${name} (${role}). Here's a quick snapshot of the system.`
);

const cards = [
    { label: 'Departments', api: DepartmentsApi.getAll },
    { label: 'Courses', api: CoursesApi.getAll },
    { label: 'Batches', api: BatchesApi.getAll },
    { label: 'Students', api: StudentsApi.getAll },
    { label: 'Payments', api: PaymentsApi.getAll }
];

Promise.allSettled(
    cards.map(c => c.api())
).then(results => {

    let html = '';

    results.forEach((res, i) => {

        const count =
            res.status === 'fulfilled' && Array.isArray(res.value)
                ? res.value.length
                : '—';

        html += `
            <div class="stat-card">
                <div class="stat-value">${count}</div>
                <div class="stat-label">${cards[i].label}</div>
            </div>
        `;
    });

    $('#stat-grid').html(html);
});

const quickLinks = [
    { label: 'Manage departments', href: 'departments.html' },
    { label: 'Manage courses', href: 'courses.html' },
    { label: 'Manage batches', href: 'batches.html' },
    { label: 'Manage students', href: 'students.html' },
    { label: 'Record a payment', href: 'payments.html' }
];

$('#quick-links').html(
    quickLinks
        .map(l => `<a class="btn" href="${l.href}">${l.label}</a>`)
        .join('')
);

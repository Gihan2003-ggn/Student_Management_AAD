const CoursesApi = {

    getAll: function (departmentId) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/courses' + (departmentId ? ('?departmentId=' + departmentId) : ''),
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    getById: function (id) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/courses/' + id,
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    create: function (data) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/courses',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    update: function (id, data) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/courses/' + id,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(data),
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    delete: function (id) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/courses/' + id,
            type: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    }
};

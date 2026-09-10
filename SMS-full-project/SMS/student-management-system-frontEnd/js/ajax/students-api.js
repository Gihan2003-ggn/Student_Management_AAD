const StudentsApi = {

    getAll: function (batchId) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/students' + (batchId ? ('?batchId=' + batchId) : ''),
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    getById: function (id) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/students/' + id,
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    create: function (data) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/students',
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
            url: CONFIG.API_BASE_URL + '/students/' + id,
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
            url: CONFIG.API_BASE_URL + '/students/' + id,
            type: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    }
};

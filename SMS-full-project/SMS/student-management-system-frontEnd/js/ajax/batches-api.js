const BatchesApi = {

    getAll: function (courseId) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/batches' + (courseId ? ('?courseId=' + courseId) : ''),
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    getById: function (id) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/batches/' + id,
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    create: function (data) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/batches',
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
            url: CONFIG.API_BASE_URL + '/batches/' + id,
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
            url: CONFIG.API_BASE_URL + '/batches/' + id,
            type: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    }
};

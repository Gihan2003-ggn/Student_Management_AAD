const PaymentsApi = {

    getAll: function (studentId) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/payments' + (studentId ? ('?studentId=' + studentId) : ''),
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    getById: function (id) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/payments/' + id,
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    create: function (data) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/payments',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    updateStatus: function (id, status) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/payments/' + id + '/status?status=' + status,
            type: 'PATCH',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    },

    delete: function (id) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/payments/' + id,
            type: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('sms_token')
            }
        });
    }
};

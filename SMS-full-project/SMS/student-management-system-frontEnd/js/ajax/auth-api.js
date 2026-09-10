const AuthApi = {

    login: function (credentials) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/auth/login',
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(credentials)
        });
    },

    register: function (data) {
        return $.ajax({
            url: CONFIG.API_BASE_URL + '/auth/register',
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(data)
        });
    }
};

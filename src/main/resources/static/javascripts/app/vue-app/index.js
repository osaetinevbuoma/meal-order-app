'use strict';

const csrf_header = $('meta[name="_csrf_header"]').attr('content'),
    csrf_token = $('meta[name="_csrf"]').attr('content');
let headers = {};
headers[csrf_header] = csrf_token;

const app = new Vue({
    el: '#app',
    data: {
        is_login: true,
        notification: '',
        user: {},
        form_notif: '',
        alert: ''
    },
    methods: {
        login: function () {
            this.is_login = true;
        },
        register: function () {
            this.is_login = false;
        },
        create_account: function () {
            this.notification = '';
            if (!_.eq(this.user.password, this.user.retype_password)) {
                this.notification = 'Passwords do not match';
                this.user.password = '';
                this.user.retype_password = '';
                return;
            }

            axios.post('/api/developer/register', this.user, { headers: headers })
                .then((res) => {
                    this.alert = 'alert-success';
                    this.form_notif = 'Log into your account';
                    this.is_login = true;
                    this.user = {};
                })
                .catch((err) => {
                    this.alert = 'alert-danger';
                    this.form_notif = err.response.data;
                    this.user.password = '';
                    this.user.retype_password = '';
                });
        }
    }
});
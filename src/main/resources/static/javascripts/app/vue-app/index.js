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
        user: {
            first_name: '',
            last_name: '',
            email: '',
            password: '',
            retype_password: ''
        }
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
                return;
            }

            axios.post('', this.user, { headers: headers })
                .then((res) => {
                    console.log(res);
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    }
});
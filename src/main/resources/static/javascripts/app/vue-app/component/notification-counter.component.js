'use strict';

const csrf_header = $('meta[name="_csrf_header"]').attr('content'),
    csrf_token = $('meta[name="_csrf"]').attr('content');
let headers = {};
headers[csrf_header] = csrf_token;

const API_URL = '/api/vendor';

Vue.component('notification-counter', {
    data: function () {
        return {
            count: 0
        }
    },
    props: {
        cart_counter: Boolean
    },
    template: `
        <button class="sidebar-trigger" title="Notification"
                onclick="window.location = '/vendor/orders'">
            <span class="ion-android-notifications"></span>
            <sup class="cart-count" v-if="count > 0">
                {{ count }}</sup>
        </button>
    `,
    mounted: function() {
        this.getOrders();
    },
    methods: {
        getOrders: function () {
            axios.get(API_URL + '/orders', { headers: headers })
                .then((res) => this.count = res.data.length)
                .catch((err) => console.log(err));
        }
    },
    watch: {
        cart_counter: function (value, oldValue) {
            if (value) this.getOrders();
        }
    }
});
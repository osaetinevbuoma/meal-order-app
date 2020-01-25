'use strict';

const app = new Vue({
    el: '#app',
    data: {
        orders: [],
        order: {},
        paymentOptions: [],
        deliveryOptions: [],
        cartCounterIncrement: false
    },
    mounted: function () {
        return this.getOrders();
    },
    methods: {
        getOrders: function () {
            axios.get(API_URL + '/order', { headers: headers })
                .then((res) => {
                    console.log(res.data)
                    this.paymentOptions = res.data.payment;
                    this.deliveryOptions = res.data.delivery;
                })
                .catch((err) => console.log(err));
        }
    }
});
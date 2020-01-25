'use strict';

const app = new Vue({
    el: '#app',
    data: {
        meal: {},
        cartCounterIncrement: false,
        show: false,
        quantity: 1,
        notificationMessage: ''
    },
    mounted: function () {
        this.getMeal();
    },
    methods: {
        /**
         * Get meal detail
         */
        getMeal: function () {
            const id = _.split(window.location.pathname, '/')[2];
            axios.get(API_URL + '/meal/' + parseInt(id), { headers: headers })
                .then((res) => this.meal = res.data)
                .catch((err) => console.log(err));
        },
        /**
         * Add meal to cart
         * @param meal
         */
        addToCart: function (meal) {
            this.notificationMessage = '';
            this.cartCounterIncrement = false;

            const cart = {
                quantity: this.quantity,
                meal: meal
            };

            axios.post(API_URL + '/cart/add', cart, { headers: headers })
                .then((res) => {
                    this.cartCounterIncrement = true;
                    this.notificationMessage = 'Added to Cart <i class="ion-checkmark"></i>';
                    this.show = true;
                    setTimeout(() => {
                        this.notificationMessage = '';
                        this.show = false;
                    }, 1500);
                })
                .catch((err) => {
                    if (err.response.status === 409) {
                        this.notificationMessage = err.response.data;
                        this.show = true;
                        setTimeout(() => {
                            this.notificationMessage = '';
                            this.show = false;
                        }, 1500);
                    }
                });
        },
        /**
         * Format prices for better display in view
         * @param price         price of product to format
         * @returns {string}    formatted price
         */
        formatPrice: function (price) {
            return '₦' + price.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g,
                '$1,');
        },
    }
});
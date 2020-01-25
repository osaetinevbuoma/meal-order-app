'use strict';

const app = new Vue({
    el: '#app',
    data: {
        cartItems: [],
        show: false,
        notification_message: '',
        cartCounterIncrement: false,
    },
    mounted: function () {
        this.getCartItems();
    },
    methods: {
        getCartItems: function () {
            axios.get(API_URL + '/cart', { headers: headers })
                .then((res) => this.cartItems = res.data)
                .catch((err) => console.log(err));
        },
        /**
         * Remove an item from cart.
         * @param index         the index of the product to remove from cart array
         * @param delete_all    boolean determining whether to remove all items from cart
         */
        removeFromCart: function (index, delete_all) {
            this.cartCounterIncrement = false;

            if (delete_all) {
                axios.delete(API_URL + '/cart/delete/all', { headers: headers })
                    .then((res) => {
                        this.cartItems = [];
                        this.cartCounterIncrement = true;
                    })
                    .catch((err) => console.log(err));
            } else {
                let cart = this.cartItems[index];
                axios.delete(API_URL + '/cart/delete/' + cart.id, { headers: headers })
                    .then((res) => {
                        this.cartItems.splice(index, 1);
                        this.cartCounterIncrement = true;
                    })
                    .catch((err) => console.log(err));
            }
        },
        /**
         * Compute sub total price of items in cart
         * @param price         price of product
         * @param quantity      quantity of product item
         * @returns {number}    formatted sub total price
         */
        computeSubTotal: function (price, quantity) {
            return price * quantity;
        },
        /**
         * Compute grand total cost of items in cart.
         * @returns {string}    formatted total of grand total cost
         */
        computeTotal: function () {
            let total = 0;
            for (let i = 0; i < this.cartItems.length; i++) {
                total += (this.cartItems[i].meal.price * this.cartItems[i].quantity);
            }

            return this.formatPrice(total);
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
        /**
         * Update quantity of products in customer's cart.
         */
        updateCart: function () {
            console.log(this.cartItems);
            axios.put(API_URL + '/cart/update', this.cartItems, { headers: headers })
                .then((res) => {
                    this.notification_message = 'Saved <i class="ion-checkmark"></i>';
                    this.show = true;
                    setTimeout(() => {
                        this.notification_message = '';
                        this.show = false;
                    }, 1500);
                })
                .catch((error) => console.log(error));
        }
    }
});
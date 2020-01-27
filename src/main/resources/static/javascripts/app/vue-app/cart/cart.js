'use strict';

const app = new Vue({
    el: '#app',
    data: {
        cartItems: [],
        paymentOptions: [],
        paymentOption: {},
        cardPaymentSelected: false,
        cardPaymentDiscount: 0,
        deliveryOptions: [],
        deliveryOption: {},
        deliveryCost: 0,
        officeDeliverySelected: false,
        distanceToOffice: 200, // assumption is that this is a fixed value
        show: false,
        notification_message: '',
        cartCounterIncrement: false,
        developerEmail: '',
        totalCost: 0,
        placeOrderLabel: 'Place Order'
    },
    mounted: function () {
        this.getCartItems();
    },
    methods: {
        getCartItems: function () {
            axios.get(API_URL + '/cart', { headers: headers })
                .then((res) => {
                    this.cartItems = res.data.cartItems;
                    this.paymentOptions = res.data.paymentOptions;
                    this.deliveryOptions = res.data.deliveryOptions;
                    this.developerEmail = res.data.developerEmail;

                    this.paymentOption = _.find(this.paymentOptions, { option: 'Pay On Delivery' });
                    this.deliveryOption = _.find(this.deliveryOptions, { type: 'Pick Up' });
                })
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
         * @returns {string}    formatted sub total price
         */
        computeSubTotal: function (price, quantity) {
            return this.formatPrice(price * quantity);
        },
        /**
         * Compute grand total cost of items in cart plus payment and delivery choices.
         * @returns {string}    formatted total of grand total cost
         */
        computeTotal: function () {
            let totalCost = 0;
            for (let i = 0; i < this.cartItems.length; i++) {
                totalCost += (this.cartItems[i].meal.price * this.cartItems[i].quantity);
            }

            if (this.cardPaymentSelected) {
                this.cardPaymentDiscount = (this.paymentOption.discount / 100) * totalCost;
                totalCost -= this.cardPaymentDiscount;
            }

            if (this.officeDeliverySelected) {
                this.deliveryCost = this.deliveryOption.amount * this.distanceToOffice;
                totalCost += this.deliveryCost;
            }

            this.totalCost = totalCost;

            return this.formatPrice(totalCost);
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
        },
        /**
         * Select payment option.
         * @param option
         */
        selectPaymentOption: function (option) {
            this.paymentOption = option;

            if (option.option === 'Card Payment') {
                this.cardPaymentSelected = true;
            } else {
                this.cardPaymentSelected = false;
                this.cardPaymentDiscount = 0;
            }
        },
        /**
         * Select delivery option
         * @param option
         */
        selectDeliveryOption: function (option) {
            this.deliveryOption = option;

            if (option.type === 'Office Delivery') {
                this.officeDeliverySelected = true;
            } else {
                this.officeDeliverySelected = false;
                this.deliveryCost = 0;
            }
        },
        /**
         * Place order and make payment
         */
        placeOrder: function () {
            let reference = Math.floor((Math.random() * 1000000000) + 1);
            let order = {
                isPlacedNow: true,
                isDispatched: false,
                isPaid: this.paymentOption.option === 'Card Payment',
                paymentOption: this.paymentOption,
                deliveryType: this.deliveryOption,
                reference: reference
            };
            let orderedMeals = [];
            for (let i = 0; i < this.cartItems.length; i++) {
                let orderedMeal = {
                    name: this.cartItems[i].meal.name,
                    price: this.cartItems[i].meal.price,
                    quantity: this.cartItems[i].quantity,
                    meal: this.cartItems[i].meal
                };

                orderedMeals.push(orderedMeal);
            }

            order.orderedMeals = orderedMeals;

            this.placeOrderLabel = '<em>Processing Order...</em>';

            if (this.paymentOption.option === 'Card Payment') {
                // Use Paystack to make payment
                let paymentHandler = PaystackPop.setup({
                    key: 'pk_test_11e5abfb56cdb46ed86516ce2620a87119819546',
                    email: this.developerEmail,
                    amount: parseInt(this.totalCost) * 100,
                    currency: 'NGN',
                    ref: reference,
                    callback: (response) => {
                        axios.post(API_URL + '/order/save', order, { headers: headers })
                            .then((res) => window.location = '/order/confirmed')
                            .catch((err) => {
                                console.log(err);
                                // in case connection to mail server fails on localhost
                                window.location = '/order/confirmed';
                            });
                    }
                });

                paymentHandler.openIframe();
            } else {
                axios.post(API_URL + '/order/save', order, { headers: headers })
                    .then((res) => window.location = '/order/confirmed')
                    .catch((err) => {
                        console.log(err)
                        // in case connection to mail server fails on localhost
                        window.location = '/order/confirmed';
                    });
            }
        }
    }
});
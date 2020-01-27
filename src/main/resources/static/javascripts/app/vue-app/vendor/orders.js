'use strict';

const app = new Vue({
    el: '#app',
    data: {
        orders: [],
        beforeFilter: [],
        orderDetails: [],
        selectedIndex: -1,
        selectedOrder: {},
        totalCost: '',
        paymentOptions: [],
        filteredPaymentOption: '',
        deliveryTypes: [],
        filteredDeliveryOption: '',
        distanceToOffice: 200,
        notification_message: '',
        mealFulfilled: false
    },
    mounted: function () {
        this.getOrders();
    },
    methods: {
        getOrders: function () {
            axios.all([this.listOrders(), this.listPaymentOptions(), this.listDeliveryTypes()])
                .then(axios.spread((orders, paymentOptions, deliveryTypes) => {
                    this.orders = orders.data;
                    this.beforeFilter = orders.data;
                    this.paymentOptions = paymentOptions.data;
                    this.deliveryTypes = deliveryTypes.data;
                }))
                .catch((err) => console.log(err));
        },
        /**
         * List all orders that have been placed.
         * @returns {*}
         */
        listOrders: function() {
            return axios.get(API_URL + '/orders', { headers: headers });
        },
        /**
         * Get details of order when an order is selected
         * @param order     the order to get details
         * @param index     index of array used to highlight selected order
         */
        getOrderDetails: function (order, index) {
            this.selectedIndex = index;
            this.selectedOrder = order;
            this.notification_message = '';

            axios.get(API_URL + '/order/' + order.id, { headers: headers })
                .then((res) => {
                    this.orderDetails = res.data;
                    this.totalCost = this.computeTotal(order);
                })
                .catch((err) => console.log(err));
        },
        /**
         * List payment options
         * @returns {*}
         */
        listPaymentOptions: function () {
            return axios.get(API_URL + '/payment-options', { headers: headers });
        },
        /**
         * List delivery types
         * @returns {*}
         */
        listDeliveryTypes: function () {
            return axios.get(API_URL + '/delivery-types', { headers: headers });
        },
        /**
         * Filter by payment option.
         * @param option    the option to use as predicate
         * @param dom_id    dom id is needed for reset the filter to its original state
         */
        filterByPaymentOption: function (option, dom_id) {
            this.filteredPaymentOption = dom_id;
            this.filteredDeliveryOption = '';
            this.orders = this.beforeFilter;
            this.orders = _.filter(this.orders, { paymentOption: option });
        },
        /**
         * Filter by delivery type.
         * @param type      the type to use as predicate
         * @param dom_id    dom id is needed for reset the filter to its original state
         */
        filterByDeliveryType: function (type, dom_id) {
            this.filteredPaymentOption = '';
            this.filteredDeliveryOption = dom_id;
            this.orders = this.beforeFilter;
            this.orders = _.filter(this.orders, { deliveryType: type });
        },
        /**
         * Reset filter to its original state.
         */
        resetFilter: function () {
            this.filteredPaymentOption = '';
            this.filteredDeliveryOption = '';
            this.orders = this.beforeFilter;
        },
        /**
         * Compute total cost of order.
         * @param order
         * @returns {string}
         */
        computeTotal: function (order) {
            let totalCost = 0;
            for (let i = 0; i < this.orderDetails.length; i++) {
                totalCost += (this.orderDetails[i].meal.price * this.orderDetails[i].quantity);
            }

            if (order.paymentOption.option === 'Card Payment') {
                totalCost -= (order.paymentOption.discount / 100) * totalCost;
            }

            if (order.deliveryType.type === 'Office Delivery') {
                this.deliveryCost = order.deliveryType.amount * this.distanceToOffice;
                totalCost += this.deliveryCost;
            }

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
         * Fulfil meal order
         */
        fulfilOrder: function () {
            this.notification_message = '';

            if (!this.selectedOrder.isPaid) {
                this.notification_message = 'Check the box if developer has paid before you can' +
                    ' fulfil order.';
                return;
            }

            axios.put(API_URL + '/fulfil-order', this.selectedOrder, { headers: headers })
                .then((res) => {
                    this.mealFulfilled = true;
                    this.notification_message = res.data + ' <i class="ion-checkmark"></i>';
                    setTimeout(() => {
                        this.notification_message = '';
                        this.mealFulfilled = false;
                    }, 1500);
                })
                .catch((err) => console.log(err));
        },
        markAsPaid: function () {
            this.selectedOrder.isPaid = true;
        }
    }
});
'use strict';

Vue.component('format-price', {
    data: function () {
        return {
            formattedPrice: ''
        }
    },
    props: {
        price: Number
    },
    template: `<span>{{ formattedPrice }}</span>`,
    mounted: function () {
        this.formatPrice();
    },
    methods: {
        formatPrice: function () {
            this.formattedPrice = '₦' + this.price.toFixed(2)
                .replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
        }
    }
});
'use strict';

const app = new Vue({
    el: '#app',
    data: {
        meals: {},
        pageNumbers: [],
        cartCounterIncrement: false
    },
    mounted: function () {
        this.getMeals();
    },
    methods: {
        /**
         * Get first page list of meals
         */
        getMeals: function () {
            axios.get(API_URL + '/meals', { headers: headers })
                .then((res) => {
                    this.meals = res.data.meals;
                    this.pageNumbers = res.data.pageNumbers;
                })
                .catch((err) => console.log(err));
        },
        /**
         * Fetch more meals depending on the page number
         * @param page
         */
        fetchMeals: function (page) {
            axios.get(API_URL + '/meals', { params: { page: page } }, { headers: headers })
                .then((res) => {
                    this.meals = res.data.meals;
                    this.pageNumbers = res.data.pageNumbers;
                    this.cartCounterIncrement = false;
                })
                .catch((err) => console.log(err));
        },
        /**
         * Add meal to cart
         * @param meal
         */
        addToCart: function (meal) {
            const cart = {
                quantity: 1,
                meal: meal
            };

            axios.post(API_URL + '/cart/add', cart, { headers: headers })
                .then((res) => this.cartCounterIncrement = true)
                .catch((err) => console.log(err));
        }
    }
});
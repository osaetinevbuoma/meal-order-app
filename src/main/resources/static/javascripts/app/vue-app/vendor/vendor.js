'use strict';

const app = new Vue({
    el: '#app',
    data: {
        meals: [],
        meal: {},
        mealFulfilled: false,
        pageNumbers: [],
    },
    mounted: function () {
        this.getMeals();
    },
    methods: {
        /**
         * Get meals
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
                    this.pageNumbers = res.data.pageNumbers
                })
                .catch((err) => console.log(err));
        },
        /**
         * Determine save operation type
         */
        saveMeal: function () {
            if (this.meal.id !== undefined) this.updateMeal();
            else this.addMeal();
        },
        /**
         * Add a new meal
         */
        addMeal: function () {
            axios.post(API_URL + '/meal/add', this.meal, { headers: headers })
                .then((res) => {
                    this.meals.content.push(res.data);
                    this.meal = {};
                })
                .catch((err) => console.log(err));
        },
        /**
         * Clear meal form
         */
        clearForm: function () {
            this.meal = {};
        },
        /**
         * Display meal information in edit field
         * @param id id of meal
         */
        editMeal: function (id) {
            this.meal = _.find(this.meals.content, { 'id': id });
        },
        /**
         * Update meal
         */
        updateMeal: function () {
            axios.put(API_URL + '/meal/update', this.meal, { headers: headers })
                .then((res) => {
                    let meal_index = _.findIndex(this.meals.content, { 'id': this.meal.id });
                    let first_half_arr = _.slice(this.meals.content, 0, meal_index);
                    let second_half_arr = _.slice(this.meals.content, meal_index+1);
                    first_half_arr.push(this.meal);
                    this.meals.content = _.concat(first_half_arr, second_half_arr);
                    this.meal = {};
                })
                .catch((err) => console.log(err));
        }
    }
});
# Meal Order
A simple app that mimics management of meals and placing orders for 
them by other users. There are two user groups: `Vendor` who uses the app to show a
collection of meals s/he sells and `User` who goes through the collection and makes 
an order.

## Tech Stack
* [Spring Framework](https://spring.io/)
* [Vue.js](https://vuejs.org)
* [H2 Database](https://www.h2database.com)

### Third party services
* Paystack
* Mailgun

## Download
Clone the repository `git clone https://github.com/osaetinevbuoma/meal-order-app.git`.

## Installation 
### Pre-Installation
Before running the app a few configurations need to be taken care of:
* The app is set to create an embedded database when it starts up and destroys it
when the server is shut down. You can change this setting to keep the database
even when the app shuts down by change the configuration `spring.jpa.hibernate.ddl-auto`
from `create-drop` to `update` which is located in the `src/main/resources/application.properties`
file.
* The app initializes the database with some data when it starts up. It also creates an account for
a `Vendor`. You can change the email address of the vendor to your preferred email
address to test the order notifications received when a meal order is placed. The
initialization is located in `src/main/java/com/modnsolutions/project/DBInit.java`.
Replace the `firstName`, `lastName`, `email` and `password` in the `vendor()` method 
with your preferred parameters.
* Database initialization can be disabled by changing the parameter of `db.init` in
`src/main/resources/application.properties` from `true` to `false`.
* **Note**: The app uses Mailgun settings to send emails. Create an account [here](http://www.mailgun.com)
if you do not already have one. In `application.properties`, input the values of your email account
username and password in `spring.mail.username` and `spring.mail.password` respectively.

### Run The App
* In the root directory, start the app with `./gradlew bootRun`.
* Once the server is booted up and initialization is complete, point your browser
to `http://localhost:8080`.

### Demo
You can access a demo online, [here](https://byteworks-meals.herokuapp.com/). Use 
the email `osaetinevbuoma@gmail.com` and password `123` to sign in as a vendor. 
You can create any `User` account using the registration form.
 
Or download and watch a [demo video](/assets/Meal_Order_Demo.mp4).

### Assets Included
* Demo Video: [here](/assets/Meal_Order_Demo.mp4)
* API Documentation: [here](/assets/API_Documentation.html)
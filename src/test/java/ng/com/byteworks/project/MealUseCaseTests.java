package ng.com.byteworks.project;

import ng.com.byteworks.project.db.entity.Meal;
import ng.com.byteworks.project.db.repository.*;
import ng.com.byteworks.project.security.WithMockDeveloper;
import ng.com.byteworks.project.security.WithMockVendor;
import ng.com.byteworks.project.service.AuthenticationService;
import ng.com.byteworks.project.service.MealService;
import ng.com.byteworks.project.service.UtilService;
import ng.com.byteworks.project.service.VendorService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration
@WithMockDeveloper
@WithMockVendor
public class MealUseCaseTests {
    @Autowired
    DeliveryTypeRepository deliveryTypeRepository;

    @Autowired
    MealRepository mealRepository;

    @Autowired
    MealOrderRepository mealOrderRepository;

    @Autowired
    OrderedMealRepository orderedMealRepository;

    @Autowired PaymentOptionRepository paymentOptionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;



    private AuthenticationService authenticationService;
    private MealService mealService;
    private Utilities utilities;
    private UtilService utilService;
    private VendorService vendorService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository);
        utilService = new UtilService();
        utilities = new Utilities(entityManager);
        utilities.createMeal();
        vendorService = new VendorService(deliveryTypeRepository, mealRepository,
                mealOrderRepository, orderedMealRepository, paymentOptionRepository, utilService);
    }

    @Test
    void testMealListForVendor() {
        List<Map<String, Object>> meals = vendorService.listMeals();
        Assertions.assertThat(meals.size()).isGreaterThan(0);
    }

    @Test
    void testAddingMealByVendor() {
        Meal meal = new Meal(Utilities.MEAL_NAME, Utilities.MEAL_IMAGE,
                Utilities.MEAL_PRICE + 20, Utilities.MEAL_DESCRIPTION, false);
        Map<String, Object> mealObj = vendorService.addMeal(meal);
        Assertions.assertThat(meal.getName()).isEqualTo(mealObj.get("name"));
    }

    @Test
    void testGetMealOperationByVendor() {
        List<Map<String, Object>> meals = vendorService.listMeals();
        Map<String, Object> meal = vendorService.getMeal((int) meals.get(0).get("id"));
        Assertions.assertThat(meal.get("name")).isEqualTo(meals.get(0).get("name"));
    }

    @Test
    void testUpdateMealOperationByVendor() {
        List<Map<String, Object>> meals = vendorService.listMeals();
        Optional<Meal> meal = mealRepository.findById((int) meals.get(0).get("id"));
        Assertions.assertThat(meal).isPresent();

        meal.get().setName("Another Name");
        meal.get().setImage("Another Image");
        meal.get().setPrice(10000D);
        meal.get().setDescription("Some description");
        meal.get().setIsAvailable(true);

        Map<String, Object> updatedMeal = vendorService.updateMeal(meal.get());
        Assertions.assertThat(updatedMeal.get("id")).isEqualTo(meal.get().getId());
        Assertions.assertThat(updatedMeal.get("name")).isEqualTo(meal.get().getName());
        Assertions.assertThat(updatedMeal.get("image")).isEqualTo(meal.get().getImage());
        Assertions.assertThat(updatedMeal.get("price")).isEqualTo(meal.get().getPrice());
        Assertions.assertThat(updatedMeal.get("description")).isEqualTo(meal.get().getDescription());
        Assertions.assertThat(updatedMeal.get("isAvailable")).isEqualTo(meal.get().getIsAvailable());
    }
}

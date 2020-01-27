package ng.com.byteworks.project.service;

import ng.com.byteworks.project.db.entity.*;
import ng.com.byteworks.project.db.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Secured("ROLE_VENDOR")
public class VendorService {
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final MealRepository mealRepository;
    private final MealOrderRepository mealOrderRepository;
    private final OrderedMealRepository orderedMealRepository;
    private final PaymentOptionRepository paymentOptionRepository;
    private final UtilService utilService;

    public VendorService(DeliveryTypeRepository deliveryTypeRepository,
                         MealRepository mealRepository, MealOrderRepository mealOrderRepository,
                         OrderedMealRepository orderedMealRepository,
                         PaymentOptionRepository paymentOptionRepository, UtilService utilService) {
        this.deliveryTypeRepository = deliveryTypeRepository;
        this.mealRepository = mealRepository;
        this.mealOrderRepository = mealOrderRepository;
        this.orderedMealRepository = orderedMealRepository;
        this.paymentOptionRepository = paymentOptionRepository;
        this.utilService = utilService;
    }

    public List<Map<String, Object>> listMeals() {
        List<Map<String, Object>> mealList = new ArrayList<>();
        List<Meal> meals = mealRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        meals.forEach(meal -> mealList.add(utilService.generateMealMap(meal)));

        return mealList;
    }

    public Map<String, Object> addMeal(Meal meal) {
        String image = "/food/food_" + utilService.generateRandomNumber(1, 12) + ".jpg";
        meal.setImage(image);
        meal.setIsAvailable(meal.getIsAvailable() != null);

        mealRepository.save(meal);

        return utilService.generateMealMap(meal);
    }

    public Map<String, Object> getMeal(int id) {
        Optional<Meal> meal = mealRepository.findById(id);
        return meal.map(utilService::generateMealMap).orElse(null);
    }

    public Map<String, Object> updateMeal(Meal updatedMeal) {
        Optional<Meal> meal = mealRepository.findById(updatedMeal.getId());
        if (!meal.isPresent()) return null;

        meal.get().setName(updatedMeal.getName());
        meal.get().setImage(updatedMeal.getImage());
        meal.get().setDescription(updatedMeal.getDescription());
        meal.get().setIsAvailable(updatedMeal.getIsAvailable());
        mealRepository.save(meal.get());

        return utilService.generateMealMap(meal.get());
    }

    /**
     * List orders made by developers
     * @return a list of orders
     */
    public List<Map<String, Object>> listOrders() {
        List<Map<String, Object>> orders = new ArrayList<>();
        List<MealOrder> mealOrders = mealOrderRepository.findAllByIsPlacedNowIsTrue();
        mealOrders.forEach(mealOrder -> orders.add(utilService.generateMealOrderMap(mealOrder)));

        return orders;
    }

    /**
     * Get details of an order. These refer to meal information.
     * @param id    id of order
     * @return list of meals
     */
    public List<Map<String, Object>> getOrderDetails(int id) {
        List<Map<String, Object>> orderDetails = new ArrayList<>();

        Optional<MealOrder> mealOrder = mealOrderRepository.findById(id);
        if (!mealOrder.isPresent()) return null;

        List<OrderedMeal> orderedMeals = orderedMealRepository.findAllByMealOrder(mealOrder.get());
        orderedMeals.forEach(orderedMeal -> orderDetails.add(utilService.generateOrderedMealMap(
                orderedMeal)));

        return orderDetails;
    }

    /**
     * List of payment options.
     * @return
     */
    public List<Map<String, Object>> listPaymentOptions() {
        List<Map<String, Object>> options = new ArrayList<>();
        List<PaymentOption> paymentOptions = paymentOptionRepository.findAll(Sort.by(
                Sort.Direction.ASC, "option"));
        paymentOptions.forEach(paymentOption -> options.add(utilService.generatePaymentOptionMap(
                paymentOption)));

        return options;
    }

    /**
     * List of delivery types
     * @return
     */
    public List<Map<String, Object>> listDeliveryTypes() {
        List<Map<String, Object>> types = new ArrayList<>();
        List<DeliveryType> deliveryTypes = deliveryTypeRepository.findAll(Sort.by(Sort.Direction.ASC,
                "type"));
        deliveryTypes.forEach(deliveryType -> types.add(utilService.generateDeliveryTypeMap(
                deliveryType)));

        return types;
    }

    /**
     * Fulfil an order request by setting dispatched and paid to true
     * @param mealOrder meal order to fulfil
     */
    public void fulfilOrder(MealOrder mealOrder) {
        mealOrder.setIsDispatched(true);
        mealOrder.setIsPaid(true);
        mealOrder.setIsPlacedNow(false);
        mealOrderRepository.save(mealOrder);
    }
}

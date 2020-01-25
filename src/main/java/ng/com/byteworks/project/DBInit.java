package ng.com.byteworks.project;

import ng.com.byteworks.project.db.entity.DeliveryType;
import ng.com.byteworks.project.db.entity.PaymentOption;
import ng.com.byteworks.project.db.entity.Role;
import ng.com.byteworks.project.db.entity.User;
import ng.com.byteworks.project.db.repository.DeliveryTypeRepository;
import ng.com.byteworks.project.db.repository.PaymentOptionRepository;
import ng.com.byteworks.project.db.repository.RoleRepository;
import ng.com.byteworks.project.db.repository.UserRepository;
import ng.com.byteworks.project.enums.RoleEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "ng.com.byteworks.db.init", havingValue = "true")
public class DBInit implements CommandLineRunner {
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final PaymentOptionRepository paymentOptionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public DBInit(DeliveryTypeRepository deliveryTypeRepository,
                  PaymentOptionRepository paymentOptionRepository, RoleRepository roleRepository,
                  UserRepository userRepository) {
        this.deliveryTypeRepository = deliveryTypeRepository;
        this.paymentOptionRepository = paymentOptionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("=================================================================");
            System.out.println("Initializing database.");

            List<Role> roles = roles();
            if (roles.size() > 0) {
                roleRepository.saveAll(roles);
                System.out.println("-- Role table initialized");
            }

            List<DeliveryType> deliveryTypes = deliveryTypes();
            if (deliveryTypes.size() > 0) {
                deliveryTypeRepository.saveAll(deliveryTypes);
                System.out.println("-- Delivery type initialized");
            }

            List<PaymentOption> paymentOptions = paymentOptions();
            if (paymentOptions.size() > 0) {
                paymentOptionRepository.saveAll(paymentOptions);
                System.out.println("-- Payment option initialized");
            }

            User vendor = vendor();
            if (null != vendor) {
                userRepository.save(vendor);
                System.out.println("-- Vendor account created");
            }

            System.out.println("Database initialization complete.");
            System.out.println("=================================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Role> roles() {
        List<Role> roles = new ArrayList<>();
        String[] roleArr = { RoleEnum.ROLE_DEVELOPER.toString(), RoleEnum.ROLE_VENDOR.toString() };
        for (String role : roleArr) {
            if (!roleRepository.findByRole(role).isPresent()) {
                roles.add(new Role(role));
            }
        }

        return roles;
    }

    private List<DeliveryType> deliveryTypes() {
        List<DeliveryType> deliveryTypes = new ArrayList<>();
        String[] types = { "Office Delivery", "Pick Up" };
        double[] amounts = { 10, 0 };
        for (int i = 0; i < types.length; i++) {
            if (!deliveryTypeRepository.findByType(types[i]).isPresent()) {
                deliveryTypes.add(new DeliveryType(types[i], amounts[i]));
            }
        }

        return deliveryTypes;
    }

    private List<PaymentOption> paymentOptions() {
        List<PaymentOption> paymentOptions = new ArrayList<>();
        String[] options = { "Card Payment", "Pay On Delivery" };
        double[] discounts = { 2.5, 0 };
        for (int i = 0; i < options.length; i++) {
            if (!paymentOptionRepository.findByOption(options[i]).isPresent()) {
                paymentOptions.add(new PaymentOption(options[i], discounts[i]));
            }
        }

        return paymentOptions;
    }

    private User vendor() {
        Optional<Role> role = roleRepository.findByRole(RoleEnum.ROLE_VENDOR.toString());
        if (!role.isPresent()) return null;

        String firstName = "John";
        String lastName = "Doe";
        String email = "osaetinevbuoma@gmail.com"; // change to desired email
        String password = new BCryptPasswordEncoder().encode("123");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) return null;

        User user = new User(firstName, lastName, email, password);
        user.setRole(role.get());

        return user;
    }
}

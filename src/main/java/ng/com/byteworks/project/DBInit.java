package ng.com.byteworks.project;

import ng.com.byteworks.project.db.entity.DeliveryType;
import ng.com.byteworks.project.db.entity.PaymentOption;
import ng.com.byteworks.project.db.entity.Role;
import ng.com.byteworks.project.db.entity.User;
import ng.com.byteworks.project.db.repository.DeliveryTypeRepository;
import ng.com.byteworks.project.db.repository.PaymentOptionRepository;
import ng.com.byteworks.project.db.repository.RoleRepository;
import ng.com.byteworks.project.db.repository.UserRepository;
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
        String[] roleArr = { "ROLE_DEVELOPER", "ROLE_VENDOR" };
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
        for (String type : types) {
            if (!deliveryTypeRepository.findByType(type).isPresent()) {
                deliveryTypes.add(new DeliveryType(type));
            }
        }

        return deliveryTypes;
    }

    private List<PaymentOption> paymentOptions() {
        List<PaymentOption> paymentOptions = new ArrayList<>();
        String[] options = { "Card Payment", "Pay On Delivery" };
        for (String option : options) {
            if (!paymentOptionRepository.findByOption(option).isPresent()) {
                paymentOptions.add(new PaymentOption(option));
            }
        }

        return paymentOptions;
    }

    private User vendor() {
        Optional<Role> role = roleRepository.findByRole("ROLE_VENDOR");
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

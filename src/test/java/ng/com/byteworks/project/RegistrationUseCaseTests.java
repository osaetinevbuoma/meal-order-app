package ng.com.byteworks.project;

import ng.com.byteworks.project.db.entity.User;
import ng.com.byteworks.project.db.repository.RoleRepository;
import ng.com.byteworks.project.db.repository.UserRepository;
import ng.com.byteworks.project.service.RegistrationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RegistrationUseCaseTests {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    private RegistrationService registrationService;
    private Utilities utilities;

    @BeforeEach
    void setup() {
        registrationService = new RegistrationService(roleRepository, userRepository);
        utilities = new Utilities(entityManager);
        utilities.createDeveloperAccount();
        utilities.createVendorAccount();
    }

    @AfterEach
    void destroy() {
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testDeveloperAccountCreation() {
        Optional<User> user = userRepository.findByEmail(Utilities.DEVELOPER_EMAIL);
        Assertions.assertThat(user).isPresent();
        Assertions.assertThat(user.get().getEmail()).isEqualTo(Utilities.DEVELOPER_EMAIL);
        Assertions.assertThat(user.get().getRole().getRole()).isEqualTo(Utilities.DEVELOPER_ROLE);
    }

    @Test
    void testVendorAccountCreation() {
        Optional<User> user = userRepository.findByEmail(Utilities.VENDOR_EMAIL);
        Assertions.assertThat(user).isPresent();
        Assertions.assertThat(user.get().getEmail()).isEqualTo(Utilities.VENDOR_EMAIL);
        Assertions.assertThat(user.get().getRole().getRole()).isEqualTo(Utilities.VENDOR_ROLE);
    }
}

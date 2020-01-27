package ng.com.byteworks.project.security;

import ng.com.byteworks.project.Utilities;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithVendorDetailsSecurityContextFactory.class,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface WithMockVendor {
    int id() default 2;
    String email() default Utilities.VENDOR_EMAIL;
    String firstName() default Utilities.VENDOR_FIRST_NAME;
    String surname() default Utilities.VENDOR_SURNAME;
    String password() default Utilities.PASSWORD;
    String role() default Utilities.VENDOR_ROLE;
}

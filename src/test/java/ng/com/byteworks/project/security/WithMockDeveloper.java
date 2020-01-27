package ng.com.byteworks.project.security;

import ng.com.byteworks.project.Utilities;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithDeveloperDetailsSecurityContextFactory.class,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface WithMockDeveloper {
    int id() default 1;
    String email() default Utilities.DEVELOPER_EMAIL;
    String firstName() default Utilities.DEVELOPER_FIRST_NAME;
    String surname() default Utilities.DEVELOPER_SURNAME;
    String password() default Utilities.PASSWORD;
    String role() default Utilities.DEVELOPER_ROLE;
}

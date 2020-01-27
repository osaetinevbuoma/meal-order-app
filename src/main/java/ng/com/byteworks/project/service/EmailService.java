package ng.com.byteworks.project.service;

import ng.com.byteworks.project.db.entity.MealOrder;
import ng.com.byteworks.project.db.entity.Role;
import ng.com.byteworks.project.db.entity.User;
import ng.com.byteworks.project.db.repository.RoleRepository;
import ng.com.byteworks.project.db.repository.UserRepository;
import ng.com.byteworks.project.enums.RoleEnum;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
@PreAuthorize("authenticated")
public class EmailService {
    private final Environment environment;
    private final HttpServletRequest httpServletRequest;
    private final RoleRepository roleRepository;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;

    public EmailService(Environment environment, HttpServletRequest httpServletRequest,
                        RoleRepository roleRepository, TemplateEngine templateEngine,
                        UserRepository userRepository) {
        this.environment = environment;
        this.httpServletRequest = httpServletRequest;
        this.roleRepository = roleRepository;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
    }

    /**
     * Java mail sender.
     *
     * @return
     */
    private JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setHost(environment.getProperty("spring.mail.host"));
        sender.setPort(Integer.parseInt(environment.getProperty("spring.mail.port")));
        sender.setUsername(environment.getProperty("spring.mail.username"));
        sender.setPassword(environment.getProperty("spring.mail.password"));
        sender.setProtocol(environment.getProperty("spring.mail.protocol"));

        Properties properties = sender.getJavaMailProperties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.connectiontimeout", String.valueOf(5000));
        properties.setProperty("mail.smtp.timeout", String.valueOf(5000));
        properties.setProperty("mail.smtp.writetimeout", String.valueOf(5000));
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.account.from", "Byteworks Eats <no-reply@byteworks.com.ng>");

        return sender;
    }

    private MimeMessageHelper mimeMessageHelper(JavaMailSenderImpl sender, MimeMessage message,
                                                String emailAddress, String subject,
                                                String htmlContent)
            throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sender.getJavaMailProperties().getProperty("mail.account.from"));
        helper.setTo(emailAddress);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.addInline("logo.png", new ClassPathResource("static/images/logo.png"),
                "image/png");

        return helper;
    }

    /**
     * Send an order confirmation email to the vendor.
     * The email content is generated by passing the list of orders to an email template that is
     * formatted to display with its own style attributes in the vendor's email.
     *
     * @param order list of orders
     */
    public void sendOrderConfirmationEmail(MealOrder order) {
        try {
            JavaMailSenderImpl sender = mailSender();

            // Base URL for links in template.
            String baseUrl = String.format("%s://%s:%d", httpServletRequest.getScheme(),
                    httpServletRequest.getServerName(), httpServletRequest.getServerPort());

            // Prepare the evaluation context
            Context context = new Context();
            context.setVariable("order", order);
            context.setVariable("baseUrl", baseUrl);
            context.setVariable("logo", "logo.png");

            // Create the HTML body using Thymeleaf
            String htmlContent = templateEngine.process("email/order_confirmation_template.html",
                    context);

            // Customer email address
            Optional<Role> role = roleRepository.findByRole(RoleEnum.ROLE_VENDOR.toString());
            Optional<User> user = userRepository.findByRole(role.get());
            String emailAddress = user.get().getEmail();

            // Prepare message using a Spring helper
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = mimeMessageHelper(sender, message, emailAddress,
                    "Meal Order Placed", htmlContent);

            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

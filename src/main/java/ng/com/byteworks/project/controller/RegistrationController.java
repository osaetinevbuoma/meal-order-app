package ng.com.byteworks.project.controller;

import ng.com.byteworks.project.db.entity.User;
import ng.com.byteworks.project.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping
public class RegistrationController {
    private final RegistrationService registrationService;

    private final static Logger log = Logger.getLogger(RegistrationController.class.getName());

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(value = "/api/developer/register", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody User user) {
        if (null == user) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        log.info(user.toString());

        user = registrationService.createUser(user);
        if (null == user) return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("A user with this email exists.");

        return ResponseEntity.ok().build();
    }
}

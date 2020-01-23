package ng.com.byteworks.project.auth;

import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
public class AuthenticatedUser extends User {
    private final ng.com.byteworks.project.db.entity.User user;

    public AuthenticatedUser(ng.com.byteworks.project.db.entity.User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole()
                .getRole()));
        this.user = user;
    }
}

package in.techbeat.palapa.validator;

import in.techbeat.palapa.model.db.User;
import in.techbeat.palapa.model.request.CreateUserRequest;
import in.techbeat.palapa.util.PalapaUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final CreateUserRequest user = (CreateUserRequest) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Username must not be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Password must not be empty");
        // TODO Validate username profanity filter
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("password", "Password and confirm password are not same");
        }
        if (!PalapaUtils.validate(user.getEmail(), PalapaUtils.VALID_EMAIL_ADDRESS_REGEX)) {
            errors.rejectValue("email", "Incorrect email address.");
        }
        if (!PalapaUtils.validate(user.getUsername(), PalapaUtils.VALID_USERNAME_REGEX)) {
            errors.rejectValue("username", "Username must be 4-15 characters long, consist of alphanumeric or underscore characters, and start with an alphabet. You provided: " + user.getUsername());
        }
        if (!PalapaUtils.validate(user.getPassword(), PalapaUtils.VALID_PASSWORD_REGEX)) {
            errors.rejectValue("password", "Password must be 6-20 characters long, and consist of at least one uppercase, one lowercase, one digit and one special character among @#$%^&+=.");
        }
    }
}
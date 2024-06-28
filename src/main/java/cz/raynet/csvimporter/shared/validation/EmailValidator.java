package cz.raynet.csvimporter.shared.validation;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator {


    /**
     * Email validation
     *
     * @param email email to validate; must contain @
     * @return true if email is valid, false otherwise
     */
    public boolean validate(final String email) {
        return email != null && email.contains("@");
    }

}

package cz.raynet.csvimporter.shared.validation;

import org.springframework.stereotype.Service;

@Service
public class PhoneValidator {

        /**
        * Phone number validation
        *
        * @param phoneNumber phone number to validate; must be in format +XXX XXX XXX XXX or +XXXXXXXXXXX
        * @return true if phone number is valid, false otherwise
        */
        public boolean validate(final String phoneNumber) {
            return phoneNumber != null && phoneNumber.matches("\\+\\d{3}(\\s*\\d){9}");
        }
}

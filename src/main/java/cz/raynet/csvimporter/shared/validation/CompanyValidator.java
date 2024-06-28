package cz.raynet.csvimporter.shared.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyValidator {
    private final EmailValidator emailValidator;
    private final PhoneValidator phoneValidator;

    public boolean validateCompany(final String title,final String email,final String phone) {
        return !title.trim().isEmpty() &&  validatePhone(phone) && validateEmail(email);
    }
    private boolean validateEmail(final String email) {
        return emailValidator.validate(email);
    }
    private boolean validatePhone(final String phone) {
        return phoneValidator.validate(phone);
    }
}

package cz.raynet.csvimporter.shared.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties("mail")
public class MailConfiguration {
    private String sourceMail;
    private String destinationMail;
    private String subjectSuccess;
    private String subjectFailure;
    private String bodySuccess;
    private String bodyFailure;
}

package cz.raynet.csvimporter.shared.mailer;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import cz.raynet.csvimporter.domain.company.model.entity.Company;
import cz.raynet.csvimporter.shared.configuration.MailConfiguration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SESMailer {
    private final AmazonSimpleEmailService sesClient;
    private final MailConfiguration mailConfiguration;

    public SESMailer(final MailConfiguration mailConfiguration) {
        this.mailConfiguration = mailConfiguration;

        sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.US_EAST_1).build();

    }

    public void sendSuccessEmail() {
        final String subject = mailConfiguration.getSubjectSuccess();
        final String body = mailConfiguration.getBodySuccess();
        sendEmail(subject, body);
    }

    public void sendFailureEmail(final List<Company> invalidCompanies) {
        final String subject = mailConfiguration.getSubjectFailure();
        final String bodyHeader = mailConfiguration.getBodyFailure();
        final StringBuilder body = new StringBuilder(bodyHeader);
        invalidCompanies.forEach(
                company -> body.append("<p>").append(company.getRegNumber()).append("</p>")
        );

        sendEmail(subject, body.toString());
    }


    private void sendEmail(final String subject,final String body) {
        final String fromAddress = mailConfiguration.getSourceMail();
        final String toAddress = mailConfiguration.getDestinationMail();

        final SendEmailRequest emailRequest = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(toAddress))
                .withMessage(new Message()
                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(body)))
                        .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                .withSource(fromAddress);

        sesClient.sendEmail(emailRequest);
    }
}

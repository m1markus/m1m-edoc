package ch.m1m.edoc.email;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;

@ApplicationScoped
@Slf4j
public class EmailService {

    @Inject
    Mailer mailer;

    @ConfigProperty(name = "quarkus.mailer.password")
    String smtpUserPassword;

    public void send(String to,
                     String subject,
                     String templateName,
                     Map map) {
        log.info("EmailService send called with...");

        mailer.send(Mail.withText(to, subject, getBody(templateName)));

        log.info("using password {}", smtpUserPassword);
    }

    private String getBody(String templateName) {
        return """
                dear %sirname,
                this is the body text test message
                with no more expanded parameters.
                """;
    }
}

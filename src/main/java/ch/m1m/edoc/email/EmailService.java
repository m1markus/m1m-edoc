package ch.m1m.edoc.email;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

@ApplicationScoped
@Slf4j
public class EmailService {

    @Inject
    Mailer mailer;

    @ConfigProperty(name = "quarkus.mailer.password", defaultValue = "email-password-is-not-set")
    String smtpUserPassword;

    public void send(String to,
                     String subject,
                     String templateName,
                     Map<String, Object> map) {
        log.info("EmailService send called with...");

        // send text mail only
        //mailer.send(Mail.withText(to, subject, getTextBody(templateName)));

        // use mvel2 template engine
        String bodyTemplateRaw = getMvelTemplate("toto");
        String htmlMailBody = (String) TemplateRuntime.eval(bodyTemplateRaw, map);

        // send html mail
        mailer.send(Mail.withHtml(to, subject, htmlMailBody));
        log.info("sent mail to {} success", to);

        //log.debug("using password {}", smtpUserPassword);
    }

    // http://mvel.documentnode.com/#language-guide-for-2.0
    //
    private String getMvelTemplate(String templateName) {

        return """
                <h2>dear @{firstname}</h2>
                <p>
                this is the body text test message
                with no more expanded parameters.</p>
                <p>
                this is the body text test message
                with no more expanded parameters.</p>
                <a href="https://www.20min.ch">confirm your e-mail address</a>
                """;
    }

    private String getHtmlBody(String templateName) {
        return """
                <h2>dear %firstname</h2>
                <p>
                this is the body text test message
                with no more expanded parameters.</p>
                <p>
                this is the body text test message
                with no more expanded parameters.</p>
                <a href="https://www.20min.ch">confirm your e-mail address</a>
                """;
    }

    private String getTextBody(String templateName) {
        return """
                dear %sirname,
                this is the body text test message
                with no more expanded parameters.
                """;
    }
}

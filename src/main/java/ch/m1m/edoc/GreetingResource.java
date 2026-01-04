package ch.m1m.edoc;

import ch.m1m.edoc.email.EmailService;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Path("/hello")
public class GreetingResource {

    @Inject
    EmailService emailService;

    @GET
    @Blocking
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        UUID uuid1 = UUID.randomUUID();
        String uuidString1 = uuid1.toString();

        UUID uuid2 = UUID.fromString(uuidString1);

        if (uuid1.equals(uuid2)) {
            log.info("uuid matched");
        } else {
            log.info("uuid equals failed");
        }

        //emailService.send("mmm7@bluewin.ch", "test-2", "login-link-confirm", null);

        return "Hello from Quarkus REST";
    }
}

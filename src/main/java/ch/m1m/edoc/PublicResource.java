package ch.m1m.edoc;

import java.security.Principal;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;

@Path("/api/public")
@Slf4j
public class PublicResource {

    private final String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String publicResource() {
        return "public";
    }

    @GET
    @Path("/me")
    @Produces(MediaType.TEXT_PLAIN)
    public String me(@Context SecurityContext securityContext) {
        Principal user = securityContext.getUserPrincipal();
        return user != null ? user.getName() : "<not logged in>";
    }

    @GET
    @Path("/code")
    @Produces("image/png")
    public Response codePng(@Context SecurityContext securityContext) throws QrGenerationException {

        //SecretGenerator secretGenerator = new DefaultSecretGenerator();
        //String secret = secretGenerator.generate();
        //
        // String secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV";

        QrData data = new QrData.Builder()
                .label("example@m1m.ch")
                .secret(secret)
                .issuer("eDoc")
                .algorithm(HashingAlgorithm.SHA1) // More on this below
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);

        return Response
                .ok(imageData)
                .type("image/png")
                .build();
    }

    @GET
    @Path("/code/verify")
    @Produces(MediaType.TEXT_PLAIN)
    public String codeVerify(@QueryParam("code") String code) {

        log.info("codeVerify code={}", code);

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        // secret = the shared secret for the user
        // code = the code submitted by the user
        boolean passed = verifier.isValidCode(secret, code);

        log.info("verify passed is: {}", passed);
        return passed ? "verify passed" : "verify failed";
    }
}

import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.ext.mail.MailMessage;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.core.common.EmailService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class MailTest {
    @Inject
    EmailService emailService;

    @Inject
    MockMailbox mockMailbox;

    @Test
    public void testSendEmail() {
        emailService.sendBookingConfirmationEmail("test", "test", "test", "test");

        // Verify the email was captured by the mock mailbox
        List<MailMessage> test = mockMailbox.getMailMessagesSentTo("test");
        assertEquals(1, test.size());
    }
}
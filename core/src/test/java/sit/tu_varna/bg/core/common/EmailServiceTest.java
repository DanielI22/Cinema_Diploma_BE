package sit.tu_varna.bg.core.common;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    Mailer mailer;

    @Mock
    Template bookingConfirmationBg;

    @Mock
    Template bookingConfirmationEn;

    @Mock
    Template ticketConfirmationBg;

    @Mock
    Template ticketConfirmationEn;

    @Mock
    TemplateInstance templateInstance;

    @InjectMocks
    EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(bookingConfirmationBg.data(any())).thenReturn(templateInstance);
        when(bookingConfirmationEn.data(any())).thenReturn(templateInstance);
        when(ticketConfirmationBg.data(any())).thenReturn(templateInstance);
        when(ticketConfirmationEn.data(any())).thenReturn(templateInstance);

        when(templateInstance.render()).thenReturn("<html>Email content</html>");
    }

    @Test
    void testSendBookingConfirmationEmailInBg() {
        String email = "test@example.com";
        String shortcode = "12345";
        String bookingsLink = "http://example.com/bookings";
        String language = "BG";

        emailService.sendBookingConfirmationEmail(email, shortcode, bookingsLink, language);

        ArgumentCaptor<Mail> mailCaptor = ArgumentCaptor.forClass(Mail.class);
        verify(mailer).send(mailCaptor.capture());

        Mail capturedMail = mailCaptor.getValue();
        assertEquals(email, capturedMail.getTo().stream().findFirst().orElse(null));
        assertEquals("Booking Confirmation", capturedMail.getSubject());
        assertEquals("<html>Email content</html>", capturedMail.getHtml());
        verify(bookingConfirmationBg).data(any());
    }

    @Test
    void testSendBookingConfirmationEmailInEn() {
        String email = "test@example.com";
        String shortcode = "12345";
        String bookingsLink = "http://example.com/bookings";
        String language = "EN";

        emailService.sendBookingConfirmationEmail(email, shortcode, bookingsLink, language);

        ArgumentCaptor<Mail> mailCaptor = ArgumentCaptor.forClass(Mail.class);
        verify(mailer).send(mailCaptor.capture());

        Mail capturedMail = mailCaptor.getValue();
        assertEquals(email, capturedMail.getTo().stream().findFirst().orElse(null));
        assertEquals("Booking Confirmation", capturedMail.getSubject());
        assertEquals("<html>Email content</html>", capturedMail.getHtml());
        verify(bookingConfirmationEn).data(any());
    }

    @Test
    void testSendTicketConfirmationEmailInBg() {
        String email = "test@example.com";
        String ticketsLink = "http://example.com/tickets";
        String language = "BG";

        emailService.sendTicketConfirmationEmail(email, ticketsLink, language);

        ArgumentCaptor<Mail> mailCaptor = ArgumentCaptor.forClass(Mail.class);
        verify(mailer).send(mailCaptor.capture());

        Mail capturedMail = mailCaptor.getValue();
        assertEquals(email, capturedMail.getTo().stream().findFirst().orElse(null));
        assertEquals("Ticket Confirmation", capturedMail.getSubject());
        assertEquals("<html>Email content</html>", capturedMail.getHtml());
        verify(ticketConfirmationBg).data(any());
    }

    @Test
    void testSendTicketConfirmationEmailInEn() {
        String email = "test@example.com";
        String ticketsLink = "http://example.com/tickets";
        String language = "EN";

        emailService.sendTicketConfirmationEmail(email, ticketsLink, language);

        ArgumentCaptor<Mail> mailCaptor = ArgumentCaptor.forClass(Mail.class);
        verify(mailer).send(mailCaptor.capture());

        Mail capturedMail = mailCaptor.getValue();
        assertEquals(email, capturedMail.getTo().stream().findFirst().orElse(null));
        assertEquals("Ticket Confirmation", capturedMail.getSubject());
        assertEquals("<html>Email content</html>", capturedMail.getHtml());
        verify(ticketConfirmationEn).data(any());
    }
}

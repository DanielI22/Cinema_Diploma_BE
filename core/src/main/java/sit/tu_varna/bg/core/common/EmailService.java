package sit.tu_varna.bg.core.common;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@SuppressWarnings("all")
public class EmailService {
    @Inject
    Mailer mailer;
    @Inject
    Template bookingConfirmationBg;
    @Inject
    Template bookingConfirmationEn;
    @Inject
    Template ticketConfirmationBg;
    @Inject
    Template ticketConfirmationEn;

    public void sendBookingConfirmationEmail(String email, String shortcode, String bookingsLink, String language) {
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("shortcode", shortcode);
        data.put("bookingsLink", bookingsLink);

        TemplateInstance instance;
        if (language.equalsIgnoreCase("BG")) {
            instance = bookingConfirmationBg.data(data);
        } else {
            instance = bookingConfirmationEn.data(data);
        }

        mailer.send(Mail.withHtml(email, "Booking Confirmation", instance.render()));
    }

    public void sendTicketConfirmationEmail(String email, String ticketsLink, String language) {
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("ticketsLink", ticketsLink);

        TemplateInstance instance;
        if (language.equalsIgnoreCase("BG")) {
            instance = ticketConfirmationBg.data(data);
        } else {
            instance = ticketConfirmationEn.data(data);
        }

        mailer.send(Mail.withHtml(email, "Ticket Confirmation", instance.render()));
    }
}

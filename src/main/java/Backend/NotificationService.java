package Backend;

import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class NotificationService {

    private final Connection connection;

    public NotificationService(Connection connection) {
        this.connection = connection;
    }

    public void sendNotification(int customerId, String message) {
        // Send UI popup
        showPopupNotification("Notification for Customer " + customerId, message);

        // Send email
        String recipientEmail = getEmailForCustomer(customerId);
        if (recipientEmail != null) {
            sendEmail(recipientEmail, "Order Notification", message);
        }
    }

    // Method to display UI popup notification
    private void showPopupNotification(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Fetch customer email from the database
    private String getEmailForCustomer(int customerId) {
        String email = null;
        String query = "SELECT Email FROM customers WHERE CustomerId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    email = rs.getString("Email");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }

    // Method to send email notification
    private void sendEmail(String recipient, String subject, String content) {
        String from = "your-email@example.com"; // Replace with your email address
        String host = "smtp.gmail.com"; // SMTP server for Gmail

        // Set properties for the email session
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Authenticate with the email server
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your-email@example.com", "your-email-password"); // Replace with your credentials
            }
        });

        try {
            // Create a MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(content);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendSMSNotification(int customerId, String message) {
        String phoneNumber = getPhoneNumberForCustomer(customerId);
        if (phoneNumber != null) {
            System.out.println("Sending SMS to " + phoneNumber + ": " + message);
            // Logic to send SMS via an SMS gateway API (e.g., Twilio, Nexmo)
        }
    }


    private String getPhoneNumberForCustomer(int customerId) {
        String phoneNumber = null;
        String query = "SELECT PhoneNumber FROM customers WHERE CustomerId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    phoneNumber = rs.getString("PhoneNumber");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }
}

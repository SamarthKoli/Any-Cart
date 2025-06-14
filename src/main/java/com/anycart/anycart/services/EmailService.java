package com.anycart.anycart.services;


import com.anycart.anycart.entities.Order;
import com.anycart.anycart.entities.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmationEmail(Order order) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(order.getUser().getEmail());
            helper.setSubject("Order Confirmation - Order #" + order.getId());
            helper.setText(buildOrderConfirmationEmail(order), true); // true for HTML content

            mailSender.send(mimeMessage);
            logger.info("Order confirmation email sent to: {}", order.getUser().getEmail());
        } catch (MessagingException e) {
            logger.error("Failed to send order confirmation email to {}: {}", 
                         order.getUser().getEmail(), e.getMessage());
            // Avoid throwing exception to prevent transaction rollback
        }
    }

    private String buildOrderConfirmationEmail(Order order) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<h1>Order Confirmation</h1>")
                    .append("<p>Dear ").append(order.getUser().getFirstName()).append(",</p>")
                    .append("<p>Thank you for your order! Your order has been successfully placed.</p>")
                    .append("<h2>Order Details</h2>")
                    .append("<p><strong>Order ID:</strong> ").append(order.getId()).append("</p>")
                    .append("<p><strong>Order Date:</strong> ").append(order.getCreatedAt()).append("</p>")
                    .append("<p><strong>Shipping Address:</strong> ").append(order.getShippingAddress()).append("</p>")
                    .append("<p><strong>Total Amount:</strong> $").append(order.getTotalAmount()).append("</p>")
                    .append("<h3>Order Items</h3>")
                    .append("<table border='1' cellpadding='5'>")
                    .append("<tr><th>Product</th><th>Quantity</th><th>Unit Price</th><th>Total</th></tr>");

        for (OrderItem item : order.getOrderItems()) {
            emailContent.append("<tr>")
                        .append("<td>").append(item.getProduct().getName()).append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        .append("<td>$").append(item.getUnitPrice()).append("</td>")
                        .append("<td>$").append(item.getUnitPrice().multiply(new java.math.BigDecimal(item.getQuantity()))).append("</td>")
                        .append("</tr>");
        }

        emailContent.append("</table>")
                    .append("<p>We will notify you once your order is shipped.</p>")
                    .append("<p>Best regards,<br>Any-Cart Team</p>");

        return emailContent.toString();
    }
}

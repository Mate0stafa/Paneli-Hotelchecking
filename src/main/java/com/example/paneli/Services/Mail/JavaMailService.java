package com.example.paneli.Services.Mail;

import com.example.paneli.DataObjects.RequestChanges;
import com.example.paneli.Models.*;
import com.example.paneli.Models.Auth.PropertyAddRequest;
import com.example.paneli.Models.Contract.Agreement;
import com.example.paneli.Models.Contract.AgreementRequest;
import com.example.paneli.Models.Contract.Changeofownership;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.TokenService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class JavaMailService {

    @Autowired
    private JavaMailSender sender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    public void sendMailPinCode(User user, PropertyAddRequest propertyAddRequest) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Property confirmation code!");

        String htmlContent =  "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>" +
                "<br><p style=\"text-align: center;font-size: 20px;\">Your property confirmation code is:"+ propertyAddRequest.getUniqueKey() + "</p>"
                + "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
                + "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved."
                + "<br>This e-mail was sent by allbookers.com.</p>";
        helper.setText(htmlContent, true);
        send(message);

    }

    //  Emails for group account
    public void sendemailwhengroupaccountiscreated(User user, Property property , Role specialrole) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject( property.getName() + " Setup Complete for Your Legal Entity ID " + specialrole.getRoleLoginId());

        String htmlContent =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div>" +
                        "  <p>Dear <strong>" + user.getFull_name() + "</strong>,</p>" +
                        "  <p>" +
                        "    Congratulations! Your property, <strong>" + property.getName() + "</strong>, has been successfully set up under your " +
                        "    <strong>" + specialrole.getRoleLoginId() + "</strong> account." +
                        "  </p>" +
                        "  <p><strong>Property Details:</strong></p>" +
                        "  <ul style=\"padding-left: 20px;\">" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Name:</strong> " + property.getName() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Address:</strong> " + property.getAddress().getStreet() + ", " + property.getAddress().getCity().getFull_name() + ", " + property.getAddress().getCity().getCounty().getCountry().getCountry_name() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Type:</strong> " + property.getHotel_type().getType() + "</li>" +
                        "  </ul>" +
                        "  <p>" +
                        "    For any assistance, we’re just a click away!" +
                        "  </p>" +
                        "  <p style=\"margin-top: 20px;\">" +
                        "    Best regards,<br>" +
                        "    The Allbookers.com Team" +
                        "  </p>" +
                        "</div>" +
                        "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                        "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                        "<br>This e-mail was sent by allbookers.com.</p>";

        helper.setText(htmlContent, true);
        send(message);

    }

    public void sendemailwhengroupaccountiscreatedAdmin(Property property1 , Property property2 , Role specialrole) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("info@allbookers.com");
        helper.setSubject(" A new group account was created with Legal Entity ID " + specialrole.getRoleLoginId());

        String htmlContent =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div>" +
                        "  <p>Dear Allbookers.com Team</p>" +
                        "  <p>" +
                        " A new group account was created with Legal Entity ID " + specialrole.getRoleLoginId() +
                        "  </p>" +
                        "  <p><strong>List of properties included:</strong></p>" +
                        "  <ul style=\"padding-left: 20px;\">" +
                        "    <li style=\"margin-bottom: 5px;\">Property Name: <strong>" + property1.getName() + "</strong> and Property Id: "+ property1.getId() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\">Property Name: <strong>" + property2.getName() + "</strong> and Property Id: "+ property2.getId() + "</li>" +
                        "  </ul>" +
                        "</div>" +
                        "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                        "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                        "<br>This e-mail was sent by allbookers.com.</p>";

        helper.setText(htmlContent, true);
        send(message);

    }

    public void sendemailwhengroupaccountAddExistingProperty(User user, Property property , Role specialrole) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject(" Successfully Added to  the group account with Legal Entity ID " + specialrole.getRoleLoginId());

        String htmlContent =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div>" +
                        "  <p>Dear <strong>" + user.getFull_name() + "</strong>,</p>" +
                        "  <p>" +
                        "    We are pleased to inform you that your existing property, <strong>" + property.getName() + "</strong>], has been successfully added to your group account with Legal Entity ID " +
                        "    <strong>" + specialrole.getRoleLoginId() + "</strong> ." +
                        "  </p>" +
                        "  <p><strong>Property Details:</strong></p>" +
                        "  <ul style=\"padding-left: 20px;\">" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Name:</strong> " + property.getName() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Address:</strong> " + property.getAddress().getStreet() + ", " + property.getAddress().getCity().getFull_name() + ", " + property.getAddress().getCity().getCounty().getCountry().getCountry_name() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Type:</strong> " + property.getHotel_type().getType() + "</li>" +
                        "  </ul>" +
                        "  <p>" +
                        "    If you need help, don't hesitate to reach out to us." +
                        "  </p>" +
                        "  <p style=\"margin-top: 20px;\">" +
                        "    Best regards,<br>" +
                        "    The Allbookers.com Team" +
                        "  </p>" +
                        "</div>" +
                        "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                        "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                        "<br>This e-mail was sent by allbookers.com.</p>";

        helper.setText(htmlContent, true);
        send(message);
    }

    public void sendemailwhengroupaccountAddExistingPropertyAdmin(Property property, Role specialrole) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("info@allbookers.com");
        helper.setSubject(" The property " + property.getName() + " was added to the group account with Legal Entity ID " + specialrole.getRoleLoginId());

        String htmlContent =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div>" +
                        "  <p>Dear Allbookers.com Team</p>" +
                        "  <p>" +
                        " The following property has been added to the group account with Legal Entity ID ( " + specialrole.getRoleLoginId() + " )." +
                        "  </p>" +
                        "  <p><strong>Property Details:</strong></p>" +
                        "  <ul style=\"padding-left: 20px;\">" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Name:</strong> " + property.getName() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property ID:</strong> " + property.getId() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Address:</strong> " + property.getAddress().getStreet() + ", " + property.getAddress().getCity().getFull_name() + ", " + property.getAddress().getCity().getCounty().getCountry().getCountry_name() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Type:</strong> " + property.getHotel_type().getType() + "</li>" +
                        "  </ul>" +
                        "</div>" +
                        "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                        "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                        "<br>This e-mail was sent by allbookers.com.</p>";

        helper.setText(htmlContent, true);
        send(message);

    }

    public void sendemailwhengroupaccountAddnewProperty(User user, Property property , Role specialrole , Agreement agreement) throws MessagingException, DocumentException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject(property.getName() +" Has Been Successfully Created");

        String email =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div>" +
                        "  <p>Dear <strong>" + user.getFull_name() + "</strong>,</p>" +
                        "  <p>" +
                        "    We are thrilled to inform you that your property, <strong>" + property.getName() +
                        "    </strong>, has been successfully created and added to your legal entity ID " + specialrole.getRoleLoginId() + " account for the first time!" +
                        "  </p>" +
                        "  <p><strong>Property Details:</strong></p>" +
                        "  <ul style=\"padding-left: 20px;\">" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Name:</strong> " + property.getName() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Address:</strong> " + property.getAddress().getStreet() + ", " + property.getAddress().getCity().getFull_name() + ", " + property.getAddress().getCity().getCounty().getCountry().getCountry_name() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Type:</strong> " + property.getHotel_type().getType() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Account Group:</strong> " + specialrole.getRoleLoginId() + "</li>" +
                        "  </ul>" +
                        "  <p>" +
                        "    Attached you will find a copy of the contract." +
                        "  </p>" +
                        "  <p>" +
                        "    If you need any help or have questions, our support team is here for you. Feel free to contact us." +
                        "  </p>" +
                        "  <p style=\"margin-top: 20px;\">" +
                        "    Best regards,<br>" +
                        "    The Allbookers.com Team" +
                        "  </p>" +
                        "</div>" +
                        "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                        "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                        "<br>This e-mail was sent by allbookers.com.</p>";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"mainDiv\">\n" +
                "      <div class=\"column\" style=\"padding-top: 5%; padding-left: 5%; float: left; width: 100%\">\n" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "      </div>\n" +
                "    <div style=\"text-align: left; \">\n" +
                "   <h2>Accommodation Agreement with Allbookers.com </h2>\n" +
                "   <p>This document represents a legal agreement between: </p>\n" +
                "        <p><strong>Allbookers</strong>, a platform operated by InterMedia Sh.P.K, a company registered under the laws of the Republic of Albania, with its registered address at Bulevardi Bajram Curri tek ura e ATSH, nr pasurie 6/516+1-20, Tirana, Albania, and VAT\n" +
                "            number: <strong>M02028001D</strong> (hereinafter referred to as \"The Management Platform\" or \"Management\n" +
                "            Panel\"),</p>\n" +
                "        <p><strong>And</strong></p>\n" +
                "        <p><strong>"+agreement.getLegal_bussines_name()+"</strong>, (the \"Partner\") <strong>"+agreement.getFirst_name()+' '+agreement.getLast_name()+"</strong>, \n" +
                "            with a registered address at <strong>"+agreement.getAddress()+"</strong>, and VAT number <strong>"+agreement.getNuis()+"</strong>\n" +
                "            (hereinafter referred to as \"Client (Hotelier)\" or \"Hotelier\").</p>\n" +
                "        <p>The Agreement includes the following terms:</p>\n" +
                "        <h3>Commission Rates for Registered Properties: </h3>\n" +
                "        <p>For all properties registered on Allbookers.com\n" +
                "            under the Partner's name, the following local commission rates apply:\n" +
                "            <ul>\n" +
                "                <li><strong>Albania:</strong> 10%</li>\n" +
                "            </ul>\n" +
                "        </p>\n" +
                "        <p>Allbookers.com provides a secure and reliable platform for property and booking management</p>\n" +
                "        <h3>Execution and performance </h3>\n" +
                "        <p>The Agreement is only effective after approval and confirmation by Allbookers.com.</p>\n" +
                "        <h3>General Terms of the Agreement:</h3>\n" +
                "        <p><strong>Purpose: </strong>This Agreement outlines the terms and conditions for the use of the Allbookers.com\n" +
                "            platform by the Hotelier to advertise and manage their Property, including accepting bookings,\n" +
                "            managing pricing and availability, and using the Management Panel accessed at\n" +
                "            panel.allbookers.com.</p>\n" +
                "        <p><strong>Partner Declaration: </strong>The Partner declares that this is a legitimate accommodation activity with\n" +
                "            all necessary licenses and permits, which may be provided upon request. Allbookers.com reserves\n" +
                "            the right to verify and investigate any details provided by the Partner in this registration.</p>\n" +
                "        <p><strong>Term of the Agreement: </strong>\n" +
                "            This Agreement will be valid from the date of signing and will continue\n" +
                "            to be in effect until terminated or canceled by either party in accordance with the terms of this\n" +
                "            Agreement.</p>\n" +
                "        <p><strong style=\"font-size: 18px;\">Start Date of the Agreement: </strong>"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+ "</p>  \n" +
                "<p><br></br><br></br></p>"+
                "        <h2>Table of Contents for the Accommodation Agreement</h2> \n" +
                "        <p><strong>1. Definitions</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>1.1 </strong>Explanation of terms used in the Agreement, including: \"Platform,\" \"Property,\"\n" +
                "        \"Booking,\" \"Management Panel,\" \"Client (Hotelier) \" etc,.\n" +
                "        </p>\n" +
                "        <p><strong>2. Purpose of the Agreement\n" +
                "        </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>2.1 </strong>Description of the service provided by Allbookers.com<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>2.2 </strong>Purpose of the agreement and use of the platform by the Client (Hotelier) for property\n" +
                "        and booking management.\n" +
                "        </p>\n" +
                "        <p><strong>3. Rights and Obligations of the Parties</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>3.1 </strong>Obligations of Allbookers.com<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>3.2 </strong>Obligations of the Property\n" +
                "        </p>\n" +
                "        <p><strong>4. Registration and Use of the Platform</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>4.1 </strong>Process for registering the property<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>4.2</strong> Steps to follow for using the management panel and updating property information.\n" +
                "        \n" +
                "        </p>\n" +
                "        <p><strong>5. Management of Reservations and Prices</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>5.1 </strong>Setting and managing pricing and availability by the hotelier.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>5.2 </strong>Policies for cancellations and changes in bookings.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>5.3 </strong>Payment and Commission Procedures.\n" +
                "        </p>\n" +
                "        <p><strong>6. Payments and Fees </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.1 </strong>Fee and Commission Structure of the Platform\n" +
                "        <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.2 </strong>Payment Methods and Deadlines<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.3 </strong>Monthly Reservation Invoices<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.4 </strong>Policies on payment delays or unpaid commissions.\n" +
                "        </p>\n" +
                "        <p><strong>7. Security and Data Protection </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>7.1 </strong>Data Protection for <strong>Client (Hotelier)</strong> and Properties.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>7.2 </strong>Security measures for personal and financial data.\n" +
                "        </p>\n" +
                "        <p><strong>8. Legal Responsibilities</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>8.1 </strong>Responsibilities for Non-Compliance with Contract Terms.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>8.2 </strong>Limits of the Platform's Liability for Reservations or Other Client (Hotelier) -Related\n" +
                "        Issues.\n" +
                "        </p>\n" +
                "        <p><strong>9. </strong>Termination of the Agreement and Property Closure<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>9.1 </strong>Termination Conditions<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>9.2 </strong>Immediate Termination and Closure of Property. <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>9.3 </strong>Notice and Payment After Termination\n" +
                "        </p>\n" +
                "        <p><strong>10. General Provisions</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>10.1 </strong>Amendments to the Agreement<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>10.2 </strong>Transfer of rights and obligations, Change of Ownership.\n" +
                "        </p>\n" +
                "        <p><strong>11. Ranking, Guest Reviews and Marketing</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>11.1 </strong>Ranking<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>11.2 </strong>Guest Reviews <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>11.3 </strong>Online Marketing\n" +
                "        </p>\n" +
                "        <p><strong>12. Miscellaneous </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.1 </strong>Indemnification by the Client (Hotelier) <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.2 </strong>Limitation of Liability <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.3 </strong>No Liability for Third-Party Services<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.4 </strong>Force Majeure<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.5 </strong>Legal Actions\n" +
                "        </p> \n" +

                "        <h3>1. Definitions</h3>\n" +
                "        <p>In addition to terms defined elsewhere in this Agreement, the following definitions apply\n" +
                "            throughout the Agreement unless the contrary intention appears:</p>\n" +
                "        <p><strong>\"Accommodation\" </strong>refers to the entity (either a legal person or a natural person) that is a party to\n" +
                "            the Agreement. If different, it also means the property associated with the Agreement that the\n" +
                "            entity is contracting for, and to which these Terms apply.</p>\n" +
                "        <p><strong>\"Accommodation Information\" </strong>refers to the details supplied by and associated with the\n" +
                "            Accommodation for presentation on the Platform. This includes images, photos, and descriptions;\n" +
                "            information about amenities and services; booking availability and room details; pricing\n" +
                "            (including all applicable Taxes unless otherwise required by law, as well as any additional fees);\n" +
                "            policies on availability, cancellations, and no-shows; other relevant policies and restrictions; and\n" +
                "            any other information that Allbookers.com is legally obligated to display on the Platform.</p>\n" +
                "        <p><strong>\"Booking\": </strong>An electronic agreement made through the Allbookers.com Platform between the\n" +
                "            Client (Hotelier) and a Guest for a stay at the Client (Hotelier) 's Property.\n" +
                "            </p>\n" +
                "        <p><strong>\"Client (Hotelier) \": </strong>An individual or legal entity that registers and manages a Property on the\n" +
                "            Allbookers.com Platform and uses the Platform's services to manage bookings and promote their\n" +
                "            Property.</p>\n" +
                "        <p><strong>\"Commission\": </strong>The fee paid by the Client (Hotelier) for the services provided by\n" +
                "            Allbookers.com, including booking facilitation and other related services.\n" +
                "            </p>\n" +
                "        <p><strong>\"Connectivity Partner\": </strong>A company or individual providing services for the integration and\n" +
                "            connection of information between the Allbookers.com Platform and other systems to enhance the\n" +
                "            functionality and efficiency of booking services.</p>\n" +
                "        <p><strong>\"Connectivity Services\": </strong>Services that enable the connection and integration of information\n" +
                "            between the Allbookers.com Platform and other management systems or related services.\n" +
                "            </p>\n" +
                "        <p><strong>\"Customer Data\": </strong>Personal information and other data collected and stored by the Platform about\n" +
                "            <strong>Client (Hotelier) </strong>and Guests, including but not limited to names, addresses, contact information,\n" +
                "            and payment details.\n" +
                "            </p>\n" +
                "        <p><strong>\"Guest\": </strong>An individual who makes a Booking for a stay at the Client (Hotelier) 's Property\n" +
                "            through the Allbookers.com Platform.\n" +
                "            </p>\n" +
                "        <p><strong>\"Management Panel\": </strong>The tool provided by Allbookers.com that allows <strong>Client (Hotelier) </strong>to\n" +
                "            manage and update their Property information, including pricing, availability, bookings, and\n" +
                "            communications with Guests.</p>\n" +
                "        <p><strong>\"Platform\": </strong>Refers to Allbookers.com, an online system that enables the registration,\n" +
                "            management, and promotion of properties via the internet and provides related services for <strong>Client (Hotelier) </strong>and Guests.</p>\n" +
                "        <p><strong>\"Property\": </strong>Any accommodation unit registered on the Allbookers.com Platform, including but\n" +
                "            not limited to hotels, apartments, villas, and vacation homes, offered for booking by <strong>Client (Hotelier) </strong>on behalf of their owners.\n" +
                "            </p>\n" +
                "        <p><strong>\"Related Services\": </strong>Additional services provided by the Platform beyond the standard booking\n" +
                "            and management functionality, including but not limited to marketing services, data analytics, and\n" +
                "            technical support.</p>\n" +
                "        <p><strong>\"Room Price\": </strong>The price set by the <strong>Client (Hotelier) </strong>for an accommodation unit at their Property,\n" +
                "            as defined in the Management Panel and displayed on the Platform for Guests.</p>   \n" +
                "        <h3>2. Purpose of the Agreement </h3>   \n" +
                "        <p><strong>2.1 Description of the Service Provided by Allbookers.com </strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2.1.1 </strong>Allbookers.com provides a comprehensive online platform designed for property owners and\n" +
                "                hoteliers to manage their accommodations and bookings effectively. The platform offers a range\n" +
                "                of features including: <br></br>\n" +
                "                &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>1. Property Listing Management: </strong>Allows users to create and maintain detailed listings of\n" +
                "           their properties, including descriptions, photos, and amenities.<br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>2. Booking Management: </strong>Facilitates the handling of reservations, including accepting,\n" +
                "           modifying, and canceling them as needed.<br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>3. Pricing and Availability Control: </strong>Provides tools for setting and updating pricing and\n" +
                "           availability in real-time. <br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>4. Promotion and Visibility: </strong> Enhances property exposure through various marketing and\n" +
                "           promotional tools to attract potential guests.\n" +
                "           <br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>5. Technical Support: </strong>Offers support to assist with any issues related to the use of the\n" +
                "           platform.   \n" +
                "        </p>\n" +
                "        <p><strong>2.2 Purpose of the Agreement and Use of the Platform by the Client (Hotelier)</strong> <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2.2.1 </strong>The purpose of this Agreement is to outline the terms and conditions under which the Client\n" +
                "            (Hotelier) will use the Allbookers.com platform. This includes: <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>1. Advertising and Managing the Property: </strong>The Client (Hotelier) agrees to utilize the\n" +
                "            platform for listing and managing their property, ensuring that the information is accurate\n" +
                "            and up-to-date.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2. Handling Bookings: </strong>The Client (Hotelier) will use the platform to accept, modify, and\n" +
                "            manage bookings made by guests<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3. Maintaining Pricing and Availability: </strong>The Client (Hotelier) is responsible for setting and\n" +
                "            updating the pricing and availability of their property through the platform<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>4. Compliance with Platform Policies: </strong>The Client (Hotelier) must adhere to the platform's\n" +
                "            terms, policies, and guidelines to ensure proper use and to maintain the quality and integrity\n" +
                "            of the services provided.\n" +
                "        </p>  \n" +
                "        <h3>3. Rights and Obligations of the Parties</h3>\n" +
                "        <p><strong>3.1 Obligations of Allbookers.com</strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.1 </strong>Allbookers.com will ensure uninterrupted and reliable access to its platform to enable the\n" +
                "            Client (Hotelier) to manage their property and reservations efficiently. This access includes the\n" +
                "            use of the Management Panel, panel.allbookers.com, which the Client (Hotelier) will use to update\n" +
                "            property information and manage reservations.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.2 </strong>Additionally, Allbookers.com will provide technical support. This support is dedicated to\n" +
                "            assisting the Client (Hotelier) in using the platform and resolving any issues that may arise during\n" +
                "            the operation of the platform. Technical support includes help with technical issues, system errors,\n" +
                "            and problems related to platform access.\n" +
                "            <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.3 </strong>To protect the Client (Hotelier) 's data and property information, Allbookers.com will take\n" +
                "            necessary measures for data security. This includes the use of advanced encryption technologies\n" +
                "            and other security measures to protect the Client (Hotelier) 's personal and financial information\n" +
                "            from unauthorized access, loss, or damage.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.4 </strong>Allbookers.com will also employ various marketing strategies to promote the Client\n" +
                "            (Hotelier) 's property on the platform and other potential networks. Promotion includes featuring\n" +
                "            the property in the platform's search listings and may include promotional campaigns and relevant\n" +
                "            advertisements.\n" +
                "            \n" +
                "        </p>\n" +
                "        <p><strong>3.2 Obligations of the Client (Hotelier) </strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.2.1 </strong>The Client (Hotelier) is responsible for the proper use of the platform in accordance with\n" +
                "            Allbookers.com's guidelines and policies. The Client (Hotelier) must ensure that their use of the\n" +
                "            platform complies with legal requirements and ethical standards. The Client (Hotelier) is\n" +
                "            responsible for any activity that occurs on their account and for assisting in resolving issues that\n" +
                "            arise during the use of the platform<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.2.2 </strong>To ensure that their property information is accurate and up-to-date, the Client (Hotelier)\n" +
                "            must keep their property information, including prices, availability, and other details, accurate and regularly updated. \n" +
                "            Any changes to the property information must be reflected on the platform immediately after they occur.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.2.3 </strong>The Client (Hotelier) must adhere to all terms and policies of the Allbookers.com platform,\n" +
                "            including policies regarding prices, availability, reservations, and cancellations. Any violation of\n" +
                "            the platform's terms and policies may result in disciplinary measures, including suspension of\n" +
                "            access to the platform.\n" +
                "        </p>\n" +
                "        <h3>4. Registration and Use of the <span style=\"color: rgb(0, 162, 255);text-decoration: underline;\">panel.allbookers.com</span> Platform</h3>\n" +
                "        <p><strong>4.1 Process for registering the property: </strong>Property registration on the Allbookers.com platform\n" +
                "            is managed by our marketing team and involves completing all necessary information for its\n" +
                "            inclusion in the system. Our team will collect and register the complete property information,\n" +
                "            including the name, address, type of accommodation, description, and any other relevant details.\n" +
                "            After completing the registration, a request will be sent to sign the relevant Agreement if the\n" +
                "            required information is not complete.\n" +
                "            </p>\n" +
                "        <p><strong>4.2 Steps to Follow to Use the Management Panel and Update Property Information</strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>1. Log In to the Management Panel: </strong>After the successful registration of the property and\n" +
                "            signing of the Agreement, you will receive login credentials for the management panel\n" +
                "            at panel.allbookers.com. Use these credentials to access the system and start managing\n" +
                "            your property<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2. Update Property Information: </strong>In the management panel, you will have the option to\n" +
                "            update your property information, including prices, availability, accommodation\n" +
                "            descriptions, and photos. Ensure that the information is accurate and up-to-date to\n" +
                "            provide a good experience for Client's (Hotelier).<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3. Manage Reservations: </strong> The panel offers the ability to manage reservations, including\n" +
                "            accepting, modifying, and canceling reservations. You will be able to view and handle\n" +
                "            all reservations in one place and communicate with <strong>Client (Hotelier)</strong> regarding any\n" +
                "            important information about their stay.\n" +
                "            <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>4. Review Reports and Analytics: </strong>The panel also provides reports and analytics on the\n" +
                "            daily performance of your property. This information is useful for monitoring activity\n" +
                "            and making necessary improvements to maximize exposure and profitability of your\n" +
                "            property.\n" +
                "            \n" +
                "        </p>\n" +
                "        <h3>5. Management of Reservations and Prices</h3>\n" +
                "        <p><strong>5.1 Setting and Managing Prices and Availability by the Hotelier: </strong>The hotelier is responsible\n" +
                "            for setting and managing the prices and availability of accommodations on the Allbookers.com\n" +
                "            platform. This process includes setting prices for different periods, offering special rates or\n" +
                "            promotions, and determining availability based on their needs and preferences. The hotelier is also\n" +
                "            responsible for updating information in the management panel to reflect changes in prices and\n" +
                "            availability, ensuring that the information is always accurate and compliant with the platform's\n" +
                "            policies.</p>\n" +
                "        <p><strong>5.2 Policies for cancellations and changes in bookings: </strong>The Allbookers.com platform offers\n" +
                "            flexibility in managing cancellation and modification policies for reservations. The hotelier must\n" +
                "            define and publish policies for cancellations and modifications in accordance with the platform's\n" +
                "            rules and their preferences. These policies must be clear and understandable for Client (Hotelier)\n" +
                "            and provide detailed information about deadlines and conditions for canceling or modifying\n" +
                "            reservations. The hotelier is responsible for adhering to these policies and ensuring that all\n" +
                "            cancellation and modification requests are handled in accordance with the established rules.</p>\n" +
                "        <p><strong>5.3 Payment and Commission Procedures: </strong>Payments for reservations will be processed in\n" +
                "            accordance with the procedures and deadlines set by the Allbookers.com platform. All reservations\n" +
                "            will be processed with credit card information, and payment will be made directly upon completion\n" +
                "            of the reservation. This means that reservations are secure, and payments will always be managed\n" +
                "            by Allbookers.com. Commissions for the provided services will be automatically deducted from\n" +
                "            reservation payments and will be detailed in the management panel. The hotelier is responsible for\n" +
                "            verifying the accuracy of payments and commissions and ensuring that all transactions are\n" +
                "            completed properly and in compliance with the platform's terms <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>5.3.1 Overbookings: </strong>Allbookers.com ensures that it manages payments through the platform and\n" +
                "            takes responsibility for any overbookings. This means that in cases of overbookings,\n" +
                "            Allbookers.com will take the necessary steps to resolve the situation and assumes responsibility\n" +
                "            for the process. In the event of overbooking, Allbookers.com will handle the issue and ensures that\n" +
                "            the guest is accommodated. If necessary, the hotel will cover the additional cost of accommodating\n" +
                "            the guest in a higher-standard property than the one originally booked.\n" +
                "        </p>\n" +
                "        <h3>6. Payments and Fees </h3>\n" +
                "        <p><strong>6.1 Fee and Commission Structure of the Platform: </strong>Allbookers.com implements a clear fee and\n" +
                "            commission structure for its services. The commission for each reservation made through the\n" +
                "            platform is 10% of the total reservation amount. This commission applies to each transaction and\n" +
                "            is included in the final payment that the hotelier will receive after the reservation is fulfilled. The\n" +
                "            fee and commission structure may be updated by the platform, and its details will always be\n" +
                "            available in the management panel.\n" +
                "            </p>\n" +
                "        <p><strong>6.2 Payment Methods and Deadlines: </strong>After completing the reservation, the guest must add their credit card details to the booking form.\n" +
                "            Allbookers.com will verify the validity of the card to ensure it is legitimate but will not process the payment.\n" +
                "            The payment for the reservation will be made directly at the property according to the hotel's terms.\n" +
                "            The card information will remain linked to the reservation for reference and security, but all financial transactions will be handled by the hotel and not by Allbookers.com.</p>\n" +
                "        <p><strong>6.3 Monthly Reservation Invoices: </strong>On the 1st of each month, Allbookers.com will compile all\n" +
                "            reservations made during the previous month and will automatically generate an invoice for these\n" +
                "            reservations in the following month. The invoice will include the 10% commission for each\n" +
                "            reservation. The invoice will be available to the hotelier and will contain all necessary information\n" +
                "            for the payment of commissions. In cases of no-shows or prepaid bookings, options will be\n" +
                "            provided to discuss and resolve issues related to reservations in accordance with the platform's\n" +
                "            policies</p>\n" +
                "        <p><strong>6.4 Policies on payment delays or unpaid commissions: </strong>In cases of late payments or unpaid\n" +
                "            commissions, Allbookers.com will follow the procedures set out to handle these situations. The\n" +
                "            hotelier is responsible for paying all unpaid commissions according to the specified deadlines. In\n" +
                "            cases of delays, the platform may apply penalties specified in the Agreement, including additional\n" +
                "            fees for late payment. In the event of ongoing delays in payments, Allbookers.com reserves the\n" +
                "            right to suspend or terminate services until all financial obligations are settled.</p>\n" +
                "        <h3>7. Security and Data Protection </h3>\n" +
                "        <p><strong>7.1 Data Protection for Client (Hotelier) and Properties: </strong>Allbookers.com is committed to\n" +
                "            protecting and securely maintaining Client (Hotelier) and property data in a reliable manner. The\n" +
                "            data we collect includes personal and financial information of Clients (Hotelier), as well as\n" +
                "            property data registered on the platform. This data is stored in compliance with applicable data\n" +
                "            protection legislation and the highest security standards. The data will be used only for the\n" +
                "            purposes for which it was collected and will not be distributed or used for other purposes without\n" +
                "            the Client's (Hotelier) consent.</p>\n" +
                "        <p><strong>7.2 Security Measures for Personal and Financial Data: </strong>Allbookers.com implements advanced\n" +
                "            security measures to protect the hoteliers' personal and financial data. These measures include the\n" +
                "            use of encryption technologies to safeguard information during transmission and storage on our\n" +
                "            servers. Additionally, the platform uses access control mechanisms and strict procedures to ensure\n" +
                "            that only authorized individuals have access to sensitive data. In the event of any risk or breach of\n" +
                "            the system, Allbookers.com will take immediate action to address and resolve security issues and\n" +
                "            will notify <strong>Client (Hotelier)</strong> and relevant authorities as required by law.\n" +
                "            </p>\n" +
                "            <h3>8. Legal Responsibilities </h3>\n" +
                "            <p><strong>8.1 Responsibilities for Non-Compliance with Agreement Terms: </strong>Each party is obligated to\n" +
                "                adhere to all terms and provisions set forth in this Agreement. In the event of non-compliance with\n" +
                "                the Agreement terms by the Client (Hotelier) , Allbookers.com has the right to take appropriate\n" +
                "                measures to rectify the breaches, including but not limited to, suspending access to the platform,\n" +
                "                suspending services, or terminating the Agreement. The Client (Hotelier) is responsible for\n" +
                "                compensating any damages caused as a result of non-compliance with the Agreement terms and\n" +
                "                for covering any expenses related to resolving issues created by their breaches.</p>\n" +
                "            <p><strong>8.2 Limits of the Platform's Liability for Reservations or Other Client (Hotelier) -Related\n" +
                "                Issues: </strong>Allbookers.com will be responsible for providing a reliable platform and fulfilling its\n" +
                "                obligations under this Agreement. Allbookers.com takes full responsibility for any instance of\n" +
                "                overbooking and will take the necessary steps to resolve such situations as outlined in the\n" +
                "                Agreement. However, the platform will not be liable for any direct or indirect damages that may\n" +
                "                result from other bookings, problems with bookings, or other issues related to clients or property\n" +
                "                data. In accordance with applicable law, Allbookers.com's liability will be limited to the amount\n" +
                "                representing the fees paid by the client for the services provided by the platform. The client\n" +
                "                acknowledges and understands that any issue related to bookings, payments, or any other problem\n" +
                "                will be addressed through the procedures outlined in this Agreement and with the assistance of the\n" +
                "                platform's technical support, according to the specified terms</p>\n" +
                "            <h3>9. Termination of the Agreement and Property Closure</h3>   \n" +
                "            <p><strong>9.1 Termination Conditions</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.1.1 Commencement and Termination of the Agreement: </strong>Unless otherwise specified, the\n" +
                "                Agreement will commence on the date of acceptance by the Property and will continue for an\n" +
                "                indefinite period.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.1.2 Termination by the Client (Hotelier): </strong>The Client (Hotelier) has the right to terminate the\n" +
                "                Agreement if Allbookers.com fails to fulfill its obligations in accordance with the terms of the\n" +
                "                Agreement. The Client (Hotelier) must provide written notice to contract@allbookers.com for\n" +
                "                termination of the Agreement and provide a remedy period to address the raised issues if necessary.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.1.3 Termination by Allbookers.com: </strong>Allbookers.com has the right to terminate the Agreement\n" +
                "                if the Client (Hotelier) breaches the terms of the Agreement, including but not limited to, noncompliance with platform policies, providing inaccurate information, or any action that damages\n" +
                "                the platform's reputation. Allbookers.com will notify the Client (Hotelier) of the breaches and offer\n" +
                "                a remedy period, except where the breach is severe and unacceptable, in which case no remedy\n" +
                "                period is required.\n" +
                "                \n" +
                "            </p>\n" +
                "            <p><strong>9.2 Immediate Termination and Property Closure</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.2.1 Immediate Termination: </strong>Either Party may terminate the Agreement (and close the Property\n" +
                "                on the Platform) or limit or suspend (all or part of the obligations, commitments, and\n" +
                "                responsibilities) under this Agreement with the other Party, with immediate effect and without the\n" +
                "                need for a notice of breach in the event of: (i) a legal or regulatory obligation; (ii) an imperative\n" +
                "                reason in accordance with applicable law; (iii) a repeated breach of the Agreement by the other\n" +
                "                Party; or (iv) a significant (actual or suspected) breach by the other Party of any term of this\n" +
                "                Agreement, instances of illegal or inappropriate content, fraud, provision of false information, or\n" +
                "                receipt of a significant number of complaints from Guests; or (v) (a submission or filing of a\n" +
                "                bankruptcy petition, or suspension of payments, or any similar action or event related to the other\n" +
                "                Party).\n" +
                "            </p>\n" +
                "            <p><strong>9.3 Notice and Payment After Termination</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.3.1 Notice of Termination: </strong>Any notice or communication from Allbookers.com regarding the\n" +
                "                \"closure\" (or similar suspend) of the Property on the website will imply the termination of the\n" +
                "                Agreement. After the termination or suspension of the Agreement, the Property must honor any\n" +
                "                unpaid reservations for Guests and pay all commissions (including costs, expenses, and interest if\n" +
                "                applicable) that are due for these reservations in accordance with the terms of the Agreement.\n" +
                "                Following the termination or suspension of the Agreement, and notwithstanding Allbookers.com's\n" +
                "                right to remove the Property from the Platform, Allbookers.com may retain and store the property\n" +
                "                page on the management Platform, not displaying it publicly but marking the availability as\n" +
                "                \"suspend\" pending full and final payment of any outstanding amounts (including any\n" +
                "                Commission).\n" +
                "            </p> \n" +
                "            <h3>10. General Provisions</h3>\n" +
                "            <p><strong>10.1 Amendments to the Agreement: </strong>Amendments to this Agreement can only be made with the\n" +
                "                written agreement of both parties. Any changes or additions to the Agreement will be valid only if\n" +
                "                documented and signed by authorized representatives of both parties. If a change is necessary to\n" +
                "                meet new legal or regulatory requirements, the parties will collaborate to make the necessary\n" +
                "                updates to the Agreement and will inform each other of such changes as soon as possible.\n" +
                "                </p>\n" +
                "            <p><strong>10.2 Transfer of Rights and Obligations (Change of Ownership): </strong>If the owner of a property\n" +
                "                wishes to transfer ownership to another party, they must follow the procedure outlined below to\n" +
                "                request a transfer of ownership through the platform panel.allbookers.com: <br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>1. Ownership Transfer Request: </strong>The current owner must submit a request for the transfer\n" +
                "                of ownership through the available option on panel.allbookers.com. This request should\n" +
                "                include complete information about the proposed new owner and all other relevant details\n" +
                "                related to the property<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>2. Verification: </strong>Allbookers.com will conduct the necessary verifications to confirm the\n" +
                "                identity of the new owner and to ensure that the property transfer complies with the\n" +
                "                platform's policies and procedures.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>3. Approval and New Agreement: </strong>Once verifications are completed and the request is\n" +
                "                accepted, Allbookers.com will issue a new Agreement that will include the new property\n" +
                "                details, the new owner's name, and any other relevant information. The new Agreement\n" +
                "                will replace the existing Agreement and will take effect upon being signed by both\n" +
                "                parties<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>4. Notification to the New Owner: </strong>After the approval and signing of the new Agreement,\n" +
                "                the new owner will be notified and will gain access to the management panel to continue\n" +
                "                managing the property and bookings in accordance with the new Agreement terms.\n" +
                "            </p>\n" +
                "            <h3>11. Ranking, Guest Reviews and Marketing </h3>\n" +
                "            <p><strong>11.1 Ranking: </strong>Allbookers.com aims to display search results that are relevant to each individual\n" +
                "                Guest by offering a personalized ranking of properties on the platform. This system may include\n" +
                "                criteria for ranking properties in search results and may be based on various factors such as service\n" +
                "                quality, user experience, and overall property performance. Allbookers.com reserves the right to\n" +
                "                change the ranking algorithm in accordance with the platform's needs and to ensure a satisfactory\n" +
                "                user experience</p>\n" +
                "            <p><strong>11.2 Guest Reviews: </strong>Guests who have stayed or had an experience at a property will be asked by\n" +
                "                Allbookers.com to provide feedback and a rating on specific aspects of their experience with the\n" +
                "                property. Allbookers.com may publish these reviews on the platform. Allbookers.com acts as a\n" +
                "                distributor and not a publisher of these reviews. Allbookers.com will evaluate Guest reviews in\n" +
                "                accordance with applicable policies. Allbookers.com will not be responsible for the reviews\n" +
                "                displayed or not displayed on the platform in accordance with applicable law.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>11.2.1 </strong>Allbookers.com may, at its sole discretion, keep reviews hidden from display on the\n" +
                "                platform, remove reviews, or request a Guest to provide a revised version of a review if it contains\n" +
                "                or refers to any content that Allbookers.com deems inappropriate and/or offensive, including but\n" +
                "                not limited to: (i) politically sensitive comments; (ii) illegal activities; (iii) personal or sensitive\n" +
                "                information (e.g., emails, addresses, phone numbers, or credit card information); (iv) links to other\n" +
                "                websites; (v) inappropriate language, sexual references, hate speech, discriminatory comments,\n" +
                "                threats, insults, or references to violence. <br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>11.2.2 </strong>Property owners must not manipulate or attempt to manipulate Guest reviews (e.g., by\n" +
                "                paying for positive reviews or posting fake reviews for a competing property).<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>11.2.3 </strong>Guest reviews are for the exclusive use of Allbookers.com. Property owners are not entitled\n" +
                "                to use Guest reviews directly or indirectly in any way without prior written approval fromAllbookers.com.</p>\n" +
                "            <p><strong>11.3 Online Marketing</strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>11.3.1 </strong>Allbookers.com engages in online marketing campaigns at its own cost and discretion and\n" +
                "            may promote the property using the property's name in such marketing, including email marketing.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>11.3.2 </strong>If the property owner becomes aware of any third-party platform behavior that infringes on\n" +
                "            their intellectual property rights, they must immediately notify Allbookers.com in writing with\n" +
                "            details of the behavior, and Allbookers.com will use reasonable commercial efforts to ensure that\n" +
                "            the third party takes appropriate measures to address the infringement.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>11.3.3 </strong>The property owner agrees not to use, directly or indirectly, the Allbookers.com brand/logo\n" +
                "            (including the business name, trademark, service mark, or any other similar sign of identity or\n" +
                "            source) for comparative pricing purposes or for any other purpose, whether on the property's\n" +
                "            platform or on any third-party platform, system, search engine, or otherwise, unless previously\n" +
                "            approved in writing by Allbookers.com.</p>\n" +
                "            <h3>12. Miscellaneous</h3>\n" +
                "            <p><strong>12.1 Indemnification by the Client (Hotelier): </strong>The Client (Hotelier) agrees to indemnify,\n" +
                "                defend, and hold harmless Allbookers.com, its affiliates, officers, directors, employees, and agents\n" +
                "                from and against any and all claims, liabilities, indemnities, losses, costs, and expenses (including\n" +
                "                reasonable attorneys' fees) arising from or related to (i) any breach of any term of the Agreement\n" +
                "                by the Client (Hotelier) ; (ii) any claim made by Guests related to the Property; (iii) any violation\n" +
                "                of applicable laws or regulations by the Client (Hotelier) ; and (iv) any negligence or misconduct\n" +
                "                by the Client (Hotelier).<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>12.1.1 Indemnification by Allbookers.com: </strong>Allbookers.com agrees to indemnify, defend, and\n" +
                "                hold harmless the Client (Hotelier) from and against any and all claims, liabilities, indemnities,\n" +
                "                losses, costs, and expenses (including reasonable attorneys' fees) arising from or related to (i) any\n" +
                "                breach of any term of the Agreement by Allbookers.com; (ii) any violation of applicable laws or\n" +
                "                regulations by Allbookers.com; and (iii) any negligence or misconduct by Allbookers.com\n" +
                "            </p>\n" +
                "            <p><strong>12.2 Limitation of Liability: </strong>Except as otherwise provided in the Agreement, neither Party shall\n" +
                "                be liable to the other Party for any indirect, incidental, consequential, special, or punitive damages,\n" +
                "                including but not limited to, loss of profits, loss of business, or loss of data, arising from or in\n" +
                "                connection with the Agreement, regardless of the cause of action, even if such damages were\n" +
                "                foreseeable or if the Party was advised of the possibility of such damages<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>12.2.1 Maximum Liability Limit: </strong>The maximum liability of either Party for any claim arising\n" +
                "                    from or in connection with the Agreement shall be limited to the amount of fees paid by the Client\n" +
                "                    (Hotelier) to Allbookers.com under the Agreement during the six (6) month period immediately\n" +
                "                    preceding the event giving rise to the claim\n" +
                "            </p>\n" +
                "            <p><strong>12.3 No Liability for Third-Party Services: </strong>Allbookers.com shall not be liable for any issues or\n" +
                "                damages arising from the use of third-party services or products not provided directly by\n" +
                "                Allbookers.com, including but not limited to, payment processing services, external booking\n" +
                "                platforms, or other third-party integrations</p>\n" +
                "            <p><strong>12.4 Force Majeure: </strong>Neither Party shall be liable for any failure to perform its obligations under\n" +
                "                the Agreement if such failure is due to events beyond its reasonable control, including but not\n" +
                "                limited to, natural disasters, war, terrorism, strikes, or other unforeseen events.</p>\n" +
                "            <p><strong>12.5 Legal Actions</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>12.5.1 Dispute Resolution: </strong>Any dispute arising from or in connection with the Agreement shall\n" +
                "                be resolved in accordance with the dispute resolution process outlined in the Agreement, including\n" +
                "                any requirement for mediation or arbitration, if applicable\n" +
                "            </p></div></div>");
        String htmlContent = stringBuilder.toString();

        // Konverto HTML në PDF
        byte[] pdfData = convertHtmlToPdf(htmlContent);

        // Shtoni PDF-në si bashkëngjitje në email
        helper.setText(email, true);
        helper.addAttachment("Agreement.pdf", new ByteArrayResource(pdfData), "application/pdf");

        send(message);
        System.out.println("Mail for accept Request for agreement Send...");
    }

    public void sendemailwhengroupaccountAddnewPropertyAdmin(Property property, Role specialrole) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("info@allbookers.com");
        helper.setSubject(" A new property (" + property.getName() + " ) was added by the group account with Legal Entity ID " + specialrole.getRoleLoginId());

        String htmlContent =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div>" +
                        "  <p>Dear Allbookers.com Team</p>" +
                        "  <p>" +
                        " A new property was added by the group account with Legal Entity ID " + specialrole.getRoleLoginId() + " ." +
                        "  </p>" +
                        "  <p><strong>Property Details:</strong></p>" +
                        "  <ul style=\"padding-left: 20px;\">" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Name:</strong> " + property.getName() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property ID:</strong> " + property.getId() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Address:</strong> " + property.getAddress().getStreet() + ", " + property.getAddress().getCity().getFull_name() + ", " + property.getAddress().getCity().getCounty().getCountry().getCountry_name() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Type:</strong> " + property.getHotel_type().getType() + "</li>" +
                        "  </ul>" +
                        "</div>" +
                        "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                        "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                        "<br>This e-mail was sent by allbookers.com.</p>";

        helper.setText(htmlContent, true);
        send(message);

    }

    public void sendemailwhengroupaccountRemovedProperty(User user, Property property , Role specialrole) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject(property.getName() +" Removed from group account with legal entity ID " + specialrole.getRoleLoginId());

        String htmlContent =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div>" +
                        "  <p>Dear <strong>" + user.getFull_name() + "</strong>,</p>" +
                        "  <p>" +
                        "    We wanted to let you know that <strong>" + property.getName() +
                        "    </strong>, has been successfully removed from your legal entity ID " + specialrole.getRoleLoginId() + "." +
                        "  </p>" +
                        "  <p><strong>Property Details:</strong></p>" +
                        "  <ul style=\"padding-left: 20px;\">" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Name:</strong> " + property.getName() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property ID:</strong> " + property.getId() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Address:</strong> " + property.getAddress().getStreet() + ", " + property.getAddress().getCity().getFull_name() + ", " + property.getAddress().getCity().getCounty().getCountry().getCountry_name() + "</li>" +
                        "  </ul>" +
                        "  <p>" +
                        "    If you believe this removal was made in error, please contact our support team as soon as possible. <br>" +
                        "    We are here to help with any further questions you might have." +
                        "  </p>" +
                        "  <p style=\"margin-top: 20px;\">" +
                        "    Best regards,<br>" +
                        "    The Allbookers.com Team" +
                        "  </p>" +
                        "</div>" +
                        "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                        "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                        "<br>This e-mail was sent by allbookers.com.</p>";

        helper.setText(htmlContent, true);
        send(message);
    }

    public void sendemailwhengroupaccountRemovedPropertyAdmin(Property property, Role specialrole) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("info@allbookers.com");
        helper.setSubject(" A property (" + property.getName() + ") has been removed from the group account with Legal Entity ID " + specialrole.getRoleLoginId());

        String htmlContent =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div>" +
                        "  <p>Dear Allbookers.com Team</p>" +
                        "  <p>" +
                        " A property (" + property.getName() + ") has been removed from the group account with Legal Entity ID " + specialrole.getRoleLoginId()+ " ."+
                        "  </p>" +
                        "  <p><strong>Property Details:</strong></p>" +
                        "  <ul style=\"padding-left: 20px;\">" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Name:</strong> " + property.getName() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property ID:</strong> " + property.getId() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Address:</strong> " + property.getAddress().getStreet() + ", " + property.getAddress().getCity().getFull_name() + ", " + property.getAddress().getCity().getCounty().getCountry().getCountry_name() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Type:</strong> " + property.getHotel_type().getType() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Account Group:</strong> " + specialrole.getRoleLoginId() + "</li>" +
                        "  </ul>" +
                        "</div>" +
                        "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                        "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                        "<br>This e-mail was sent by allbookers.com.</p>";

        helper.setText(htmlContent, true);
        send(message);

    }

    public void sendMailPinCodeUser(User user, String uniqueCode) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("User confirmation code!");

        String htmlContent = "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>" +
                "<br><div style=\"text-align: center; width:580px; margin: auto;\">" +
                "<p style=\"width: 60%; margin-left: 20%; font-size: 16px; color:black !important; font-family: BlinkMacSystemFont,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;\">" +
                "Your user confirmation code is:<b>" + uniqueCode + "</b><br> \n" +
                "<br>Best Regards,<br>\n" +
                "The Allbookers.com Team</p></div>" +
                "</div><br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                "<br>This e-mail was sent by allbookers.com.</p></div>";

        helper.setText(htmlContent, true);
        send(message);

    }

    private String escapeHtml(String input) {
        return input.replaceAll("&", "&amp;");
    }

    //    Request for agreement
    public void Requestforagreement(AgreementRequest agreementRequest) throws MessagingException{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(agreementRequest.getProperty().getAddress().getEmail());
        helper.setSubject("Request for agreement from allbookers.com ");


        String htmlContent = "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>"
                + "<div style=\"text-align: left; width:580px; margin: 15px auto;\">"
                +"<h2>Dear " + agreementRequest.getProperty().getName() + ", </h2>"
                + "<p style=\"font-size: 16px;\">We are happy to let you know that <a href=\"https://allbookers.com/\" style=\"text-decoration: none;\"> Allbookers.com</a> has requested an agreement for the property "+ agreementRequest.getProperty().getName() + ". </p>"
                + "<p style=\"font-size: 16px;\">You can view it on <a href=\"https://panel.allbookers.com/\" style=\"text-decoration: none;\"> panel.allbookers.com</a>. </p>"
                + "<p style=\"font-size: 16px;\">We wish you the best of luck in your new venture. </p><br>"
                + "<p style=\"font-size: 16px;\">Kind regards,</p>"
                + "<p style=\"font-size: 16px;\">The <a href=\"https://allbookers.com/\" style=\"text-decoration: none;\">Allbookers.com </a> Team\n</p>"
                + "</div><br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
                + "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved."
                + "<br>This e-mail was sent by allbookers.com.</p></div>";

        helper.setText(htmlContent, true);
        send(message);
        System.out.println("Mail for Request for agreement Send...");
    }

    //    Refuse Request for agreement
    public void refuseRequestforagreement(AgreementRequest agreementRequest) throws MessagingException{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("info@allbookers.com");
        helper.setSubject(agreementRequest.getProperty().getName() + " refused the request for an agreement. ");


        String htmlContent = "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>"
                + "</div><div style=\"text-align: left; width:580px; margin: 15px auto;\">"
                + "<p style=\"font-size: 20px;\">Your request for an agreement has been <b><span style=\"color:red\"> refused </span></b> by "+ agreementRequest.getProperty().getName() + ". </p><br>"
                + "<p style=\"font-size: 20px;\">You can resend it from <a href=\"https://panel.allbookers.com/\" style=\"text-decoration: none;\"> panel.allbookers.com .</a> </p>"
                + "</div><br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
                + "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved."
                + "<br>This e-mail was sent by allbookers.com.</p></div>";

        helper.setText(htmlContent, true);
        send(message);
        System.out.println("Mail for refuse Request for agreement Send...");
    }

    //i dergohet email Allbookers ne rast se hotelieri pranon marrveshjen
    public void acceptRequestforagreement(Agreement agreement,AgreementRequest agreementRequest) throws MessagingException, DocumentException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("info@allbookers.com");
        helper.setSubject(agreementRequest.getProperty().getName() + " accepted the request for an agreement. ");

        String htmlContent = "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>"
                + "<div style=\"text-align: left; width:580px; margin: 15px auto;\">"
                + "<p style=\"font-size: 20px;\">Your request for an agreement has been <b><span style=\"color:green\"> accepted </span></b> by "+ agreementRequest.getProperty().getName() + ". </p>"
                + "<p style=\"font-size: 20px;\">You can view it from <a href=\"https://panel.allbookers.com/\" style=\"text-decoration: none;\"> panel.allbookers.com .</a> </p>"
                + "</div><br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
                + "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved."
                + "<br>This e-mail was sent by allbookers.com.</p></div>";

        helper.setText(htmlContent, true);

        // Generate PDF content
        StringBuilder pdfContentBuilder = new StringBuilder();
        pdfContentBuilder.append("<div class=\"mainDiv\">\n" +
                "      <div class=\"column\" style=\"float: left; width: 100%\">\n" +
//                    "    <img src=\"https://allbookers.com/images/allbookersorange.png\" style=\"width: 350px; margin-bottom: 5px;\"></img>\n" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "      </div>\n" +
                "    <div style=\"text-align: left; \">\n" +
                "<h2>Marrëveshja e Akomodimit me Allbookers.com</h2>\n" +
                " <p>Ky dokument përfaqëson një marrëveshje ligjore mes:</p>\n" +
                " <p>Ndërmjet:</p>\n" +
                " <p><strong>Allbookers</strong>, një platformë e operuar nga <strong>InterMedia Sh.P.K</strong>, një shoqëri e regjistruar sipas ligjeve të Republikës së Shqipërisë, me seli në <strong>Bulevardi Bajram Curri tek ura e ATSH, nr pasurie 6/516+1-20</strong>, NIPT <strong>M02028001D</strong>, këtu e tutje referuar si \"Kompani\" ose \"Allbookers\",</p>\n" +
                " <p><strong>Dhe</strong></p>\n" +
                " <p><strong>"+agreement.getFirst_name()+" "+agreement.getLast_name()+"</strong>, pronar i pronës <strong>"+agreement.getProperty().getName()+"</strong>, e vendosur në <strong>"+agreement.getAddress()+"</strong>, NIPT <strong>"+agreement.getNuis()+"</strong>, referuar si \"Klienti (Hotelieri)\" ose \"Hotelieri\",</p>\n" +
                " <h3>Marrëveshja përfshin kushtet e mëposhtme :</h3>\n" +
                " <p>Për të gjitha pronat që do të regjistrohen në Allbookers.com të listuara nën emrin e Partnerit,\n" +
                "     zbatohen përqindjet e mëposhtme të komisionit lokal:</p>\n" +
                " <ul><li>Shqipëri: 10%</li></ul>\n" +
                " <p><strong>Allbookers.com</strong> ofron një platformë të sigurt dhe të besueshme për menaxhimin e pronave dhe\n" +
                "     rezervimeve.</p>\n" +
                " <h3>Ekzekutimi dhe performanca</h3>\n" +
                " <p>Marrëveshja është e vlefshme vetëm pas miratimit dhe konfirmimit nga Allbookers.com.</p>\n" +
                " <h3>Kushtet e Përgjithshme të Marrëveshjes:</h3>\n" +
                " <p>Kjo Marrëveshje përcakton termat dhe kushtet për përdorimin e platformës Allbookers.com nga\n" +
                "     hotelieri për të reklamuar dhe menaxhuar pronën e tij, duke përfshirë pranimin e rezervimeve,\n" +
                "     menaxhimin e çmimeve dhe disponueshmërisë dhe përdorimin e panelit të administrimit aksesuar\n" +
                "     në <a href=\"https://panel.allbookers.com\">panel.allbookers.com</a>.</p>\n" +
                " <h3>Deklarimi nga Partneri</h3>\n" +
                " <p>Partneri deklaron se kjo është një veprimtari legjitime akomodimi me të gjitha licencat dhe lejet e\n" +
                "     nevojshme, të cilat mund të tregohen me kërkesën e parë. Allbookers.com . rezervon të drejtën të\n" +
                "     verifikojë dhe hetojë çdo detaj të dhënë nga Partneri në këtë regjistrim.</p>\n" +
                " <h3>Kohëzgjatja e Marrëveshjes</h3>\n" +
                " <p>Kjo marrëveshje do të jetë e vlefshme nga data e nënshkrimit dhe do të vazhdojë të jetë në fuqi\n" +
                "     deri në përfundimin ose anulimin nga njëra palë në përputhje me termat e kësaj Marrëveshjeje.</p>\n" +
                " <p><strong style=\"font-size: 18px;\">Data e fillimit të Marrëveshjes: </strong>"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+ "</p> " +
                "<p><br><br></br></br></p>\n" +
                " <h2>Përmbajtja e Marrëveshjes</h2>\n" +
                " <p><strong>1. Përkufizimet</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>1.1</strong>Shpjegimi i termave të përdorur në Marrëveshje, përfshirë: \"Platforma\", \"Prona\",\n" +
                "     \"Rezervimi\", \"Paneli i Menaxhimit\", \"Klienti (Hotelieri)\" etj. <br></br>\n" +
                "     <strong>2. Qëllimi i Marrëveshjes</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>2.1 </strong>Përshkrimi i shërbimit të ofruar nga Allbookers.com. <br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>2.2 </strong>Qëllimi i marrëveshjes dhe përdorimi i platformës nga Klienti për menaxhimin e pronësdhe rezervimeve. <br></br>\n" +
                "     <strong>3. Të Drejtat dhe Detyrimet e Palëve</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>3.1</strong> Detyrimet e Allbookers.com. <br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>3.2 </strong>Detyrimet e Pronës. <br></br>\n" +
                "     <strong>4. Regjistrimi dhe Përdorimi i Platformës</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>4.1</strong> Procesi i regjistrimit të pronës. <br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>4.2</strong> Hapat për përdorimin e panelit të menaxhimit dhe përditësimin e informacionit të\n" +
                "     pronës.<br></br>\n" +
                "     <strong>5. Menaxhimi i Rezervimeve dhe Çmimeve</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>5.1</strong> Përcaktimi dhe menaxhimi i çmimeve dhe disponueshmërisë nga   hotelieri. <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>5.2</strong> Politikat për anullimet dhe ndryshimet e rezervimeve.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>5.3 </strong>Procedura e Pagesave dhe Komisioneve.<br></br>\n" +
                "     <strong>6. Pagesat dhe Tarifat</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>6.1 </strong>Struktura e tarifave dhe komisioneve të platformës. <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>6.2 </strong>Metodat e Pagesës dhe Afatet. <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>6.3 </strong>Fatura Mujore për Rezervimet. <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>6.4 </strong>Politikat për vonesat në pagesë ose komisionet e papaguara. <br></br>\n" +
                "     <strong>7. Siguria dhe Mbrojtja e të Dhënave</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>7.1 </strong>Mbrojtja e të dhënave për Klientët dhe Pronat.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>7.2 </strong>Masat e sigurisë për të dhënat personale dhe financiare.<br></br>\n" +
                "     <strong>8. Përgjegjësitë Ligjore</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>8.1 </strong>Përgjegjësitë për Mosrespektimin e Kushteve të Marrëveshjes.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>8.2 </strong>Kufizimet e Përgjegjësisë së Platformës për Rezervimet ose Çështjet e tjera që lidhen\n" +
                "     me Klientët.<br></br>\n" +
                "     <strong>9. Përfundimi i Marrëveshjes dhe Mbyllja e Pronës</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>9.1 </strong>Kushtet për përfundimin.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>9.2 </strong>Përfundimi i menjëhershëm dhe mbyllja e pronës.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>9.3 </strong>Njoftimi dhe Pagesat pas Përfundimit.<br></br>\n" +
                "     <strong>10. Dispozita të Përgjithshme</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>10.1 </strong>Ndryshimet në Marrëveshje<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>10.2 </strong>Transferimi i të drejtave dhe detyrimeve, Ndryshimi i Pronësisë.<br></br>\n" +
                "     <strong>11. Renditja, Vlerësimet e Mysafirëve dhe Marketingu</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.1 </strong>Renditja<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.2 </strong>Vlerësimet e Mysafirëve<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.3 </strong>Marketingu Online<br></br>\n" +
                "     <strong>12. Të Ndryshme</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.1 </strong>Dëmshpërblimi nga Klienti<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.2 </strong>Kufizimi i Përgjegjësisë<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.3 </strong>Asnjë Përgjegjësi për Shërbimet e Palëve të Treta<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.4 </strong>Forca Madhore<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.5 </strong>Veprimet Ligjore<br></br>\n" +
                " </p>\n" +
                "<p><br></br><br></br></p>\n" +
                " <h3>1. Përkufizimet</h3>\n" +
                " <p> Përveç termave të përcaktuar në pjesë të tjera të kësaj Marrëveshjeje, termat e mëposhtëm do të\n" +
                "     kenë kuptimin e caktuar në gjithë Marrëveshjen, përveç nëse ka një qëllim të kundërt:</p>\n" +
                " <p><strong>\"Akomodimi\"</strong> i referohet entitetit (person juridik ose fizik) që është palë në Marrëveshje. Nëse\n" +
                "     është ndryshe, do të thotë gjithashtu prona që është lidhur me Marrëveshjen që entiteti po\n" +
                "     kontrakton dhe për të cilën këto Kushte janë të aplikueshme.</p>\n" +
                " <p><strong>\"Çmimi i Dhomës\"</strong>: Çmimi i caktuar nga Hotelieri për njësitë akomoduese në Pronën e tij/saj, " +
                "     siç është i përcaktuar në Panelin e Menaxhimit dhe i paraqitur në Platformë për mysafirët.</p>\n" +
                " <p><strong>\"Informacioni i Akomodimit\"</strong> i referohet detajeve të ofruara nga dhe të lidhura me Akomodimin për t'u shfaqur në Platformë. " +
                "     Kjo përfshin imazhe, foto dhe përshkrime; informacion mbi shërbimet dhe pajisjet; disponueshmërinë e dhomave për rezervim; " +
                "     çmimet (përfshirë të gjitha taksat e zbatueshme, përveç rasteve kur kërkohet ndryshe me ligj, si dhe çdo tarifë shtesë); " +
                "     politikat e disponueshmërisë, anulimit dhe mungesës së paraqitjes; politika dhe kufizime të tjera të rëndësishme; " +
                "     si dhe çdo informacion tjetër që Allbookers.com është ligjërisht i detyruar të shfaqë në Platformë.</p>\n" +
                " <p><strong>\"Komisioni\"</strong>: Tarifa e paguar nga Klienti për shërbimet e ofruara nga Allbookers.com, përfshirë\n" +
                "     ndërmjetësimin e rezervimeve dhe shërbime të tjera të lidhura.</p>\n" +
                " <p><strong>\"Klienti (Hotelieri)\"</strong>: Një individ ose entitet ligjor që regjistron dhe menaxhon një Pronë në\n" +
                "     Platformën Allbookers.com, dhe që përdor shërbimet e Platformës për të menaxhuar rezervimet\n" +
                "     dhe promovimin e Pronës së tij/saj.</p>\n" +
                " <p><strong>\"Paneli i Menaxhimit\"</strong>: Mjeti i ofruar nga Allbookers.com që lejon Klientët të menaxhojnë dhe\n" +
                "     të azhurnojnë informacionin e Pronës së tyre, përfshirë çmimet, disponueshmërinë, rezervimet dhe\n" +
                "     komunikimet me Mysafirët.</p>\n" +
                " <p><strong>\"Partneri i Sinkronizimit të Allbookers.com me platforma të tjera\"</strong>: Një kompani ose individ\n" +
                "     që ofron shërbime për lidhjen e informacionit dhe integrimin midis Platformës Allbookers.com\n" +
                "     dhe sistemeve të tjera për të përmirësuar funksionalitetin dhe efikasitetin e shërbimeve të\n" +
                "     rezervimeve.</p>\n" +
                " <p><strong>\"Platforma\"</strong>: Do të thotë Allbookers.com, një sistem online që mundëson regjistrimin,\n" +
                "     menaxhimin dhe promovimin e pronave përmes internetit dhe ofron shërbime të lidhura për\n" +
                "     Klientët dhe Mysafirët.</p>\n" +
                " <p><strong>\"Prona\"</strong>: Çdo njësi akomoduese e regjistruar në Platformën Allbookers.com, përfshirë por pa u\n" +
                "     kufizuar në hotele, apartamente, vilë dhe shtëpi pushimi, që ofrohet për rezervim në emër të\n" +
                "     pronarëve të tyre.</p>\n" +
                " <p><strong>\"Rezervimi\"</strong>: Një marrëveshje elektronike e realizuar përmes Platformës Allbookers.com, midis\n" +
                "     Klientit dhe një Mysafiri për një qëndrim në Pronën e Klientit.</p>\n" +
                " <p><strong>\"Shërbimet e Lidhjes me Channel Manager\"</strong>: Shërbime që mundësojnë lidhjen dhe integrimin\n" +
                "     e informacionit midis Platformës Allbookers.com dhe sistemeve të tjera të menaxhimit të\n" +
                "     rezervimeve ose shërbimeve të tjera.</p>\n" +
                " <p><strong>\"Shërbime të Tjera\"</strong>: Shërbime të tjera të ofruara nga Platforma përveç funksionalitetit të\n" +
                "     zakonshëm të regjistrimit dhe menaxhimit të rezervimeve, duke përfshirë por pa u kufizuar në\n" +
                "     shërbime marketingu, analiza të të dhënave dhe mbështetje teknike.</p>\n" +
                " <p><strong>\"Të Dhënat e Klientit\"</strong>: Informacioni personal dhe të dhënat e tjera që mblidhen dhe ruhen\n" +
                "     nga Platforma për Klientët dhe Mysafirët, duke përfshirë por pa u kufizuar në emrin,\n" +
                "     adresën, informacionin e kontaktit dhe informacionin e pagesave.</p>\n" +
                " <h3>2. Qëllimi i Marrëveshjes</h3>\n" +
                " <h4>2.1 Përshkrimi i shërbimit të ofruar nga Allbookers.com:</h4>\n" +
                " <p>Allbookers.com ofron një platformë online të plotë të dizajnuar për hotelierët për të menaxhuar akomodimet dhe rezervimet e tyre në mënyrë efektive. Platforma ofron një gamë shërbimesh duke  përfshirë:</p>  " +
                "<p>&#160;&#160;&#160;&#160;<strong>1. Menaxhimi i Listimeve të Pronës:</strong> Lejon përdoruesit të krijojnë dhe mbajnë pronat e tyre,<br></br> " +
                " &#160;&#160;&#160;&#160;duke përfshirë përshkrime, foto, facilitetet etj.<br></br>" +
                " &#160;&#160;&#160;&#160;<strong>2. Menaxhimi i Rezervimeve:</strong> Lehtëson përpunimin e rezervimeve, duke përfshirë pranimin, <br></br> " +
                " &#160;&#160;&#160;&#160;modifikimin dhe anullimin e tyre sipas nevojës.<br></br>" +
                " &#160;&#160;&#160;&#160;<strong>3. Kontrolli i Çmimeve dhe Disponueshmërisë:</strong> Ofron mjete për vendosjen dhe përditësimin <br></br> " +
                " &#160;&#160;&#160;&#160;e çmimeve dhe disponueshmërisë në kohë reale.<br></br>" +
                " &#160;&#160;&#160;&#160;<strong>4. Promovimi dhe Vizibiliteti:</strong> Rrit ekspozimin e pronës përmes mjeteve të ndryshme të marketingut <br></br> " +
                " &#160;&#160;&#160;&#160;dhe promovimit për të tërhequr mysafirë nga e gjithë bota.<br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>5. Mbështetje Teknike:</strong> Ofron mbështetje për të ndihmuar me çdo problem që lidhet mepërdorimin <br></br> " +
                " &#160;&#160;&#160;&#160;e platformës.</p>\n" +
                "<h3>2.2 Qëllimi i marrëveshjes dhe përdorimi i platformës nga Klienti për menaxhimin e pronës dhe rezervimeve:</h3>\n" +
                "<p>Qëllimi i kësaj Marrëveshjeje është të përcaktojë termat dhe kushtet sipas të cilëve Klienti(Hotelieri) do të përdorë platformën Allbookers.com. Kjo përfshin:</p>\n" +
                "<p>&#160;&#160;&#160;&#160;<strong>1. Reklamimi dhe Menaxhimi i Pronës:</strong> Klienti bie dakord të përdorë platformën për të listuar dhe <br></br> " +
                "   &#160;&#160;&#160;&#160;menaxhuar pronën e tij, duke siguruar që informacioni të jetë i saktë dhe i përditësuar. <br></br>\n" +
                "   &#160;&#160;&#160;&#160;<strong>2. Menaxhimi i Rezervimeve:</strong> Klienti do të përdorë platformën për të pranuar, modifikuar dhe menaxhuar <br></br>" +
                "   &#160;&#160;&#160;&#160;rezervimet e bëra nga mysafirët<br></br>\n" +
                "   &#160;&#160;&#160;&#160;<strong>3. Mbrojtja e Çmimeve dhe Disponueshmërisë:</strong> Klienti është përgjegjës për vendosjen dhe përditësimin e <br></br> " +
                "   &#160;&#160;&#160;&#160;çmimeve dhe disponueshmërisë së pronës së tij përmes platformës.<br></br>\n" +
                "   &#160;&#160;&#160;&#160;<strong>4. Përputhshmëria me Politikat e Platformës:</strong> Klienti duhet të respektojë termat, politikat  dhe udhëzimet e <br></br> " +
                "   &#160;&#160;&#160;&#160;platformës për të siguruar përdorimin e duhur dhe për të ruajtur cilësinë dhe integritetin e shërbimeve të <br></br> " +
                "   &#160;&#160;&#160;&#160;ofruara\n" +
                "</p>" +
                " <h3>3. Të Drejtat dhe Detyrimet e Palëve</h3>\n" +
                " <strong>3.1 Detyrimet e Allbookers.com:</strong>\n" +
                " <p>&#160;&#160;&#160;&#160;<strong>3.1.1 </strong>Allbookers.com do të sigurojë akses të pandërprerë dhe të besueshëm në platformën e saj për\n" +
                "     të mundësuar që Klienti të menaxhojë pronën dhe rezervimet në mënyrë efikase. Ky akses përfshin\n" +
                "     përdorimin e Panelit të Menaxhimit, <b>panel.allbookers.com</b>  të cilin Klienti do ta përdorë për të\n" +
                "     përditësuar informacionin mbi pronën dhe për të menaxhuar rezervimet.</p>\n" +
                "     &#160;&#160;&#160;&#160;<strong>3.1.2 </strong>Përveç kësaj, Allbookers.com do të ofrojë mbështetje teknike. Kjo mbështetje është e\n" +
                "     dedikuar për të ndihmuar Klientin në përdorimin e platformës dhe për të zgjidhur çdo problem që\n" +
                "     mund të ndodhi gjatë operimit të platformës. Mbështetja teknike përfshin ndihmën për çështjet\n" +
                "     teknike, gabimet në sistem dhe problemet e lidhura me aksesin në platformë.\n <br></br>" +

                " &#160;&#160;&#160;&#160;<strong>3.1.3 </strong>Për të mbrojtur të dhënat e Klientit dhe informacionin e pronës, Allbookers.com do të marrë\n" +
                "     masa të nevojshme për sigurinë e të dhënave. Kjo përfshin përdorimin e teknologjive të avancuara\n" +
                "     të enkriptimit dhe masave të tjera sigurie për të mbrojtur informacionin personal dhe financiar të\n" +
                "     Klientit nga akses i paautorizuar, humbja, ose dëmtimi. <br></br><br></br>" +

                " &#160;&#160;&#160;&#160;<strong>3.1.4 </strong>Allbookers.com gjithashtu do të përdorë strategji të ndryshme marketingu për të promovuar\n" +
                "     pronën e Klientit në platformë dhe në rrjete të tjera të mundshme. Promovimi përfshin përfshirjen\n" +
                "     e pronës në listat e kërkimeve të platformës dhe mund të përfshijë fushata promocionale dhe\n" +
                "     reklamat përkatëse.\n" +
                "     <br></br>" +
                " <h4>3.2 Detyrimet e Pronës:</h4>\n" +
                "   <p>&#160;&#160;&#160;&#160;<strong>3.2.1 </strong>Klienti është përgjegjës për përdorimin e duhur të platformës në përputhje me udhëzimet dhe\n" +
                "     politikat e Allbookers.com. Klienti duhet të sigurojë që përdorimi i platformës të jetë në përputhje\n" +
                "     me ligjin dhe standardet etike. Klienti është përgjegjës për çdo aktivitet që ndodh në llogarinë e tij\n" +
                "     dhe për ndihmën e ofruar për të zgjidhur çështjet që ndodhin gjatë përdorimit të platformës <br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>3.2.2 </strong>Për të siguruar që informacioni mbi pronën e tij është i saktë dhe i përditësuar, Klienti duhet\n" +
                "     të mbajë informacionin mbi pronën e tij, përfshirë çmimet, disponueshmërinë, dhe detajet e tjera,\n" +
                "     të sakta dhe të përditësuara në mënyrë të rregullt. Çdo ndryshim në informacionin e pronës duhet\n" +
                "     të pasqyrohet në platformë menjëherë pas ndodhjes. \n" +
                "    <br></br>" +
                " &#160;&#160;&#160;&#160;<strong>3.2.3 </strong>Klienti duhet të respektojë të gjitha kushtet dhe politikat e platformës Allbookers.com, duke\n" +
                "     përfshirë politikat për çmimet, disponueshmërinë, rezervimet dhe anullimet. Çdo shkelje e\n" +
                "     kushteve dhe politikave të platformës mund të rezultojë në masa disiplinore, duke përfshirë\n" +
                "     ndalimin e aksesit në platformë.</p>" +
                " <h3>4. Regjistrimi dhe Përdorimi i Platformës <span style=\"color: rgb(0, 162, 255); text-decoration: underline;\">panel.allbookers.com</span></h3> \n" +
                " <p><strong>4.1 Procesi i regjistrimit të pronës: </strong>Regjistrimi i pronës në platformën Allbookers.com është i\n" +
                "     menaxhuar nga stafi ynë i marketingut dhe përfshin plotësimin e të gjitha të dhënave të nevojshme\n" +
                "     për përfshirjen e saj në sistem. Stafi ynë do të mbledhë dhe do të regjistrojë informacionin e plote\n" +
                "     të pronës, duke përfshirë emrin, adresën, llojin e akomodimit, përshkrimin dhe çdo informacion\n" +
                "     tjetër të rëndësishëm. Pas përfundimit të regjistrimit, do të dërgohet një kërkesë për të nënshkruar\n" +
                "     Marrëveshjen përkatëse, nëse informacionet e nevojshme nuk janë të plota. Marrëveshja do të hyjë\n" +
                "     në fuqi vetëm pas miratimit dhe nënshkrimit nga ana juaj." +
                "     </p>\n" +
                " <p><strong>4.2 Hapat për përdorimin e panelit të menaxhimit dhe përditësimin e informacionit të\n" +
                "     pronës:</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>1. Hyni në Panelin e Menaxhimit: </strong>Pas regjistrimit të suksesshëm të pronës dhe nënshkrimit\n" +
                "     të Marrëveshjes, do të merrni kredencialet e hyrjes për panelin e menaxhimit në adresën\n" +
                "     panel.allbookers.com. Përdorni këto kredenciale për të hyrë në sistem dhe për të filluar\n" +
                "     menaxhimin e pronës suaj.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>2. Përditësoni Informacionin e Pronës: </strong>Në panelin e menaxhimit, do të keni mundësinë të\n" +
                "     përditësoni informacionin e pronës suaj, duke përfshirë çmimet, disponueshmërinë,\n" +
                "     përshkrimet e akomodimeve dhe fotografitë. Sigurohuni që të mbani informacionin të saktë\n" +
                "     dhe të përditësuar për të ofruar një përvojë të mirë për klientët<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>3. Menaxhoni Rezervimet: </strong>Paneli ofron mundësi për menaxhimin e rezervimeve, duke\n" +
                "     përfshirë pranimin, modifikimin dhe anullimin e rezervimeve. Do të mund të shihni dhe\n" +
                "     trajtoni të gjitha rezervimet në një vend të vetëm dhe të komunikoni me klientët për çdo\n" +
                "     informacion të rëndësishëm në lidhje me qëndrimin e tyre<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>4. Kontrolloni Raportet dhe Analizat: </strong>Paneli gjithashtu ofron raportet dhe analizat mbi\n" +
                "     performancën ditore të pronës suaj. Ky informacion është i dobishëm për të monitoruar\n" +
                "     aktivitetin dhe për të bërë përmirësime të nevojshme për të maksimizuar ekspozimin dhe\n" +
                "     përfitimet e pronës suaj.\n" +
                " </p>\n" +
                " <h3>5. Menaxhimi i Rezervimeve dhe Çmimeve</h3>\n" +
                " <p><strong>5.1 Përcaktimi dhe menaxhimi i çmimeve dhe disponueshmërisë nga hotelieri:</strong> Hotelieri ka\n" +
                "     përgjegjësinë për të përcaktuar dhe menaxhuar çmimet dhe disponueshmërinë e akomodimeve në\n" +
                "     platformën Allbookers.com. Ky proces përfshin caktimin e çmimeve për periudha të ndryshme,\n" +
                "     ofrimin e mundësive për tarifat e veçanta ose për promocione dhe përcaktimin e disponueshmërisë\n" +
                "     në bazë të nevojave dhe preferencave të tij. Hotelieri është gjithashtu përgjegjës për përditësimin\n" +
                "     e informacionit në panelin e menaxhimit për të reflektuar ndryshimet në çmime dhe\n" +
                "     disponueshmëri, duke siguruar që informacioni të jetë gjithmonë i saktë dhe në përputhje me\n" +
                "     politikat e platformës.</p>\n" +
                " <p><strong>5.2 Politikat për anullimet dhe ndryshimet e rezervimeve: </strong>Platforma Allbookers.com ofron\n" +
                "     fleksibilitet në menaxhimin e politikave të anullimeve dhe ndryshimeve për rezervimet. Hotelieri\n" +
                "     duhet të përcaktojë dhe të publikojë politikat për anullimet dhe ndryshimet në rezervime në\n" +
                "     përputhje me rregullat e platformës dhe preferencat e tij. Këto politika duhet të jenë të qarta dhe të\n" +
                "     kuptueshme për klientët dhe të ofrojnë informacion të detajuar mbi afatet dhe kushtet për anullimin\n" +
                "     ose ndryshimin e rezervimeve. Hotelieri është përgjegjës për respektimin e këtyre politikave dhe\n" +
                "     për të siguruar që të gjitha kërkesat për anullime dhe ndryshime të trajtohen në përputhje me\n" +
                "     rregullat e përcaktuara.</p>\n" +
                " <p><strong>5.3 Procedura e pagesave dhe komisioneve: </strong>Pagesat për rezervimet do të kryhen në përputhje\n" +
                "     me procedurat dhe afatet e përcaktuara nga platforma Allbookers.com. Të gjitha rezervimet do të\n" +
                "     kryhen me të dhëna kartë krediti, dhe pagesa do të bëhet direkt pas përfundimit të rezervimit. Kjo\n" +
                "     do të thotë se rezervimet janë të sigurta dhe pagesat do të menaxhohen gjithmonë nga\n" +
                "     Allbookers.com. Komisionet për shërbimet e ofruara do të zbriten automatikisht nga pagesat e\n" +
                "     rezervimeve dhe do të raportohen në mënyrë të detajuar në panelin e menaxhimit. Hotelieri është\n" +
                "     përgjegjës për verifikimin e saktsisë së pagesave dhe komisioneve dhe për të siguruar që të gjitha\n" +
                "     transaksionet të jenë të përfunduara në mënyrë të duhur dhe të përputhshme me kushtet e\n" +
                "     platformës.<br></br><br></br>\n" +
                "     <strong>5.3.1 </strong>Allbookers.com siguron që të administrojë pagesat përmes platformës dhe mban përgjegjësi\n" +
                "     për çdo mbivendosje rezervimesh (overbooking). Kjo do të nënkuptojë që në rastet e\n" +
                "     mbivendosjeve, Allbookers.com do të ndërmarrë masat e nevojshme për të zgjidhur situatën dhe\n" +
                "     do të mbajë përgjegjësinë për këtë proces. Në rastet e mbivendosjes së rezervimeve (overbooking),\n" +
                "     Allbookers.com merr përgjegjësinë për të zgjidhur problemin dhe siguron që klienti të\n" +
                "     akomodohet. Nëse është e nevojshme, hoteli do të mbulojë koston shtesë për akomodimin e klientit\n" +
                "     në një strukturë me standard më të lartë se ajo e rezervuar fillimisht.\n" +
                " </p>\n" +
                " <h3>6. Pagesat dhe Tarifat</h3>\n" +
                " <p><strong>6.1 Struktura e tarifave dhe komisioneve të platformës: </strong>Allbookers.com aplikon një strukturë\n" +
                "     të qartë tarifash dhe komisionesh për shërbimet e saj. Komisioni për çdo rezervim të kryer përmes\n" +
                "     platformës është 10% e shumës totale të rezervimit. Ky komision zbatohet për çdo transaksion dhe \n" +
                "     është i përfshirë në pagesën përfundimtare që hotelieri do të marrë pas përmbushjes së rezervimit.\n" +
                "     Struktura e tarifave dhe komisioneve mund të përditësohet nga platforma dhe detajet e saj do të\n" +
                "     jenë gjithmonë të disponueshme në panelin e menaxhimit.</p>\n" +
                " <p><strong>6.2 Metodat e Pagesës dhe Afatet: </strong>Pas përfundimit të rezervimit, mysafiri do të duhet të shtojë të dhënat e kartës së kreditit në formën e rezervimit. " +
                "     <strong>Allbookers.com</strong> do të verifikojë vlefshmërinë e kartës për të siguruar që është e vërtetë, por nuk do të përpunojë pagesën. Pagesa për rezervimin do të kryhet drejtpërdrejt në pronë sipas kushteve të hotelierit. " +
                "     Informacioni i kartës do të mbetet i lidhur me rezervimin për referencë dhe siguri, por të gjitha transaksionet financiare do të menaxhohen nga hoteli dhe jo nga <strong>Allbookers.com</strong>.</p>\n" +
                " <p><strong>6.3 Fatura mujore e rezervimeve: </strong>Në datën 1 të çdo muaji, Allbookers.com do të mbledhë të\n" +
                "     gjitha rezervimet e kryera gjatë muajit të mëparshëm dhe do të gjenerojë automatikisht një faturë\n" +
                "     për këto rezervime muajin pasardhës. Fatura do të përfshijë komisionin prej 10% për çdo rezervim.\n" +
                "     Fatura do të jetë e disponueshme për hotelierin dhe do të përmbajë të gjitha informacionet e\n" +
                "     nevojshme për pagesën e komisioneve. Në rastet kur Mysafiri nuk është paraqitur (no-show) ose\n" +
                "     ka kryer pagesa paraprake, do të ofrohen mundësi për të diskutuar dhe zgjidhur çështjet e lidhura\n" +
                "     me rezervimet në përputhje me politikat e platformës.</p>\n" +
                " <p><strong>6.4 Politikat për vonesat në pagesë ose komisionet e papaguara: </strong>Në rastet e vonesave në pagesa\n" +
                "     ose komisioneve të papaguara, Allbookers.com do të ndjekë procedurat e përcaktuara për të\n" +
                "     trajtuar këto situata. Hotelieri është përgjegjës për të paguar të gjitha komisionet e papaguara në\n" +
                "     përputhje me afatet e përcaktuara. Në rastet e vonesave, platforma mund të aplikojë ndëshkime të\n" +
                "     përcaktuara në Marrëveshje, përfshirë tarifa shtesë për vonesën. Nëse ndodhin vonesa të\n" +
                "     vazhdueshme në pagesa, Allbookers.com rezervon të drejtën të pezullojë ose të ndërpresë\n" +
                "     shërbimin deri në shlyerjen e plotë të detyrimeve financiare.</p>\n" +
                " <h3>7. Siguria dhe Mbrojtja e të Dhënave </h3>  \n" +
                " <p><strong>7.1 Mbrojtja e të dhënave për Klientët dhe Pronat: </strong>Allbookers.com është e angazhuar për të\n" +
                "     mbrojtur dhe ruajtur të dhënat e klientëve dhe pronave në mënyrë të sigurt dhe të besueshme. Të\n" +
                "     dhënat që mbledhim përfshijnë informacionin personal dhe financiar të klientëve, si dhe të dhënat\n" +
                "     e pronave të regjistruara në platformë. Këto të dhëna ruhen në përputhje me legjislacionin në fuqi\n" +
                "     për mbrojtjen e të dhënave dhe standardet më të larta të sigurisë. Të dhënat do të përdoren vetëm\n" +
                "     për qëllimet për të cilat janë mbledhur dhe nuk do të shpërndahen apo të përdoren për qëllime të\n" +
                "     tjera pa pëlqimin e klientëve.</p>\n" +
                " <p><strong>7.2 Masat e sigurisë për të dhënat personale dhe financiare: </strong>Allbookers.com zbaton masa të\n" +
                "     avancuara të sigurisë për të mbrojtur të dhënat personale dhe financiare të klientëve dhe\n" +
                "     hotelierëve. Këto masa përfshijnë përdorimin e teknologjive të kriptimit për të mbrojtur\n" +
                "     informacionin gjatë transferimit dhe ruajtjes së tij në serverat tanë. Po ashtu, platforma përdor\n" +
                "     mekanizma të kontrollit të qasjes dhe procedura të rrepta për të siguruar që vetëm personat e \n" +
                "     autorizuar kanë qasje në të dhënat e ndjeshme. Në rast të ndonjë rreziku ose ndërhyrjeje në sistem,\n" +
                "     Allbookers.com do të ndërmarrë veprime të menjëhershme për të adresuar dhe zgjidhur problemet\n" +
                "     e sigurisë, dhe do të njoftojë klientët dhe autoritetet përkatëse sipas kërkesave ligjore.\n" +
                "     </p>\n" +
                " <h3>8. Përgjegjësitë Ligjore</h3>\n" +
                " <p><strong>8.1 Përgjegjësitë për mosrespektimin e kushteve të Marrëveshjes: </strong>Secila palë është e detyruar\n" +
                "     të respektojë të gjitha kushtet dhe dispozitat e përcaktuara në këtë Marrëveshje. Në rast të\n" +
                "     mosrespektimit të kushteve të Marrëveshjes nga ana e Klientit (Hotelierit), Allbookers.com ka të\n" +
                "     drejtën të ndërmarrë masa të përshtatshme për të korrigjuar shkeljet, duke përfshirë, por pa u\n" +
                "     kufizuar në, ndalimin e aksesit në platformë, pezullimin e shërbimeve, ose përfundimin e\n" +
                "     Marrëveshjes. Klienti është përgjegjës për të kompensuar çdo dëm të shkaktuar si rezultat i\n" +
                "     mosrespektimit të kushteve të Marrëveshjes dhe për të mbuluar çdo shpenzim të lidhur me\n" +
                "     zgjidhjen e problemeve të krijuara nga shkeljet e tij.</p>\n" +
                " <p><strong>8.2 Kufizimet e Përgjegjësisë së Platformës për Rezervimet ose Çështjet e tjera që lidhen me\n" +
                "     Klientët:</strong> Allbookers.com do të jetë përgjegjëse për ofrimin e një platforme të besueshme dhe\n" +
                "     përmbushjen e detyrimeve të saj sipas kësaj Marrëveshjeje. Allbookers.com mban përgjegjësi të\n" +
                "     plotë për çdo rast mbivendosjeje rezervimesh (overbooking) dhe do të ndërmarrë masat e\n" +
                "     nevojshme për të zgjidhur situatat e tilla sipas kushteve të përcaktuara në Marrëveshje. Megjithatë,\n" +
                "     platforma nuk do të mbajë përgjegjësi për çdo dëm të drejtpërdrejtë ose të tërthortë që mund të\n" +
                "     ndodhin si rezultat i rezervimeve të tjera, problemeve me rezervimet ose çështjeve të tjera që lidhen\n" +
                "     me klientët ose të dhënat e pronave. Në përputhje me ligjin në fuqi, përgjegjësia e Allbookers.com\n" +
                "     do të jetë e kufizuar në shumën që përfaqëson tarifat e paguara nga klienti për shërbimet e ofruara\n" +
                "     nga platforma. Klienti pranon dhe kupton se çdo çështje që lidhet me rezervimet, pagesat, ose\n" +
                "     ndonjë problem tjetër do të trajtohet përmes procedurave të parashikuara në këtë Marrëveshje dhe\n" +
                "     me ndihmën e mbështetjes teknike të platformës, sipas kushteve të përcaktuara.</p>\n" +
                " <h3>9. Përfundimi i Marrëveshjes dhe Mbyllja e Pronës</h3>\n" +
                " <p><strong>9.1 Kushtet për përfundimin </strong><br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.1.1 </strong>Fillimi dhe Përfundimi i Marrëveshjes: Përveç nëse specifikohet ndryshe, Marrëveshja do\n" +
                " të fillojë në datën e pranimit nga Prona dhe do të vazhdojë për një periudhë të pacaktuar.\n" +
                " <br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.1.2 </strong>Përfundimi nga Klienti (Hotelieri): Klienti (Hotelieri) ka të drejtë të përfundojë\n" +
                " Marrëveshjen nëse Allbookers.com nuk përmbush detyrimet e tij në përputhje me kushtet e\n" +
                " Marrëveshjes. Klienti (Hotelieri) duhet të dërgojë një njoftim me shkrim në\n" +
                " contract@allbookers.com për përfundimin e Marrëveshjes dhe të ofrojë një periudhë korrigjimi\n" +
                " për të adresuar çështjet e ngritura, nëse është e nevojshme.<br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.1.3 Përfundimi nga Allbookers.com: </strong>Allbookers.com ka të drejtë të përfundojë Marrëveshjen\n" +
                " nëse Klienti (Hotelieri) shkel kushtet e Marrëveshjes, përfshirë por pa u kufizuar në\n" +
                " mosrespektimin e politikave të platformës, dhënien e informacionit të pasaktë ose çdo veprim që\n" +
                " dëmton reputacionin e platformës. Allbookers.com do të njoftojë Klientin (Hotelierin) për shkeljet\n" +
                " dhe do të ofrojë një periudhë korrigjimi, përveç rasteve kur shkelja është e rëndë dhe e\n" +
                " papranueshme, në të cilat raste nuk kërkohet periudhë korrigjimi.\n" +
                " \n" +
                " </p>\n" +
                " <p><strong>9.2 Përfundim i Menjëhershëm dhe Mbyllja e Pronës</strong><br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.2.1 Përfundim i Menjëhershëm: </strong>Secila Palë mund të përfundojë Marrëveshjen (dhe të mbyllë\n" +
                " Pronën në Platformë) ose të kufizojë ose pezullojë (të gjitha ose pjesërisht detyrimet, angazhimet\n" +
                " dhe përgjegjësitë) sipas kësaj Marrëveshjeje me Palën tjetër, me efekt të menjëhershëm dhe pa\n" +
                " nevojë për njoftim për shkelje në rastet e: (i) një detyrimi ligjor ose rregullator; (ii) një arsyeje të\n" +
                " rëndësishme në përputhje me ligjin në fuqi; (iii) një shkelje të përsëritur të Marrëveshjes nga Pala\n" +
                " tjetër; ose (iv) një shkelje të rëndësishme (reale ose të dyshuar) nga Pala tjetër të çdo kushti të\n" +
                " kësaj Marrëveshjeje, raste të përmbajtjes së paligjshme ose të papërshtatshme, mashtrim, dhënie e\n" +
                " informacionit të rremë, ose marrje e një numri të konsiderueshëm ankesash nga Mysafirët; ose (v)\n" +
                " (para ose gjatë paraqitjes së një kërkese për falimentim, pezullimi i pagesave, ose ndonjë veprim\n" +
                " ose ngjarje të ngjashme në lidhje me Palën tjetër).\n" +
                " </p>\n" +
                " <p><strong>9.3 Njoftimi dhe Pagesa pas Përfundimit </strong><br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.3.1 </strong>Pas përfundimit ose pezullimit të Marrëveshjes, Prona duhet të respektojë rezervimet e\n" +
                " papaguara për Mysafirët dhe të paguajë të gjitha komisionet (përfshirë kostot, shpenzimet,\n" +
                " interesat nëse është e aplikueshme) që janë të detyrueshme për këto rezervime në përputhje me\n" +
                " kushtet e Marrëveshjes. Pas përfundimit ose pezullimit të Marrëveshjes dhe pavarësisht të drejtës\n" +
                " së Allbookers.com për të hequr Pronën nga Platforma, Allbookers.com mund të mbajë dhe ruajë\n" +
                " faqen e pronës në Platformën e menaxhimit, jo shfaqjen e tij ne publik, por të mbyllë\n" +
                " disponueshmërinë (statusi: \"suspend (mbyllur)\") në pritje të pagesës të plotë dhe përfundimtare të\n" +
                " çdo shume të detyrueshme dhe të papaguar (përfshirë çdo Komision).\n" +
                " </p>\n" +
                " <h3>10. Dispozita të Përgjithshme</h3>\n" +
                " <p><strong>10.1 Ndryshimet në Marrëveshje: </strong>Ndryshimet në këtë Marrëveshje mund të bëhen vetëm me\n" +
                "     marrëveshjen e shkruar të të dyja palëve. Çdo ndryshim ose plotësim i Marrëveshjes do të jetë i\n" +
                "     vlefshëm vetëm nëse është i dokumentuar dhe është nënshkruar nga përfaqësuesit e autorizuar të\n" +
                "     të dyja palëve. Në rast se një ndryshim është i nevojshëm për të përmbushur kërkesa ligjore ose\n" +
                "     rregullatore të reja, palët do të bashkëpunojnë për të bërë përditësime të nevojshme në Marrëveshje\n" +
                "     dhe do të informojnë njëra-tjetrën për çdo ndryshim të tillë sa më shpejt të jetë e mundur.\n" +
                "     </p>\n" +
                " <p><strong>10.2 Transferimi i të drejtave dhe detyrimeve (Ndryshimi I pronësisë): </strong>Në rast se pronari i një prone \n" +
                "     dëshiron të transferojë pronësinë e saj në një palë tjetër, duhet të\n" +
                "     ndjekë procedurën e mëposhtme për të bërë kërkesën për transferim pronësie përmes platformës\n" +
                "     panel.allbookers.com:<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>1. Kërkesa për Transferim Pronësie: </strong>Pronari aktual duhet të dërgojë një kërkesë për\n" +
                "     transferim pronësie përmes opsionit të disponueshëm në panel.allbookers.com. Kjo\n" +
                "     kërkesë duhet të përmbajë informacion të plotë mbi pronarin e ri të propozuar dhe të gjithë\n" +
                "     detajet e tjera të rëndësishme që ndërlidhen me pronën.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>2. Verifikimi: </strong>Allbookers.com do të kryejë verifikimet e nevojshme për të konfirmuar\n" +
                "     identitetin e pronarit të ri dhe për të siguruar që transferimi i pronës është në përputhje me\n" +
                "     politikat dhe procedurat e platformës.\n" +
                "     <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>3. Miratimi dhe Marrëveshja e Re: </strong>Pasi të përfundojnë verifikimet dhe të pranohet kërkesa,\n" +
                "     Allbookers.com do të dërgojë një Marrëveshje të re që do të përfshijë të dhënat e reja të\n" +
                "     pronës, emrin e ri të pronarit, dhe çdo informacion tjetër të rëndësishëm. Marrëveshja e re\n" +
                "     do të zëvendësojë Marrëveshjen ekzistuese dhe do të hyjë në fuqi pasi të nënshkruhet nga\n" +
                "     të dyja palët.\n" +
                "     <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>4. Njoftimi për Pjesën tjetër: </strong>Pas aprovimit dhe nënshkrimit të Marrëveshjes së re, pronari\n" +
                "     i ri do të informohet dhe do të marrë qasje në panelin e menaxhimit për të vazhduar\n" +
                "     menaxhimin e pronës dhe rezervimeve në përputhje me kushtet e reja të Marrëveshjes.\n" +
                " </p>\n" +
                " <h3>11. Renditja, Vlerësimet e Mysafirëve dhe Marketingu</h3>\n" +
                " <p><strong>11.1 Renditja </strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.1.1 </strong>Allbookers.com ka për qëllim të shfaqë rezultatet e kërkimeve të përshtatshme për çdo\n" +
                "     Mysafir të veçantë, duke ofruar një renditje të personalizuar të Pronave në Platformë. Ky sistem\n" +
                "     mund të përfshijë kriteret për renditjen e pronave në rezultatet e kërkimeve dhe mund të bazohen\n" +
                "     në faktorë të ndryshëm si cilësia e shërbimit, përvoja e përdoruesve, dhe performanca e\n" +
                "     përgjithshme e pronës. Allbookers.com ruan të drejtën për të ndryshuar algoritmin e renditjes në\n" +
                "     përputhje me nevojat e platformës dhe për të siguruar një përvojë të kënaqshme për përdoruesit<br></br>\n" +
                "     \n" +
                " </p>\n" +
                " <p><strong>11.2 Vlerësimet e Mysafirëve: </strong>Mysafirët që kanë arritur ose qëndruar në Pronë do të kërkohen\n" +
                "     nga Allbookers.com për të komentuar dhe dhënë një vlerësim për aspekte të caktuara të përvojës\n" +
                "     së tyre me Pronën. Allbookers.com mund të publikojë këto vlerësime në Platformë.\n" +
                "     Allbookers.com është një shpërndarës dhe jo një botues i këtyre vlerësimeve. Allbookers.com do\n" +
                "     të vlerësojë vlerësimet e Mysafirëve në përputhje me Politikat e aplikueshme. Allbookers.com nuk\n" +
                "     do të jetë përgjegjëse në lidhje me vlerësimet e Mysafirëve të shfaqura, ose të mos shfaqura, në\n" +
                "     Platformë në përputhje me ligjin e aplikueshëm.<br></br>\n" +
                "     <strong>11.2.1 </strong>Allbookers.com mund, sipas diskrecionit të saj të vetëm, të mbajë të fshehta vlerësimet nga\n" +
                "     shfaqja në Platformë, të heqë vlerësimet, ose të kërkojë nga një Mysafir të ofrojë një version të \n" +
                "     ndryshuar të vlerësimit nëse ato përmbajnë ose i referohen ndonjë gjëje që Allbookers.com e\n" +
                "     përcakton si të papërshtatshme dhe/ose ofenduese, duke përfshirë, por pa u kufizuar në: (i) komente\n" +
                "     politiko-sensitive; (ii) aktivitete të paligjshme; (iii) informacion personal ose të ndjeshëm (p.sh.,\n" +
                "     emaile, adresa, numra telefoni, ose informacioni i kartës së kreditit); (iv) faqe të tjera interneti; (v)\n" +
                "     fjalor të pahijshëm, referenca seksuale, fjalë urrejtjeje, komente diskriminuese, kërcënime,\n" +
                "     ofendime, ose referenca për dhunë.\n" +
                "     <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.2.2 </strong>Pronari i Pronës nuk duhet të manipulojë ose të përpiqet të manipulojë vlerësimet e\n" +
                "     Mysafirëve (p.sh. duke paguar për vlerësime pozitive ose duke postuar vlerësime të rreme për një\n" +
                "     pronë konkurrente).<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.2.3 </strong>Vlerësimet e Mysafirëve janë për përdorim ekskluziv nga Allbookers.com. Pronari i Pronës\n" +
                "     nuk ka të drejtë të përdorë drejtpërdrejt ose tërthorazi vlerësimet e Mysafirëve në ndonjë mënyrë\n" +
                "     pa miratimin e mëparshëm me shkrim nga Allbookers.com.\n" +
                " </p>\n" +
                " <p><strong>11.3 Marketingu (Online) </strong><br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>11.3.1 </strong>Allbookers.com ndjek fushata marketingu online në koston dhe diskrecionin e saj dhe mund\n" +
                " të promovoje Pronën duke përdorur emrin e Pronës në këtë marketing, duke përfshirë marketingun\n" +
                " me email.<br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>11.3.2 </strong>Nëse Pronari i Pronës bëhet i vetëdijshëm për sjellje nga një platformë e palës së tretë që\n" +
                " shkel të drejtat e tij të Pronësisë Intelektuale, ai duhet të njoftojë menjëherë Allbookers.com me\n" +
                " shkrim dhe detaje të sjelljes, dhe Allbookers.com do të përdorë përpjekjet tregtare të arsyeshme\n" +
                " për të siguruar që palës së tretë të marrë masat përkatëse për këtë shkelje.<br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>11.3.3 </strong>Pronari i Pronës bie dakord të mos përdorë, drejtpërdrejt ose tërthorazi, markën/logo-n e\n" +
                " Allbookers.com (përfshirë emrin e biznesit, markën tregtare, shenjën e shërbimit, ose ndonjë\n" +
                " shenjë tjetër të ngjashme të identitetit ose burimit) për qëllime krahasimi çmimesh ose për ndonjë\n" +
                " qëllim tjetër, qoftë në platformën e Pronës ose në ndonjë platformë të palës së tretë, sistem, motor,\n" +
                " ose ndryshe, nëse nuk është miratuar paraprakisht me shkrim nga Allbookers.com.\n" +
                " \n" +
                " </p>\n" +
                " <h3>12. Të ndryshme</h3>     \n" +
                " <p><strong>12.1 Dëmshpërblimi nga Klienti: </strong>Klienti është dakord të dëmshpërblejë, mbrojë dhe të mbajë pa\n" +
                "     dëme Allbookers.com, filialet e tij, zyrtarët, drejtorët, punonjësit dhe agjentët nga dhe kundër çdo\n" +
                "     të gjitha pretendimeve, përgjegjësive, dëmshpërblimeve, humbjeve, kostove dhe shpenzimeve\n" +
                "     (përfshirë tarifat e arsyeshme të avokatëve) që lindin nga ose në lidhje me (i) shkeljen e çdo kushti\n" +
                "     të Marrëveshjes nga Klienti; (ii) çdo pretendim të bërë nga Mysafirët që lidhet me Pronën; (iii)\n" +
                "     çdo shkelje të ligjeve ose rregulloreve të aplikueshme nga Klienti; dhe (iv) çdo neglizhencë ose\n" +
                "     sjellje të pahijshme nga Klienti.<br></br>\n" +
                "     <strong>12.1.1 Dëmshpërblimi nga Allbookers.com: </strong>Allbookers.com është dakord të dëmshpërblejë,\n" +
                "     mbrojë dhe të mbajë pa dëme Klientin nga dhe kundër çdo të gjitha pretendimeve, përgjegjësive,\n" +
                "     dëmshpërblimeve, humbjeve, kostove dhe shpenzimeve (përfshirë tarifat e arsyeshme të\n" +
                "     avokatëve) që lindin nga ose në lidhje me (i) shkeljen e çdo kushti të Marrëveshjes nga\n" +
                "     Allbookers.com; (ii) çdo shkelje të ligjeve ose rregulloreve të aplikueshme nga Allbookers.com;\n" +
                "     dhe (iii) çdo neglizhencë ose sjellje të pahijshme nga Allbookers.com.\n" +
                " </p>\n" +
                " <p><strong>12.2 Kufizimi i Përgjegjësisë: </strong>Përveç siç parashikohet ndryshe në Marrëveshje, asnjëra Palë nuk\n" +
                "     do të jetë përgjegjëse ndaj Palës tjetër për ndonjë dëm indirekt, rastësor, pasues, të veçantë ose\n" +
                "     ndëshkues, duke përfshirë por jo të kufizuar në, humbjen e fitimeve, humbjen e biznesit, ose\n" +
                "     humbjen e të dhënave, që lindin nga ose në lidhje me Marrëveshjen, pavarësisht nga shkaku i\n" +
                "     veprimit, madje edhe nëse këto dëme ishin të parashikueshme ose nëse Pala është këshilluar për\n" +
                "     mundësinë e këtyre dëmeve.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.2.1 </strong>Kufizimi Maksimal i Përgjegjësisë: Përgjegjësia maksimale e çdo Pale për çdo kërkesë\n" +
                "     që lind nga ose në lidhje me Marrëveshjen do të kufizohet në shumën e tarifave të paguara nga\n" +
                "     Klienti në Allbookers.com sipas Marrëveshjes gjatë periudhës gjashtë (6) mujore menjëherë para\n" +
                "     ngjarjes që jep shkak për kërkesën.\n" +
                " </p>\n" +
                " <p><strong>12.3 Asnjë Përgjegjësi për Shërbimet e Palëve të Treta: </strong>Allbookers.com nuk do të jetë\n" +
                "     përgjegjës për ndonjë problem ose dëme që lindin nga përdorimi i shërbimeve ose produkteve të\n" +
                "     palëve të treta që nuk ofrohen drejtpërdrejt nga Allbookers.com, duke përfshirë por jo të kufizuar\n" +
                "     në, shërbimet e përpunimit të pagesave, platforma të jashtme të rezervimeve, ose integrime të tjera\n" +
                "     të palëve të treta.\n" +
                "     </p>\n" +
                " <p><strong>12.4 Forca Madhore: </strong>Asnjëra Palë nuk do të jetë përgjegjëse për ndonjë dështim për të kryer\n" +
                "     detyrimet e saj sipas Marrëveshjes nëse ky dështim është për shkak të ngjarjeve jashtë kontrollit\n" +
                "     të saj të arsyeshëm, duke përfshirë por jo të kufizuar në, katastrofa natyrore, luftë, terrorizëm,\n" +
                "     greva, ose ngjarje të tjera të papritura.</p>\n" +
                " <p><strong>12.5 Veprimet Ligjore</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.5.1 Zgjidhja e Mosmarrëveshjeve: </strong>Çdo mosmarrëveshje që lind nga ose në lidhje me\n" +
                "     Marrëveshjen do të zgjidhet në përputhje me procesin e zgjidhjes së mosmarrëveshjeve të\n" +
                "     parashikuara në Marrëveshje, duke përfshirë çdo kërkesë për mediatim ose arbitrazh, nëse është e\n" +
                "     aplikueshme.\n" +
                " </p> " +
                "</div></div>");
        String pdfContent = pdfContentBuilder.toString();

        // Convert HTML to PDF
        byte[] pdfData = convertHtmlToPdf(pdfContent);

        // Add PDF as attachment
        helper.addAttachment("Marreveshje.pdf", new ByteArrayResource(pdfData), "application/pdf");

        send(message);
        System.out.println("Mail for accept Request for agreement Send...");
    }

    //     Request Change of ownership
    public void RequestChangeofownership(Changeofownership changeofownership) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("info@allbookers.com");
        helper.setSubject("Request Change of ownership from " + changeofownership.getAgreement().getLegal_bussines_name());

        String htmlContent = "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>"
                + "<div style=\"background: #FFFFFF; padding: 20px; max-width: 500px; margin: auto;\">"
                + "<p style=\"font-size: 18px;\">"
                + changeofownership.getAgreement().getLegal_bussines_name() + " has made a request for a change of ownership."
                + "</p>"
                + "<p style=\"font-size: 18px;\">"
                + "You can view it on <a href=\"https://panel.allbookers.com/\" style=\"text-decoration: none;\">panel.allbookers.com</a>."
                + "</p>"
                + "</div>"
                + "<hr style=\"width: 50%; margin: 20px auto;\">"
                + "<p style=\"text-align: center\">© Copyright 2024 Allbookers.com | All rights reserved.<br>This e-mail was sent by allbookers.com.</p>"
                + "</div>";

        helper.setText(htmlContent, true);
        send(message);
        System.out.println("Mail for Request Change of ownership...");
    }

    //    Refuse Request Change of ownership
    public void refuseRequestChangeofownership(Changeofownership changeofownership) throws MessagingException{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(changeofownership.getEmail());
        helper.setSubject("Allbookers.com refused the request for an agreement.");


        String htmlContent = "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>"
                + "</div><div style=\"text-align: left;max-width: 500px; margin: auto; padding: 20px;\">"
                + "<p style=\"font-size: 20px;\">Your request for an agreement has been <b><span style=\"color:red\"> refused </span></b> by Allbookers.com. </p>"
                + "</div><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
                + "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved."
                + "<br>This e-mail was sent by allbookers.com.</p></div>";

        helper.setText(htmlContent, true);
        send(message);
        System.out.println("Mail for refuse Request for agreement Send...");
    }

    //    Accept Request Change of ownership
    public void acceptRequestChangeofownership(Agreement agreement) throws MessagingException, DocumentException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(agreement.getEmail());
        helper.setSubject("Allbookers.com accepted the request for an agreement. ");


        String email =  "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>"
                +"<div style=\"text-align: left; max-width: 580px; margin: auto;\">"
                +"<br><h2>Dear " + agreement.getFirst_name() + " " + agreement.getLast_name() + ", </h2>"
                + "<p style=\"font-size: 16px;\">We are happy to let you know that the change of ownership process is complete and that you are now ready to start working with Allbookers.com.</p>"
                + "<p style=\"font-size: 16px;\">Please find attached an approved copy of your Agreement.</p>"
                + "<p style=\"font-size: 16px;\">We wish you the best of luck in your new venture. </p>"
                + "<p style=\"font-size: 16px;\">Kind regards,</p>"
                + "<p style=\"font-size: 16px;\">The <a href=\"https://allbookers.com/\" style=\"text-decoration: none;\">Allbookers.com </a> Team\n</p>"
                + "</div><br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
                + "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved."
                + "<br>This e-mail was sent by allbookers.com.</p></div>";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"mainDiv\">\n" +
                "      <div class=\"column\" style=\"padding-top: 5%; padding-left: 5%; float: left; width: 100%\">\n" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "      </div>\n" +
                "    <div style=\"text-align: left; \">\n" +
                "   <h2>Accommodation Agreement with Allbookers.com </h2>\n" +
                "      <p>This document represents a legal agreement between: </p>\n" +
                "        <p><strong>Allbookers</strong>, a platform operated by InterMedia Sh.P.K, a company registered under the laws of the Republic of Albania, with its registered address at Bulevardi Bajram Curri tek ura e ATSH, nr pasurie 6/516+1-20, Tirana, Albania, and VAT\n" +
                "            number: <strong>M02028001D</strong> (hereinafter referred to as \"The Management Platform\" or \"Management\n" +
                "            Panel\"),</p>\n" +
                "        <p><strong>And</strong></p>\n" +
                "        <p><strong>"+agreement.getLegal_bussines_name()+"</strong>, (the \"Partner\") <strong>"+agreement.getFirst_name()+' '+agreement.getLast_name()+"</strong>, \n" +
                "            with a registered address at <strong>"+agreement.getAddress()+"</strong>, and VAT number <strong>"+agreement.getNuis()+"</strong>\n" +
                "            (hereinafter referred to as \"Client (Hotelier)\" or \"Hotelier\").</p>\n" +
                "        <p>The Agreement includes the following terms:</p>\n" +
                "        <h3>Commission Rates for Registered Properties: </h3>\n" +
                "        <p>For all properties registered on Allbookers.com\n" +
                "            under the Partner's name, the following local commission rates apply:\n" +
                "            <ul>\n" +
                "                <li><strong>Albania:</strong> 10%</li>\n" +
                "            </ul>\n" +
                "        </p>\n" +
                "        <p>Allbookers.com provides a secure and reliable platform for property and booking management</p>\n" +
                "        <h3>Execution and performance </h3>\n" +
                "        <p>The Agreement is only effective after approval and confirmation by Allbookers.com.</p>\n" +
                "        <h3>General Terms of the Agreement:</h3>\n" +
                "        <p><strong>Purpose: </strong>This Agreement outlines the terms and conditions for the use of the Allbookers.com\n" +
                "            platform by the Hotelier to advertise and manage their Property, including accepting bookings,\n" +
                "            managing pricing and availability, and using the Management Panel accessed at\n" +
                "            panel.allbookers.com.</p>\n" +
                "        <p><strong>Partner Declaration: </strong>The Partner declares that this is a legitimate accommodation activity with\n" +
                "            all necessary licenses and permits, which may be provided upon request. Allbookers.com reserves\n" +
                "            the right to verify and investigate any details provided by the Partner in this registration.</p>\n" +
                "        <p><strong>Term of the Agreement: </strong>\n" +
                "            This Agreement will be valid from the date of signing and will continue\n" +
                "            to be in effect until terminated or canceled by either party in accordance with the terms of this\n" +
                "            Agreement.</p>\n" +
                "        <p><strong style=\"font-size: 18px;\">Start Date of the Agreement: </strong>"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+ "</p>  \n" +
                "<p><br></br><br></br></p>"+
                "        <h2>Table of Contents for the Accommodation Agreement</h2> \n" +
                "        <p><strong>1. Definitions</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>1.1 </strong>Explanation of terms used in the Agreement, including: \"Platform,\" \"Property,\"\n" +
                "        \"Booking,\" \"Management Panel,\" \"Client (Hotelier) \" etc,.\n" +
                "        </p>\n" +
                "        <p><strong>2. Purpose of the Agreement\n" +
                "        </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>2.1 </strong>Description of the service provided by Allbookers.com<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>2.2 </strong>Purpose of the agreement and use of the platform by the Client (Hotelier) for property\n" +
                "        and booking management.\n" +
                "        </p>\n" +
                "        <p><strong>3. Rights and Obligations of the Parties</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>3.1 </strong>Obligations of Allbookers.com<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>3.2 </strong>Obligations of the Property\n" +
                "        </p>\n" +
                "        <p><strong>4. Registration and Use of the Platform</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>4.1 </strong>Process for registering the property<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>4.2</strong> Steps to follow for using the management panel and updating property information.\n" +
                "        \n" +
                "        </p>\n" +
                "        <p><strong>5. Management of Reservations and Prices</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>5.1 </strong>Setting and managing pricing and availability by the hotelier.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>5.2 </strong>Policies for cancellations and changes in bookings.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>5.3 </strong>Payment and Commission Procedures.\n" +
                "        </p>\n" +
                "        <p><strong>6. Payments and Fees </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.1 </strong>Fee and Commission Structure of the Platform\n" +
                "        <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.2 </strong>Payment Methods and Deadlines<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.3 </strong>Monthly Reservation Invoices<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.4 </strong>Policies on payment delays or unpaid commissions.\n" +
                "        </p>\n" +
                "        <p><strong>7. Security and Data Protection </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>7.1 </strong>Data Protection for <strong>Client (Hotelier)</strong> and Properties.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>7.2 </strong>Security measures for personal and financial data.\n" +
                "        </p>\n" +
                "        <p><strong>8. Legal Responsibilities</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>8.1 </strong>Responsibilities for Non-Compliance with Contract Terms.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>8.2 </strong>Limits of the Platform's Liability for Reservations or Other Client (Hotelier) -Related\n" +
                "        Issues.\n" +
                "        </p>\n" +
                "        <p><strong>9. </strong>Termination of the Agreement and Property Closure<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>9.1 </strong>Termination Conditions<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>9.2 </strong>Immediate Termination and Closure of Property. <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>9.3 </strong>Notice and Payment After Termination\n" +
                "        </p>\n" +
                "        <p><strong>10. General Provisions</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>10.1 </strong>Amendments to the Agreement<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>10.2 </strong>Transfer of rights and obligations, Change of Ownership.\n" +
                "        </p>\n" +
                "        <p><strong>11. Ranking, Guest Reviews and Marketing</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>11.1 </strong>Ranking<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>11.2 </strong>Guest Reviews <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>11.3 </strong>Online Marketing\n" +
                "        </p>\n" +
                "        <p><strong>12. Miscellaneous </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.1 </strong>Indemnification by the Client (Hotelier) <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.2 </strong>Limitation of Liability <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.3 </strong>No Liability for Third-Party Services<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.4 </strong>Force Majeure<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.5 </strong>Legal Actions\n" +
                "        </p> \n" +

                "        <h3>1. Definitions</h3>\n" +
                "        <p>In addition to terms defined elsewhere in this Agreement, the following definitions apply\n" +
                "            throughout the Agreement unless the contrary intention appears:</p>\n" +
                "        <p><strong>\"Accommodation\" </strong>refers to the entity (either a legal person or a natural person) that is a party to\n" +
                "            the Agreement. If different, it also means the property associated with the Agreement that the\n" +
                "            entity is contracting for, and to which these Terms apply.</p>\n" +
                "        <p><strong>\"Accommodation Information\" </strong>refers to the details supplied by and associated with the\n" +
                "            Accommodation for presentation on the Platform. This includes images, photos, and descriptions;\n" +
                "            information about amenities and services; booking availability and room details; pricing\n" +
                "            (including all applicable Taxes unless otherwise required by law, as well as any additional fees);\n" +
                "            policies on availability, cancellations, and no-shows; other relevant policies and restrictions; and\n" +
                "            any other information that Allbookers.com is legally obligated to display on the Platform.</p>\n" +
                "        <p><strong>\"Booking\": </strong>An electronic agreement made through the Allbookers.com Platform between the\n" +
                "            Client (Hotelier) and a Guest for a stay at the Client (Hotelier) 's Property.\n" +
                "            </p>\n" +
                "        <p><strong>\"Client (Hotelier) \": </strong>An individual or legal entity that registers and manages a Property on the\n" +
                "            Allbookers.com Platform and uses the Platform's services to manage bookings and promote their\n" +
                "            Property.</p>\n" +
                "        <p><strong>\"Commission\": </strong>The fee paid by the Client (Hotelier) for the services provided by\n" +
                "            Allbookers.com, including booking facilitation and other related services.\n" +
                "            </p>\n" +
                "        <p><strong>\"Connectivity Partner\": </strong>A company or individual providing services for the integration and\n" +
                "            connection of information between the Allbookers.com Platform and other systems to enhance the\n" +
                "            functionality and efficiency of booking services.</p>\n" +
                "        <p><strong>\"Connectivity Services\": </strong>Services that enable the connection and integration of information\n" +
                "            between the Allbookers.com Platform and other management systems or related services.\n" +
                "            </p>\n" +
                "        <p><strong>\"Customer Data\": </strong>Personal information and other data collected and stored by the Platform about\n" +
                "            <strong>Client (Hotelier) </strong>and Guests, including but not limited to names, addresses, contact information,\n" +
                "            and payment details.\n" +
                "            </p>\n" +
                "        <p><strong>\"Guest\": </strong>An individual who makes a Booking for a stay at the Client (Hotelier) 's Property\n" +
                "            through the Allbookers.com Platform.\n" +
                "            </p>\n" +
                "        <p><strong>\"Management Panel\": </strong>The tool provided by Allbookers.com that allows <strong>Client (Hotelier) </strong>to\n" +
                "            manage and update their Property information, including pricing, availability, bookings, and\n" +
                "            communications with Guests.</p>\n" +
                "        <p><strong>\"Platform\": </strong>Refers to Allbookers.com, an online system that enables the registration,\n" +
                "            management, and promotion of properties via the internet and provides related services for <strong>Client (Hotelier) </strong>and Guests.</p>\n" +
                "        <p><strong>\"Property\": </strong>Any accommodation unit registered on the Allbookers.com Platform, including but\n" +
                "            not limited to hotels, apartments, villas, and vacation homes, offered for booking by <strong>Client (Hotelier) </strong>on behalf of their owners.\n" +
                "            </p>\n" +
                "        <p><strong>\"Related Services\": </strong>Additional services provided by the Platform beyond the standard booking\n" +
                "            and management functionality, including but not limited to marketing services, data analytics, and\n" +
                "            technical support.</p>\n" +
                "        <p><strong>\"Room Price\": </strong>The price set by the <strong>Client (Hotelier) </strong>for an accommodation unit at their Property,\n" +
                "            as defined in the Management Panel and displayed on the Platform for Guests.</p>   \n" +
                "        <h3>2. Purpose of the Agreement </h3>   \n" +
                "        <p><strong>2.1 Description of the Service Provided by Allbookers.com </strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2.1.1 </strong>Allbookers.com provides a comprehensive online platform designed for property owners and\n" +
                "                hoteliers to manage their accommodations and bookings effectively. The platform offers a range\n" +
                "                of features including: <br></br>\n" +
                "                &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>1. Property Listing Management: </strong>Allows users to create and maintain detailed listings of\n" +
                "           their properties, including descriptions, photos, and amenities.<br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>2. Booking Management: </strong>Facilitates the handling of reservations, including accepting,\n" +
                "           modifying, and canceling them as needed.<br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>3. Pricing and Availability Control: </strong>Provides tools for setting and updating pricing and\n" +
                "           availability in real-time. <br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>4. Promotion and Visibility: </strong> Enhances property exposure through various marketing and\n" +
                "           promotional tools to attract potential guests.\n" +
                "           <br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>5. Technical Support: </strong>Offers support to assist with any issues related to the use of the\n" +
                "           platform.   \n" +
                "        </p>\n" +
                "        <p><strong>2.2 Purpose of the Agreement and Use of the Platform by the Client (Hotelier)</strong> <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2.2.1 </strong>The purpose of this Agreement is to outline the terms and conditions under which the Client\n" +
                "            (Hotelier) will use the Allbookers.com platform. This includes: <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>1. Advertising and Managing the Property: </strong>The Client (Hotelier) agrees to utilize the\n" +
                "            platform for listing and managing their property, ensuring that the information is accurate\n" +
                "            and up-to-date.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2. Handling Bookings: </strong>The Client (Hotelier) will use the platform to accept, modify, and\n" +
                "            manage bookings made by guests<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3. Maintaining Pricing and Availability: </strong>The Client (Hotelier) is responsible for setting and\n" +
                "            updating the pricing and availability of their property through the platform<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>4. Compliance with Platform Policies: </strong>The Client (Hotelier) must adhere to the platform's\n" +
                "            terms, policies, and guidelines to ensure proper use and to maintain the quality and integrity\n" +
                "            of the services provided.\n" +
                "        </p>  \n" +
                "        <h3>3. Rights and Obligations of the Parties</h3>\n" +
                "        <p><strong>3.1 Obligations of Allbookers.com</strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.1 </strong>Allbookers.com will ensure uninterrupted and reliable access to its platform to enable the\n" +
                "            Client (Hotelier) to manage their property and reservations efficiently. This access includes the\n" +
                "            use of the Management Panel, panel.allbookers.com, which the Client (Hotelier) will use to update\n" +
                "            property information and manage reservations.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.2 </strong>Additionally, Allbookers.com will provide technical support. This support is dedicated to\n" +
                "            assisting the Client (Hotelier) in using the platform and resolving any issues that may arise during\n" +
                "            the operation of the platform. Technical support includes help with technical issues, system errors,\n" +
                "            and problems related to platform access.\n" +
                "            <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.3 </strong>To protect the Client (Hotelier) 's data and property information, Allbookers.com will take\n" +
                "            necessary measures for data security. This includes the use of advanced encryption technologies\n" +
                "            and other security measures to protect the Client (Hotelier) 's personal and financial information\n" +
                "            from unauthorized access, loss, or damage.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.4 </strong>Allbookers.com will also employ various marketing strategies to promote the Client\n" +
                "            (Hotelier) 's property on the platform and other potential networks. Promotion includes featuring\n" +
                "            the property in the platform's search listings and may include promotional campaigns and relevant\n" +
                "            advertisements.\n" +
                "            \n" +
                "        </p>\n" +
                "        <p><strong>3.2 Obligations of the Client (Hotelier) </strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.2.1 </strong>The Client (Hotelier) is responsible for the proper use of the platform in accordance with\n" +
                "            Allbookers.com's guidelines and policies. The Client (Hotelier) must ensure that their use of the\n" +
                "            platform complies with legal requirements and ethical standards. The Client (Hotelier) is\n" +
                "            responsible for any activity that occurs on their account and for assisting in resolving issues that\n" +
                "            arise during the use of the platform<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.2.2 </strong>To ensure that their property information is accurate and up-to-date, the Client (Hotelier)\n" +
                "            must keep their property information, including prices, availability, and other details, accurate and regularly updated. \n" +
                "            Any changes to the property information must be reflected on the platform immediately after they occur.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.2.3 </strong>The Client (Hotelier) must adhere to all terms and policies of the Allbookers.com platform,\n" +
                "            including policies regarding prices, availability, reservations, and cancellations. Any violation of\n" +
                "            the platform's terms and policies may result in disciplinary measures, including suspension of\n" +
                "            access to the platform.\n" +
                "        </p>\n" +
                "        <h3>4. Registration and Use of the <span style=\"color: rgb(0, 162, 255);text-decoration: underline;\">panel.allbookers.com</span> Platform</h3>\n" +
                "        <p><strong>4.1 Process for registering the property: </strong>Property registration on the Allbookers.com platform\n" +
                "            is managed by our marketing team and involves completing all necessary information for its\n" +
                "            inclusion in the system. Our team will collect and register the complete property information,\n" +
                "            including the name, address, type of accommodation, description, and any other relevant details.\n" +
                "            After completing the registration, a request will be sent to sign the relevant Agreement if the\n" +
                "            required information is not complete.\n" +
                "            </p>\n" +
                "        <p><strong>4.2 Steps to Follow to Use the Management Panel and Update Property Information</strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>1. Log In to the Management Panel: </strong>After the successful registration of the property and\n" +
                "            signing of the Agreement, you will receive login credentials for the management panel\n" +
                "            at panel.allbookers.com. Use these credentials to access the system and start managing\n" +
                "            your property<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2. Update Property Information: </strong>In the management panel, you will have the option to\n" +
                "            update your property information, including prices, availability, accommodation\n" +
                "            descriptions, and photos. Ensure that the information is accurate and up-to-date to\n" +
                "            provide a good experience for Client's (Hotelier).<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3. Manage Reservations: </strong> The panel offers the ability to manage reservations, including\n" +
                "            accepting, modifying, and canceling reservations. You will be able to view and handle\n" +
                "            all reservations in one place and communicate with <strong>Client (Hotelier)</strong> regarding any\n" +
                "            important information about their stay.\n" +
                "            <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>4. Review Reports and Analytics: </strong>The panel also provides reports and analytics on the\n" +
                "            daily performance of your property. This information is useful for monitoring activity\n" +
                "            and making necessary improvements to maximize exposure and profitability of your\n" +
                "            property.\n" +
                "            \n" +
                "        </p>\n" +
                "        <h3>5. Management of Reservations and Prices</h3>\n" +
                "        <p><strong>5.1 Setting and Managing Prices and Availability by the Hotelier: </strong>The hotelier is responsible\n" +
                "            for setting and managing the prices and availability of accommodations on the Allbookers.com\n" +
                "            platform. This process includes setting prices for different periods, offering special rates or\n" +
                "            promotions, and determining availability based on their needs and preferences. The hotelier is also\n" +
                "            responsible for updating information in the management panel to reflect changes in prices and\n" +
                "            availability, ensuring that the information is always accurate and compliant with the platform's\n" +
                "            policies.</p>\n" +
                "        <p><strong>5.2 Policies for cancellations and changes in bookings: </strong>The Allbookers.com platform offers\n" +
                "            flexibility in managing cancellation and modification policies for reservations. The hotelier must\n" +
                "            define and publish policies for cancellations and modifications in accordance with the platform's\n" +
                "            rules and their preferences. These policies must be clear and understandable for Client (Hotelier)\n" +
                "            and provide detailed information about deadlines and conditions for canceling or modifying\n" +
                "            reservations. The hotelier is responsible for adhering to these policies and ensuring that all\n" +
                "            cancellation and modification requests are handled in accordance with the established rules.</p>\n" +
                "        <p><strong>5.3 Payment and Commission Procedures: </strong>Payments for reservations will be processed in\n" +
                "            accordance with the procedures and deadlines set by the Allbookers.com platform. All reservations\n" +
                "            will be processed with credit card information, and payment will be made directly upon completion\n" +
                "            of the reservation. This means that reservations are secure, and payments will always be managed\n" +
                "            by Allbookers.com. Commissions for the provided services will be automatically deducted from\n" +
                "            reservation payments and will be detailed in the management panel. The hotelier is responsible for\n" +
                "            verifying the accuracy of payments and commissions and ensuring that all transactions are\n" +
                "            completed properly and in compliance with the platform's terms <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>5.3.1 Overbookings: </strong>Allbookers.com ensures that it manages payments through the platform and\n" +
                "            takes responsibility for any overbookings. This means that in cases of overbookings,\n" +
                "            Allbookers.com will take the necessary steps to resolve the situation and assumes responsibility\n" +
                "            for the process. In the event of overbooking, Allbookers.com will handle the issue and ensures that\n" +
                "            the guest is accommodated. If necessary, the hotel will cover the additional cost of accommodating\n" +
                "            the guest in a higher-standard property than the one originally booked.\n" +
                "        </p>\n" +
                "        <h3>6. Payments and Fees </h3>\n" +
                "        <p><strong>6.1 Fee and Commission Structure of the Platform: </strong>Allbookers.com implements a clear fee and\n" +
                "            commission structure for its services. The commission for each reservation made through the\n" +
                "            platform is 10% of the total reservation amount. This commission applies to each transaction and\n" +
                "            is included in the final payment that the hotelier will receive after the reservation is fulfilled. The\n" +
                "            fee and commission structure may be updated by the platform, and its details will always be\n" +
                "            available in the management panel.\n" +
                "            </p>\n" +
                "            <p><strong>6.2 Payment Methods and Deadlines: </strong>After completing the reservation, the guest must add their credit card details to the booking form.\n" +
                "            Allbookers.com will verify the validity of the card to ensure it is legitimate but will not process the payment.\n" +
                "            The payment for the reservation will be made directly at the property according to the hotel's terms.\n" +
                "            The card information will remain linked to the reservation for reference and security, but all financial transactions will be handled by the hotel and not by Allbookers.com.</p>\n" +
                "        <p><strong>6.3 Monthly Reservation Invoices: </strong>On the 1st of each month, Allbookers.com will compile all\n" +
                "            reservations made during the previous month and will automatically generate an invoice for these\n" +
                "            reservations in the following month. The invoice will include the 10% commission for each\n" +
                "            reservation. The invoice will be available to the hotelier and will contain all necessary information\n" +
                "            for the payment of commissions. In cases of no-shows or prepaid bookings, options will be\n" +
                "            provided to discuss and resolve issues related to reservations in accordance with the platform's\n" +
                "            policies</p>\n" +
                "        <p><strong>6.4 Policies on payment delays or unpaid commissions: </strong>In cases of late payments or unpaid\n" +
                "            commissions, Allbookers.com will follow the procedures set out to handle these situations. The\n" +
                "            hotelier is responsible for paying all unpaid commissions according to the specified deadlines. In\n" +
                "            cases of delays, the platform may apply penalties specified in the Agreement, including additional\n" +
                "            fees for late payment. In the event of ongoing delays in payments, Allbookers.com reserves the\n" +
                "            right to suspend or terminate services until all financial obligations are settled.</p>\n" +
                "        <h3>7. Security and Data Protection </h3>\n" +
                "        <p><strong>7.1 Data Protection for Client (Hotelier) and Properties: </strong>Allbookers.com is committed to\n" +
                "            protecting and securely maintaining Client (Hotelier) and property data in a reliable manner. The\n" +
                "            data we collect includes personal and financial information of Clients (Hotelier), as well as\n" +
                "            property data registered on the platform. This data is stored in compliance with applicable data\n" +
                "            protection legislation and the highest security standards. The data will be used only for the\n" +
                "            purposes for which it was collected and will not be distributed or used for other purposes without\n" +
                "            the Client's (Hotelier) consent.</p>\n" +
                "        <p><strong>7.2 Security Measures for Personal and Financial Data: </strong>Allbookers.com implements advanced\n" +
                "            security measures to protect the hoteliers' personal and financial data. These measures include the\n" +
                "            use of encryption technologies to safeguard information during transmission and storage on our\n" +
                "            servers. Additionally, the platform uses access control mechanisms and strict procedures to ensure\n" +
                "            that only authorized individuals have access to sensitive data. In the event of any risk or breach of\n" +
                "            the system, Allbookers.com will take immediate action to address and resolve security issues and\n" +
                "            will notify <strong>Client (Hotelier)</strong> and relevant authorities as required by law.\n" +
                "            </p>\n" +
                "            <h3>8. Legal Responsibilities </h3>\n" +
                "            <p><strong>8.1 Responsibilities for Non-Compliance with Agreement Terms: </strong>Each party is obligated to\n" +
                "                adhere to all terms and provisions set forth in this Agreement. In the event of non-compliance with\n" +
                "                the Agreement terms by the Client (Hotelier) , Allbookers.com has the right to take appropriate\n" +
                "                measures to rectify the breaches, including but not limited to, suspending access to the platform,\n" +
                "                suspending services, or terminating the Agreement. The Client (Hotelier) is responsible for\n" +
                "                compensating any damages caused as a result of non-compliance with the Agreement terms and\n" +
                "                for covering any expenses related to resolving issues created by their breaches.</p>\n" +
                "            <p><strong>8.2 Limits of the Platform's Liability for Reservations or Other Client (Hotelier) -Related\n" +
                "                Issues: </strong>Allbookers.com will be responsible for providing a reliable platform and fulfilling its\n" +
                "                obligations under this Agreement. Allbookers.com takes full responsibility for any instance of\n" +
                "                overbooking and will take the necessary steps to resolve such situations as outlined in the\n" +
                "                Agreement. However, the platform will not be liable for any direct or indirect damages that may\n" +
                "                result from other bookings, problems with bookings, or other issues related to clients or property\n" +
                "                data. In accordance with applicable law, Allbookers.com's liability will be limited to the amount\n" +
                "                representing the fees paid by the client for the services provided by the platform. The client\n" +
                "                acknowledges and understands that any issue related to bookings, payments, or any other problem\n" +
                "                will be addressed through the procedures outlined in this Agreement and with the assistance of the\n" +
                "                platform's technical support, according to the specified terms</p>\n" +
                "            <h3>9. Termination of the Agreement and Property Closure</h3>   \n" +
                "            <p><strong>9.1 Termination Conditions</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.1.1 Commencement and Termination of the Agreement: </strong>Unless otherwise specified, the\n" +
                "                Agreement will commence on the date of acceptance by the Property and will continue for an\n" +
                "                indefinite period.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.1.2 Termination by the Client (Hotelier): </strong>The Client (Hotelier) has the right to terminate the\n" +
                "                Agreement if Allbookers.com fails to fulfill its obligations in accordance with the terms of the\n" +
                "                Agreement. The Client (Hotelier) must provide written notice to contract@allbookers.com for\n" +
                "                termination of the Agreement and provide a remedy period to address the raised issues if necessary.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.1.3 Termination by Allbookers.com: </strong>Allbookers.com has the right to terminate the Agreement\n" +
                "                if the Client (Hotelier) breaches the terms of the Agreement, including but not limited to, noncompliance with platform policies, providing inaccurate information, or any action that damages\n" +
                "                the platform's reputation. Allbookers.com will notify the Client (Hotelier) of the breaches and offer\n" +
                "                a remedy period, except where the breach is severe and unacceptable, in which case no remedy\n" +
                "                period is required.\n" +
                "                \n" +
                "            </p>\n" +
                "            <p><strong>9.2 Immediate Termination and Property Closure</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.2.1 Immediate Termination: </strong>Either Party may terminate the Agreement (and close the Property\n" +
                "                on the Platform) or limit or suspend (all or part of the obligations, commitments, and\n" +
                "                responsibilities) under this Agreement with the other Party, with immediate effect and without the\n" +
                "                need for a notice of breach in the event of: (i) a legal or regulatory obligation; (ii) an imperative\n" +
                "                reason in accordance with applicable law; (iii) a repeated breach of the Agreement by the other\n" +
                "                Party; or (iv) a significant (actual or suspected) breach by the other Party of any term of this\n" +
                "                Agreement, instances of illegal or inappropriate content, fraud, provision of false information, or\n" +
                "                receipt of a significant number of complaints from Guests; or (v) (a submission or filing of a\n" +
                "                bankruptcy petition, or suspension of payments, or any similar action or event related to the other\n" +
                "                Party).\n" +
                "            </p>\n" +
                "            <p><strong>9.3 Notice and Payment After Termination</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.3.1 Notice of Termination: </strong>Any notice or communication from Allbookers.com regarding the\n" +
                "                \"closure\" (or similar suspend) of the Property on the website will imply the termination of the\n" +
                "                Agreement. After the termination or suspension of the Agreement, the Property must honor any\n" +
                "                unpaid reservations for Guests and pay all commissions (including costs, expenses, and interest if\n" +
                "                applicable) that are due for these reservations in accordance with the terms of the Agreement.\n" +
                "                Following the termination or suspension of the Agreement, and notwithstanding Allbookers.com's\n" +
                "                right to remove the Property from the Platform, Allbookers.com may retain and store the property\n" +
                "                page on the management Platform, not displaying it publicly but marking the availability as\n" +
                "                \"suspend\" pending full and final payment of any outstanding amounts (including any\n" +
                "                Commission).\n" +
                "            </p> \n" +
                "            <h3>10. General Provisions</h3>\n" +
                "            <p><strong>10.1 Amendments to the Agreement: </strong>Amendments to this Agreement can only be made with the\n" +
                "                written agreement of both parties. Any changes or additions to the Agreement will be valid only if\n" +
                "                documented and signed by authorized representatives of both parties. If a change is necessary to\n" +
                "                meet new legal or regulatory requirements, the parties will collaborate to make the necessary\n" +
                "                updates to the Agreement and will inform each other of such changes as soon as possible.\n" +
                "                </p>\n" +
                "            <p><strong>10.2 Transfer of Rights and Obligations (Change of Ownership): </strong>If the owner of a property\n" +
                "                wishes to transfer ownership to another party, they must follow the procedure outlined below to\n" +
                "                request a transfer of ownership through the platform panel.allbookers.com: <br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>1. Ownership Transfer Request: </strong>The current owner must submit a request for the transfer\n" +
                "                of ownership through the available option on panel.allbookers.com. This request should\n" +
                "                include complete information about the proposed new owner and all other relevant details\n" +
                "                related to the property<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>2. Verification: </strong>Allbookers.com will conduct the necessary verifications to confirm the\n" +
                "                identity of the new owner and to ensure that the property transfer complies with the\n" +
                "                platform's policies and procedures.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>3. Approval and New Agreement: </strong>Once verifications are completed and the request is\n" +
                "                accepted, Allbookers.com will issue a new Agreement that will include the new property\n" +
                "                details, the new owner's name, and any other relevant information. The new Agreement\n" +
                "                will replace the existing Agreement and will take effect upon being signed by both\n" +
                "                parties<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>4. Notification to the New Owner: </strong>After the approval and signing of the new Agreement,\n" +
                "                the new owner will be notified and will gain access to the management panel to continue\n" +
                "                managing the property and bookings in accordance with the new Agreement terms.\n" +
                "            </p>\n" +
                "            <h3>11. Ranking, Guest Reviews and Marketing </h3>\n" +
                "            <p><strong>11.1 Ranking: </strong>Allbookers.com aims to display search results that are relevant to each individual\n" +
                "                Guest by offering a personalized ranking of properties on the platform. This system may include\n" +
                "                criteria for ranking properties in search results and may be based on various factors such as service\n" +
                "                quality, user experience, and overall property performance. Allbookers.com reserves the right to\n" +
                "                change the ranking algorithm in accordance with the platform's needs and to ensure a satisfactory\n" +
                "                user experience</p>\n" +
                "            <p><strong>11.2 Guest Reviews: </strong>Guests who have stayed or had an experience at a property will be asked by\n" +
                "                Allbookers.com to provide feedback and a rating on specific aspects of their experience with the\n" +
                "                property. Allbookers.com may publish these reviews on the platform. Allbookers.com acts as a\n" +
                "                distributor and not a publisher of these reviews. Allbookers.com will evaluate Guest reviews in\n" +
                "                accordance with applicable policies. Allbookers.com will not be responsible for the reviews\n" +
                "                displayed or not displayed on the platform in accordance with applicable law.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>11.2.1 </strong>Allbookers.com may, at its sole discretion, keep reviews hidden from display on the\n" +
                "                platform, remove reviews, or request a Guest to provide a revised version of a review if it contains\n" +
                "                or refers to any content that Allbookers.com deems inappropriate and/or offensive, including but\n" +
                "                not limited to: (i) politically sensitive comments; (ii) illegal activities; (iii) personal or sensitive\n" +
                "                information (e.g., emails, addresses, phone numbers, or credit card information); (iv) links to other\n" +
                "                websites; (v) inappropriate language, sexual references, hate speech, discriminatory comments,\n" +
                "                threats, insults, or references to violence. <br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>11.2.2 </strong>Property owners must not manipulate or attempt to manipulate Guest reviews (e.g., by\n" +
                "                paying for positive reviews or posting fake reviews for a competing property).<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>11.2.3 </strong>Guest reviews are for the exclusive use of Allbookers.com. Property owners are not entitled\n" +
                "                to use Guest reviews directly or indirectly in any way without prior written approval fromAllbookers.com.</p>\n" +
                "            <p><strong>11.3 Online Marketing</strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>11.3.1 </strong>Allbookers.com engages in online marketing campaigns at its own cost and discretion and\n" +
                "            may promote the property using the property's name in such marketing, including email marketing.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>11.3.2 </strong>If the property owner becomes aware of any third-party platform behavior that infringes on\n" +
                "            their intellectual property rights, they must immediately notify Allbookers.com in writing with\n" +
                "            details of the behavior, and Allbookers.com will use reasonable commercial efforts to ensure that\n" +
                "            the third party takes appropriate measures to address the infringement.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>11.3.3 </strong>The property owner agrees not to use, directly or indirectly, the Allbookers.com brand/logo\n" +
                "            (including the business name, trademark, service mark, or any other similar sign of identity or\n" +
                "            source) for comparative pricing purposes or for any other purpose, whether on the property's\n" +
                "            platform or on any third-party platform, system, search engine, or otherwise, unless previously\n" +
                "            approved in writing by Allbookers.com.</p>\n" +
                "            <h3>12. Miscellaneous</h3>\n" +
                "            <p><strong>12.1 Indemnification by the Client (Hotelier): </strong>The Client (Hotelier) agrees to indemnify,\n" +
                "                defend, and hold harmless Allbookers.com, its affiliates, officers, directors, employees, and agents\n" +
                "                from and against any and all claims, liabilities, indemnities, losses, costs, and expenses (including\n" +
                "                reasonable attorneys' fees) arising from or related to (i) any breach of any term of the Agreement\n" +
                "                by the Client (Hotelier) ; (ii) any claim made by Guests related to the Property; (iii) any violation\n" +
                "                of applicable laws or regulations by the Client (Hotelier) ; and (iv) any negligence or misconduct\n" +
                "                by the Client (Hotelier).<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>12.1.1 Indemnification by Allbookers.com: </strong>Allbookers.com agrees to indemnify, defend, and\n" +
                "                hold harmless the Client (Hotelier) from and against any and all claims, liabilities, indemnities,\n" +
                "                losses, costs, and expenses (including reasonable attorneys' fees) arising from or related to (i) any\n" +
                "                breach of any term of the Agreement by Allbookers.com; (ii) any violation of applicable laws or\n" +
                "                regulations by Allbookers.com; and (iii) any negligence or misconduct by Allbookers.com\n" +
                "            </p>\n" +
                "            <p><strong>12.2 Limitation of Liability: </strong>Except as otherwise provided in the Agreement, neither Party shall\n" +
                "                be liable to the other Party for any indirect, incidental, consequential, special, or punitive damages,\n" +
                "                including but not limited to, loss of profits, loss of business, or loss of data, arising from or in\n" +
                "                connection with the Agreement, regardless of the cause of action, even if such damages were\n" +
                "                foreseeable or if the Party was advised of the possibility of such damages<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>12.2.1 Maximum Liability Limit: </strong>The maximum liability of either Party for any claim arising\n" +
                "                    from or in connection with the Agreement shall be limited to the amount of fees paid by the Client\n" +
                "                    (Hotelier) to Allbookers.com under the Agreement during the six (6) month period immediately\n" +
                "                    preceding the event giving rise to the claim\n" +
                "            </p>\n" +
                "            <p><strong>12.3 No Liability for Third-Party Services: </strong>Allbookers.com shall not be liable for any issues or\n" +
                "                damages arising from the use of third-party services or products not provided directly by\n" +
                "                Allbookers.com, including but not limited to, payment processing services, external booking\n" +
                "                platforms, or other third-party integrations</p>\n" +
                "            <p><strong>12.4 Force Majeure: </strong>Neither Party shall be liable for any failure to perform its obligations under\n" +
                "                the Agreement if such failure is due to events beyond its reasonable control, including but not\n" +
                "                limited to, natural disasters, war, terrorism, strikes, or other unforeseen events.</p>\n" +
                "            <p><strong>12.5 Legal Actions</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>12.5.1 Dispute Resolution: </strong>Any dispute arising from or in connection with the Agreement shall\n" +
                "                be resolved in accordance with the dispute resolution process outlined in the Agreement, including\n" +
                "                any requirement for mediation or arbitration, if applicable\n" +
                "            </p></div></div>");
        String htmlContent = stringBuilder.toString();

        // Konverto HTML në PDF
        byte[] pdfData = convertHtmlToPdf(htmlContent);

        // Shtoni PDF-në si bashkëngjitje në email
        helper.setText(email, true);
        helper.addAttachment("Agreement.pdf", new ByteArrayResource(pdfData), "application/pdf");

        send(message);
        System.out.println("Mail for accept Request for agreement Send...");
    }

    //dergohet email ne hotelier kur pranohet kerkesa per marrveshje nga hotelieri
    public void albaniaagreement(Agreement agreement, String wheresendemail) throws MessagingException, DocumentException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(wheresendemail);
        helper.setSubject("Allbookers Marrëveshja");

        String email = "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>"
                +"<div style=\"text-align: left; max-width: 580px; margin: auto;\">"
                +"<br><h3>Përshëndetje " + agreement.getLegal_bussines_name() + ",\n</h3>" +
                "<p>Bashkëngjitur do të gjeni kopjen e kontratës tuaj.\n</p>" +
                "<p>Gjithë të mirat.</p>" +
                "<p>Nga <a href=\"https://allbookers.com/\" style=\"text-decoration: none;\">Allbookers.com</a>\n</p></div>"
                + "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
                + "<p style=\"text-align: center;\">© E drejta e autorit 2024 Allbookers.com | Te gjitha te drejtat e rezervuara."
                + "<br>Ky e-mail është dërguar nga allbookers.com.</p>";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"mainDiv\">\n" +
                "      <div class=\"column\" style=\"float: left; width: 100%\">\n" +
//                    "    <img src=\"https://allbookers.com/images/allbookersorange.png\" style=\"width: 350px; margin-bottom: 5px;\"></img>\n" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "      </div>\n" +
                "    <div style=\"text-align: left; \">\n" +
                "<h2>Marrëveshja e Akomodimit me Allbookers.com</h2>\n" +
                " <p>Ky dokument përfaqëson një marrëveshje ligjore mes:</p>\n" +
                " <p>Ndërmjet:</p>\n" +
                " <p><strong>Allbookers</strong>, një platformë e operuar nga <strong>InterMedia Sh.P.K</strong>, një shoqëri e regjistruar sipas ligjeve të Republikës së Shqipërisë, me seli në <strong>Bulevardi Bajram Curri tek ura e ATSH, nr pasurie 6/516+1-20</strong>, NIPT <strong>M02028001D</strong>, këtu e tutje referuar si \"Kompani\" ose \"Allbookers\",</p>\n" +
                " <p><strong>Dhe</strong></p>\n" +
                " <p><strong>"+agreement.getFirst_name()+" "+agreement.getLast_name()+"</strong>, pronar i pronës <strong>"+agreement.getProperty().getName()+"</strong>, e vendosur në <strong>"+agreement.getAddress()+"</strong>, NIPT <strong>"+agreement.getNuis()+"</strong>, referuar si \"Klienti (Hotelieri)\" ose \"Hotelieri\",</p>\n" +
                " <h3>Marrëveshja përfshin kushtet e mëposhtme :</h3>\n" +
                " <p>Për të gjitha pronat që do të regjistrohen në Allbookers.com të listuara nën emrin e Partnerit,\n" +
                "     zbatohen përqindjet e mëposhtme të komisionit lokal:</p>\n" +
                " <ul><li>Shqipëri: 10%</li></ul>\n" +
                " <p><strong>Allbookers.com</strong> ofron një platformë të sigurt dhe të besueshme për menaxhimin e pronave dhe\n" +
                "     rezervimeve.</p>\n" +
                " <h3>Ekzekutimi dhe performanca</h3>\n" +
                " <p>Marrëveshja është e vlefshme vetëm pas miratimit dhe konfirmimit nga Allbookers.com.</p>\n" +
                " <h3>Kushtet e Përgjithshme të Marrëveshjes:</h3>\n" +
                " <p>Kjo Marrëveshje përcakton termat dhe kushtet për përdorimin e platformës Allbookers.com nga\n" +
                "     hotelieri për të reklamuar dhe menaxhuar pronën e tij, duke përfshirë pranimin e rezervimeve,\n" +
                "     menaxhimin e çmimeve dhe disponueshmërisë dhe përdorimin e panelit të administrimit aksesuar\n" +
                "     në <a href=\"https://panel.allbookers.com\">panel.allbookers.com</a>.</p>\n" +
                " <h3>Deklarimi nga Partneri</h3>\n" +
                " <p>Partneri deklaron se kjo është një veprimtari legjitime akomodimi me të gjitha licencat dhe lejet e\n" +
                "     nevojshme, të cilat mund të tregohen me kërkesën e parë. Allbookers.com . rezervon të drejtën të\n" +
                "     verifikojë dhe hetojë çdo detaj të dhënë nga Partneri në këtë regjistrim.</p>\n" +
                " <h3>Kohëzgjatja e Marrëveshjes</h3>\n" +
                " <p>Kjo marrëveshje do të jetë e vlefshme nga data e nënshkrimit dhe do të vazhdojë të jetë në fuqi\n" +
                "     deri në përfundimin ose anulimin nga njëra palë në përputhje me termat e kësaj Marrëveshjeje.</p>\n" +
                " <p><strong style=\"font-size: 18px;\">Data e fillimit të Marrëveshjes: </strong>"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+ "</p> " +
                "<p><br><br></br></br></p>\n" +
                " <h2>Përmbajtja e Marrëveshjes</h2>\n" +
                " <p><strong>1. Përkufizimet</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>1.1</strong>Shpjegimi i termave të përdorur në Marrëveshje, përfshirë: \"Platforma\", \"Prona\",\n" +
                "     \"Rezervimi\", \"Paneli i Menaxhimit\", \"Klienti (Hotelieri)\" etj. <br></br>\n" +
                "     <strong>2. Qëllimi i Marrëveshjes</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>2.1 </strong>Përshkrimi i shërbimit të ofruar nga Allbookers.com. <br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>2.2 </strong>Qëllimi i marrëveshjes dhe përdorimi i platformës nga Klienti për menaxhimin e pronësdhe rezervimeve. <br></br>\n" +
                "     <strong>3. Të Drejtat dhe Detyrimet e Palëve</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>3.1</strong> Detyrimet e Allbookers.com. <br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>3.2 </strong>Detyrimet e Pronës. <br></br>\n" +
                "     <strong>4. Regjistrimi dhe Përdorimi i Platformës</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>4.1</strong> Procesi i regjistrimit të pronës. <br></br>\n" +
                "     &#160;&#160;&#160;&#160; <strong>4.2</strong> Hapat për përdorimin e panelit të menaxhimit dhe përditësimin e informacionit të\n" +
                "     pronës.<br></br>\n" +
                "     <strong>5. Menaxhimi i Rezervimeve dhe Çmimeve</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>5.1</strong> Përcaktimi dhe menaxhimi i çmimeve dhe disponueshmërisë nga   hotelieri. <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>5.2</strong> Politikat për anullimet dhe ndryshimet e rezervimeve.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>5.3 </strong>Procedura e Pagesave dhe Komisioneve.<br></br>\n" +
                "     <strong>6. Pagesat dhe Tarifat</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>6.1 </strong>Struktura e tarifave dhe komisioneve të platformës. <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>6.2 </strong>Metodat e Pagesës dhe Afatet. <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>6.3 </strong>Fatura Mujore për Rezervimet. <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>6.4 </strong>Politikat për vonesat në pagesë ose komisionet e papaguara. <br></br>\n" +
                "     <strong>7. Siguria dhe Mbrojtja e të Dhënave</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>7.1 </strong>Mbrojtja e të dhënave për Klientët dhe Pronat.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>7.2 </strong>Masat e sigurisë për të dhënat personale dhe financiare.<br></br>\n" +
                "     <strong>8. Përgjegjësitë Ligjore</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>8.1 </strong>Përgjegjësitë për Mosrespektimin e Kushteve të Marrëveshjes.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>8.2 </strong>Kufizimet e Përgjegjësisë së Platformës për Rezervimet ose Çështjet e tjera që lidhen\n" +
                "     me Klientët.<br></br>\n" +
                "     <strong>9. Përfundimi i Marrëveshjes dhe Mbyllja e Pronës</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>9.1 </strong>Kushtet për përfundimin.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>9.2 </strong>Përfundimi i menjëhershëm dhe mbyllja e pronës.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>9.3 </strong>Njoftimi dhe Pagesat pas Përfundimit.<br></br>\n" +
                "     <strong>10. Dispozita të Përgjithshme</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>10.1 </strong>Ndryshimet në Marrëveshje<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>10.2 </strong>Transferimi i të drejtave dhe detyrimeve, Ndryshimi i Pronësisë.<br></br>\n" +
                "     <strong>11. Renditja, Vlerësimet e Mysafirëve dhe Marketingu</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.1 </strong>Renditja<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.2 </strong>Vlerësimet e Mysafirëve<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.3 </strong>Marketingu Online<br></br>\n" +
                "     <strong>12. Të Ndryshme</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.1 </strong>Dëmshpërblimi nga Klienti<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.2 </strong>Kufizimi i Përgjegjësisë<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.3 </strong>Asnjë Përgjegjësi për Shërbimet e Palëve të Treta<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.4 </strong>Forca Madhore<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.5 </strong>Veprimet Ligjore<br></br>\n" +
                " </p>\n" +
                "<p><br></br><br></br></p>\n" +
                " <h3>1. Përkufizimet</h3>\n" +
                " <p> Përveç termave të përcaktuar në pjesë të tjera të kësaj Marrëveshjeje, termat e mëposhtëm do të\n" +
                "     kenë kuptimin e caktuar në gjithë Marrëveshjen, përveç nëse ka një qëllim të kundërt:</p>\n" +
                " <p><strong>\"Akomodimi\"</strong> i referohet entitetit (person juridik ose fizik) që është palë në Marrëveshje. Nëse\n" +
                "     është ndryshe, do të thotë gjithashtu prona që është lidhur me Marrëveshjen që entiteti po\n" +
                "     kontrakton dhe për të cilën këto Kushte janë të aplikueshme.</p>\n" +
                " <p><strong>\"Çmimi i Dhomës\"</strong>: Çmimi i caktuar nga Hotelieri për njësitë akomoduese në Pronën e tij/saj, " +
                "     siç është i përcaktuar në Panelin e Menaxhimit dhe i paraqitur në Platformë për mysafirët.</p>\n" +
                " <p><strong>\"Informacioni i Akomodimit\"</strong> i referohet detajeve të ofruara nga dhe të lidhura me Akomodimin për t'u shfaqur në Platformë. " +
                "     Kjo përfshin imazhe, foto dhe përshkrime; informacion mbi shërbimet dhe pajisjet; disponueshmërinë e dhomave për rezervim; " +
                "     çmimet (përfshirë të gjitha taksat e zbatueshme, përveç rasteve kur kërkohet ndryshe me ligj, si dhe çdo tarifë shtesë); " +
                "     politikat e disponueshmërisë, anulimit dhe mungesës së paraqitjes; politika dhe kufizime të tjera të rëndësishme; " +
                "     si dhe çdo informacion tjetër që Allbookers.com është ligjërisht i detyruar të shfaqë në Platformë.</p>\n" +
                " <p><strong>\"Komisioni\"</strong>: Tarifa e paguar nga Klienti për shërbimet e ofruara nga Allbookers.com, përfshirë\n" +
                "     ndërmjetësimin e rezervimeve dhe shërbime të tjera të lidhura.</p>\n" +
                " <p><strong>\"Klienti (Hotelieri)\"</strong>: Një individ ose entitet ligjor që regjistron dhe menaxhon një Pronë në\n" +
                "     Platformën Allbookers.com, dhe që përdor shërbimet e Platformës për të menaxhuar rezervimet\n" +
                "     dhe promovimin e Pronës së tij/saj.</p>\n" +
                " <p><strong>\"Paneli i Menaxhimit\"</strong>: Mjeti i ofruar nga Allbookers.com që lejon Klientët të menaxhojnë dhe\n" +
                "     të azhurnojnë informacionin e Pronës së tyre, përfshirë çmimet, disponueshmërinë, rezervimet dhe\n" +
                "     komunikimet me Mysafirët.</p>\n" +
                " <p><strong>\"Partneri i Sinkronizimit të Allbookers.com me platforma të tjera\"</strong>: Një kompani ose individ\n" +
                "     që ofron shërbime për lidhjen e informacionit dhe integrimin midis Platformës Allbookers.com\n" +
                "     dhe sistemeve të tjera për të përmirësuar funksionalitetin dhe efikasitetin e shërbimeve të\n" +
                "     rezervimeve.</p>\n" +
                " <p><strong>\"Platforma\"</strong>: Do të thotë Allbookers.com, një sistem online që mundëson regjistrimin,\n" +
                "     menaxhimin dhe promovimin e pronave përmes internetit dhe ofron shërbime të lidhura për\n" +
                "     Klientët dhe Mysafirët.</p>\n" +
                " <p><strong>\"Prona\"</strong>: Çdo njësi akomoduese e regjistruar në Platformën Allbookers.com, përfshirë por pa u\n" +
                "     kufizuar në hotele, apartamente, vilë dhe shtëpi pushimi, që ofrohet për rezervim në emër të\n" +
                "     pronarëve të tyre.</p>\n" +
                " <p><strong>\"Rezervimi\"</strong>: Një marrëveshje elektronike e realizuar përmes Platformës Allbookers.com, midis\n" +
                "     Klientit dhe një Mysafiri për një qëndrim në Pronën e Klientit.</p>\n" +
                " <p><strong>\"Shërbimet e Lidhjes me Channel Manager\"</strong>: Shërbime që mundësojnë lidhjen dhe integrimin\n" +
                "     e informacionit midis Platformës Allbookers.com dhe sistemeve të tjera të menaxhimit të\n" +
                "     rezervimeve ose shërbimeve të tjera.</p>\n" +
                " <p><strong>\"Shërbime të Tjera\"</strong>: Shërbime të tjera të ofruara nga Platforma përveç funksionalitetit të\n" +
                "     zakonshëm të regjistrimit dhe menaxhimit të rezervimeve, duke përfshirë por pa u kufizuar në\n" +
                "     shërbime marketingu, analiza të të dhënave dhe mbështetje teknike.</p>\n" +
                " <p><strong>\"Të Dhënat e Klientit\"</strong>: Informacioni personal dhe të dhënat e tjera që mblidhen dhe ruhen\n" +
                "     nga Platforma për Klientët dhe Mysafirët, duke përfshirë por pa u kufizuar në emrin,\n" +
                "     adresën, informacionin e kontaktit dhe informacionin e pagesave.</p>\n" +
                " <h3>2. Qëllimi i Marrëveshjes</h3>\n" +
                " <h4>2.1 Përshkrimi i shërbimit të ofruar nga Allbookers.com:</h4>\n" +
                " <p>Allbookers.com ofron një platformë online të plotë të dizajnuar për hotelierët për të menaxhuar akomodimet dhe rezervimet e tyre në mënyrë efektive. Platforma ofron një gamë shërbimesh duke  përfshirë:</p>  " +
                "<p>&#160;&#160;&#160;&#160;<strong>1. Menaxhimi i Listimeve të Pronës:</strong> Lejon përdoruesit të krijojnë dhe mbajnë pronat e tyre,<br></br> " +
                " &#160;&#160;&#160;&#160;duke përfshirë përshkrime, foto, facilitetet etj.<br></br>" +
                " &#160;&#160;&#160;&#160;<strong>2. Menaxhimi i Rezervimeve:</strong> Lehtëson përpunimin e rezervimeve, duke përfshirë pranimin, <br></br> " +
                " &#160;&#160;&#160;&#160;modifikimin dhe anullimin e tyre sipas nevojës.<br></br>" +
                " &#160;&#160;&#160;&#160;<strong>3. Kontrolli i Çmimeve dhe Disponueshmërisë:</strong> Ofron mjete për vendosjen dhe përditësimin <br></br> " +
                " &#160;&#160;&#160;&#160;e çmimeve dhe disponueshmërisë në kohë reale.<br></br>" +
                " &#160;&#160;&#160;&#160;<strong>4. Promovimi dhe Vizibiliteti:</strong> Rrit ekspozimin e pronës përmes mjeteve të ndryshme të marketingut <br></br> " +
                " &#160;&#160;&#160;&#160;dhe promovimit për të tërhequr mysafirë nga e gjithë bota.<br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>5. Mbështetje Teknike:</strong> Ofron mbështetje për të ndihmuar me çdo problem që lidhet mepërdorimin <br></br> " +
                " &#160;&#160;&#160;&#160;e platformës.</p>\n" +
                "<h3>2.2 Qëllimi i marrëveshjes dhe përdorimi i platformës nga Klienti për menaxhimin e pronës dhe rezervimeve:</h3>\n" +
                "<p>Qëllimi i kësaj Marrëveshjeje është të përcaktojë termat dhe kushtet sipas të cilëve Klienti(Hotelieri) do të përdorë platformën Allbookers.com. Kjo përfshin:</p>\n" +
                "<p>&#160;&#160;&#160;&#160;<strong>1. Reklamimi dhe Menaxhimi i Pronës:</strong> Klienti bie dakord të përdorë platformën për të listuar dhe <br></br> " +
                "   &#160;&#160;&#160;&#160;menaxhuar pronën e tij, duke siguruar që informacioni të jetë i saktë dhe i përditësuar. <br></br>\n" +
                "   &#160;&#160;&#160;&#160;<strong>2. Menaxhimi i Rezervimeve:</strong> Klienti do të përdorë platformën për të pranuar, modifikuar dhe menaxhuar <br></br>" +
                "   &#160;&#160;&#160;&#160;rezervimet e bëra nga mysafirët<br></br>\n" +
                "   &#160;&#160;&#160;&#160;<strong>3. Mbrojtja e Çmimeve dhe Disponueshmërisë:</strong> Klienti është përgjegjës për vendosjen dhe përditësimin e <br></br> " +
                "   &#160;&#160;&#160;&#160;çmimeve dhe disponueshmërisë së pronës së tij përmes platformës.<br></br>\n" +
                "   &#160;&#160;&#160;&#160;<strong>4. Përputhshmëria me Politikat e Platformës:</strong> Klienti duhet të respektojë termat, politikat  dhe udhëzimet e <br></br> " +
                "   &#160;&#160;&#160;&#160;platformës për të siguruar përdorimin e duhur dhe për të ruajtur cilësinë dhe integritetin e shërbimeve të <br></br> " +
                "   &#160;&#160;&#160;&#160;ofruara\n" +
                "</p>" +
                " <h3>3. Të Drejtat dhe Detyrimet e Palëve</h3>\n" +
                " <strong>3.1 Detyrimet e Allbookers.com:</strong>\n" +
                " <p>&#160;&#160;&#160;&#160;<strong>3.1.1 </strong>Allbookers.com do të sigurojë akses të pandërprerë dhe të besueshëm në platformën e saj për\n" +
                "     të mundësuar që Klienti të menaxhojë pronën dhe rezervimet në mënyrë efikase. Ky akses përfshin\n" +
                "     përdorimin e Panelit të Menaxhimit, <b>panel.allbookers.com</b>  të cilin Klienti do ta përdorë për të\n" +
                "     përditësuar informacionin mbi pronën dhe për të menaxhuar rezervimet.</p>\n" +
                "     &#160;&#160;&#160;&#160;<strong>3.1.2 </strong>Përveç kësaj, Allbookers.com do të ofrojë mbështetje teknike. Kjo mbështetje është e\n" +
                "     dedikuar për të ndihmuar Klientin në përdorimin e platformës dhe për të zgjidhur çdo problem që\n" +
                "     mund të ndodhi gjatë operimit të platformës. Mbështetja teknike përfshin ndihmën për çështjet\n" +
                "     teknike, gabimet në sistem dhe problemet e lidhura me aksesin në platformë.\n <br></br>" +

                " &#160;&#160;&#160;&#160;<strong>3.1.3 </strong>Për të mbrojtur të dhënat e Klientit dhe informacionin e pronës, Allbookers.com do të marrë\n" +
                "     masa të nevojshme për sigurinë e të dhënave. Kjo përfshin përdorimin e teknologjive të avancuara\n" +
                "     të enkriptimit dhe masave të tjera sigurie për të mbrojtur informacionin personal dhe financiar të\n" +
                "     Klientit nga akses i paautorizuar, humbja, ose dëmtimi. <br></br><br></br>" +

                " &#160;&#160;&#160;&#160;<strong>3.1.4 </strong>Allbookers.com gjithashtu do të përdorë strategji të ndryshme marketingu për të promovuar\n" +
                "     pronën e Klientit në platformë dhe në rrjete të tjera të mundshme. Promovimi përfshin përfshirjen\n" +
                "     e pronës në listat e kërkimeve të platformës dhe mund të përfshijë fushata promocionale dhe\n" +
                "     reklamat përkatëse.\n" +
                "     <br></br>" +
                " <h4>3.2 Detyrimet e Pronës:</h4>\n" +
                "   <p>&#160;&#160;&#160;&#160;<strong>3.2.1 </strong>Klienti është përgjegjës për përdorimin e duhur të platformës në përputhje me udhëzimet dhe\n" +
                "     politikat e Allbookers.com. Klienti duhet të sigurojë që përdorimi i platformës të jetë në përputhje\n" +
                "     me ligjin dhe standardet etike. Klienti është përgjegjës për çdo aktivitet që ndodh në llogarinë e tij\n" +
                "     dhe për ndihmën e ofruar për të zgjidhur çështjet që ndodhin gjatë përdorimit të platformës <br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>3.2.2 </strong>Për të siguruar që informacioni mbi pronën e tij është i saktë dhe i përditësuar, Klienti duhet\n" +
                "     të mbajë informacionin mbi pronën e tij, përfshirë çmimet, disponueshmërinë, dhe detajet e tjera,\n" +
                "     të sakta dhe të përditësuara në mënyrë të rregullt. Çdo ndryshim në informacionin e pronës duhet\n" +
                "     të pasqyrohet në platformë menjëherë pas ndodhjes. \n" +
                "    <br></br>" +
                " &#160;&#160;&#160;&#160;<strong>3.2.3 </strong>Klienti duhet të respektojë të gjitha kushtet dhe politikat e platformës Allbookers.com, duke\n" +
                "     përfshirë politikat për çmimet, disponueshmërinë, rezervimet dhe anullimet. Çdo shkelje e\n" +
                "     kushteve dhe politikave të platformës mund të rezultojë në masa disiplinore, duke përfshirë\n" +
                "     ndalimin e aksesit në platformë.</p>" +
                " <h3>4. Regjistrimi dhe Përdorimi i Platformës <span style=\"color: rgb(0, 162, 255); text-decoration: underline;\">panel.allbookers.com</span></h3> \n" +
                " <p><strong>4.1 Procesi i regjistrimit të pronës: </strong>Regjistrimi i pronës në platformën Allbookers.com është i\n" +
                "     menaxhuar nga stafi ynë i marketingut dhe përfshin plotësimin e të gjitha të dhënave të nevojshme\n" +
                "     për përfshirjen e saj në sistem. Stafi ynë do të mbledhë dhe do të regjistrojë informacionin e plote\n" +
                "     të pronës, duke përfshirë emrin, adresën, llojin e akomodimit, përshkrimin dhe çdo informacion\n" +
                "     tjetër të rëndësishëm. Pas përfundimit të regjistrimit, do të dërgohet një kërkesë për të nënshkruar\n" +
                "     Marrëveshjen përkatëse, nëse informacionet e nevojshme nuk janë të plota. Marrëveshja do të hyjë\n" +
                "     në fuqi vetëm pas miratimit dhe nënshkrimit nga ana juaj." +
                "     </p>\n" +
                " <p><strong>4.2 Hapat për përdorimin e panelit të menaxhimit dhe përditësimin e informacionit të\n" +
                "     pronës:</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>1. Hyni në Panelin e Menaxhimit: </strong>Pas regjistrimit të suksesshëm të pronës dhe nënshkrimit\n" +
                "     të Marrëveshjes, do të merrni kredencialet e hyrjes për panelin e menaxhimit në adresën\n" +
                "     panel.allbookers.com. Përdorni këto kredenciale për të hyrë në sistem dhe për të filluar\n" +
                "     menaxhimin e pronës suaj.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>2. Përditësoni Informacionin e Pronës: </strong>Në panelin e menaxhimit, do të keni mundësinë të\n" +
                "     përditësoni informacionin e pronës suaj, duke përfshirë çmimet, disponueshmërinë,\n" +
                "     përshkrimet e akomodimeve dhe fotografitë. Sigurohuni që të mbani informacionin të saktë\n" +
                "     dhe të përditësuar për të ofruar një përvojë të mirë për klientët<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>3. Menaxhoni Rezervimet: </strong>Paneli ofron mundësi për menaxhimin e rezervimeve, duke\n" +
                "     përfshirë pranimin, modifikimin dhe anullimin e rezervimeve. Do të mund të shihni dhe\n" +
                "     trajtoni të gjitha rezervimet në një vend të vetëm dhe të komunikoni me klientët për çdo\n" +
                "     informacion të rëndësishëm në lidhje me qëndrimin e tyre<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>4. Kontrolloni Raportet dhe Analizat: </strong>Paneli gjithashtu ofron raportet dhe analizat mbi\n" +
                "     performancën ditore të pronës suaj. Ky informacion është i dobishëm për të monitoruar\n" +
                "     aktivitetin dhe për të bërë përmirësime të nevojshme për të maksimizuar ekspozimin dhe\n" +
                "     përfitimet e pronës suaj.\n" +
                " </p>\n" +
                " <h3>5. Menaxhimi i Rezervimeve dhe Çmimeve</h3>\n" +
                " <p><strong>5.1 Përcaktimi dhe menaxhimi i çmimeve dhe disponueshmërisë nga hotelieri:</strong> Hotelieri ka\n" +
                "     përgjegjësinë për të përcaktuar dhe menaxhuar çmimet dhe disponueshmërinë e akomodimeve në\n" +
                "     platformën Allbookers.com. Ky proces përfshin caktimin e çmimeve për periudha të ndryshme,\n" +
                "     ofrimin e mundësive për tarifat e veçanta ose për promocione dhe përcaktimin e disponueshmërisë\n" +
                "     në bazë të nevojave dhe preferencave të tij. Hotelieri është gjithashtu përgjegjës për përditësimin\n" +
                "     e informacionit në panelin e menaxhimit për të reflektuar ndryshimet në çmime dhe\n" +
                "     disponueshmëri, duke siguruar që informacioni të jetë gjithmonë i saktë dhe në përputhje me\n" +
                "     politikat e platformës.</p>\n" +
                " <p><strong>5.2 Politikat për anullimet dhe ndryshimet e rezervimeve: </strong>Platforma Allbookers.com ofron\n" +
                "     fleksibilitet në menaxhimin e politikave të anullimeve dhe ndryshimeve për rezervimet. Hotelieri\n" +
                "     duhet të përcaktojë dhe të publikojë politikat për anullimet dhe ndryshimet në rezervime në\n" +
                "     përputhje me rregullat e platformës dhe preferencat e tij. Këto politika duhet të jenë të qarta dhe të\n" +
                "     kuptueshme për klientët dhe të ofrojnë informacion të detajuar mbi afatet dhe kushtet për anullimin\n" +
                "     ose ndryshimin e rezervimeve. Hotelieri është përgjegjës për respektimin e këtyre politikave dhe\n" +
                "     për të siguruar që të gjitha kërkesat për anullime dhe ndryshime të trajtohen në përputhje me\n" +
                "     rregullat e përcaktuara.</p>\n" +
                " <p><strong>5.3 Procedura e pagesave dhe komisioneve: </strong>Pagesat për rezervimet do të kryhen në përputhje\n" +
                "     me procedurat dhe afatet e përcaktuara nga platforma Allbookers.com. Të gjitha rezervimet do të\n" +
                "     kryhen me të dhëna kartë krediti, dhe pagesa do të bëhet direkt pas përfundimit të rezervimit. Kjo\n" +
                "     do të thotë se rezervimet janë të sigurta dhe pagesat do të menaxhohen gjithmonë nga\n" +
                "     Allbookers.com. Komisionet për shërbimet e ofruara do të zbriten automatikisht nga pagesat e\n" +
                "     rezervimeve dhe do të raportohen në mënyrë të detajuar në panelin e menaxhimit. Hotelieri është\n" +
                "     përgjegjës për verifikimin e saktsisë së pagesave dhe komisioneve dhe për të siguruar që të gjitha\n" +
                "     transaksionet të jenë të përfunduara në mënyrë të duhur dhe të përputhshme me kushtet e\n" +
                "     platformës.<br></br><br></br>\n" +
                "     <strong>5.3.1 </strong>Allbookers.com siguron që të administrojë pagesat përmes platformës dhe mban përgjegjësi\n" +
                "     për çdo mbivendosje rezervimesh (overbooking). Kjo do të nënkuptojë që në rastet e\n" +
                "     mbivendosjeve, Allbookers.com do të ndërmarrë masat e nevojshme për të zgjidhur situatën dhe\n" +
                "     do të mbajë përgjegjësinë për këtë proces. Në rastet e mbivendosjes së rezervimeve (overbooking),\n" +
                "     Allbookers.com merr përgjegjësinë për të zgjidhur problemin dhe siguron që klienti të\n" +
                "     akomodohet. Nëse është e nevojshme, hoteli do të mbulojë koston shtesë për akomodimin e klientit\n" +
                "     në një strukturë me standard më të lartë se ajo e rezervuar fillimisht.\n" +
                " </p>\n" +
                " <h3>6. Pagesat dhe Tarifat</h3>\n" +
                " <p><strong>6.1 Struktura e tarifave dhe komisioneve të platformës: </strong>Allbookers.com aplikon një strukturë\n" +
                "     të qartë tarifash dhe komisionesh për shërbimet e saj. Komisioni për çdo rezervim të kryer përmes\n" +
                "     platformës është 10% e shumës totale të rezervimit. Ky komision zbatohet për çdo transaksion dhe \n" +
                "     është i përfshirë në pagesën përfundimtare që hotelieri do të marrë pas përmbushjes së rezervimit.\n" +
                "     Struktura e tarifave dhe komisioneve mund të përditësohet nga platforma dhe detajet e saj do të\n" +
                "     jenë gjithmonë të disponueshme në panelin e menaxhimit.</p>\n" +
                " <p><strong>6.2 Metodat e Pagesës dhe Afatet: </strong>Pas përfundimit të rezervimit, mysafiri do të duhet të shtojë të dhënat e kartës së kreditit në formën e rezervimit. " +
                "     <strong>Allbookers.com</strong> do të verifikojë vlefshmërinë e kartës për të siguruar që është e vërtetë, por nuk do të përpunojë pagesën. Pagesa për rezervimin do të kryhet drejtpërdrejt në pronë sipas kushteve të hotelierit. " +
                "     Informacioni i kartës do të mbetet i lidhur me rezervimin për referencë dhe siguri, por të gjitha transaksionet financiare do të menaxhohen nga hoteli dhe jo nga <strong>Allbookers.com</strong>.</p>\n" +
                " <p><strong>6.3 Fatura mujore e rezervimeve: </strong>Në datën 1 të çdo muaji, Allbookers.com do të mbledhë të\n" +
                "     gjitha rezervimet e kryera gjatë muajit të mëparshëm dhe do të gjenerojë automatikisht një faturë\n" +
                "     për këto rezervime muajin pasardhës. Fatura do të përfshijë komisionin prej 10% për çdo rezervim.\n" +
                "     Fatura do të jetë e disponueshme për hotelierin dhe do të përmbajë të gjitha informacionet e\n" +
                "     nevojshme për pagesën e komisioneve. Në rastet kur Mysafiri nuk është paraqitur (no-show) ose\n" +
                "     ka kryer pagesa paraprake, do të ofrohen mundësi për të diskutuar dhe zgjidhur çështjet e lidhura\n" +
                "     me rezervimet në përputhje me politikat e platformës.</p>\n" +
                " <p><strong>6.4 Politikat për vonesat në pagesë ose komisionet e papaguara: </strong>Në rastet e vonesave në pagesa\n" +
                "     ose komisioneve të papaguara, Allbookers.com do të ndjekë procedurat e përcaktuara për të\n" +
                "     trajtuar këto situata. Hotelieri është përgjegjës për të paguar të gjitha komisionet e papaguara në\n" +
                "     përputhje me afatet e përcaktuara. Në rastet e vonesave, platforma mund të aplikojë ndëshkime të\n" +
                "     përcaktuara në Marrëveshje, përfshirë tarifa shtesë për vonesën. Nëse ndodhin vonesa të\n" +
                "     vazhdueshme në pagesa, Allbookers.com rezervon të drejtën të pezullojë ose të ndërpresë\n" +
                "     shërbimin deri në shlyerjen e plotë të detyrimeve financiare.</p>\n" +
                " <h3>7. Siguria dhe Mbrojtja e të Dhënave </h3>  \n" +
                " <p><strong>7.1 Mbrojtja e të dhënave për Klientët dhe Pronat: </strong>Allbookers.com është e angazhuar për të\n" +
                "     mbrojtur dhe ruajtur të dhënat e klientëve dhe pronave në mënyrë të sigurt dhe të besueshme. Të\n" +
                "     dhënat që mbledhim përfshijnë informacionin personal dhe financiar të klientëve, si dhe të dhënat\n" +
                "     e pronave të regjistruara në platformë. Këto të dhëna ruhen në përputhje me legjislacionin në fuqi\n" +
                "     për mbrojtjen e të dhënave dhe standardet më të larta të sigurisë. Të dhënat do të përdoren vetëm\n" +
                "     për qëllimet për të cilat janë mbledhur dhe nuk do të shpërndahen apo të përdoren për qëllime të\n" +
                "     tjera pa pëlqimin e klientëve.</p>\n" +
                " <p><strong>7.2 Masat e sigurisë për të dhënat personale dhe financiare: </strong>Allbookers.com zbaton masa të\n" +
                "     avancuara të sigurisë për të mbrojtur të dhënat personale dhe financiare të klientëve dhe\n" +
                "     hotelierëve. Këto masa përfshijnë përdorimin e teknologjive të kriptimit për të mbrojtur\n" +
                "     informacionin gjatë transferimit dhe ruajtjes së tij në serverat tanë. Po ashtu, platforma përdor\n" +
                "     mekanizma të kontrollit të qasjes dhe procedura të rrepta për të siguruar që vetëm personat e \n" +
                "     autorizuar kanë qasje në të dhënat e ndjeshme. Në rast të ndonjë rreziku ose ndërhyrjeje në sistem,\n" +
                "     Allbookers.com do të ndërmarrë veprime të menjëhershme për të adresuar dhe zgjidhur problemet\n" +
                "     e sigurisë, dhe do të njoftojë klientët dhe autoritetet përkatëse sipas kërkesave ligjore.\n" +
                "     </p>\n" +
                " <h3>8. Përgjegjësitë Ligjore</h3>\n" +
                " <p><strong>8.1 Përgjegjësitë për mosrespektimin e kushteve të Marrëveshjes: </strong>Secila palë është e detyruar\n" +
                "     të respektojë të gjitha kushtet dhe dispozitat e përcaktuara në këtë Marrëveshje. Në rast të\n" +
                "     mosrespektimit të kushteve të Marrëveshjes nga ana e Klientit (Hotelierit), Allbookers.com ka të\n" +
                "     drejtën të ndërmarrë masa të përshtatshme për të korrigjuar shkeljet, duke përfshirë, por pa u\n" +
                "     kufizuar në, ndalimin e aksesit në platformë, pezullimin e shërbimeve, ose përfundimin e\n" +
                "     Marrëveshjes. Klienti është përgjegjës për të kompensuar çdo dëm të shkaktuar si rezultat i\n" +
                "     mosrespektimit të kushteve të Marrëveshjes dhe për të mbuluar çdo shpenzim të lidhur me\n" +
                "     zgjidhjen e problemeve të krijuara nga shkeljet e tij.</p>\n" +
                " <p><strong>8.2 Kufizimet e Përgjegjësisë së Platformës për Rezervimet ose Çështjet e tjera që lidhen me\n" +
                "     Klientët:</strong> Allbookers.com do të jetë përgjegjëse për ofrimin e një platforme të besueshme dhe\n" +
                "     përmbushjen e detyrimeve të saj sipas kësaj Marrëveshjeje. Allbookers.com mban përgjegjësi të\n" +
                "     plotë për çdo rast mbivendosjeje rezervimesh (overbooking) dhe do të ndërmarrë masat e\n" +
                "     nevojshme për të zgjidhur situatat e tilla sipas kushteve të përcaktuara në Marrëveshje. Megjithatë,\n" +
                "     platforma nuk do të mbajë përgjegjësi për çdo dëm të drejtpërdrejtë ose të tërthortë që mund të\n" +
                "     ndodhin si rezultat i rezervimeve të tjera, problemeve me rezervimet ose çështjeve të tjera që lidhen\n" +
                "     me klientët ose të dhënat e pronave. Në përputhje me ligjin në fuqi, përgjegjësia e Allbookers.com\n" +
                "     do të jetë e kufizuar në shumën që përfaqëson tarifat e paguara nga klienti për shërbimet e ofruara\n" +
                "     nga platforma. Klienti pranon dhe kupton se çdo çështje që lidhet me rezervimet, pagesat, ose\n" +
                "     ndonjë problem tjetër do të trajtohet përmes procedurave të parashikuara në këtë Marrëveshje dhe\n" +
                "     me ndihmën e mbështetjes teknike të platformës, sipas kushteve të përcaktuara.</p>\n" +
                " <h3>9. Përfundimi i Marrëveshjes dhe Mbyllja e Pronës</h3>\n" +
                " <p><strong>9.1 Kushtet për përfundimin </strong><br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.1.1 </strong>Fillimi dhe Përfundimi i Marrëveshjes: Përveç nëse specifikohet ndryshe, Marrëveshja do\n" +
                " të fillojë në datën e pranimit nga Prona dhe do të vazhdojë për një periudhë të pacaktuar.\n" +
                " <br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.1.2 </strong>Përfundimi nga Klienti (Hotelieri): Klienti (Hotelieri) ka të drejtë të përfundojë\n" +
                " Marrëveshjen nëse Allbookers.com nuk përmbush detyrimet e tij në përputhje me kushtet e\n" +
                " Marrëveshjes. Klienti (Hotelieri) duhet të dërgojë një njoftim me shkrim në\n" +
                " contract@allbookers.com për përfundimin e Marrëveshjes dhe të ofrojë një periudhë korrigjimi\n" +
                " për të adresuar çështjet e ngritura, nëse është e nevojshme.<br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.1.3 Përfundimi nga Allbookers.com: </strong>Allbookers.com ka të drejtë të përfundojë Marrëveshjen\n" +
                " nëse Klienti (Hotelieri) shkel kushtet e Marrëveshjes, përfshirë por pa u kufizuar në\n" +
                " mosrespektimin e politikave të platformës, dhënien e informacionit të pasaktë ose çdo veprim që\n" +
                " dëmton reputacionin e platformës. Allbookers.com do të njoftojë Klientin (Hotelierin) për shkeljet\n" +
                " dhe do të ofrojë një periudhë korrigjimi, përveç rasteve kur shkelja është e rëndë dhe e\n" +
                " papranueshme, në të cilat raste nuk kërkohet periudhë korrigjimi.\n" +
                " \n" +
                " </p>\n" +
                " <p><strong>9.2 Përfundim i Menjëhershëm dhe Mbyllja e Pronës</strong><br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.2.1 Përfundim i Menjëhershëm: </strong>Secila Palë mund të përfundojë Marrëveshjen (dhe të mbyllë\n" +
                " Pronën në Platformë) ose të kufizojë ose pezullojë (të gjitha ose pjesërisht detyrimet, angazhimet\n" +
                " dhe përgjegjësitë) sipas kësaj Marrëveshjeje me Palën tjetër, me efekt të menjëhershëm dhe pa\n" +
                " nevojë për njoftim për shkelje në rastet e: (i) një detyrimi ligjor ose rregullator; (ii) një arsyeje të\n" +
                " rëndësishme në përputhje me ligjin në fuqi; (iii) një shkelje të përsëritur të Marrëveshjes nga Pala\n" +
                " tjetër; ose (iv) një shkelje të rëndësishme (reale ose të dyshuar) nga Pala tjetër të çdo kushti të\n" +
                " kësaj Marrëveshjeje, raste të përmbajtjes së paligjshme ose të papërshtatshme, mashtrim, dhënie e\n" +
                " informacionit të rremë, ose marrje e një numri të konsiderueshëm ankesash nga Mysafirët; ose (v)\n" +
                " (para ose gjatë paraqitjes së një kërkese për falimentim, pezullimi i pagesave, ose ndonjë veprim\n" +
                " ose ngjarje të ngjashme në lidhje me Palën tjetër).\n" +
                " </p>\n" +
                " <p><strong>9.3 Njoftimi dhe Pagesa pas Përfundimit </strong><br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>9.3.1 </strong>Pas përfundimit ose pezullimit të Marrëveshjes, Prona duhet të respektojë rezervimet e\n" +
                " papaguara për Mysafirët dhe të paguajë të gjitha komisionet (përfshirë kostot, shpenzimet,\n" +
                " interesat nëse është e aplikueshme) që janë të detyrueshme për këto rezervime në përputhje me\n" +
                " kushtet e Marrëveshjes. Pas përfundimit ose pezullimit të Marrëveshjes dhe pavarësisht të drejtës\n" +
                " së Allbookers.com për të hequr Pronën nga Platforma, Allbookers.com mund të mbajë dhe ruajë\n" +
                " faqen e pronës në Platformën e menaxhimit, jo shfaqjen e tij ne publik, por të mbyllë\n" +
                " disponueshmërinë (statusi: \"suspend (mbyllur)\") në pritje të pagesës të plotë dhe përfundimtare të\n" +
                " çdo shume të detyrueshme dhe të papaguar (përfshirë çdo Komision).\n" +
                " </p>\n" +
                " <h3>10. Dispozita të Përgjithshme</h3>\n" +
                " <p><strong>10.1 Ndryshimet në Marrëveshje: </strong>Ndryshimet në këtë Marrëveshje mund të bëhen vetëm me\n" +
                "     marrëveshjen e shkruar të të dyja palëve. Çdo ndryshim ose plotësim i Marrëveshjes do të jetë i\n" +
                "     vlefshëm vetëm nëse është i dokumentuar dhe është nënshkruar nga përfaqësuesit e autorizuar të\n" +
                "     të dyja palëve. Në rast se një ndryshim është i nevojshëm për të përmbushur kërkesa ligjore ose\n" +
                "     rregullatore të reja, palët do të bashkëpunojnë për të bërë përditësime të nevojshme në Marrëveshje\n" +
                "     dhe do të informojnë njëra-tjetrën për çdo ndryshim të tillë sa më shpejt të jetë e mundur.\n" +
                "     </p>\n" +
                " <p><strong>10.2 Transferimi i të drejtave dhe detyrimeve (Ndryshimi I pronësisë): </strong>Në rast se pronari i një prone \n" +
                "     dëshiron të transferojë pronësinë e saj në një palë tjetër, duhet të\n" +
                "     ndjekë procedurën e mëposhtme për të bërë kërkesën për transferim pronësie përmes platformës\n" +
                "     panel.allbookers.com:<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>1. Kërkesa për Transferim Pronësie: </strong>Pronari aktual duhet të dërgojë një kërkesë për\n" +
                "     transferim pronësie përmes opsionit të disponueshëm në panel.allbookers.com. Kjo\n" +
                "     kërkesë duhet të përmbajë informacion të plotë mbi pronarin e ri të propozuar dhe të gjithë\n" +
                "     detajet e tjera të rëndësishme që ndërlidhen me pronën.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>2. Verifikimi: </strong>Allbookers.com do të kryejë verifikimet e nevojshme për të konfirmuar\n" +
                "     identitetin e pronarit të ri dhe për të siguruar që transferimi i pronës është në përputhje me\n" +
                "     politikat dhe procedurat e platformës.\n" +
                "     <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>3. Miratimi dhe Marrëveshja e Re: </strong>Pasi të përfundojnë verifikimet dhe të pranohet kërkesa,\n" +
                "     Allbookers.com do të dërgojë një Marrëveshje të re që do të përfshijë të dhënat e reja të\n" +
                "     pronës, emrin e ri të pronarit, dhe çdo informacion tjetër të rëndësishëm. Marrëveshja e re\n" +
                "     do të zëvendësojë Marrëveshjen ekzistuese dhe do të hyjë në fuqi pasi të nënshkruhet nga\n" +
                "     të dyja palët.\n" +
                "     <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>4. Njoftimi për Pjesën tjetër: </strong>Pas aprovimit dhe nënshkrimit të Marrëveshjes së re, pronari\n" +
                "     i ri do të informohet dhe do të marrë qasje në panelin e menaxhimit për të vazhduar\n" +
                "     menaxhimin e pronës dhe rezervimeve në përputhje me kushtet e reja të Marrëveshjes.\n" +
                " </p>\n" +
                " <h3>11. Renditja, Vlerësimet e Mysafirëve dhe Marketingu</h3>\n" +
                " <p><strong>11.1 Renditja </strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.1.1 </strong>Allbookers.com ka për qëllim të shfaqë rezultatet e kërkimeve të përshtatshme për çdo\n" +
                "     Mysafir të veçantë, duke ofruar një renditje të personalizuar të Pronave në Platformë. Ky sistem\n" +
                "     mund të përfshijë kriteret për renditjen e pronave në rezultatet e kërkimeve dhe mund të bazohen\n" +
                "     në faktorë të ndryshëm si cilësia e shërbimit, përvoja e përdoruesve, dhe performanca e\n" +
                "     përgjithshme e pronës. Allbookers.com ruan të drejtën për të ndryshuar algoritmin e renditjes në\n" +
                "     përputhje me nevojat e platformës dhe për të siguruar një përvojë të kënaqshme për përdoruesit<br></br>\n" +
                "     \n" +
                " </p>\n" +
                " <p><strong>11.2 Vlerësimet e Mysafirëve: </strong>Mysafirët që kanë arritur ose qëndruar në Pronë do të kërkohen\n" +
                "     nga Allbookers.com për të komentuar dhe dhënë një vlerësim për aspekte të caktuara të përvojës\n" +
                "     së tyre me Pronën. Allbookers.com mund të publikojë këto vlerësime në Platformë.\n" +
                "     Allbookers.com është një shpërndarës dhe jo një botues i këtyre vlerësimeve. Allbookers.com do\n" +
                "     të vlerësojë vlerësimet e Mysafirëve në përputhje me Politikat e aplikueshme. Allbookers.com nuk\n" +
                "     do të jetë përgjegjëse në lidhje me vlerësimet e Mysafirëve të shfaqura, ose të mos shfaqura, në\n" +
                "     Platformë në përputhje me ligjin e aplikueshëm.<br></br>\n" +
                "     <strong>11.2.1 </strong>Allbookers.com mund, sipas diskrecionit të saj të vetëm, të mbajë të fshehta vlerësimet nga\n" +
                "     shfaqja në Platformë, të heqë vlerësimet, ose të kërkojë nga një Mysafir të ofrojë një version të \n" +
                "     ndryshuar të vlerësimit nëse ato përmbajnë ose i referohen ndonjë gjëje që Allbookers.com e\n" +
                "     përcakton si të papërshtatshme dhe/ose ofenduese, duke përfshirë, por pa u kufizuar në: (i) komente\n" +
                "     politiko-sensitive; (ii) aktivitete të paligjshme; (iii) informacion personal ose të ndjeshëm (p.sh.,\n" +
                "     emaile, adresa, numra telefoni, ose informacioni i kartës së kreditit); (iv) faqe të tjera interneti; (v)\n" +
                "     fjalor të pahijshëm, referenca seksuale, fjalë urrejtjeje, komente diskriminuese, kërcënime,\n" +
                "     ofendime, ose referenca për dhunë.\n" +
                "     <br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.2.2 </strong>Pronari i Pronës nuk duhet të manipulojë ose të përpiqet të manipulojë vlerësimet e\n" +
                "     Mysafirëve (p.sh. duke paguar për vlerësime pozitive ose duke postuar vlerësime të rreme për një\n" +
                "     pronë konkurrente).<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>11.2.3 </strong>Vlerësimet e Mysafirëve janë për përdorim ekskluziv nga Allbookers.com. Pronari i Pronës\n" +
                "     nuk ka të drejtë të përdorë drejtpërdrejt ose tërthorazi vlerësimet e Mysafirëve në ndonjë mënyrë\n" +
                "     pa miratimin e mëparshëm me shkrim nga Allbookers.com.\n" +
                " </p>\n" +
                " <p><strong>11.3 Marketingu (Online) </strong><br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>11.3.1 </strong>Allbookers.com ndjek fushata marketingu online në koston dhe diskrecionin e saj dhe mund\n" +
                " të promovoje Pronën duke përdorur emrin e Pronës në këtë marketing, duke përfshirë marketingun\n" +
                " me email.<br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>11.3.2 </strong>Nëse Pronari i Pronës bëhet i vetëdijshëm për sjellje nga një platformë e palës së tretë që\n" +
                " shkel të drejtat e tij të Pronësisë Intelektuale, ai duhet të njoftojë menjëherë Allbookers.com me\n" +
                " shkrim dhe detaje të sjelljes, dhe Allbookers.com do të përdorë përpjekjet tregtare të arsyeshme\n" +
                " për të siguruar që palës së tretë të marrë masat përkatëse për këtë shkelje.<br></br>\n" +
                " &#160;&#160;&#160;&#160;<strong>11.3.3 </strong>Pronari i Pronës bie dakord të mos përdorë, drejtpërdrejt ose tërthorazi, markën/logo-n e\n" +
                " Allbookers.com (përfshirë emrin e biznesit, markën tregtare, shenjën e shërbimit, ose ndonjë\n" +
                " shenjë tjetër të ngjashme të identitetit ose burimit) për qëllime krahasimi çmimesh ose për ndonjë\n" +
                " qëllim tjetër, qoftë në platformën e Pronës ose në ndonjë platformë të palës së tretë, sistem, motor,\n" +
                " ose ndryshe, nëse nuk është miratuar paraprakisht me shkrim nga Allbookers.com.\n" +
                " \n" +
                " </p>\n" +
                " <h3>12. Të ndryshme</h3>     \n" +
                " <p><strong>12.1 Dëmshpërblimi nga Klienti: </strong>Klienti është dakord të dëmshpërblejë, mbrojë dhe të mbajë pa\n" +
                "     dëme Allbookers.com, filialet e tij, zyrtarët, drejtorët, punonjësit dhe agjentët nga dhe kundër çdo\n" +
                "     të gjitha pretendimeve, përgjegjësive, dëmshpërblimeve, humbjeve, kostove dhe shpenzimeve\n" +
                "     (përfshirë tarifat e arsyeshme të avokatëve) që lindin nga ose në lidhje me (i) shkeljen e çdo kushti\n" +
                "     të Marrëveshjes nga Klienti; (ii) çdo pretendim të bërë nga Mysafirët që lidhet me Pronën; (iii)\n" +
                "     çdo shkelje të ligjeve ose rregulloreve të aplikueshme nga Klienti; dhe (iv) çdo neglizhencë ose\n" +
                "     sjellje të pahijshme nga Klienti.<br></br>\n" +
                "     <strong>12.1.1 Dëmshpërblimi nga Allbookers.com: </strong>Allbookers.com është dakord të dëmshpërblejë,\n" +
                "     mbrojë dhe të mbajë pa dëme Klientin nga dhe kundër çdo të gjitha pretendimeve, përgjegjësive,\n" +
                "     dëmshpërblimeve, humbjeve, kostove dhe shpenzimeve (përfshirë tarifat e arsyeshme të\n" +
                "     avokatëve) që lindin nga ose në lidhje me (i) shkeljen e çdo kushti të Marrëveshjes nga\n" +
                "     Allbookers.com; (ii) çdo shkelje të ligjeve ose rregulloreve të aplikueshme nga Allbookers.com;\n" +
                "     dhe (iii) çdo neglizhencë ose sjellje të pahijshme nga Allbookers.com.\n" +
                " </p>\n" +
                " <p><strong>12.2 Kufizimi i Përgjegjësisë: </strong>Përveç siç parashikohet ndryshe në Marrëveshje, asnjëra Palë nuk\n" +
                "     do të jetë përgjegjëse ndaj Palës tjetër për ndonjë dëm indirekt, rastësor, pasues, të veçantë ose\n" +
                "     ndëshkues, duke përfshirë por jo të kufizuar në, humbjen e fitimeve, humbjen e biznesit, ose\n" +
                "     humbjen e të dhënave, që lindin nga ose në lidhje me Marrëveshjen, pavarësisht nga shkaku i\n" +
                "     veprimit, madje edhe nëse këto dëme ishin të parashikueshme ose nëse Pala është këshilluar për\n" +
                "     mundësinë e këtyre dëmeve.<br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.2.1 </strong>Kufizimi Maksimal i Përgjegjësisë: Përgjegjësia maksimale e çdo Pale për çdo kërkesë\n" +
                "     që lind nga ose në lidhje me Marrëveshjen do të kufizohet në shumën e tarifave të paguara nga\n" +
                "     Klienti në Allbookers.com sipas Marrëveshjes gjatë periudhës gjashtë (6) mujore menjëherë para\n" +
                "     ngjarjes që jep shkak për kërkesën.\n" +
                " </p>\n" +
                " <p><strong>12.3 Asnjë Përgjegjësi për Shërbimet e Palëve të Treta: </strong>Allbookers.com nuk do të jetë\n" +
                "     përgjegjës për ndonjë problem ose dëme që lindin nga përdorimi i shërbimeve ose produkteve të\n" +
                "     palëve të treta që nuk ofrohen drejtpërdrejt nga Allbookers.com, duke përfshirë por jo të kufizuar\n" +
                "     në, shërbimet e përpunimit të pagesave, platforma të jashtme të rezervimeve, ose integrime të tjera\n" +
                "     të palëve të treta.\n" +
                "     </p>\n" +
                " <p><strong>12.4 Forca Madhore: </strong>Asnjëra Palë nuk do të jetë përgjegjëse për ndonjë dështim për të kryer\n" +
                "     detyrimet e saj sipas Marrëveshjes nëse ky dështim është për shkak të ngjarjeve jashtë kontrollit\n" +
                "     të saj të arsyeshëm, duke përfshirë por jo të kufizuar në, katastrofa natyrore, luftë, terrorizëm,\n" +
                "     greva, ose ngjarje të tjera të papritura.</p>\n" +
                " <p><strong>12.5 Veprimet Ligjore</strong><br></br>\n" +
                "     &#160;&#160;&#160;&#160;<strong>12.5.1 Zgjidhja e Mosmarrëveshjeve: </strong>Çdo mosmarrëveshje që lind nga ose në lidhje me\n" +
                "     Marrëveshjen do të zgjidhet në përputhje me procesin e zgjidhjes së mosmarrëveshjeve të\n" +
                "     parashikuara në Marrëveshje, duke përfshirë çdo kërkesë për mediatim ose arbitrazh, nëse është e\n" +
                "     aplikueshme.\n" +
                " </p> " +
                "</div></div>");
        String htmlContent = stringBuilder.toString();

        // Konverto HTML në PDF
        byte[] pdfData = convertHtmlToPdf(htmlContent);

        // Shtoni PDF-në si bashkëngjitje në email
        helper.setText(email, true);
        helper.addAttachment("Marreveshje.pdf", new ByteArrayResource(pdfData), "application/pdf");

        send(message);
        System.out.println("Emaili per te njoftuar marrëveshjen u dergua...");
    }

    public byte[] convertHtmlToPdf(String htmlContent) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();
        renderer = null;
        return outputStream.toByteArray();
    }

    public void sendNewUserCreatedData(User user, String password) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("New Account");

        String html = "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                "    <tbody style=\"width: 100%;\">" +
                "      <tr>" +
                "        <td colspan=\"1\">" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "        </td>" +
                "      </tr>" +
                "    </tbody>" +
                "  </table>" +
                "</div>" +
                "<div style=\"text-align: left; max-width: 580px; margin: 15px auto;\">" +
                "<p style=\"font-size: 16px; color:black !important; font-family: BlinkMacSystemFont,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;\">" +
                "Your account is ready!<br>" +
                "You may now sign-in with the following details:<br>" +
                "Username: " + user.getUsername() + "<br>" +
                "Password: <b>" + password + "</b><br><br>" +
                "When you click on \"Login\" you will create automatically a new password.<br><br>" +
                "Best Regards,<br>" +
                "The Allbookers.com Team</p>" +
                "</div>" +
                "<hr style=\"width: 35%; margin: 20px auto;\">" +
                "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                "<br>This e-mail was sent by allbookers.com.</p>" +
                "</div>";




        message.setContent(html, "text/html; charset=utf-8");
        send(message);
    }

    public void send(MimeMessage message){
        CompletableFuture.runAsync(() -> sender.send(message));
    }

    public void requestForChanges(User user, Property property, RequestChanges requestChanges, String adminEmail) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("workklea@gmail.com");
        System.out.println(adminEmail + " tfwvcfw");
        helper.setSubject("Request for Change of Address by User");
        String emailContent =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>" +
                        "<table style='width: 100%; border-collapse: collapse; max-width: 600px; margin: auto;'>" +
                        "  <tr>" +
                        "    <td colspan='2' style='padding: 5px;'>" +
                        "      <h2 style='color: #F26436;'>Request for Change of Address</h2>" +
                        "    </td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>User:</strong></td>" +
                        "    <td style='padding: 5px;'>" + user.getFull_name() + " (ID: " + user.getId() + ")</td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>Property:</strong></td>" +
                        "    <td style='padding: 5px;'>" + property.getName() + " (ID: " + property.getId() + ")</td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>Requested Fields</strong></td>" +
                        "    <td style='padding: 5px;'></td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>Street Name:</strong></td>" +
                        "    <td style='padding: 5px;'>" + requestChanges.getStreetName() + "</td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>City Name:</strong></td>" +
                        "    <td style='padding: 5px;'>" + requestChanges.getCityName() + " (ID: " + requestChanges.getCityId() + ")</td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>Zip Code:</strong></td>" +
                        "    <td style='padding: 5px;'>" + requestChanges.getZipCode() + "</td>" +
                        "  </tr>" +
                        "</table>" +
                        "<br>" +
                        "<p style='max-width: 600px; margin: auto;'>To view the full details and approve the request, please <a href='https://panel.allbookers.com/editAddress?id=" + property.getId() + "' style='color: #F26436; text-decoration: none;'>click here</a>.</p>" +
                        "</div>";
        helper.setText(emailContent, true);
        sender.send(message);
    }

    public void requestAccepted(Property property, AddressChange addressChange) throws MessagingException {
        String email;
        if (property.getAddress().getEmail() != null && !property.getAddress().getEmail().trim().equals("")) {
            email = property.getAddress().getEmail();
        } else {
            if (property.getRoles().get(0).getUsers().get(0).getEmail() != null && !property.getRoles().get(0).getUsers().get(0).getEmail().trim().equals("")) {
                email = property.getRoles().get(0).getUsers().get(0).getEmail();
            } else {
                email = null;
            }
        }
        System.out.println(email);
        if (email != null) {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Request for Change of Address Accepted");
            String emailContent =
                    "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                            "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                            "    <tbody style=\"width: 100%;\">" +
                            "      <tr>" +
                            "        <td colspan=\"1\">" +
                            "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                            "        </td>" +
                            "      </tr>" +
                            "    </tbody>" +
                            "  </table>" +
                            "</div>" +
                            "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>" +
                            "<table style='width: 100%; border-collapse: collapse; max-width: 600px; margin: auto;'>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>User:</strong></td>" +
                            "    <td style='padding: 5px;'>" + property.getRoles().get(0).getUsers().get(0).getFull_name() + " (ID: " + property.getRoles().get(0).getUsers().get(0).getId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Property:</strong></td>" +
                            "    <td style='padding: 5px;'>" + property.getName() + " (ID: " + property.getId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Requested Fields</strong></td>" +
                            "    <td style='padding: 5px;'></td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Street Name:</strong></td>" +
                            "    <td style='padding: 5px;'>" + addressChange.getStreetName() + "</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>City Name:</strong></td>" +
                            "    <td style='padding: 5px;'>" + addressChange.getCityName() + " (ID: " + addressChange.getCityId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Zip Code:</strong></td>" +
                            "    <td style='padding: 5px;'>" + addressChange.getZipCode() + "</td>" +
                            "  </tr>" +
                            "</table>" +
                            "<br>" +
                            "<p style='max-width: 600px; margin: auto;'>To view changes, please <a href='https://panel.allbookers.com/addressDetails?id=" + property.getId() + "' style='color: #F26436; text-decoration: none;'>click here</a>.</p>" +
                            "</div>";
            helper.setText(emailContent, true);
            sender.send(message);
        }
    }

    public void requestRefused(Property property, AddressChange addressChange) throws MessagingException {
        String email;
        if (property.getAddress().getEmail() != null && !property.getAddress().getEmail().trim().equals("")) {
            email = property.getAddress().getEmail();;
        } else {
            if (property.getRoles().get(0).getUsers().get(0).getEmail() != null && !property.getRoles().get(0).getUsers().get(0).getEmail().trim().equals("")) {
                email = property.getRoles().get(0).getUsers().get(0).getEmail();
            } else {
                email = null;
            }
        }
        System.out.println(email + " email121");
        System.out.println(email);
        if (email != null) {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Request for Change of Address Refused");
            String emailContent =
                    "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                            "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                            "    <tbody style=\"width: 100%;\">" +
                            "      <tr>" +
                            "        <td colspan=\"1\">" +
                            "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                            "        </td>" +
                            "      </tr>" +
                            "    </tbody>" +
                            "  </table>" +
                            "</div>" +
                            "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333; text-align: left; margin: auto; max-width: 600px;'>" +
                            "<table style='width: 100%; border-collapse: collapse;'>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>User:</strong></td>" +
                            "    <td style='padding: 5px;'>" + property.getRoles().get(0).getUsers().get(0).getFull_name() + " (ID: " + property.getRoles().get(0).getUsers().get(0).getId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Property:</strong></td>" +
                            "    <td style='padding: 5px;'>" + property.getName() + " (ID: " + property.getId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Requested Fields</strong></td>" +
                            "    <td style='padding: 5px;'></td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Street Name:</strong></td>" +
                            "    <td style='padding: 5px;'>" + addressChange.getStreetName() + "</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>City Name:</strong></td>" +
                            "    <td style='padding: 5px;'>" + addressChange.getCityName() + " (ID: " + addressChange.getCityId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Zip Code:</strong></td>" +
                            "    <td style='padding: 5px;'>" + addressChange.getZipCode() + "</td>" +
                            "  </tr>" +
                            "</table>" +
                            "<br>" +
                            "<p>To make other changes, please <a href='https://panel.allbookers.com/addressDetails?id=" + property.getId() + "' style='color: #F26436; text-decoration: none;'>click here</a>.</p>" +
                            "</div>";
            helper.setText(emailContent, true);
            sender.send(message);
        }
    }

    public void notifyDescriptionChangeRequest(User user, Property property, DescriptionChangeRequest changeRequest, String adminEmail) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(adminEmail);
        helper.setSubject("Request for Description Change by User");

        String emailContent =
                "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                        + "<div style='background-color: #f0f0f0; padding: 7px; text-align: center;'>"
                        + "<img src='https://www.allbookers.com/images/logoallbookers.png' alt='AllBookers Logo' style='max-width: 200px;'>"
                        + "</div>"
                        + "<h2>Request for Description Change</h2>"
                        + "<p><strong>User:</strong> " + user.getFull_name() + " (ID: " + user.getId() + ")</p>"
                        + "<p><strong>Property:</strong> " + property.getName() + " (ID: " + property.getId() + ")</p>"
                        + "<br>"
                        + "<p><strong>Requested Description:</strong></p>"
                        + "<p>" + changeRequest.getRequestedDescription() + "</p>"
                        + "<br>"
                        + "<p>To view the full details and approve the request, please <a href='https://panel.allbookers.com/propertyDescription?id=" + property.getId() + "' style='color: #F26436; text-decoration: none;'>click here</a>.</p>"
                        + "</div>";

        helper.setText(emailContent, true);
        sender.send(message);
    }

    private String getPropertyOwnerEmail(Property property) {
        String email = null;
        if (property.getAddress() != null && property.getAddress().getEmail() != null && !property.getAddress().getEmail().trim().isEmpty()) {
            email = property.getAddress().getEmail();
        } else if (!property.getRoles().get(0).getUsers().isEmpty() &&
                property.getRoles().get(0).getUsers().get(0).getEmail() != null &&
                !property.getRoles().get(0).getUsers().get(0).getEmail().trim().isEmpty()) {
            email = property.getRoles().get(0).getUsers().get(0).getEmail();
        }
        return email;
    }

    public void notifyDescriptionChangeApproved(Property property, String newDescription) throws MessagingException {
        String email = getPropertyOwnerEmail(property);
        if (email == null) {
            System.err.println("No valid email found for property ID: " + property.getId());
            return;
        }

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Request for Change of Description Approved");

        String emailContent =
                "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                        + "<div style='background-color: #f0f0f0; padding: 7px; text-align: center;'>"
                        + "<img src='https://www.allbookers.com/images/logoallbookers.png' alt='AllBookers Logo' style='max-width: 200px;'>"
                        + "</div>"
                        + "<h2>Request for Change of Description Approved</h2>"
                        + "<p><strong>User:</strong> " + property.getRoles().get(0).getUsers().get(0).getFull_name() + " (ID: " + property.getRoles().get(0).getUsers().get(0).getId() + ")</p>"
                        + "<p><strong>Property:</strong> " + property.getName() + " (ID: " + property.getId() + ")</p>"
                        + "<p><strong>Description:</strong> " + newDescription + "</p>"
                        + "<p>To make other changes, please <a href='https://panel.allbookers.com/textOfProperty?id=" + property.getId() + "' style='color: #F26436; text-decoration: none;'>click here</a>.</p>"
                        + "</div>";

        helper.setText(emailContent, true);
        sender.send(message);
    }

    public void notifyDescriptionChangeRejected(Property property, String rejectedDescription) throws MessagingException {
        String email = getPropertyOwnerEmail(property);
        if (email == null) {
            System.err.println("No valid email found for property ID: " + property.getId());
            return;
        }

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Request for Change of Description Rejected");

        String emailContent =
                "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                        + "<div style='background-color: #f0f0f0; padding: 7px; text-align: center;'>"
                        + "<img src='https://www.allbookers.com/images/logoallbookers.png' alt='AllBookers Logo' style='max-width: 200px;'>"
                        + "</div>"
                        + "<h2>Request for Change of Description Rejected</h2>"
                        + "<p><strong>User:</strong> " + property.getRoles().get(0).getUsers().get(0).getFull_name() + " (ID: " + property.getRoles().get(0).getUsers().get(0).getId() + ")</p>"
                        + "<p><strong>Property:</strong> " + property.getName() + " (ID: " + property.getId() + ")</p>"
                        + "<p><strong>Rejected Description:</strong> " + rejectedDescription + "</p>"
                        + "<p>To make other changes, please <a href='https://panel.allbookers.com/textOfProperty?id=" + property.getId() + "' style='color: #F26436; text-decoration: none;'>click here</a>.</p>"
                        + "</div>";

        helper.setText(emailContent, true);
        sender.send(message);
    }

    public void sendRequestPropertyName(User user, Property property, PropertyChange propertyChange    , String adminEmail) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("workklea@gmail.com");
        System.out.println(adminEmail + " tfwvcfw");
        helper.setSubject("Request for Change of Property Name by User");
        String emailContent =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>" +
                        "<table style='width: 100%; border-collapse: collapse; max-width: 600px; margin: auto;'>" +
                        "  <tr>" +
                        "    <td colspan='2' style='padding: 5px;'>" +
                        "      <h2 style='color: #F26436;'>Request for Change of Property Name</h2>" +
                        "    </td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>User:</strong></td>" +
                        "    <td style='padding: 5px;'>" + user.getFull_name() + " (ID: " + user.getId() + ")</td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>Property:</strong></td>" +
                        "    <td style='padding: 5px;'>" + property.getName() + " (ID: " + property.getId() + ")</td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>Requested Fields</strong></td>" +
                        "    <td style='padding: 5px;'></td>" +
                        "  </tr>" +
                        "  <tr>" +
                        "    <td style='padding: 5px;'><strong>Property Name:</strong></td>" +
                        "    <td style='padding: 5px;'>" + propertyChange.getPropertyName() + "</td>" +
                        "  </tr>" +
                        "</table>" +
                        "<br>" +
                        "<p style='max-width: 600px; margin: auto;'>To view the full details and approve the request, please <a href='https://panel.allbookers.com/editAddress?id=" + property.getId() + "' style='color: #F26436; text-decoration: none;'>click here</a>.</p>" +
                        "</div>";
        helper.setText(emailContent, true);
        sender.send(message);
    }

    public void requestPropertyNameAccepted(Property property, PropertyChange propertyChange, Optional<User> adminUser) throws MessagingException {
        if (adminUser.isPresent()) {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(adminUser.get().getEmail());
            helper.setSubject("Request for Change of Property Name Accepted");
            String emailContent =
                    "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                            "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                            "    <tbody style=\"width: 100%;\">" +
                            "      <tr>" +
                            "        <td colspan=\"1\">" +
                            "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                            "        </td>" +
                            "      </tr>" +
                            "    </tbody>" +
                            "  </table>" +
                            "</div>" +
                            "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>" +
                            "<table style='width: 100%; border-collapse: collapse; max-width: 600px; margin: auto;'>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>User:</strong></td>" +
                            "    <td style='padding: 5px;'>" + adminUser.get().getFull_name() + " (ID: " + adminUser.get().getId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Property:</strong></td>" +
                            "    <td style='padding: 5px;'>" + property.getName() + " (ID: " + property.getId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Requested Fields</strong></td>" +
                            "    <td style='padding: 5px;'></td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Property Name:</strong></td>" +
                            "    <td style='padding: 5px;'>" + propertyChange.getPropertyName() + "</td>" +
                            "  </tr>" +
                            "</table>" +
                            "<br>" +
                            "<p style='max-width: 600px; margin: auto;'>To view changes, please <a href='https://panel.allbookers.com/addressDetails?id=" + property.getId() + "' style='color: #F26436; text-decoration: none;'>click here</a>.</p>" +
                            "</div>";
            helper.setText(emailContent, true);
            sender.send(message);
        }
    }

    public void requestPropertyNameRefused(Property property, PropertyChange propertyChange, Optional<User> adminUser) throws MessagingException {
        if(adminUser.isPresent()){
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(adminUser.get().getEmail());
            helper.setSubject("Request for Change of Property Name Refused");
            String emailContent =
                    "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                            "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                            "    <tbody style=\"width: 100%;\">" +
                            "      <tr>" +
                            "        <td colspan=\"1\">" +
                            "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                            "        </td>" +
                            "      </tr>" +
                            "    </tbody>" +
                            "  </table>" +
                            "</div>" +
                            "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333; text-align: left; margin: auto; max-width: 600px;'>" +
                            "<table style='width: 100%; border-collapse: collapse;'>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>User:</strong></td>" +
                            "    <td style='padding: 5px;'>" + adminUser.get().getFull_name() + " (ID: " + adminUser.get().getId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Property:</strong></td>" +
                            "    <td style='padding: 5px;'>" + property.getName() + " (ID: " + property.getId() + ")</td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Requested Fields</strong></td>" +
                            "    <td style='padding: 5px;'></td>" +
                            "  </tr>" +
                            "  <tr>" +
                            "    <td style='padding: 5px;'><strong>Property Name:</strong></td>" +
                            "    <td style='padding: 5px;'>" + propertyChange.getPropertyName() + "</td>" +
                            "  </tr>" +

                            "</table>" +
                            "<br>" +
                            "<p>To make other changes, please <a href='https://panel.allbookers.com/addressDetails?id=" + property.getId() + "' style='color: #F26436; text-decoration: none;'>click here</a>.</p>" +
                            "</div>";
            helper.setText(emailContent, true);
            sender.send(message);
        }
    }

    public void sendemailwhenAddnewProperty(User user, Property property , Agreement agreement) throws MessagingException, DocumentException    {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject(property.getName() +" Has Been Successfully Created");

        String email =
                "<div style=\"background: #dbdbdb; font-size: 1rem; padding: 12px 6px;\">" +
                        "  <table style=\"max-width: 600px; margin: auto; width: 100%;\">" +
                        "    <tbody style=\"width: 100%;\">" +
                        "      <tr>" +
                        "        <td colspan=\"1\">" +
                        "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</div>" +
                        "<div>" +
                        "  <p>Dear <strong>" + user.getFull_name() + "</strong>,</p>" +
                        "  <p>" +
                        "    We are thrilled to inform you that your property, <strong>" + property.getName() +
                        "    </strong>, has been successfully created and added to your legal entity ID " + property.getLoginId() + " account for the first time!" +
                        "  </p>" +
                        "  <p><strong>Property Details:</strong></p>" +
                        "  <ul style=\"padding-left: 20px;\">" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Name:</strong> " + property.getName() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Address:</strong> " + property.getAddress().getStreet() + ", " + property.getAddress().getCity().getFull_name() + ", " + property.getAddress().getCity().getCounty().getCountry().getCountry_name() + "</li>" +
                        "    <li style=\"margin-bottom: 5px;\"><strong>Property Type:</strong> " + property.getHotel_type().getType() + "</li>" +
                        "  </ul>" +
                        "  <p>" +
                        "    Attached you will find a copy of the contract." +
                        "  </p>" +
                        "  <p>" +
                        "    If you need any help or have questions, our support team is here for you. Feel free to contact us." +
                        "  </p>" +
                        "  <p style=\"margin-top: 20px;\">" +
                        "    Best regards,<br>" +
                        "    The Allbookers.com Team" +
                        "  </p>" +
                        "</div>" +
                        "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                        "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved." +
                        "<br>This e-mail was sent by allbookers.com.</p>";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"mainDiv\">\n" +
                "      <div class=\"column\" style=\"padding-top: 5%; padding-left: 5%; float: left; width: 100%\">\n" +
                "          <img src=\"https://www.allbookers.com/images/logoallbookers.png\" width=\"180px;\" valign=\"middle\"/>" +
                "      </div>\n" +
                "    <div style=\"text-align: left; \">\n" +
                "   <h2>Accommodation Agreement with Allbookers.com </h2>\n" +
                "       <p>This document represents a legal agreement between: </p>\n" +
                "        <p><strong>Allbookers</strong>, a platform operated by InterMedia Sh.P.K, a company registered under the laws of the Republic of Albania, with its registered address at Bulevardi Bajram Curri tek ura e ATSH, nr pasurie 6/516+1-20, Tirana, Albania, and VAT\n" +
                "            number: <strong>M02028001D</strong> (hereinafter referred to as \"The Management Platform\" or \"Management\n" +
                "            Panel\"),</p>\n" +
                "        <p><strong>And</strong></p>\n" +
                "        <p><strong>"+agreement.getLegal_bussines_name()+"</strong>, (the \"Partner\") <strong>"+agreement.getFirst_name()+' '+agreement.getLast_name()+"</strong>, \n" +
                "            with a registered address at <strong>"+agreement.getAddress()+"</strong>, and VAT number <strong>"+agreement.getNuis()+"</strong>\n" +
                "            (hereinafter referred to as \"Client (Hotelier)\" or \"Hotelier\").</p>\n" +
                "        <p>The Agreement includes the following terms:</p>\n" +
                "        <h3>Commission Rates for Registered Properties: </h3>\n" +
                "        <p>For all properties registered on Allbookers.com\n" +
                "            under the Partner's name, the following local commission rates apply:\n" +
                "            <ul>\n" +
                "                <li><strong>Albania:</strong> 10%</li>\n" +
                "            </ul>\n" +
                "        </p>\n" +
                "        <p>Allbookers.com provides a secure and reliable platform for property and booking management</p>\n" +
                "        <h3>Execution and performance </h3>\n" +
                "        <p>The Agreement is only effective after approval and confirmation by Allbookers.com.</p>\n" +
                "        <h3>General Terms of the Agreement:</h3>\n" +
                "        <p><strong>Purpose: </strong>This Agreement outlines the terms and conditions for the use of the Allbookers.com\n" +
                "            platform by the Hotelier to advertise and manage their Property, including accepting bookings,\n" +
                "            managing pricing and availability, and using the Management Panel accessed at\n" +
                "            panel.allbookers.com.</p>\n" +
                "        <p><strong>Partner Declaration: </strong>The Partner declares that this is a legitimate accommodation activity with\n" +
                "            all necessary licenses and permits, which may be provided upon request. Allbookers.com reserves\n" +
                "            the right to verify and investigate any details provided by the Partner in this registration.</p>\n" +
                "        <p><strong>Term of the Agreement: </strong>\n" +
                "            This Agreement will be valid from the date of signing and will continue\n" +
                "            to be in effect until terminated or canceled by either party in accordance with the terms of this\n" +
                "            Agreement.</p>\n" +
                "        <p><strong style=\"font-size: 18px;\">Start Date of the Agreement: </strong>"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+ "</p>  \n" +
                "<p><br></br><br></br></p>"+
                "        <h2>Table of Contents for the Accommodation Agreement</h2> \n" +
                "        <p><strong>1. Definitions</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>1.1 </strong>Explanation of terms used in the Agreement, including: \"Platform,\" \"Property,\"\n" +
                "        \"Booking,\" \"Management Panel,\" \"Client (Hotelier) \" etc,.\n" +
                "        </p>\n" +
                "        <p><strong>2. Purpose of the Agreement\n" +
                "        </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>2.1 </strong>Description of the service provided by Allbookers.com<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>2.2 </strong>Purpose of the agreement and use of the platform by the Client (Hotelier) for property\n" +
                "        and booking management.\n" +
                "        </p>\n" +
                "        <p><strong>3. Rights and Obligations of the Parties</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>3.1 </strong>Obligations of Allbookers.com<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>3.2 </strong>Obligations of the Property\n" +
                "        </p>\n" +
                "        <p><strong>4. Registration and Use of the Platform</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>4.1 </strong>Process for registering the property<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>4.2</strong> Steps to follow for using the management panel and updating property information.\n" +
                "        \n" +
                "        </p>\n" +
                "        <p><strong>5. Management of Reservations and Prices</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>5.1 </strong>Setting and managing pricing and availability by the hotelier.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>5.2 </strong>Policies for cancellations and changes in bookings.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>5.3 </strong>Payment and Commission Procedures.\n" +
                "        </p>\n" +
                "        <p><strong>6. Payments and Fees </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.1 </strong>Fee and Commission Structure of the Platform\n" +
                "        <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.2 </strong>Payment Methods and Deadlines<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.3 </strong>Monthly Reservation Invoices<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>6.4 </strong>Policies on payment delays or unpaid commissions.\n" +
                "        </p>\n" +
                "        <p><strong>7. Security and Data Protection </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>7.1 </strong>Data Protection for <strong>Client (Hotelier)</strong> and Properties.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>7.2 </strong>Security measures for personal and financial data.\n" +
                "        </p>\n" +
                "        <p><strong>8. Legal Responsibilities</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>8.1 </strong>Responsibilities for Non-Compliance with Contract Terms.<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>8.2 </strong>Limits of the Platform's Liability for Reservations or Other Client (Hotelier) -Related\n" +
                "        Issues.\n" +
                "        </p>\n" +
                "        <p><strong>9. </strong>Termination of the Agreement and Property Closure<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>9.1 </strong>Termination Conditions<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>9.2 </strong>Immediate Termination and Closure of Property. <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>9.3 </strong>Notice and Payment After Termination\n" +
                "        </p>\n" +
                "        <p><strong>10. General Provisions</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>10.1 </strong>Amendments to the Agreement<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>10.2 </strong>Transfer of rights and obligations, Change of Ownership.\n" +
                "        </p>\n" +
                "        <p><strong>11. Ranking, Guest Reviews and Marketing</strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>11.1 </strong>Ranking<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>11.2 </strong>Guest Reviews <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>11.3 </strong>Online Marketing\n" +
                "        </p>\n" +
                "        <p><strong>12. Miscellaneous </strong><br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.1 </strong>Indemnification by the Client (Hotelier) <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.2 </strong>Limitation of Liability <br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.3 </strong>No Liability for Third-Party Services<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.4 </strong>Force Majeure<br></br>\n" +
                "        &#160;&#160;&#160;&#160;<strong>12.5 </strong>Legal Actions\n" +
                "        </p> \n" +

                "        <h3>1. Definitions</h3>\n" +
                "        <p>In addition to terms defined elsewhere in this Agreement, the following definitions apply\n" +
                "            throughout the Agreement unless the contrary intention appears:</p>\n" +
                "        <p><strong>\"Accommodation\" </strong>refers to the entity (either a legal person or a natural person) that is a party to\n" +
                "            the Agreement. If different, it also means the property associated with the Agreement that the\n" +
                "            entity is contracting for, and to which these Terms apply.</p>\n" +
                "        <p><strong>\"Accommodation Information\" </strong>refers to the details supplied by and associated with the\n" +
                "            Accommodation for presentation on the Platform. This includes images, photos, and descriptions;\n" +
                "            information about amenities and services; booking availability and room details; pricing\n" +
                "            (including all applicable Taxes unless otherwise required by law, as well as any additional fees);\n" +
                "            policies on availability, cancellations, and no-shows; other relevant policies and restrictions; and\n" +
                "            any other information that Allbookers.com is legally obligated to display on the Platform.</p>\n" +
                "        <p><strong>\"Booking\": </strong>An electronic agreement made through the Allbookers.com Platform between the\n" +
                "            Client (Hotelier) and a Guest for a stay at the Client (Hotelier) 's Property.\n" +
                "            </p>\n" +
                "        <p><strong>\"Client (Hotelier) \": </strong>An individual or legal entity that registers and manages a Property on the\n" +
                "            Allbookers.com Platform and uses the Platform's services to manage bookings and promote their\n" +
                "            Property.</p>\n" +
                "        <p><strong>\"Commission\": </strong>The fee paid by the Client (Hotelier) for the services provided by\n" +
                "            Allbookers.com, including booking facilitation and other related services.\n" +
                "            </p>\n" +
                "        <p><strong>\"Connectivity Partner\": </strong>A company or individual providing services for the integration and\n" +
                "            connection of information between the Allbookers.com Platform and other systems to enhance the\n" +
                "            functionality and efficiency of booking services.</p>\n" +
                "        <p><strong>\"Connectivity Services\": </strong>Services that enable the connection and integration of information\n" +
                "            between the Allbookers.com Platform and other management systems or related services.\n" +
                "            </p>\n" +
                "        <p><strong>\"Customer Data\": </strong>Personal information and other data collected and stored by the Platform about\n" +
                "            <strong>Client (Hotelier) </strong>and Guests, including but not limited to names, addresses, contact information,\n" +
                "            and payment details.\n" +
                "            </p>\n" +
                "        <p><strong>\"Guest\": </strong>An individual who makes a Booking for a stay at the Client (Hotelier) 's Property\n" +
                "            through the Allbookers.com Platform.\n" +
                "            </p>\n" +
                "        <p><strong>\"Management Panel\": </strong>The tool provided by Allbookers.com that allows <strong>Client (Hotelier) </strong>to\n" +
                "            manage and update their Property information, including pricing, availability, bookings, and\n" +
                "            communications with Guests.</p>\n" +
                "        <p><strong>\"Platform\": </strong>Refers to Allbookers.com, an online system that enables the registration,\n" +
                "            management, and promotion of properties via the internet and provides related services for <strong>Client (Hotelier) </strong>and Guests.</p>\n" +
                "        <p><strong>\"Property\": </strong>Any accommodation unit registered on the Allbookers.com Platform, including but\n" +
                "            not limited to hotels, apartments, villas, and vacation homes, offered for booking by <strong>Client (Hotelier) </strong>on behalf of their owners.\n" +
                "            </p>\n" +
                "        <p><strong>\"Related Services\": </strong>Additional services provided by the Platform beyond the standard booking\n" +
                "            and management functionality, including but not limited to marketing services, data analytics, and\n" +
                "            technical support.</p>\n" +
                "        <p><strong>\"Room Price\": </strong>The price set by the <strong>Client (Hotelier) </strong>for an accommodation unit at their Property,\n" +
                "            as defined in the Management Panel and displayed on the Platform for Guests.</p>   \n" +
                "        <h3>2. Purpose of the Agreement </h3>   \n" +
                "        <p><strong>2.1 Description of the Service Provided by Allbookers.com </strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2.1.1 </strong>Allbookers.com provides a comprehensive online platform designed for property owners and\n" +
                "                hoteliers to manage their accommodations and bookings effectively. The platform offers a range\n" +
                "                of features including: <br></br>\n" +
                "                &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>1. Property Listing Management: </strong>Allows users to create and maintain detailed listings of\n" +
                "           their properties, including descriptions, photos, and amenities.<br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>2. Booking Management: </strong>Facilitates the handling of reservations, including accepting,\n" +
                "           modifying, and canceling them as needed.<br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>3. Pricing and Availability Control: </strong>Provides tools for setting and updating pricing and\n" +
                "           availability in real-time. <br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>4. Promotion and Visibility: </strong> Enhances property exposure through various marketing and\n" +
                "           promotional tools to attract potential guests.\n" +
                "           <br></br>\n" +
                "           &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<strong>5. Technical Support: </strong>Offers support to assist with any issues related to the use of the\n" +
                "           platform.   \n" +
                "        </p>\n" +
                "        <p><strong>2.2 Purpose of the Agreement and Use of the Platform by the Client (Hotelier)</strong> <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2.2.1 </strong>The purpose of this Agreement is to outline the terms and conditions under which the Client\n" +
                "            (Hotelier) will use the Allbookers.com platform. This includes: <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>1. Advertising and Managing the Property: </strong>The Client (Hotelier) agrees to utilize the\n" +
                "            platform for listing and managing their property, ensuring that the information is accurate\n" +
                "            and up-to-date.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2. Handling Bookings: </strong>The Client (Hotelier) will use the platform to accept, modify, and\n" +
                "            manage bookings made by guests<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3. Maintaining Pricing and Availability: </strong>The Client (Hotelier) is responsible for setting and\n" +
                "            updating the pricing and availability of their property through the platform<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>4. Compliance with Platform Policies: </strong>The Client (Hotelier) must adhere to the platform's\n" +
                "            terms, policies, and guidelines to ensure proper use and to maintain the quality and integrity\n" +
                "            of the services provided.\n" +
                "        </p>  \n" +
                "        <h3>3. Rights and Obligations of the Parties</h3>\n" +
                "        <p><strong>3.1 Obligations of Allbookers.com</strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.1 </strong>Allbookers.com will ensure uninterrupted and reliable access to its platform to enable the\n" +
                "            Client (Hotelier) to manage their property and reservations efficiently. This access includes the\n" +
                "            use of the Management Panel, panel.allbookers.com, which the Client (Hotelier) will use to update\n" +
                "            property information and manage reservations.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.2 </strong>Additionally, Allbookers.com will provide technical support. This support is dedicated to\n" +
                "            assisting the Client (Hotelier) in using the platform and resolving any issues that may arise during\n" +
                "            the operation of the platform. Technical support includes help with technical issues, system errors,\n" +
                "            and problems related to platform access.\n" +
                "            <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.3 </strong>To protect the Client (Hotelier) 's data and property information, Allbookers.com will take\n" +
                "            necessary measures for data security. This includes the use of advanced encryption technologies\n" +
                "            and other security measures to protect the Client (Hotelier) 's personal and financial information\n" +
                "            from unauthorized access, loss, or damage.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.1.4 </strong>Allbookers.com will also employ various marketing strategies to promote the Client\n" +
                "            (Hotelier) 's property on the platform and other potential networks. Promotion includes featuring\n" +
                "            the property in the platform's search listings and may include promotional campaigns and relevant\n" +
                "            advertisements.\n" +
                "            \n" +
                "        </p>\n" +
                "        <p><strong>3.2 Obligations of the Client (Hotelier) </strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.2.1 </strong>The Client (Hotelier) is responsible for the proper use of the platform in accordance with\n" +
                "            Allbookers.com's guidelines and policies. The Client (Hotelier) must ensure that their use of the\n" +
                "            platform complies with legal requirements and ethical standards. The Client (Hotelier) is\n" +
                "            responsible for any activity that occurs on their account and for assisting in resolving issues that\n" +
                "            arise during the use of the platform<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.2.2 </strong>To ensure that their property information is accurate and up-to-date, the Client (Hotelier)\n" +
                "            must keep their property information, including prices, availability, and other details, accurate and regularly updated. \n" +
                "            Any changes to the property information must be reflected on the platform immediately after they occur.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3.2.3 </strong>The Client (Hotelier) must adhere to all terms and policies of the Allbookers.com platform,\n" +
                "            including policies regarding prices, availability, reservations, and cancellations. Any violation of\n" +
                "            the platform's terms and policies may result in disciplinary measures, including suspension of\n" +
                "            access to the platform.\n" +
                "        </p>\n" +
                "        <h3>4. Registration and Use of the <span style=\"color: rgb(0, 162, 255);text-decoration: underline;\">panel.allbookers.com</span> Platform</h3>\n" +
                "        <p><strong>4.1 Process for registering the property: </strong>Property registration on the Allbookers.com platform\n" +
                "            is managed by our marketing team and involves completing all necessary information for its\n" +
                "            inclusion in the system. Our team will collect and register the complete property information,\n" +
                "            including the name, address, type of accommodation, description, and any other relevant details.\n" +
                "            After completing the registration, a request will be sent to sign the relevant Agreement if the\n" +
                "            required information is not complete.\n" +
                "            </p>\n" +
                "        <p><strong>4.2 Steps to Follow to Use the Management Panel and Update Property Information</strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>1. Log In to the Management Panel: </strong>After the successful registration of the property and\n" +
                "            signing of the Agreement, you will receive login credentials for the management panel\n" +
                "            at panel.allbookers.com. Use these credentials to access the system and start managing\n" +
                "            your property<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>2. Update Property Information: </strong>In the management panel, you will have the option to\n" +
                "            update your property information, including prices, availability, accommodation\n" +
                "            descriptions, and photos. Ensure that the information is accurate and up-to-date to\n" +
                "            provide a good experience for Client's (Hotelier).<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>3. Manage Reservations: </strong> The panel offers the ability to manage reservations, including\n" +
                "            accepting, modifying, and canceling reservations. You will be able to view and handle\n" +
                "            all reservations in one place and communicate with <strong>Client (Hotelier)</strong> regarding any\n" +
                "            important information about their stay.\n" +
                "            <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>4. Review Reports and Analytics: </strong>The panel also provides reports and analytics on the\n" +
                "            daily performance of your property. This information is useful for monitoring activity\n" +
                "            and making necessary improvements to maximize exposure and profitability of your\n" +
                "            property.\n" +
                "            \n" +
                "        </p>\n" +
                "        <h3>5. Management of Reservations and Prices</h3>\n" +
                "        <p><strong>5.1 Setting and Managing Prices and Availability by the Hotelier: </strong>The hotelier is responsible\n" +
                "            for setting and managing the prices and availability of accommodations on the Allbookers.com\n" +
                "            platform. This process includes setting prices for different periods, offering special rates or\n" +
                "            promotions, and determining availability based on their needs and preferences. The hotelier is also\n" +
                "            responsible for updating information in the management panel to reflect changes in prices and\n" +
                "            availability, ensuring that the information is always accurate and compliant with the platform's\n" +
                "            policies.</p>\n" +
                "        <p><strong>5.2 Policies for cancellations and changes in bookings: </strong>The Allbookers.com platform offers\n" +
                "            flexibility in managing cancellation and modification policies for reservations. The hotelier must\n" +
                "            define and publish policies for cancellations and modifications in accordance with the platform's\n" +
                "            rules and their preferences. These policies must be clear and understandable for Client (Hotelier)\n" +
                "            and provide detailed information about deadlines and conditions for canceling or modifying\n" +
                "            reservations. The hotelier is responsible for adhering to these policies and ensuring that all\n" +
                "            cancellation and modification requests are handled in accordance with the established rules.</p>\n" +
                "        <p><strong>5.3 Payment and Commission Procedures: </strong>Payments for reservations will be processed in\n" +
                "            accordance with the procedures and deadlines set by the Allbookers.com platform. All reservations\n" +
                "            will be processed with credit card information, and payment will be made directly upon completion\n" +
                "            of the reservation. This means that reservations are secure, and payments will always be managed\n" +
                "            by Allbookers.com. Commissions for the provided services will be automatically deducted from\n" +
                "            reservation payments and will be detailed in the management panel. The hotelier is responsible for\n" +
                "            verifying the accuracy of payments and commissions and ensuring that all transactions are\n" +
                "            completed properly and in compliance with the platform's terms <br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>5.3.1 Overbookings: </strong>Allbookers.com ensures that it manages payments through the platform and\n" +
                "            takes responsibility for any overbookings. This means that in cases of overbookings,\n" +
                "            Allbookers.com will take the necessary steps to resolve the situation and assumes responsibility\n" +
                "            for the process. In the event of overbooking, Allbookers.com will handle the issue and ensures that\n" +
                "            the guest is accommodated. If necessary, the hotel will cover the additional cost of accommodating\n" +
                "            the guest in a higher-standard property than the one originally booked.\n" +
                "        </p>\n" +
                "        <h3>6. Payments and Fees </h3>\n" +
                "        <p><strong>6.1 Fee and Commission Structure of the Platform: </strong>Allbookers.com implements a clear fee and\n" +
                "            commission structure for its services. The commission for each reservation made through the\n" +
                "            platform is 10% of the total reservation amount. This commission applies to each transaction and\n" +
                "            is included in the final payment that the hotelier will receive after the reservation is fulfilled. The\n" +
                "            fee and commission structure may be updated by the platform, and its details will always be\n" +
                "            available in the management panel.\n" +
                "            </p>\n" +
                "        <p><strong>6.2 Payment Methods and Deadlines: </strong>After completing the reservation, the guest must add their credit card details to the booking form.\n" +
                "            Allbookers.com will verify the validity of the card to ensure it is legitimate but will not process the payment.\n" +
                "            The payment for the reservation will be made directly at the property according to the hotel's terms.\n" +
                "            The card information will remain linked to the reservation for reference and security, but all financial transactions will be handled by the hotel and not by Allbookers.com.</p>\n" +
                "        <p><strong>6.3 Monthly Reservation Invoices: </strong>On the 1st of each month, Allbookers.com will compile all\n" +
                "            reservations made during the previous month and will automatically generate an invoice for these\n" +
                "            reservations in the following month. The invoice will include the 10% commission for each\n" +
                "            reservation. The invoice will be available to the hotelier and will contain all necessary information\n" +
                "            for the payment of commissions. In cases of no-shows or prepaid bookings, options will be\n" +
                "            provided to discuss and resolve issues related to reservations in accordance with the platform's\n" +
                "            policies</p>\n" +
                "        <p><strong>6.4 Policies on payment delays or unpaid commissions: </strong>In cases of late payments or unpaid\n" +
                "            commissions, Allbookers.com will follow the procedures set out to handle these situations. The\n" +
                "            hotelier is responsible for paying all unpaid commissions according to the specified deadlines. In\n" +
                "            cases of delays, the platform may apply penalties specified in the Agreement, including additional\n" +
                "            fees for late payment. In the event of ongoing delays in payments, Allbookers.com reserves the\n" +
                "            right to suspend or terminate services until all financial obligations are settled.</p>\n" +
                "        <h3>7. Security and Data Protection </h3>\n" +
                "        <p><strong>7.1 Data Protection for Client (Hotelier) and Properties: </strong>Allbookers.com is committed to\n" +
                "            protecting and securely maintaining Client (Hotelier) and property data in a reliable manner. The\n" +
                "            data we collect includes personal and financial information of Clients (Hotelier), as well as\n" +
                "            property data registered on the platform. This data is stored in compliance with applicable data\n" +
                "            protection legislation and the highest security standards. The data will be used only for the\n" +
                "            purposes for which it was collected and will not be distributed or used for other purposes without\n" +
                "            the Client's (Hotelier) consent.</p>\n" +
                "        <p><strong>7.2 Security Measures for Personal and Financial Data: </strong>Allbookers.com implements advanced\n" +
                "            security measures to protect the hoteliers' personal and financial data. These measures include the\n" +
                "            use of encryption technologies to safeguard information during transmission and storage on our\n" +
                "            servers. Additionally, the platform uses access control mechanisms and strict procedures to ensure\n" +
                "            that only authorized individuals have access to sensitive data. In the event of any risk or breach of\n" +
                "            the system, Allbookers.com will take immediate action to address and resolve security issues and\n" +
                "            will notify <strong>Client (Hotelier)</strong> and relevant authorities as required by law.\n" +
                "            </p>\n" +
                "            <h3>8. Legal Responsibilities </h3>\n" +
                "            <p><strong>8.1 Responsibilities for Non-Compliance with Agreement Terms: </strong>Each party is obligated to\n" +
                "                adhere to all terms and provisions set forth in this Agreement. In the event of non-compliance with\n" +
                "                the Agreement terms by the Client (Hotelier) , Allbookers.com has the right to take appropriate\n" +
                "                measures to rectify the breaches, including but not limited to, suspending access to the platform,\n" +
                "                suspending services, or terminating the Agreement. The Client (Hotelier) is responsible for\n" +
                "                compensating any damages caused as a result of non-compliance with the Agreement terms and\n" +
                "                for covering any expenses related to resolving issues created by their breaches.</p>\n" +
                "            <p><strong>8.2 Limits of the Platform's Liability for Reservations or Other Client (Hotelier) -Related\n" +
                "                Issues: </strong>Allbookers.com will be responsible for providing a reliable platform and fulfilling its\n" +
                "                obligations under this Agreement. Allbookers.com takes full responsibility for any instance of\n" +
                "                overbooking and will take the necessary steps to resolve such situations as outlined in the\n" +
                "                Agreement. However, the platform will not be liable for any direct or indirect damages that may\n" +
                "                result from other bookings, problems with bookings, or other issues related to clients or property\n" +
                "                data. In accordance with applicable law, Allbookers.com's liability will be limited to the amount\n" +
                "                representing the fees paid by the client for the services provided by the platform. The client\n" +
                "                acknowledges and understands that any issue related to bookings, payments, or any other problem\n" +
                "                will be addressed through the procedures outlined in this Agreement and with the assistance of the\n" +
                "                platform's technical support, according to the specified terms</p>\n" +
                "            <h3>9. Termination of the Agreement and Property Closure</h3>   \n" +
                "            <p><strong>9.1 Termination Conditions</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.1.1 Commencement and Termination of the Agreement: </strong>Unless otherwise specified, the\n" +
                "                Agreement will commence on the date of acceptance by the Property and will continue for an\n" +
                "                indefinite period.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.1.2 Termination by the Client (Hotelier): </strong>The Client (Hotelier) has the right to terminate the\n" +
                "                Agreement if Allbookers.com fails to fulfill its obligations in accordance with the terms of the\n" +
                "                Agreement. The Client (Hotelier) must provide written notice to contract@allbookers.com for\n" +
                "                termination of the Agreement and provide a remedy period to address the raised issues if necessary.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.1.3 Termination by Allbookers.com: </strong>Allbookers.com has the right to terminate the Agreement\n" +
                "                if the Client (Hotelier) breaches the terms of the Agreement, including but not limited to, noncompliance with platform policies, providing inaccurate information, or any action that damages\n" +
                "                the platform's reputation. Allbookers.com will notify the Client (Hotelier) of the breaches and offer\n" +
                "                a remedy period, except where the breach is severe and unacceptable, in which case no remedy\n" +
                "                period is required.\n" +
                "                \n" +
                "            </p>\n" +
                "            <p><strong>9.2 Immediate Termination and Property Closure</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.2.1 Immediate Termination: </strong>Either Party may terminate the Agreement (and close the Property\n" +
                "                on the Platform) or limit or suspend (all or part of the obligations, commitments, and\n" +
                "                responsibilities) under this Agreement with the other Party, with immediate effect and without the\n" +
                "                need for a notice of breach in the event of: (i) a legal or regulatory obligation; (ii) an imperative\n" +
                "                reason in accordance with applicable law; (iii) a repeated breach of the Agreement by the other\n" +
                "                Party; or (iv) a significant (actual or suspected) breach by the other Party of any term of this\n" +
                "                Agreement, instances of illegal or inappropriate content, fraud, provision of false information, or\n" +
                "                receipt of a significant number of complaints from Guests; or (v) (a submission or filing of a\n" +
                "                bankruptcy petition, or suspension of payments, or any similar action or event related to the other\n" +
                "                Party).\n" +
                "            </p>\n" +
                "            <p><strong>9.3 Notice and Payment After Termination</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>9.3.1 Notice of Termination: </strong>Any notice or communication from Allbookers.com regarding the\n" +
                "                \"closure\" (or similar suspend) of the Property on the website will imply the termination of the\n" +
                "                Agreement. After the termination or suspension of the Agreement, the Property must honor any\n" +
                "                unpaid reservations for Guests and pay all commissions (including costs, expenses, and interest if\n" +
                "                applicable) that are due for these reservations in accordance with the terms of the Agreement.\n" +
                "                Following the termination or suspension of the Agreement, and notwithstanding Allbookers.com's\n" +
                "                right to remove the Property from the Platform, Allbookers.com may retain and store the property\n" +
                "                page on the management Platform, not displaying it publicly but marking the availability as\n" +
                "                \"suspend\" pending full and final payment of any outstanding amounts (including any\n" +
                "                Commission).\n" +
                "            </p> \n" +
                "            <h3>10. General Provisions</h3>\n" +
                "            <p><strong>10.1 Amendments to the Agreement: </strong>Amendments to this Agreement can only be made with the\n" +
                "                written agreement of both parties. Any changes or additions to the Agreement will be valid only if\n" +
                "                documented and signed by authorized representatives of both parties. If a change is necessary to\n" +
                "                meet new legal or regulatory requirements, the parties will collaborate to make the necessary\n" +
                "                updates to the Agreement and will inform each other of such changes as soon as possible.\n" +
                "                </p>\n" +
                "            <p><strong>10.2 Transfer of Rights and Obligations (Change of Ownership): </strong>If the owner of a property\n" +
                "                wishes to transfer ownership to another party, they must follow the procedure outlined below to\n" +
                "                request a transfer of ownership through the platform panel.allbookers.com: <br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>1. Ownership Transfer Request: </strong>The current owner must submit a request for the transfer\n" +
                "                of ownership through the available option on panel.allbookers.com. This request should\n" +
                "                include complete information about the proposed new owner and all other relevant details\n" +
                "                related to the property<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>2. Verification: </strong>Allbookers.com will conduct the necessary verifications to confirm the\n" +
                "                identity of the new owner and to ensure that the property transfer complies with the\n" +
                "                platform's policies and procedures.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>3. Approval and New Agreement: </strong>Once verifications are completed and the request is\n" +
                "                accepted, Allbookers.com will issue a new Agreement that will include the new property\n" +
                "                details, the new owner's name, and any other relevant information. The new Agreement\n" +
                "                will replace the existing Agreement and will take effect upon being signed by both\n" +
                "                parties<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>4. Notification to the New Owner: </strong>After the approval and signing of the new Agreement,\n" +
                "                the new owner will be notified and will gain access to the management panel to continue\n" +
                "                managing the property and bookings in accordance with the new Agreement terms.\n" +
                "            </p>\n" +
                "            <h3>11. Ranking, Guest Reviews and Marketing </h3>\n" +
                "            <p><strong>11.1 Ranking: </strong>Allbookers.com aims to display search results that are relevant to each individual\n" +
                "                Guest by offering a personalized ranking of properties on the platform. This system may include\n" +
                "                criteria for ranking properties in search results and may be based on various factors such as service\n" +
                "                quality, user experience, and overall property performance. Allbookers.com reserves the right to\n" +
                "                change the ranking algorithm in accordance with the platform's needs and to ensure a satisfactory\n" +
                "                user experience</p>\n" +
                "            <p><strong>11.2 Guest Reviews: </strong>Guests who have stayed or had an experience at a property will be asked by\n" +
                "                Allbookers.com to provide feedback and a rating on specific aspects of their experience with the\n" +
                "                property. Allbookers.com may publish these reviews on the platform. Allbookers.com acts as a\n" +
                "                distributor and not a publisher of these reviews. Allbookers.com will evaluate Guest reviews in\n" +
                "                accordance with applicable policies. Allbookers.com will not be responsible for the reviews\n" +
                "                displayed or not displayed on the platform in accordance with applicable law.<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>11.2.1 </strong>Allbookers.com may, at its sole discretion, keep reviews hidden from display on the\n" +
                "                platform, remove reviews, or request a Guest to provide a revised version of a review if it contains\n" +
                "                or refers to any content that Allbookers.com deems inappropriate and/or offensive, including but\n" +
                "                not limited to: (i) politically sensitive comments; (ii) illegal activities; (iii) personal or sensitive\n" +
                "                information (e.g., emails, addresses, phone numbers, or credit card information); (iv) links to other\n" +
                "                websites; (v) inappropriate language, sexual references, hate speech, discriminatory comments,\n" +
                "                threats, insults, or references to violence. <br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>11.2.2 </strong>Property owners must not manipulate or attempt to manipulate Guest reviews (e.g., by\n" +
                "                paying for positive reviews or posting fake reviews for a competing property).<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>11.2.3 </strong>Guest reviews are for the exclusive use of Allbookers.com. Property owners are not entitled\n" +
                "                to use Guest reviews directly or indirectly in any way without prior written approval fromAllbookers.com.</p>\n" +
                "            <p><strong>11.3 Online Marketing</strong><br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>11.3.1 </strong>Allbookers.com engages in online marketing campaigns at its own cost and discretion and\n" +
                "            may promote the property using the property's name in such marketing, including email marketing.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>11.3.2 </strong>If the property owner becomes aware of any third-party platform behavior that infringes on\n" +
                "            their intellectual property rights, they must immediately notify Allbookers.com in writing with\n" +
                "            details of the behavior, and Allbookers.com will use reasonable commercial efforts to ensure that\n" +
                "            the third party takes appropriate measures to address the infringement.<br></br>\n" +
                "            &#160;&#160;&#160;&#160;<strong>11.3.3 </strong>The property owner agrees not to use, directly or indirectly, the Allbookers.com brand/logo\n" +
                "            (including the business name, trademark, service mark, or any other similar sign of identity or\n" +
                "            source) for comparative pricing purposes or for any other purpose, whether on the property's\n" +
                "            platform or on any third-party platform, system, search engine, or otherwise, unless previously\n" +
                "            approved in writing by Allbookers.com.</p>\n" +
                "            <h3>12. Miscellaneous</h3>\n" +
                "            <p><strong>12.1 Indemnification by the Client (Hotelier): </strong>The Client (Hotelier) agrees to indemnify,\n" +
                "                defend, and hold harmless Allbookers.com, its affiliates, officers, directors, employees, and agents\n" +
                "                from and against any and all claims, liabilities, indemnities, losses, costs, and expenses (including\n" +
                "                reasonable attorneys' fees) arising from or related to (i) any breach of any term of the Agreement\n" +
                "                by the Client (Hotelier) ; (ii) any claim made by Guests related to the Property; (iii) any violation\n" +
                "                of applicable laws or regulations by the Client (Hotelier) ; and (iv) any negligence or misconduct\n" +
                "                by the Client (Hotelier).<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>12.1.1 Indemnification by Allbookers.com: </strong>Allbookers.com agrees to indemnify, defend, and\n" +
                "                hold harmless the Client (Hotelier) from and against any and all claims, liabilities, indemnities,\n" +
                "                losses, costs, and expenses (including reasonable attorneys' fees) arising from or related to (i) any\n" +
                "                breach of any term of the Agreement by Allbookers.com; (ii) any violation of applicable laws or\n" +
                "                regulations by Allbookers.com; and (iii) any negligence or misconduct by Allbookers.com\n" +
                "            </p>\n" +
                "            <p><strong>12.2 Limitation of Liability: </strong>Except as otherwise provided in the Agreement, neither Party shall\n" +
                "                be liable to the other Party for any indirect, incidental, consequential, special, or punitive damages,\n" +
                "                including but not limited to, loss of profits, loss of business, or loss of data, arising from or in\n" +
                "                connection with the Agreement, regardless of the cause of action, even if such damages were\n" +
                "                foreseeable or if the Party was advised of the possibility of such damages<br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>12.2.1 Maximum Liability Limit: </strong>The maximum liability of either Party for any claim arising\n" +
                "                    from or in connection with the Agreement shall be limited to the amount of fees paid by the Client\n" +
                "                    (Hotelier) to Allbookers.com under the Agreement during the six (6) month period immediately\n" +
                "                    preceding the event giving rise to the claim\n" +
                "            </p>\n" +
                "            <p><strong>12.3 No Liability for Third-Party Services: </strong>Allbookers.com shall not be liable for any issues or\n" +
                "                damages arising from the use of third-party services or products not provided directly by\n" +
                "                Allbookers.com, including but not limited to, payment processing services, external booking\n" +
                "                platforms, or other third-party integrations</p>\n" +
                "            <p><strong>12.4 Force Majeure: </strong>Neither Party shall be liable for any failure to perform its obligations under\n" +
                "                the Agreement if such failure is due to events beyond its reasonable control, including but not\n" +
                "                limited to, natural disasters, war, terrorism, strikes, or other unforeseen events.</p>\n" +
                "            <p><strong>12.5 Legal Actions</strong><br></br>\n" +
                "                &#160;&#160;&#160;&#160;<strong>12.5.1 Dispute Resolution: </strong>Any dispute arising from or in connection with the Agreement shall\n" +
                "                be resolved in accordance with the dispute resolution process outlined in the Agreement, including\n" +
                "                any requirement for mediation or arbitration, if applicable\n" +
                "            </p></div></div>");
        String htmlContent = stringBuilder.toString();

        // Konverto HTML në PDF
        byte[] pdfData = convertHtmlToPdf(htmlContent);

        // Shtoni PDF-në si bashkëngjitje në email
        helper.setText(email, true);
        helper.addAttachment("Agreement.pdf", new ByteArrayResource(pdfData), "application/pdf");

        send(message);
        System.out.println("Mail for accept Request for agreement Send...");
    }

    public void forgotPassEmail(Long userId) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(userRepository.findById(userId).get().getEmail());
        helper.setSubject("Request to reset your password");
        String htmlContent = "<div style=\"font-family: system-ui;\">" +
                "<div style=\"background: #dbdbdb; display: flex;\">" +
                "<div style=\"margin: 15px auto; width: 248px;\">" +
                "<img src=\"https://allbookers.com/images/logoallbookers.png\" style=\"width: 100%; margin-bottom: 5px; display: block;\">" +
                "</div>" +
                "<div style=\"width: 7%; margin-top: 1%; color: black;\">" +
                "</div>" +
                "</div>" +
                "<div style=\"text-align: center; width: 75%; margin: 1% 10%;\">" +
                "<div style=\"width: 60%; margin: 0 auto; font-size: 16px; color: black; font-family: BlinkMacSystemFont,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;\">" +
                "<h3>Forgot your password?</h3>" +
                "<p>Simply click on the button below to choose a new one.</p>" +
                "<a style=\"text-align: center; font-size: 20px;\" href='http://localhost:2025/user/changePassword?token=" + tokenService.generateSecureToken(userId) + "'>" +
                "<button style=\"padding: 10px 50px; border-radius: 5px; border: 1px solid cornflowerblue; color: #417eeb; font-weight: 600; background-color: #e7effd;\" class=\"backlogin\" type=\"button\">Reset password</button>" +
                "</a>" +
                "</div>" +
                "</div>" +
                "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                "<p style=\"text-align: center;\">© Copyright 2025 Allbookers.com | All rights reserved.<br>This e-mail was sent by allbookers.com.</p>" +
                "</div>";

        helper.setText(htmlContent, true);
        sender.send(message);

    }

    public void emailConfirmation(Long userId) throws MessagingException {

        User user = userRepository.findById(userId).get();

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Email confirmation request");
        String htmlContent = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Email confirmation request | Hotels Checking</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table style=\"max-width: 600px; margin: auto; width: 100%;\">\n" +
                "    <tbody style=\"width: 100%;\">\n" +
                "    <tr>\n" +
                "        <td colspan=\"1\">\n" +
                "            <p>Dear" + user.getFull_name() + ",</p>\n" +
                "            <p>\n" +
                "                <span>\n" +
                "                    We, the Allbookers.com team welcome you as a new user of Hotel Checking Panel.\n" +
                "                </span>\n" +
                "                <br>\n" +
                "                <span>\n" +
                "                    This email is a request to confirm the email address which you registered with on our platform.\n" +
                "                    <br>\n" +
                "                    To confirm your email you simply need to click the button below.\n" +
                "                </span>\n" +
                "                <br>\n" +
                "                <span>\n" +
                "                    <button >\n" +
                "                        <a href=\"http://localhost:2025/\" role=\"button\">Confirm Email</a>\n" +
                "                    </button>\n" +
                "                    <span>\n" +
                "                        If the button doesn’t work, you can copy and paste this link into your browser:\n" +
                "                        <br>\n" +
                "                        <a href=\"#\">Copy Link</a> &nbsp;\n" +
                "                        <small>* This link expires after 24 hours</small>\n" +
                "                        <br>\n" +
                "                        By verifying your email you will be able to access the full features of Hotel Checking Panel.\n" +
                "                        <br>\n" +
                "                        If you didn’t sign up for an account, you can safely ignore this email or contact our support team <a href=\"https://allbookers.com/customerservice.en.html\" >here</a>.\n" +
                "                    </span>\n" +
                "                </span>\n" +
                "            </p>\n" +
                "            <p style=\"text-align: center;\">\n" +
                "                © Copyright 2025 Allbookers.com | All rights reserved.\n" +
                "                <br>\n" +
                "                This e-mail was sent by allbookers.com.\n" +
                "            </p>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
        helper.setText(htmlContent, true);
        sender.send(message);
    }


}
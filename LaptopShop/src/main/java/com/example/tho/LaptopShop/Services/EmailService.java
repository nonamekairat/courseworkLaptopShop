package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.EmailMessage;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.models.VerificationToken;
import com.example.tho.LaptopShop.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final VerificationTokenService verificationTokenService;
    private final TemplateEngine templateEngine;
    private final PeopleRepository peopleRepository;


    public void sendMail(EmailMessage emailMessage) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("nonamekairat@gmail.com");
        helper.setTo(emailMessage.getTo());
        helper.setSubject(emailMessage.getSubject());
        helper.setText(emailMessage.getMessage(),true);
        javaMailSender.send(message);
    }

    public void sendHtmlMail(Person person) throws MessagingException {
        VerificationToken verificationToken = verificationTokenService.findByPerson(person);
        // check if the user has a token
        if(verificationToken != null){
            String token = verificationToken.getToken();
            Context context = new Context();
            context.setVariable("title","Verify your email address");
            context.setVariable("link","http://localhost:8080/auth/activation?token="+token);
            //create an html template and pass the variables to it
            String body = templateEngine.process("auth/verification",context);

            //Send the verification email
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(person.getEmail());
            helper.setSubject("email address verification");
            helper.setText(body,true);
            javaMailSender.send(message);
        }
    }
    public void sendForgottenUsername(String recipientEmail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String username = peopleRepository.findByEmail(recipientEmail).getUsername();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("nonamekairat@gmai.com");
        helper.setTo(recipientEmail);

        String subject = "Here's your forgotten username";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your username.</p>"
                + "<p>Below you can see your username:</p>"
                + "<p> " + username +" </p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your username, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        javaMailSender.send(message);
    }

    public void sendForgottenPassword(String recipientEmail, String link) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("nonamekairat@gmai.com");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        javaMailSender.send(message);
    }
}

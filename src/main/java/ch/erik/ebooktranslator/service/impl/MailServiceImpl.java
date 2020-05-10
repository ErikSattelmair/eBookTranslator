package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Value("${mail.recipient}")
    private String to;

    @Value("${mail.recipient-bcc}")
    private String toBcc;

    @Value("${mail.subject}")
    private String subject;

    @Value("${mail.text}")
    private String text;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean sendMail(final byte[] ebbok) {
        final MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(this.to);
            helper.setBcc(this.toBcc);
            helper.setSubject(this.subject);
            helper.setText(text);
            helper.setValidateAddresses(true);
            this.mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            log.error("Error while sending email to {}. See Stacktrace: {}", to, e.getMessage());
        }

        return false;
    }

}

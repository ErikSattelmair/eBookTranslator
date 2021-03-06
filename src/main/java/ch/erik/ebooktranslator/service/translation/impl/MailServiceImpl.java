package ch.erik.ebooktranslator.service.translation.impl;

import ch.erik.ebooktranslator.service.translation.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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
    public boolean sendMail(final byte[] original, final byte[] translated) {
        final MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(this.to);
            helper.setBcc(this.toBcc);
            helper.setSubject(this.subject);
            helper.setText(text);
            helper.setValidateAddresses(true);

            helper.addAttachment("original.epub", new ByteArrayResource(IOUtils.toByteArray(new ByteArrayInputStream(original))));
            helper.addAttachment("translated.epub", new ByteArrayResource(IOUtils.toByteArray(new ByteArrayInputStream(translated))));

            this.mailSender.send(message);
            return true;
        } catch (MessagingException | IOException e) {
            log.error("Error while sending email to {}. See Stacktrace: {}", to, e.getMessage());
        }

        return false;
    }

}

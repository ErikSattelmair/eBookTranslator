package ch.erik.ebooktranslator.service.translation;

public interface MailService {

    boolean sendMail(final byte[] original, final byte[] translated);

}

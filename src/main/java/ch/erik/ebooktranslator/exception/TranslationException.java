package ch.erik.ebooktranslator.exception;

public class TranslationException extends Throwable {

    public TranslationException(String message, Exception e) {
        super(message, e);
    }
}

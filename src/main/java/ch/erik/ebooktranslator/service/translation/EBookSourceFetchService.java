package ch.erik.ebooktranslator.service.translation;

public interface EBookSourceFetchService {

    byte[] getEbookSource(final String directoryPath) throws Exception;

}

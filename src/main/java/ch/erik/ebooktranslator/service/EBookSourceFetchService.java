package ch.erik.ebooktranslator.service;

public interface EBookSourceFetchService {

    byte[] getEbookSource(final String directoryPath) throws Exception;

}

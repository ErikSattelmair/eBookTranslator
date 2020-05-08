package ch.erik.ebooktranslator.service;

import java.io.IOException;

public interface EBookSaveService {

    void saveBook(final byte[] eBook) throws IOException;

}

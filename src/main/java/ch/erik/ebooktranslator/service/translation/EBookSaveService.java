package ch.erik.ebooktranslator.service.translation;

import java.io.IOException;

public interface EBookSaveService {

    void saveBook(final byte[] eBook) throws IOException;

}

package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.service.EBookSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Slf4j
public class EBookToFileSaveService implements EBookSaveService {

    private static final String DEST_FOLDER_PATH = "/Users/erik/Downloads";

    @Override
    public void saveBook(final byte[] eBook) throws IOException {
        if (eBook == null) {
            throw new IllegalArgumentException("eBook must not be null!");
        }

        Files.write(new File(DEST_FOLDER_PATH + "translated_book_" + System.currentTimeMillis() + ".epub").toPath(), eBook);
    }
}

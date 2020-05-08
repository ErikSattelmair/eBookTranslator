package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.service.EBookSourceFetchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;

@Service
@Slf4j
public class FileEBookSourceFetchService implements EBookSourceFetchService {

    private static final String RELEVANT_FILE_EXTENSION = ".epub";

    @Override
    public byte[] getEbookSource(final String directoryPath) throws Exception {
        final GenericExtFilter filter = new GenericExtFilter(RELEVANT_FILE_EXTENSION);
        final File downloadDirectory = new File(directoryPath);

        if(!downloadDirectory.isDirectory()){
            throw new Exception("Directory does not exists :" + directoryPath);
        }

        // list out all the file name and filter by the extension
        final String[] list = downloadDirectory.list(filter);

        if (list == null || list.length != 1) {
            throw new Exception("No files end with:" + RELEVANT_FILE_EXTENSION);
        }

        final File ebookFile = new File(directoryPath + File.separator + list[0]);
        final byte[] ebbokContent = Files.readAllBytes(ebookFile.toPath());
        ebookFile.delete();

        return ebbokContent;
    }


    // inner class, generic extension filter
    public static class GenericExtFilter implements FilenameFilter {

        private final String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }
}

package ch.erik.ebooktranslator.service.translation.impl;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.model.TranslationRequestModel;
import ch.erik.ebooktranslator.service.translation.*;
import ch.erik.ebooktranslator.service.translation.impl.translationengine.DeeplTranslationEngineClient;
import ch.erik.ebooktranslator.service.translation.impl.translationengine.GoogleTranslationEngineClient;
import ch.erik.ebooktranslator.service.translation.impl.translationengine.MyMemoryTranslationEngineClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
@Slf4j
public class WorkflowEngineImpl implements WorkflowEngine {

    private static final String DOWNLOAD_FOLDER = "/Users/erik/Downloads";

    @Autowired
    private EBookLibraryClient eBookLibraryClient;

    @Autowired
    private EBookSourceFetchService eBookSourceFetchService;

    @Autowired
    private EBookSaveService eBookSaveService;

    @Autowired
    private MailService mailService;

    @Override
    public boolean startWorkflow(final TranslationRequestModel translationRequestModel) {
        log.info("Start processing...");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final String ebookFilePath = translationRequestModel.getEbookFilePath();
        final boolean useEbookOnDisk = StringUtils.isNotBlank(ebookFilePath);
        byte[] eBook;

        try {
            if (useEbookOnDisk) {
                log.debug("Use ebook file from disk at {}", ebookFilePath);

                eBook = Files.readAllBytes(new File(ebookFilePath).toPath());
            } else {
                log.info("Downloading E-Book...");
                this.eBookLibraryClient.downloadEBook();
                log.info("Downloading E-Book done");

                log.info("Fetching E-Book service...");
                eBook = this.eBookSourceFetchService.getEbookSource(DOWNLOAD_FOLDER);
            }

            log.info("E-Book fetched");

            log.info("Translating E-Book...");
            final EBookTranslator eBookTranslator = new EBookFileTranslator(getTranslationEngine(translationRequestModel));
            final boolean useProxy = translationRequestModel.isUseProxy();
            final String ebookCoverImage = translationRequestModel.getCoverImageFilePath();
            final byte[] translatedEbook = eBookTranslator.translateEBook(eBook, ebookCoverImage, useProxy);
            log.info("Translation done");

            log.info("Saving E-Book...");
            this.eBookSaveService.saveBook(translatedEbook);
            log.info("E-Book saved");

            log.info("Send E-Book via mail to Steffen and me...");
            final boolean emailSendingResult = this.mailService.sendMail(translatedEbook);
            log.info(emailSendingResult ? "E-Mail send successfully" : "E-Mail could not be sent");

            stopWatch.stop();
            log.info("Processing took " + stopWatch.getTime() + " seconds.");

            log.info("Processing done");

            return true;
        } catch (Exception | TranslationException e) {
            log.error("Error while processing workflow! Reason {}", e.getMessage());
        }

        return false;
    }

    private TranslationLibraryClient getTranslationEngine(final TranslationRequestModel translationRequestModel) {
        if (translationRequestModel.isUseDeepl()) {
            return new DeeplTranslationEngineClient();
        } else if (translationRequestModel.isUseGoogle()) {
            return new GoogleTranslationEngineClient();
        } else {
            return new MyMemoryTranslationEngineClient();
        }
    }
}

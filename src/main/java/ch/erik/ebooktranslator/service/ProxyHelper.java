package ch.erik.ebooktranslator.service;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public class ProxyHelper {

    private static final Random RANDOM = new Random();
    private static List<String> PROXY_LIST;

    static {
        try {
            PROXY_LIST = Files.readAllLines(new ClassPathResource("proxies/proxy_list.txt").getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ProxyHelper() {
    }

    public static String getRandomProxy() {
        return PROXY_LIST.get(RANDOM.nextInt(PROXY_LIST.size()));
    }

}

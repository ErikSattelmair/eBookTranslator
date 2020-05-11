package ch.erik.ebooktranslator.service.translation;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public class ProxyUtil {

    private static final Random RANDOM = new Random();

    private static List<String> HTTP_PROXY_LIST;

    private static List<String> HTTPS_PROXY_LIST;

    static {
        try {
            HTTP_PROXY_LIST = Files.readAllLines(new ClassPathResource("proxies/http_proxy_list.txt").getFile().toPath());
            HTTPS_PROXY_LIST = Files.readAllLines(new ClassPathResource("proxies/https_proxy_list.txt").getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ProxyUtil() {
    }

    public static String getRandomHttpProxy() {
        return HTTP_PROXY_LIST.get(RANDOM.nextInt(HTTP_PROXY_LIST.size()));
    }

    public static String getRandomHttpsProxy() {
        return HTTPS_PROXY_LIST.get(RANDOM.nextInt(HTTPS_PROXY_LIST.size()));
    }

}

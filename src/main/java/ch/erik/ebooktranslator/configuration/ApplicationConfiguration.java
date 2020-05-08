package ch.erik.ebooktranslator.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ch.erik.ebooktranslator.service.*")
public class ApplicationConfiguration {
}

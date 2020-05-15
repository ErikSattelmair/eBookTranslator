package ch.erik.ebooktranslator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @NotNull
    private Locale id;

    private String text;

}

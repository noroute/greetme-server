package poc.openshift.greetme.server.logging;

import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.logging.logback.ColorConverter;

public class AnsiEnabledColorConverter extends ColorConverter {

    public AnsiEnabledColorConverter() {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
    }
}

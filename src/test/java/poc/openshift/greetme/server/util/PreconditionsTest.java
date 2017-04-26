package poc.openshift.greetme.server.util;

import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.fail;

public class PreconditionsTest {

    @Test
    public void checkNotNull_returns_reference_when_reference_is_not_null() throws Exception {
        // given
        Object nonNullReference = new Object();

        // when
        Object returnedReference = Preconditions.checkNotNull(nonNullReference, "nonNullReference");

        // then
        assertThat(returnedReference).isSameAs(nonNullReference);
    }

    @Test
    public void checkNotNull_throws_PreconditionNotFulfilledException_when_reference_is_null() throws Exception {
        // given
        Object nullReference = null;

        // when
        try {
            Preconditions.checkNotNull(nullReference, "nullReference");
        }
        // then
        catch (PreconditionNotFulfilledException e) {
            assertThat(e.getMessage()).isEqualTo("nullReference may not be null");
            return;
        }
        fail("expected PreconditionNotFulfilledException with message \"nullReference may not be null\"");
    }

    @Test
    public void checkNotEmpty_returns_string_when_string_is_not_null() throws Exception {
        // given
        String nonNullString = "valid string";

        // when
        String returnedReference = Preconditions.checkNotNull(nonNullString, "nonNullString");

        // then
        assertThat(returnedReference).isSameAs(nonNullString);
    }

    @Test
    public void checkNotEmpty_throws_PreconditionNotFulfilledException_when_string_is_null() throws Exception {
        // given
        String nullString = null;

        // when
        try {
            Preconditions.checkNotEmpty(nullString, "nullString");
        }
        // then
        catch (PreconditionNotFulfilledException e) {
            assertThat(e.getMessage()).isEqualTo("nullString may not be null");
            return;
        }
        fail("expected PreconditionNotFulfilledException with message \"nullString may not be null\"");
    }

    @Test
    public void checkNotEmpty_throws_PreconditionNotFulfilledException_when_string_is_empty() throws Exception {
        // given
        String emptyString = "";

        // when
        try {
            Preconditions.checkNotEmpty(emptyString, "emptyString");
        }
        // then
        catch (PreconditionNotFulfilledException e) {
            assertThat(e.getMessage()).isEqualTo("emptyString may not be empty");
            return;
        }
        fail("expected PreconditionNotFulfilledException with message \"emptyString may not be empty\"");
    }

    @Test
    public void checkNotEmpty_throws_PreconditionNotFulfilledException_when_string_is_empty_after_whitespace_has_been_trimmed() throws Exception {
        // given
        String stringWithSpaces = "  ";

        // when
        try {
            Preconditions.checkNotEmpty(stringWithSpaces, "stringWithSpaces");
        }
        // then
        catch (PreconditionNotFulfilledException e) {
            assertThat(e.getMessage()).isEqualTo("stringWithSpaces may not be empty");
            return;
        }
        fail("expected PreconditionNotFulfilledException with message \"stringWithSpaces may not be empty\"");
    }

    @Test
    public void checkLanguage_returns_language_code_when_it_is_valid() throws Exception {
        // given
        String validLanguageCode = Locale.ENGLISH.getLanguage();

        // when
        String returnedReference = Preconditions.checkLanguage(validLanguageCode, "validLanguageCode");

        // then
        assertThat(returnedReference).isSameAs(validLanguageCode);
    }

    @Test
    public void checkLanguage_throws_PreconditionNotFulfilledException_when_language_code_is_empty() throws Exception {
        // given
        String emptyLanguageCode = "";

        // when
        try {
            Preconditions.checkLanguage(emptyLanguageCode, "emptyLanguageCode");
        }
        // then
        catch (PreconditionNotFulfilledException e) {
            assertThat(e.getMessage()).isEqualTo("emptyLanguageCode may not be empty");
            return;
        }
        fail("expected PreconditionNotFulfilledException with message \"emptyLanguageCode may not be empty\"");
    }

    @Test
    public void checkLanguage_throws_PreconditionNotFulfilledException_when_language_code_is_invalid() throws Exception {
        // given
        String invalidLanguageCode = "12";

        // when
        try {
            Preconditions.checkLanguage(invalidLanguageCode, "invalidLanguageCode");
        }
        // then
        catch (PreconditionNotFulfilledException e) {
            assertThat(e.getMessage()).isEqualTo("invalidLanguageCode 12 must be one returned by Locale.getISOLanguages()");
            return;
        }
        fail("expected PreconditionNotFulfilledException with message \"invalidLanguageCode 12 must be one returned by Locale.getISOLanguages()\"");
    }
}
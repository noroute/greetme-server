package poc.openshift.greetme.server.util;

import org.junit.Test;

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
    public void checkNotNull_throws_NullPointerException_when_reference_is_null() throws Exception {
        // given
        Object nullReference = null;

        // when
        try {
            Preconditions.checkNotNull(nullReference, "nullReference");
        }
        // then
        catch (NullPointerException e) {
            assertThat(e.getMessage()).isEqualTo("nullReference may not be null");
            return;
        }
        fail("expected NullPointerException with message \"nullReference may not be null\"");
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
    public void checkNotEmpty_throws_NullPointerException_when_string_is_null() throws Exception {
        // given
        String nullString = null;

        // when
        try {
            Preconditions.checkNotEmpty(nullString, "nullString");
        }
        // then
        catch (NullPointerException e) {
            assertThat(e.getMessage()).isEqualTo("nullString may not be null");
            return;
        }
        fail("expected NullPointerException with message \"nullString may not be null\"");
    }

    @Test
    public void checkNotEmpty_throws_IllegalArgumentException_when_string_is_empty() throws Exception {
        // given
        String emptyString = "";

        // when
        try {
            Preconditions.checkNotEmpty(emptyString, "emptyString");
        }
        // then
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("emptyString may not be empty");
            return;
        }
        fail("expected IllegalArgumentException with message \"emptyString may not be empty\"");
    }

    @Test
    public void checkNotEmpty_throws_IllegalArgumentException_when_string_is_empty_after_whitespace_has_been_trimmed() throws Exception {
        // given
        String stringWithSpaces = "  ";

        // when
        try {
            Preconditions.checkNotEmpty(stringWithSpaces, "stringWithSpaces");
        }
        // then
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("stringWithSpaces may not be empty");
            return;
        }
        fail("expected IllegalArgumentException with message \"stringWithSpaces may not be empty\"");
    }
}
package poc.openshift.greetme.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import poc.openshift.greetme.server.util.Preconditions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Preconditions.class})
public class GoogleTranslateClientTest {

    @Mock
    private RestTemplate clientMock;

    @InjectMocks
    private GoogleTranslateClient googleTranslateClient = new GoogleTranslateClient();

    @Test
    public void checks_preconditions() throws Exception {
        // given
        mockStatic(Preconditions.class);
        configureRestTemplateStub();

        // when
        googleTranslateClient.translate("someText", "someSourceLanguage", "someTargetLanguage");

        // then
        verifyStatic();
        Preconditions.checkNotEmpty(eq("someText"), anyString());

        // and
        verifyStatic();
        Preconditions.checkLanguage(eq("someSourceLanguage"), anyString());

        // and
        verifyStatic();
        Preconditions.checkLanguage(eq("someTargetLanguage"), anyString());
    }

    private void configureRestTemplateStub() {
        @SuppressWarnings("unchecked")
        ResponseEntity<List> responseEntityMock = mock(ResponseEntity.class);
        when(responseEntityMock.getBody()).thenReturn(Arrays.asList(Arrays.asList(Arrays.asList("someTranslatedText"))));
        when(clientMock.exchange(anyObject(), eq(List.class))).thenReturn(responseEntityMock);
    }
}
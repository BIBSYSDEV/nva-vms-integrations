package no.sikt.nva.vms.kaltura;

import static no.unit.nva.testutils.RandomDataGenerator.randomInteger;
import static no.unit.nva.testutils.RandomDataGenerator.randomString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.kaltura.client.types.BaseEntry;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KalturaClientTest {

    private KalturaClient client;

    private KalturaMock mock;

    @AfterEach
    public void tearDown() {
        mock.shutDown();
    }

    @BeforeEach
    void init() {
        mock = new KalturaMock();
        client = new KalturaClient(mock.getClientServiceUrl().toString(), randomString(), randomInteger(),
                                   randomString(), randomInteger(), randomInteger());
    }

    @Test
    void shouldReturnEntryGivenEntryId() throws Exception {
        var entryId = mock.createMediaEntryAndEntryId();
        var expectedResult = mock.getMediaEntry();
        var actualResult = client.getMediaEntry(entryId);

        assertThat(actualResult.getId(), is(equalTo(expectedResult.getId())));
        assertThat(actualResult.getDownloadUrl(), is(equalTo(expectedResult.getDownloadUrl())));
        assertThat(actualResult.getDataUrl(), is(equalTo(expectedResult.getDataUrl())));
        assertThat(actualResult.getUserId(), is(equalTo(expectedResult.getUserId())));
        assertThat(actualResult.getThumbnailUrl(), is(equalTo(expectedResult.getThumbnailUrl())));
    }

    @Test
    void shouldThrowExceptionIfEntryIsNotValid() {
        var entryId = mock.createMediaEntryAndEntryIdWhichNotExist();
        assertThrows(Exception.class, () -> client.getMediaEntry(entryId));
    }

    @Test
    void shouldReturnUserEntriesFromKaltura() throws Exception {
        var expectedEntries = mock.createClientWithEntries();
        var clientUserId = expectedEntries.get(0).getCreatorId();
        var actualEntries = client.getMediaEntries(clientUserId, 10, 0);

        var expectedPresentationsIdList = expectedEntries.stream()
                                              .map(FakeMediaEntry::getId)
                                              .collect(Collectors.toList());
        var actualPresentationsIdList = actualEntries.stream()
                                            .map(BaseEntry::getId)
                                            .collect(Collectors.toList());

        var expectedPresentationsDownloadUrlList = expectedEntries.stream()
                                                       .map(FakeMediaEntry::getDownloadUrl)
                                                       .collect(Collectors.toList());
        var actualPresentationsDownloadUrlList = actualEntries.stream()
                                                     .map(BaseEntry::getDownloadUrl)
                                                     .collect(Collectors.toList());

        assertThat(actualEntries.size(), is(equalTo(expectedEntries.size())));
        assertThat(actualPresentationsIdList, is(equalTo(expectedPresentationsIdList)));
        assertThat(actualPresentationsDownloadUrlList, is(equalTo(expectedPresentationsDownloadUrlList)));
    }

    @Test
    void shouldThrowExceptionWhenFetchingEntriesWithInvalidAdminSecretInKalturaClient() {
        var entryId = mock.createClientWithInvalidAdminSecret();
        assertThrows(Exception.class, () -> client.getMediaEntry(entryId));
    }
}

package no.sikt.nva.vms.kaltura;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static no.unit.nva.testutils.RandomDataGenerator.randomBoolean;
import static no.unit.nva.testutils.RandomDataGenerator.randomInteger;
import static no.unit.nva.testutils.RandomDataGenerator.randomString;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import no.sikt.nva.vms.kaltura.FakeMediaEntry.OperationAttribute;
import no.unit.nva.stubs.WiremockHttpClient;

public class KalturaMock {

    public static final String KALTURA_GET_SINGLE_MEDIA_ENTRY_PATH = "/api_v3/service/media/action/get";
    public static final String KALTURA_GET_ENTRIES_PATH = "/api_v3/service/media/action/list";
    public static final String LIST_RESPONSE_TYPE = "KalturaMediaListResponse";
    private final Map<String, FakeMediaEntry> singleEntryMap;
    private String singleEntryId;
    private WireMockServer server;
    private HttpClient httpClient;
    private URI clientServiceUrl;

    public KalturaMock() {
        singleEntryMap = new ConcurrentHashMap<>();
        this.initialize();
    }

    public void shutDown() {
        server.stop();
    }

    public URI getClientServiceUrl() {
        return clientServiceUrl;
    }

    public List<FakeMediaEntry> createClientWithEntries() {
        var entriesToUser = createRandomMediaEntryList();
        addResponseForFetchingEntries(
            String.valueOf(new KalturaEntriesResponse(entriesToUser, entriesToUser.size(), LIST_RESPONSE_TYPE)));
        return entriesToUser;
    }

    public String createMediaEntryAndEntryId() {
        singleEntryId = randomString();
        singleEntryMap.put(singleEntryId, createRandomMediaEntry());
        addResponseForFetchingSingleMediaEntry(getMediaEntry().toString());
        return singleEntryId;
    }

    public String createMediaEntryAndEntryIdWhichNotExist() {
        singleEntryId = randomString();
        singleEntryMap.put(singleEntryId, createRandomMediaEntry());
        addResponseForFetchingSingleMediaEntry(createKalturaApiExceptionResponseBody().toString());
        return singleEntryId;
    }

    public String createClientWithInvalidAdminSecret() {
        singleEntryId = randomString();
        addResponseForFetchingSingleMediaEntry(createKalturaApiExceptionResponseBody().toString());
        return singleEntryId;
    }

    public FakeMediaEntry getMediaEntry() {
        return singleEntryMap.get(singleEntryId);
    }

    private void initialize() {
        server = new WireMockServer(options().dynamicPort());
        server.start();
        clientServiceUrl = URI.create(server.baseUrl());
        httpClient = WiremockHttpClient.create();
    }

    private FakeMediaEntry createRandomMediaEntry() {
        return new FakeMediaEntry(randomInteger(), randomString(), randomString(), randomString(), randomBoolean(),
                                  randomString(), randomString(), randomString(), randomInteger(), randomInteger(),
                                  randomString(), randomString(), randomInteger(), randomString(),
                                  randomInteger(), randomString(), randomString(), randomString(), randomInteger(),
                                  randomString(), randomInteger(), randomInteger(), randomInteger(), randomString(),
                                  randomString(), createOperationAttributeList(), randomString(), randomString(),
                                  randomInteger(), randomInteger(), randomInteger(), randomString(), randomString(),
                                  randomString(), randomString(), randomString(), randomString(), randomString(),
                                  randomInteger(), randomString(), randomString(), randomString(), randomString(),
                                  randomInteger(), randomString(), randomInteger(), randomString(), randomInteger(),
                                  randomInteger(), randomInteger(), randomString(), randomInteger(), randomInteger(),
                                  randomInteger(), randomInteger(), randomInteger(), randomInteger(), randomString(),
                                  randomString(), randomString(), randomString(), randomString(), randomInteger(),
                                  randomInteger(), randomInteger(), randomString(), randomInteger(), randomString(),
                                  randomString(), new ArrayList<>(), randomString(), randomString());
    }

    private List<FakeMediaEntry> createRandomMediaEntryList() {
        var minMediaEntries = 1;
        var maxMediaEntries = 10;
        return IntStream.range(0, randomInteger(maxMediaEntries) + minMediaEntries)
                   .boxed()
                   .map(item -> createRandomMediaEntry())
                   .collect(Collectors.toList());
    }

    private ArrayList<OperationAttribute> createOperationAttributeList() {
        return new ArrayList<>();
    }

    private KalturaIdNotFoundResponse createKalturaApiExceptionResponseBody() {
        return new KalturaIdNotFoundResponse(randomString(), randomString(), randomString(),
                                             Collections.singletonMap(randomString(), randomString()));
    }

    private void addResponseForFetchingSingleMediaEntry(String responseBody) {
        server.stubFor(post(urlPathEqualTo(KALTURA_GET_SINGLE_MEDIA_ENTRY_PATH))
                           .withHeader("Accept-Charset", equalTo("utf-8,ISO-8859-1;q=0.7,*;q=0.5"))
                           .withHeader("Accept", equalTo("application/json"))
                           .willReturn(ok().withBody(responseBody)));
    }

    private void addResponseForFetchingEntries(String responseBody) {
        server.stubFor(post(urlPathEqualTo(KALTURA_GET_ENTRIES_PATH))
                           .withHeader("Accept-Charset", equalTo("utf-8,ISO-8859-1;q=0.7,*;q=0.5"))
                           .withHeader("Accept", equalTo("application/json"))
                           .willReturn(ok().withBody(responseBody)));
    }
}



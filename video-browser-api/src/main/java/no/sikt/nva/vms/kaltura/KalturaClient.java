package no.sikt.nva.vms.kaltura;

import com.kaltura.client.APIOkRequestsExecutor;
import com.kaltura.client.Client;
import com.kaltura.client.Configuration;
import com.kaltura.client.enums.SessionType;
import com.kaltura.client.services.MediaService;
import com.kaltura.client.types.FilterPager;
import com.kaltura.client.types.MediaEntry;
import com.kaltura.client.types.MediaEntryFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KalturaClient {

    public static final String PRIVILEGES = "disableentitlement";
    public static final String FETCH_ENTRY_ERROR_LOGG_MESSAGE = "Could not fetch entry from kaltura {}";
    public static final String RESOURCE_NOT_FOUND = "Resource with provided ID is not found";
    private static final Logger logger = LoggerFactory.getLogger(KalturaClient.class);
    private final String adminSecret;
    private final Integer adminId;

    private final String userId;
    private final Configuration config;

    public KalturaClient(String serviceUrl, String adminSecret, Integer adminId, String userId,
                         Integer connectTimout, Integer readTimeout) {

        this.adminSecret = adminSecret;
        this.adminId = adminId;
        this.userId = userId;
        this.config = new Configuration();
        config.setEndpoint(serviceUrl);
        config.setReadTimeout(readTimeout);
        config.setConnectTimeout(connectTimout);
    }

    public List<MediaEntry> getMediaEntries(String user, int limit, int offset) throws Exception {
        MediaEntryFilter filter = new MediaEntryFilter();
        filter.setCreatorIdEqual(user);
        FilterPager pager = new FilterPager();
        pager.setPageSize(limit);
        pager.setPageIndex(offset);
        final CountDownLatch doneSignal = new CountDownLatch(1);

        List<MediaEntry> mediaEntries = new ArrayList<>();

        MediaService.ListMediaBuilder requestBuilder = MediaService.list(filter, pager);
        requestBuilder.setCompletion(response -> {
            mediaEntries.addAll(response.results.getObjects());
            doneSignal.countDown();
        });
        Client client = getClient(SessionType.ADMIN);
        APIOkRequestsExecutor.getExecutor().queue(requestBuilder.build(client));
        doneSignal.await();
        return mediaEntries;
    }

    public MediaEntry getMediaEntry(String entryId) throws Exception {
        List<MediaEntry> fetchedEntries = new ArrayList<>();
        MediaService.GetMediaBuilder requestBuilder = MediaService.get(entryId);

        final CountDownLatch doneSignal = new CountDownLatch(1);

        requestBuilder.setCompletion(response -> {
            if (response.isSuccess()) {
                fetchedEntries.add(response.results);
            } else {
                logger.error(FETCH_ENTRY_ERROR_LOGG_MESSAGE, response.error);
            }
            doneSignal.countDown();
        });
        Client client = getClient(SessionType.ADMIN);
        APIOkRequestsExecutor.getExecutor().queue(requestBuilder.build(client));
        doneSignal.await();
        if (fetchedEntries.isEmpty()) {
            throw new Exception(RESOURCE_NOT_FOUND);
        } else {
            return fetchedEntries.get(0);
        }
    }

    /**
     * @param sessionType ADMIN or USER
     */
    public Client getClient(SessionType sessionType) throws Exception {
        Client client = new Client(config);
        var sessionId = client.generateSessionV2(adminSecret, userId, sessionType, adminId, 86_400, PRIVILEGES);
        client.setSessionId(sessionId);
        return client;
    }
}

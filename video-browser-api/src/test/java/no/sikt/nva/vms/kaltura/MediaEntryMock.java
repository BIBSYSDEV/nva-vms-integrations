package no.sikt.nva.vms.kaltura;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Objects;
import no.unit.nva.commons.json.JsonSerializable;

public class MediaEntryMock implements JsonSerializable {

    public int accessControlId;
    public String adminTags;
    public String application;
    public String applicationVersion;
    public boolean blockAutoTranscript;
    public String capabilities;
    public String categories;
    public String categoriesIds;
    public int conversionProfileId;
    public int createdAt;
    public String creatorId;
    public String description;
    public int displayInSearch;
    public String downloadUrl;
    public int endDate;
    public String entitledUsersEdit;
    public String entitledUsersPublish;
    public String entitledUsersView;
    public int groupId;
    public String id;
    public int licenseType;
    public int moderationCount;
    public int moderationStatus;
    public String name;
    public String objectType;
    public ArrayList<OperationAttribute> operationAttributes;
    public String parentEntryId;
    public String partnerData;
    public int partnerId;
    public int partnerSortValue;
    public int rank;
    public String redirectEntryId;
    public String referenceId;
    public String replacedEntryId;
    public String replacementStatus;
    public String replacingEntryId;
    public String rootEntryId;
    public String searchText;
    public int startDate;
    public String status;
    public String tags;
    public String templateEntryId;
    public String thumbnailUrl;
    public int totalRank;
    public String type;
    public int updatedAt;
    public String userId;
    public int version;
    public int votes;
    public int duration;
    public String durationType;
    public int height;
    public int lastPlayedAt;
    public int msDuration;
    public int plays;
    public int views;
    public int width;
    public String conversionQuality;
    public String creditUrl;
    public String creditUserName;
    public String dataUrl;
    public String flavorParamsIds;
    public int isTrimDisabled;
    public int mediaDate;
    public int mediaType;
    public String searchProviderId;
    public int searchProviderType;
    public String sourceType;
    public String sourceVersion;
    public ArrayList<?> streams;
    public String assetParamsIds;
    public String externalSourceType;

    @JsonCreator
    public MediaEntryMock(int accessControlId, String adminTags, String application, String applicationVersion,
                          boolean blockAutoTranscript, String capabilities, String categories, String categoriesIds,
                          int conversionProfileId, int createdAt, String creatorId, String description,
                          int displayInSearch, String downloadUrl, int endDate, String entitledUsersEdit,
                          String entitledUsersPublish, String entitledUsersView, int groupId, String id,
                          int licenseType, int moderationCount, int moderationStatus, String name, String objectType,
                          ArrayList<OperationAttribute> operationAttributes, String parentEntryId, String partnerData,
                          int partnerId, int partnerSortValue, int rank, String redirectEntryId, String referenceId,
                          String replacedEntryId, String replacementStatus, String replacingEntryId, String rootEntryId,
                          String searchText, int startDate, String status, String tags, String templateEntryId,
                          String thumbnailUrl, int totalRank, String type, int updatedAt, String userId, int version,
                          int votes, int duration, String durationType, int height, int lastPlayedAt, int msDuration,
                          int plays, int views, int width, String conversionQuality, String creditUrl,
                          String creditUserName, String dataUrl, String flavorParamsIds, int isTrimDisabled,
                          int mediaDate, int mediaType, String searchProviderId, int searchProviderType,
                          String sourceType, String sourceVersion, ArrayList<?> streams, String assetParamsIds,
                          String externalSourceType) {
        this.accessControlId = accessControlId;
        this.adminTags = adminTags;
        this.application = application;
        this.applicationVersion = applicationVersion;
        this.blockAutoTranscript = blockAutoTranscript;
        this.capabilities = capabilities;
        this.categories = categories;
        this.categoriesIds = categoriesIds;
        this.conversionProfileId = conversionProfileId;
        this.createdAt = createdAt;
        this.creatorId = creatorId;
        this.description = description;
        this.displayInSearch = displayInSearch;
        this.downloadUrl = downloadUrl;
        this.endDate = endDate;
        this.entitledUsersEdit = entitledUsersEdit;
        this.entitledUsersPublish = entitledUsersPublish;
        this.entitledUsersView = entitledUsersView;
        this.groupId = groupId;
        this.id = id;
        this.licenseType = licenseType;
        this.moderationCount = moderationCount;
        this.moderationStatus = moderationStatus;
        this.name = name;
        this.objectType = objectType;
        this.operationAttributes = operationAttributes;
        this.parentEntryId = parentEntryId;
        this.partnerData = partnerData;
        this.partnerId = partnerId;
        this.partnerSortValue = partnerSortValue;
        this.rank = rank;
        this.redirectEntryId = redirectEntryId;
        this.referenceId = referenceId;
        this.replacedEntryId = replacedEntryId;
        this.replacementStatus = replacementStatus;
        this.replacingEntryId = replacingEntryId;
        this.rootEntryId = rootEntryId;
        this.searchText = searchText;
        this.startDate = startDate;
        this.status = status;
        this.tags = tags;
        this.templateEntryId = templateEntryId;
        this.thumbnailUrl = thumbnailUrl;
        this.totalRank = totalRank;
        this.type = type;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.version = version;
        this.votes = votes;
        this.duration = duration;
        this.durationType = durationType;
        this.height = height;
        this.lastPlayedAt = lastPlayedAt;
        this.msDuration = msDuration;
        this.plays = plays;
        this.views = views;
        this.width = width;
        this.conversionQuality = conversionQuality;
        this.creditUrl = creditUrl;
        this.creditUserName = creditUserName;
        this.dataUrl = dataUrl;
        this.flavorParamsIds = flavorParamsIds;
        this.isTrimDisabled = isTrimDisabled;
        this.mediaDate = mediaDate;
        this.mediaType = mediaType;
        this.searchProviderId = searchProviderId;
        this.searchProviderType = searchProviderType;
        this.sourceType = sourceType;
        this.sourceVersion = sourceVersion;
        this.streams = streams;
        this.assetParamsIds = assetParamsIds;
        this.externalSourceType = externalSourceType;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessControlId, adminTags, application, applicationVersion, blockAutoTranscript,
                            capabilities, categories, categoriesIds, conversionProfileId, createdAt, creatorId,
                            description, displayInSearch, downloadUrl, endDate, entitledUsersEdit, entitledUsersPublish,
                            entitledUsersView, groupId, id, licenseType, moderationCount, moderationStatus, name,
                            objectType, operationAttributes, parentEntryId, partnerData, partnerId, partnerSortValue,
                            rank, redirectEntryId, referenceId, replacedEntryId, replacementStatus, replacingEntryId,
                            rootEntryId, searchText, startDate, status, tags, templateEntryId, thumbnailUrl, totalRank,
                            type, updatedAt, userId, version, votes, duration, durationType, height, lastPlayedAt,
                            msDuration, plays, views, width, conversionQuality, creditUrl, creditUserName, dataUrl,
                            flavorParamsIds, isTrimDisabled, mediaDate, mediaType, searchProviderId, searchProviderType,
                            sourceType, sourceVersion, streams, assetParamsIds, externalSourceType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaEntryMock that = (MediaEntryMock) o;
        return accessControlId == that.accessControlId
               && blockAutoTranscript == that.blockAutoTranscript
               && conversionProfileId == that.conversionProfileId
               && createdAt == that.createdAt
               && displayInSearch == that.displayInSearch
               && endDate == that.endDate
               && groupId == that.groupId
               && licenseType == that.licenseType
               && moderationCount == that.moderationCount
               && moderationStatus == that.moderationStatus
               && partnerId == that.partnerId
               && partnerSortValue == that.partnerSortValue
               && rank == that.rank
               && startDate == that.startDate
               && totalRank == that.totalRank
               && updatedAt == that.updatedAt
               && version == that.version
               && votes == that.votes
               && duration == that.duration
               && height == that.height
               && lastPlayedAt == that.lastPlayedAt
               && msDuration == that.msDuration
               && plays == that.plays
               && views == that.views
               && width == that.width
               && isTrimDisabled == that.isTrimDisabled
               && mediaDate == that.mediaDate
               && mediaType == that.mediaType
               && searchProviderType == that.searchProviderType
               && Objects.equals(adminTags, that.adminTags)
               && Objects.equals(application, that.application)
               && Objects.equals(applicationVersion, that.applicationVersion)
               && Objects.equals(capabilities, that.capabilities)
               && Objects.equals(categories, that.categories)
               && Objects.equals(categoriesIds, that.categoriesIds)
               && Objects.equals(creatorId, that.creatorId)
               && Objects.equals(description, that.description)
               && Objects.equals(downloadUrl, that.downloadUrl)
               && Objects.equals(entitledUsersEdit, that.entitledUsersEdit)
               && Objects.equals(entitledUsersPublish, that.entitledUsersPublish)
               && Objects.equals(entitledUsersView, that.entitledUsersView)
               && Objects.equals(id, that.id)
               && Objects.equals(name, that.name)
               && Objects.equals(objectType, that.objectType)
               && Objects.equals(operationAttributes, that.operationAttributes)
               && Objects.equals(parentEntryId, that.parentEntryId)
               && Objects.equals(partnerData, that.partnerData)
               && Objects.equals(redirectEntryId, that.redirectEntryId)
               && Objects.equals(referenceId, that.referenceId)
               && Objects.equals(replacedEntryId, that.replacedEntryId)
               && Objects.equals(replacementStatus, that.replacementStatus)
               && Objects.equals(replacingEntryId, that.replacingEntryId)
               && Objects.equals(rootEntryId, that.rootEntryId)
               && Objects.equals(searchText, that.searchText)
               && Objects.equals(status, that.status)
               && Objects.equals(tags, that.tags)
               && Objects.equals(templateEntryId, that.templateEntryId)
               && Objects.equals(thumbnailUrl, that.thumbnailUrl)
               && Objects.equals(type, that.type)
               && Objects.equals(userId, that.userId)
               && Objects.equals(durationType, that.durationType)
               && Objects.equals(conversionQuality, that.conversionQuality)
               && Objects.equals(creditUrl, that.creditUrl)
               && Objects.equals(creditUserName, that.creditUserName)
               && Objects.equals(dataUrl, that.dataUrl)
               && Objects.equals(flavorParamsIds, that.flavorParamsIds)
               && Objects.equals(searchProviderId, that.searchProviderId)
               && Objects.equals(sourceType, that.sourceType)
               && Objects.equals(sourceVersion, that.sourceVersion)
               && Objects.equals(streams, that.streams)
               && Objects.equals(assetParamsIds, that.assetParamsIds)
               && Objects.equals(externalSourceType, that.externalSourceType);
    }

    public String toString() {
        return toJsonString();
    }

    public class OperationAttribute {

        public String objectType;

        public OperationAttribute(String objectType) {
            this.objectType = objectType;
        }
    }
}



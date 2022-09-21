# nva-vms-service
This repository contains components for DLR integration into VMS, as Kaltura and Panopto.

For now VMS service is used to fetch resources from Kaltura only.

Other VMS integrations should be implemented in this service.

## Required configuration
The following configuration must be made in the Secrets Manager in the AWS account
where it will be deployed:
Key: ```kalturaClientConfigs```


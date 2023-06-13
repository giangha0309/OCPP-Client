package eu.chargetime.ocpp.jsonclientimplementation.ocppClient;

import eu.chargetime.ocpp.feature.profile.ClientCoreEventHandler;
import eu.chargetime.ocpp.model.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class ClientCoreEventHandlerConfig {

    @Bean
    public ClientCoreEventHandler configTestClient() {
        return new ClientCoreEventHandler() {
            @Override
            public ChangeAvailabilityConfirmation handleChangeAvailabilityRequest(ChangeAvailabilityRequest request) {
                System.out.println(request);
                if(request.getConnectorId() == 1 && request.getType() == AvailabilityType.valueOf("Operative")){
                    return new ChangeAvailabilityConfirmation(AvailabilityStatus.Accepted);
                }
                else if(request.getConnectorId() == 1 && request.getType() == AvailabilityType.valueOf("Inoperative")){
                    return new ChangeAvailabilityConfirmation(AvailabilityStatus.Rejected);
                }
                else{
                    return new ChangeAvailabilityConfirmation(AvailabilityStatus.Scheduled);
                }
            }

            @Override
            public GetConfigurationConfirmation handleGetConfigurationRequest(GetConfigurationRequest request) {

                System.out.println(request);
                GetConfigurationConfirmation getConfigurationConfirmation = new GetConfigurationConfirmation();
                KeyValueType[] keyValueTypes = new KeyValueType[request.getKey().length];
                int i = 0;
                for(String key : request.getKey()){
                    KeyValueType keyValueType = new KeyValueType();
                    keyValueType.setKey(key);
                    keyValueType.setValue("value");
                    keyValueType.setReadonly(true);
                    keyValueTypes[i] = keyValueType;
                    i ++;
                }
                getConfigurationConfirmation.setConfigurationKey(keyValueTypes);
                if(request.getKey().length > 0){
                    return getConfigurationConfirmation;
                }
                return null;
            }

            @Override
            public ChangeConfigurationConfirmation handleChangeConfigurationRequest(ChangeConfigurationRequest request) {

                System.out.println(request);
                if(request.getKey().isBlank()){
                    return new ChangeConfigurationConfirmation(ConfigurationStatus.Rejected);
                }
                else if(request.getValue().isBlank()){
                    return new ChangeConfigurationConfirmation(ConfigurationStatus.Rejected);
                }
                else if(request.validate()){
                    return new ChangeConfigurationConfirmation(ConfigurationStatus.Accepted);
                }
                else if(request.transactionRelated()){
                    return new ChangeConfigurationConfirmation(ConfigurationStatus.NotSupported);
                }
                else{
                    return new ChangeConfigurationConfirmation(ConfigurationStatus.RebootRequired);
                }
            }

            @Override
            public ClearCacheConfirmation handleClearCacheRequest(ClearCacheRequest request) {

                System.out.println(request);
                if(request.validate()){
                    return new ClearCacheConfirmation(ClearCacheStatus.Accepted);
                }
                else{
                    return new ClearCacheConfirmation(ClearCacheStatus.Rejected);
                }
            }

            @Override
            public DataTransferConfirmation handleDataTransferRequest(DataTransferRequest request) {

                System.out.println(request);
                DataTransferConfirmation dataTransferConfirmation = new DataTransferConfirmation();
                dataTransferConfirmation.setData(request.getData());
                if(request.getVendorId().equals("1")){
                    dataTransferConfirmation.setStatus(DataTransferStatus.Accepted);
                }
                else{
                    dataTransferConfirmation.setStatus(DataTransferStatus.Rejected);
                }
                return dataTransferConfirmation;
            }

            @Override
            public RemoteStartTransactionConfirmation handleRemoteStartTransactionRequest(RemoteStartTransactionRequest request) {

                System.out.println(request);
                if(request.getIdTag().equals("1")){
                    return new RemoteStartTransactionConfirmation(RemoteStartStopStatus.Accepted);
                }
                else{
                    return new RemoteStartTransactionConfirmation(RemoteStartStopStatus.Rejected);
                }
            }

            @Override
            public RemoteStopTransactionConfirmation handleRemoteStopTransactionRequest(RemoteStopTransactionRequest request) {

                System.out.println(request);
                if(request.getTransactionId() > 3){
                    return new RemoteStopTransactionConfirmation(RemoteStartStopStatus.Rejected);
                }
                else{
                    return new RemoteStopTransactionConfirmation(RemoteStartStopStatus.Accepted);
                }
            }

            @Override
            public ResetConfirmation handleResetRequest(ResetRequest request) {

                System.out.println(request);
                if(request.getType() == ResetType.Hard){
                    return new ResetConfirmation(ResetStatus.Accepted);
                }
                else{
                    return new ResetConfirmation(ResetStatus.Rejected);
                }
            }

            @Override
            public UnlockConnectorConfirmation handleUnlockConnectorRequest(UnlockConnectorRequest request) {

                System.out.println(request);
                if(request.getConnectorId() < 0){
                    return new UnlockConnectorConfirmation(UnlockStatus.UnlockFailed);
                }
                else if(request.getConnectorId() > 10){
                    return new UnlockConnectorConfirmation(UnlockStatus.NotSupported);
                }
                else{
                    return new UnlockConnectorConfirmation(UnlockStatus.Unlocked);
                }
            }
        };
    }
}

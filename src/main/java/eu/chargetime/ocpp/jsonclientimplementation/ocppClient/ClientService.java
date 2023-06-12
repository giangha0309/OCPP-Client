package eu.chargetime.ocpp.jsonclientimplementation.ocppClient;

import eu.chargetime.ocpp.JSONClient;
import eu.chargetime.ocpp.OccurenceConstraintException;
import eu.chargetime.ocpp.UnsupportedFeatureException;
import eu.chargetime.ocpp.feature.profile.ClientCoreProfile;
import eu.chargetime.ocpp.jsonclientimplementation.config.ApiConfigurations;
import eu.chargetime.ocpp.model.core.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
@RestController
@Slf4j
@RequestMapping(value="/Client")
@Import({JsonClientConfig.class, ClientCoreProfileConfig.class, ClientCoreEventHandlerConfig.class,
        ApiConfigurations.class})
public class ClientService {
    @Autowired
    private JSONClient jsonClient;

    @Autowired
    private ClientCoreProfile clientCoreProfile;

    @Autowired
    private ApiConfigurations apiConfigurations;


    @GetMapping(path = "/Authorize")
    public ResponseEntity<Object> Authorize(
            @RequestParam("IdTag") String IdTag
    ) throws Exception {
        Map<String, Object> resInf = new HashMap<>();
        JSONObject res;
        String url = "ws://" + apiConfigurations.getWebSocketBaseUrl()+"/"+apiConfigurations.getChargeBoxId();

        AuthorizeRequest testRequest = clientCoreProfile.createAuthorizeRequest(IdTag);
        jsonClient.connect(url, null);
        try {
            AuthorizeConfirmation authorizeConfirmation = (AuthorizeConfirmation) jsonClient.send(testRequest)
                    .toCompletableFuture().get();
            if(authorizeConfirmation.getIdTagInfo().getStatus() == AuthorizationStatus.Accepted){
                resInf.put("Message","Authorize Success");
            }else{
                resInf.put("Message","Authorize Fail");
            }
        } catch (OccurenceConstraintException | UnsupportedFeatureException
                | ExecutionException | InterruptedException e) {
            log.error("Exception occurred: " + e);
            log.error("Test will fail");
        }
        log.info("==============================");
//        resInf.put("Message","Authorize Success");
        res = new JSONObject(resInf);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping(path = "/StartTransaction")
    public ResponseEntity<Object> StartTransaction(
            @RequestParam(name = "connectorId") int connectorId,
            @RequestParam(name = "idTag") String IdTag,
            @RequestParam(name = "meterStart") int meterStart,
            @RequestParam(name = "reservationId", required = false) Integer reservationId
    )throws Exception {
        Map<String, Object> resInf = new HashMap<>();
        JSONObject res;
        String url = "ws://" + apiConfigurations.getWebSocketBaseUrl()+"/"+apiConfigurations.getChargeBoxId();
        ZonedDateTime timestamp = ZonedDateTime.now();

        StartTransactionRequest request = clientCoreProfile.createStartTransactionRequest(connectorId, IdTag, meterStart, timestamp);
        request.setReservationId(reservationId);
        jsonClient.connect(url, null);
        try {
            StartTransactionConfirmation confirmation = (StartTransactionConfirmation) jsonClient.send(request)
                    .toCompletableFuture().get();
            resInf.put("info",confirmation.getIdTagInfo().toString());
        } catch (OccurenceConstraintException | UnsupportedFeatureException
                | ExecutionException | InterruptedException e) {
            log.error("Exception occurred: " + e);
            log.error("Test will fail");
        }
        log.info("==============================");
//        resInf.put("Message","Authorize Success");
        res = new JSONObject(resInf);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/StopTransaction")
    public ResponseEntity<Object> StopTransaction(
            @RequestParam(name = "stopValue") Integer stopValue,
            @RequestParam(name = "transactionId") Integer transactionId,
            @RequestParam(name = "idTag", required = false, defaultValue = "") String idTag,
            @RequestParam(name = "reason") Reason reason,
            @RequestParam(name = "transactionData", required = false) MeterValue[] transactionData

    ) throws Exception {
        Map<String, Object> resInf = new HashMap<>();
        JSONObject res;
        String url = "ws://" + apiConfigurations.getWebSocketBaseUrl()+"/"+apiConfigurations.getChargeBoxId();
        ZonedDateTime timestamp = ZonedDateTime.now();

        StopTransactionRequest request = clientCoreProfile.createStopTransactionRequest(stopValue, timestamp, transactionId);
        request.setReason(reason);
        request.setIdTag(idTag);
        request.setTransactionData(transactionData);
        jsonClient.connect(url, null);
        try {
            StopTransactionConfirmation confirmation = (StopTransactionConfirmation) jsonClient.send(request)
                    .toCompletableFuture().get();
        } catch (OccurenceConstraintException | UnsupportedFeatureException
                | ExecutionException | InterruptedException e) {
            log.error("Exception occurred: " + e);
            log.error("Test will fail");
        }
        log.info("==============================");
        resInf.put("Message","Stop Transaction Success");
        res = new JSONObject(resInf);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/StatusNotification")
    public ResponseEntity<Object> StatusNotification(
            @RequestParam(name = "connectorId") Integer connectorId,
            @RequestParam(name = "errorCode") ChargePointErrorCode errorCode,
            @RequestParam(name = "info", required = false, defaultValue = "") String info,
            @RequestParam(name = "status") ChargePointStatus status,
            @RequestParam(name = "vendorId", required = false, defaultValue = "") String vendorId,
            @RequestParam(name = "vendorErrorCode", required = false, defaultValue = "") String vendorErrorCode
    ) throws Exception {
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> resInf = new HashMap<>();
        JSONObject res;
        String url = "ws://" + apiConfigurations.getWebSocketBaseUrl() + "/" + apiConfigurations.getChargeBoxId();


        StatusNotificationRequest request = clientCoreProfile.createStatusNotificationRequest(connectorId, errorCode, status);
        request.setInfo(info);
        request.setVendorId(vendorId);
        request.setVendorErrorCode(vendorErrorCode);
        jsonClient.connect(url, null);
        try {
            StatusNotificationConfirmation confirmation = (StatusNotificationConfirmation) jsonClient.send(request)
                    .toCompletableFuture().get();
            resInf.put("Message", confirmation.toString());
        } catch (OccurenceConstraintException | UnsupportedFeatureException
                | ExecutionException | InterruptedException e) {
            log.error("Exception occurred: " + e);
            log.error("Test will fail");
        }
        log.info("==============================");
        res = new JSONObject(resInf);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/HeartBeat")
    public ResponseEntity<Object> HeartBeat()
            throws Exception {
        Map<String, Object> resInf = new HashMap<>();
        JSONObject res;
        String url = "ws://" + apiConfigurations.getWebSocketBaseUrl() + "/" + apiConfigurations.getChargeBoxId();

        HeartbeatRequest request = clientCoreProfile.createHeartbeatRequest();
        jsonClient.connect(url, null);
        try {
            HeartbeatConfirmation confirmation = (HeartbeatConfirmation) jsonClient.send(request)
                    .toCompletableFuture().get();
            resInf.put("Message", confirmation.getCurrentTime());
        } catch (OccurenceConstraintException | UnsupportedFeatureException
                | ExecutionException | InterruptedException e) {
            log.error("Exception occurred: " + e);
            log.error("Test will fail");
        }
        log.info("==============================");
        res = new JSONObject(resInf);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/BootNotification")
    public ResponseEntity<Object> BootNotification(
            @RequestParam(name= "chargeBoxSerialNumber", required = false, defaultValue = "") String chargeBoxSerialNumber,
            @RequestParam(name= "chargePointModel") String chargePointModel,
            @RequestParam(name= "chargePointSerialNumber", required = false, defaultValue = "") String chargePointSerialNumber,
            @RequestParam(name= "chargePointVendor") String chargePointVendor,
            @RequestParam(name= "firmwareVersion", required = false, defaultValue = "") String firmwareVersion,
            @RequestParam(name= "iccid", required = false, defaultValue = "") String iccid,
            @RequestParam(name= "imsi", required = false, defaultValue = "") String imsi,
            @RequestParam(name= "meterSerialNumber", required = false, defaultValue = "") String meterSerialNumber,
            @RequestParam(name= "meterType", required = false, defaultValue = "") String meterType
    )
            throws Exception {
        Map<String, Object> resInf = new HashMap<>();
        JSONObject res;
        String url = "ws://" + apiConfigurations.getWebSocketBaseUrl() + "/" + apiConfigurations.getChargeBoxId();

        BootNotificationRequest request = clientCoreProfile.createBootNotificationRequest(chargePointVendor, chargePointModel);
        request.setImsi(imsi);
        request.setIccid(iccid);
        request.setFirmwareVersion(firmwareVersion);
        request.setMeterType(meterType);
        request.setChargePointSerialNumber(chargePointSerialNumber);
        request.setMeterSerialNumber(meterSerialNumber);
        request.setChargePointVendor(chargePointVendor);
        request.setChargePointModel(chargePointModel);
        request.setChargeBoxSerialNumber(chargeBoxSerialNumber);
        jsonClient.connect(url, null);
        try {
            BootNotificationConfirmation confirmation = (BootNotificationConfirmation) jsonClient.send(request)
                    .toCompletableFuture().get();
            resInf.put("Status", confirmation.getStatus());
        } catch (OccurenceConstraintException | UnsupportedFeatureException
                | ExecutionException | InterruptedException e) {
            log.error("Exception occurred: " + e);
            log.error("Test will fail");
        }
        log.info("==============================");
        res = new JSONObject(resInf);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/MeterValues")
    public ResponseEntity<Object> MeterValues(
            @RequestParam(name = "connectorId") Integer connectorId,
            @RequestParam(name = "transactionId", required = false) Integer transactionId,
            @RequestParam(name = "meterValue") MeterValue[] meterValue
    )
            throws Exception {
        Map<String, Object> resInf = new HashMap<>();
        JSONObject res;
        String url = "ws://" + apiConfigurations.getWebSocketBaseUrl() + "/" + apiConfigurations.getChargeBoxId();


        MeterValuesRequest request = clientCoreProfile.createMeterValuesRequest(connectorId, meterValue);
        request.setTransactionId(transactionId);
        jsonClient.connect(url, null);
        try {
            MeterValuesConfirmation confirmation = (MeterValuesConfirmation) jsonClient.send(request)
                    .toCompletableFuture().get();
            resInf.put("Message", confirmation.toString());
        } catch (OccurenceConstraintException | UnsupportedFeatureException
                | ExecutionException | InterruptedException e) {
            log.error("Exception occurred: " + e);
            log.error("Test will fail");
        }
        log.info("==============================");
        res = new JSONObject(resInf);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping(path = "/DataTransfer")
    public ResponseEntity<Object> DataTransfer(
            @RequestParam("vendorId") String vendorId,
            @RequestParam(name = "messageID", required = false) String messageID,
            @RequestParam(name = "data", required = false) String data
    ) throws Exception {
        Map<String, Object> resInf = new HashMap<>();
        JSONObject res;
        String url = "ws://" + apiConfigurations.getWebSocketBaseUrl() + "/" + apiConfigurations.getChargeBoxId();

        DataTransferRequest request = clientCoreProfile.createDataTransferRequest(vendorId);
        if(messageID != null && data != null){
            request.setMessageId(messageID);
            request.setData(data);
        }
        jsonClient.connect(url, null);
        try {
            DataTransferConfirmation confirmation = (DataTransferConfirmation) jsonClient.send(request)
                    .toCompletableFuture().get();
            resInf.put("Message", confirmation.toString());
        } catch (OccurenceConstraintException | UnsupportedFeatureException
                | ExecutionException | InterruptedException e) {
            log.error("Exception occurred: " + e);
            log.error("Test will fail");
        }
        log.info("==============================");
        res = new JSONObject(resInf);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

package eu.chargetime.ocpp.jsonclientimplementation.ocppClient;

import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInputProvider;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogOutputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProvider;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutputProvider;
import com.pi4j.plugin.mock.provider.i2c.MockI2CProvider;
import com.pi4j.plugin.mock.provider.pwm.MockPwmProvider;
import com.pi4j.plugin.mock.provider.serial.MockSerialProvider;
import com.pi4j.plugin.mock.provider.spi.MockSpiProvider;
import eu.chargetime.ocpp.JSONClient;
import eu.chargetime.ocpp.feature.profile.ClientCoreEventHandler;
import eu.chargetime.ocpp.model.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


@Slf4j
public class ClientCoreEventHandlerConfig {

    private GpioController gpio;

    private GpioPinDigitalOutput relay;

//    Context pi4j = Pi4J.newContextBuilder()
//            .add(new MockPlatform())
//            .add(MockAnalogInputProvider.newInstance(),
//                    MockAnalogOutputProvider.newInstance(),
//                    MockSpiProvider.newInstance(),
//                    MockPwmProvider.newInstance(),
//                    MockSerialProvider.newInstance(),
//                    MockI2CProvider.newInstance(),
//                    MockDigitalInputProvider.newInstance(),
//                    MockDigitalOutputProvider.newInstance())
////            .add(new MyCustomADCProvider(/* implements AnalogInputProvider, id="my-adc-prov" */))
////            .add(new MyCustomSPIProvider(/* implements SpiProvider, id="my-spi-prov" */))
//            .build();


//    public ClientCoreEventHandlerConfig() {
//
//        // Create a GPIO controller instance
//        gpio = GpioFactory.getInstance();
//
//        // Specify the GPIO pin number for relay control
//        relay = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Relay", PinState.LOW);
//    }

    @Bean
    public ClientCoreEventHandler configTestClient() {
        return new ClientCoreEventHandler() {
            @Override
            public ChangeAvailabilityConfirmation handleChangeAvailabilityRequest(ChangeAvailabilityRequest request) {
                System.out.println(request);
                // ... handle event

//                // Code to interact with the real charger point and change the availability
//                if (request.getType() == AvailabilityType.Inoperative) {
//                    relay.low(); // Deactivate the relay
//                } else {
//                    relay.high(); // Activate the relay
//                }

                return new ChangeAvailabilityConfirmation(AvailabilityStatus.Accepted);
            }

            @Override
            public GetConfigurationConfirmation handleGetConfigurationRequest(GetConfigurationRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public ChangeConfigurationConfirmation handleChangeConfigurationRequest(ChangeConfigurationRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public ClearCacheConfirmation handleClearCacheRequest(ClearCacheRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public DataTransferConfirmation handleDataTransferRequest(DataTransferRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public RemoteStartTransactionConfirmation handleRemoteStartTransactionRequest(RemoteStartTransactionRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public RemoteStopTransactionConfirmation handleRemoteStopTransactionRequest(RemoteStopTransactionRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public ResetConfirmation handleResetRequest(ResetRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public UnlockConnectorConfirmation handleUnlockConnectorRequest(UnlockConnectorRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }
        };
    }
}

package com.example.demo.config;

import com.example.demo.service.MqttService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
public class MqttConfig {

    @Value("${mqtt.broker}")
    private String brokerUrl;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.topic.events}")
    private String eventsTopic;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);


        factory.setConnectionOptions(options);
        return factory;
    }

//    @Bean(name = "mqttInputChannel")
//    public MessageChannel mqttInputChannel() {
//        return new DirectChannel();
//    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow mqttInboundFlow(MqttPahoClientFactory factory, MqttService mqttService) {
        return IntegrationFlow.from(new MqttPahoMessageDrivenChannelAdapter(
                        clientId + "_in", factory, eventsTopic))
                .handle((payload, headers) -> {
                    System.out.println("Flow primio poruku na topicu: " + headers.get("mqtt_receivedTopic"));
                    mqttService.receive(payload);
                    return null;
                })
                .get();
    }


//    @Bean
//    public IntegrationFlow mqttInboundFlow(MqttPahoClientFactory factory, MqttService mqttService) {
//        return IntegrationFlow.from(new MqttPahoMessageDrivenChannelAdapter(
//                        clientId + "_in", factory, eventsTopic))
//                .handle(mqttService, "receive")
//                .get();
//    }

//    @Bean
//    public MqttPahoMessageDrivenChannelAdapter inbound() {
//        MqttPahoMessageDrivenChannelAdapter adapter =
//                new MqttPahoMessageDrivenChannelAdapter(
//                        clientId + "_in",
//                        mqttClientFactory(),
//                        eventsTopic
//                );
//
//        adapter.setOutputChannel(mqttInputChannel());
//        return adapter;
//    }

//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannel")
//    public MessageHandler handler(MqttService mqttService) {
//        return message -> {
//            mqttService.receive(message);
//        };
//    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler handler =
                new MqttPahoMessageHandler(clientId + "_out", mqttClientFactory());

        handler.setAsync(true);
        handler.setDefaultTopic("interfon/kapija/komande");

        return handler;
    }
}
package com.example.demo.service;

import com.example.demo.dto.IntercomEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private final MessageChannel mqttOutboundChannel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MqttService(MessageChannel mqttOutboundChannel) {
        this.mqttOutboundChannel = mqttOutboundChannel;
    }

    public void receive(Object payload) {
        try {
            System.out.println("--- NOVI EVENT STIGAO ---");
            System.out.println("Sirovi payload: " + payload);

            String stringPayload;
            if (payload instanceof byte[]) {
                stringPayload = new String((byte[]) payload);
            } else {
                stringPayload = payload.toString();
            }

            IntercomEventDto dto = objectMapper.readValue(stringPayload, IntercomEventDto.class);

            System.out.println("Stan: " + dto.getStan());
            System.out.println("Slika: " + dto.getImage_url());
            System.out.println("Vreme: " + dto.getTimestamp());
            System.out.println("-------------------------");

        } catch (Exception e) {
            System.err.println("Greška prilikom obrade MQTT poruke!");
            e.printStackTrace();
        }
    }

    public void sendOpenCommand() {
        String payload = "{\"type\":\"OPEN\"}";

        Message<String> message = MessageBuilder
                .withPayload(payload)
                .setHeader(MqttHeaders.TOPIC, "interfon/kapija/komande")
                .build();

        mqttOutboundChannel.send(message);
    }
}
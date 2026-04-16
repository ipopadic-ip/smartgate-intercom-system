package com.example.demo.controller;

import com.example.demo.service.MqttService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GateController {

    private final MqttService mqttService;

    public GateController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @PostMapping("/open")
    public ResponseEntity<String> openGate() {
        mqttService.sendOpenCommand();
        return ResponseEntity.ok("Kapija komanda poslata");
    }
}
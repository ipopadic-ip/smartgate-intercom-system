package com.example.demo.controller;

import com.example.demo.service.MqttService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GateController {

    private final MqttService mqttService;

    public GateController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @PostMapping("/open")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> openGate(Authentication auth) {

        String username = auth.getName();

        mqttService.sendOpenCommand(username);

        return ResponseEntity.ok("Kapija otvorena od: " + username);
    }
}
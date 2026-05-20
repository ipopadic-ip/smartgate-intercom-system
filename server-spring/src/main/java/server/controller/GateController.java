package server.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.service.MqttService;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class GateController {

    private final MqttService mqttService;

    public GateController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @PostMapping("/open")
    @PreAuthorize("hasAnyRole('TENANT', 'ROLE_ADMIN')")
    public ResponseEntity<String> openGate(Authentication auth) {
    
    	String username = auth.getName();

        mqttService.sendOpenCommand();

        return ResponseEntity.ok("Kapija otvorena od: " + username);
    }
}
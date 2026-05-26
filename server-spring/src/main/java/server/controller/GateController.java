package server.controller;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.dto.ActionLogDTO;
import server.dto.UserDTO;
import server.model.User;
import server.service.ActionLogService;
import server.service.MqttService;
import server.service.UserService;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class GateController {

    private final MqttService mqttService;
    @Autowired
    private ActionLogService actionLogService;
    @Autowired
    private UserService userService;

    public GateController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @PostMapping("/open")
    @PreAuthorize("hasAnyRole('TENANT', 'ADMIN')")
    public ResponseEntity<String> openGate(Authentication auth) {

        User user = userService.findByUsername(auth.getName());

        mqttService.sendOpenCommand();

        ActionLogDTO log = new ActionLogDTO();
        log.setTimestamp(LocalDateTime.now());
        log.setUser(new UserDTO(user.getId(), null, null, null, null, null));
        log.setActive(true);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            log.setAction("ADMIN OPEN GATE");
            log.setDoorNumber(null);
        } else {
            log.setAction("TENANT OPEN GATE");

            if (user.getApartment() != null) {
                log.setDoorNumber(user.getApartment().getDoorNumber());
            }
        }

        actionLogService.save(log);

        return ResponseEntity.ok("Kapija otvorena od: " + user.getUsername());
    }
}
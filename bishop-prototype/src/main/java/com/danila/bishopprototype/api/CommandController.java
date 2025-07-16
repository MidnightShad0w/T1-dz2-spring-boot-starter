package com.danila.bishopprototype.api;

import com.danila.bishopprototype.api.dto.NewCommandDto;
import com.danila.synthetichumancorestarter.application.CommandService;
import com.danila.synthetichumancorestarter.infrastructure.audit.WeylandWatchingYou;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/commands")
public class CommandController {
    private final CommandService service;

    @Autowired
    public CommandController(CommandService service) {
        this.service = service;
    }

    @PostMapping
    @WeylandWatchingYou
    public ResponseEntity<Void> create(@RequestBody NewCommandDto dto) {
        service.accept(dto.toCommand());
        return ResponseEntity.accepted().build();
    }
}

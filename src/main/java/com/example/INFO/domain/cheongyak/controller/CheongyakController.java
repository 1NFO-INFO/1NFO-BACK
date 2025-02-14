package com.example.INFO.domain.cheongyak.controller;

import com.example.INFO.domain.cheongyak.dto.response.CheongyakDetailsResponse;
import com.example.INFO.domain.cheongyak.service.CheongyakService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cheongyak")
@RequiredArgsConstructor
public class CheongyakController {

    private final CheongyakService cheongyakService;

    @GetMapping
    public ResponseEntity<Page<CheongyakDetailsResponse>> list(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(cheongyakService.list(pageable).map(CheongyakDetailsResponse::fromDto));
    }
}

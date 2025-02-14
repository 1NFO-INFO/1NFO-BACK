package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.repository.CheongyakDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheongyakService {

    private final CheongyakDetailsRepository cheongyakDetailsRepository;

    public Page<CheongyakDetailsDto> list(Pageable pageable) {
        return cheongyakDetailsRepository.findAll(pageable).map(CheongyakDetailsDto::fromEntity);
    }
}

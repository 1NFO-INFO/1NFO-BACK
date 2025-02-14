package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.model.entity.CheongyakDetailsEntity;
import com.example.INFO.domain.cheongyak.repository.CheongyakDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheongyakService {

    private final CheongyakDetailsRepository cheongyakDetailsRepository;

    public Page<CheongyakDetailsDto> list(Pageable pageable, List<String> housingTypes) {
        pageable = addDefaultSorting(pageable);

        Specification<CheongyakDetailsEntity> spec =
                Specification.where(housingTypeIn(housingTypes));

        return cheongyakDetailsRepository.findAll(spec, pageable).map(CheongyakDetailsDto::fromEntity);
    }

    private Pageable addDefaultSorting(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Order.desc("recruitmentNoticeDate")));
        }
        return pageable;
    }

    private static Specification<CheongyakDetailsEntity> housingTypeIn(List<String> housingTypes) {
        return (root, query, criteriaBuilder) -> {
            if (housingTypes == null || housingTypes.isEmpty() || housingTypes.contains("전체")) {
                return criteriaBuilder.conjunction();
            }
            return root.get("housingType").in(housingTypes);
        };
    }
}

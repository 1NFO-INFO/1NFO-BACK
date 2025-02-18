package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.model.constant.CheongyakStatus;
import com.example.INFO.domain.cheongyak.model.entity.CheongyakDetailsEntity;
import com.example.INFO.domain.cheongyak.repository.CheongyakDetailsRepository;
import com.example.INFO.global.exception.NotFoundException;
import com.example.INFO.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheongyakService {

    private final CheongyakDetailsRepository cheongyakDetailsRepository;

    public Page<CheongyakDetailsDto> list(Pageable pageable, List<String> housingTypes, CheongyakStatus status) {
        pageable = addDefaultSorting(pageable);

        Specification<CheongyakDetailsEntity> spec =
                Specification.where(housingTypeIn(housingTypes))
                        .and(status != null ? statusFilter(status) : null);

        return cheongyakDetailsRepository.findAll(spec, pageable).map(CheongyakDetailsDto::fromEntity);
    }

    public CheongyakDetailsDto details(Long id) {
        return cheongyakDetailsRepository.findById(id)
                .map(CheongyakDetailsDto::fromEntity)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "청약 상세 정보를 찾을 수 없습니다."));
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
    private static Specification<CheongyakDetailsEntity> statusFilter(CheongyakStatus status) {
        return (root, query, criteriaBuilder) -> {
            LocalDate currentDate = LocalDate.now();

            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            return switch (status) {
                case ANNOUNCED -> criteriaBuilder.greaterThan(root.get("applicationStartDate"), currentDate);
                case ONGOING -> criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("applicationStartDate"), currentDate),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("applicationEndDate"), currentDate)
                );
                case CLOSED -> criteriaBuilder.lessThan(root.get("applicationEndDate"), currentDate);
            };
        };
    }
}

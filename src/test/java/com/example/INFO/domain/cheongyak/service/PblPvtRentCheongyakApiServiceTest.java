package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.global.config.RestTemplateConfig;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {PblPvtRentCheongyakApiService.class, RestTemplateConfig.class})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Disabled("공공 데이터 API 호출 테스트 비활성화 (실제 API 호출)")
class PblPvtRentCheongyakApiServiceTest {

    private final PblPvtRentCheongyakApiService pblPvtRentCheongyakApiService;

    @Test
    @DisplayName("api 요청 성공 테스트 - 예외 발생하지 않음")
    void getAllData_DoesNotThrowException() {
        assertDoesNotThrow(pblPvtRentCheongyakApiService::getAllData);
    }

    @Test
    @DisplayName("api 요청 성공 테스트 - null이 아닌 데이터 반환")
    void getAllData_ReturnNotNull() {
        List<CheongyakDetailsDto> allData = pblPvtRentCheongyakApiService.getAllData();

        assertNotNull(allData);
    }
}

package com.example.INFO.domain.ticket.service;

import com.example.INFO.domain.ticket.domain.TicketData;
import com.example.INFO.domain.ticket.domain.TicketProperties;
import com.example.INFO.domain.ticket.domain.repository.TicketDataRepository;
import com.example.INFO.domain.ticket.dto.res.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketDataRepository repository;
    private final TicketProperties ticketProperties;

    public void fetchAndSaveAllData() {
        int pageNo = 1;
        int numOfRows = 100; // numOfRows를 100으로 고정

        while (true) {
            try {
                // API 호출 URL
                String url = String.format("%s?serviceKey=%s&PageNo=%d&numOfrows=%d",
                        ticketProperties.getBaseUrl(), ticketProperties.getApikey(), pageNo, numOfRows);

                // RestTemplate으로 API 호출
                RestTemplate restTemplate = new RestTemplate();
                String response = restTemplate.getForObject(new URI(url), String.class);

                // 응답 디코딩 (UTF-8 강제 적용)
                String decodedResponse = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                // XML 데이터 파싱
                List<TicketData> dataList = parseXmlData(decodedResponse);

                // 더 이상 데이터가 없으면 종료
                if (dataList.isEmpty()) {
                    break;
                }

                // 데이터 저장
                saveOrUpdateData(dataList);
                pageNo++;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    // XML 데이터 파싱
    private List<TicketData> parseXmlData(String xml) {
        List<TicketData> dataList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // UTF-8로 InputStream 강제 설정
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            Document doc = builder.parse(inputStream);

            NodeList itemList = doc.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);

                TicketData data = TicketData.builder()
                        .seq(getTagValue("seq", item))
                        .title(getTagValue("title", item))
                        .discountRate(getTagValue("discountRate", item))
                        .price(getTagValue("price", item))
                        .startDate(getTagValue("startDate", item))
                        .endDate(getTagValue("endDate", item))
                        .place(getTagValue("place", item))
                        .area(getTagValue("area", item))
                        .img(getTagValue("img", item))
                        .imgDesc(getTagValue("imgDesc", item))
                        .build();

                dataList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    // 데이터 저장 또는 업데이트
    private void saveOrUpdateData(List<TicketData> dataList) {
        for (TicketData data : dataList) {
            if (repository.existsById(data.getSeq())) {
                TicketData existingData = repository.findById(data.getSeq()).orElse(null);
                if (existingData != null && !existingData.equals(data)) {
                    repository.save(data); // 변경된 데이터만 업데이트
                }
            } else {
                repository.save(data); // 새로운 데이터 추가
            }
        }
    }

    // XML 태그 값 가져오기
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            try {
                String rawValue = nodeList.item(0).getTextContent();
                rawValue = StringEscapeUtils.unescapeHtml4(rawValue); // HTML 엔티티 디코딩
                return URLDecoder.decode(rawValue, StandardCharsets.UTF_8.name()); // URL 디코딩
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private Integer parseDiscountRate(String discountRate) {
        try {
            return (discountRate == null || discountRate.isEmpty()) ? -1 : Integer.parseInt(discountRate);
        } catch (Exception e) {
            return -1; // 빈 값이나 파싱 실패 시 -1로 설정
        }
    }
    private LocalDate parseDate(String date) {
        try {
            return (date == null || date.isEmpty()) ? LocalDate.MIN : LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return null; // 오류 발생 시 null 반환
        }
    }
    //페이징 기능 삽입
    private List<TicketResponse> getPagedList(List<TicketResponse> list, int page, int size) {
        int start = page * size;
        int end = Math.min(start + size, list.size());

        if (start > list.size()) {
            return Collections.emptyList();
        }
        return list.subList(start, end);
    }

    // 높은 할인율순 정렬
    public List<TicketResponse> getSortedByDiscountRateDesc(int page, int size) {
        List<TicketResponse> sortedList = repository.findAll().stream()
                .sorted(Comparator.comparing((TicketData data) -> parseDiscountRate(data.getDiscountRate())).reversed())
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return getPagedList(sortedList, page, size);
    }

    // startDate 최신순 정렬 (가장 최근 날짜가 위로)
    public List<TicketResponse> getSortedByStartDateDesc(int page, int size) {
        List<TicketResponse> sortedList = repository.findAll().stream()
                .sorted(Comparator.comparing((TicketData data) -> parseDate(data.getStartDate())).reversed())
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return getPagedList(sortedList, page, size);
    }

    // 마감 임박순
    public List<TicketResponse> getSortedByEndDateClosestToToday(int page, int size) {
        LocalDate today = LocalDate.now();

        List<TicketResponse> sortedList = repository.findAll().stream()
                .filter(ticket -> {
                    LocalDate startDate = convertToDate(ticket.getStartDate());
                    LocalDate endDate = convertToDate(ticket.getEndDate());
                    return startDate != null && endDate != null && !startDate.isAfter(today) && !endDate.isBefore(today);
                })
                .sorted(Comparator.comparing(ticket -> convertToDate(ticket.getEndDate())))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return getPagedList(sortedList, page, size);
    }

    //
    private LocalDate convertToDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null; // ✅ 필터링 과정에서 제거될 수 있도록 null 반환
        }

        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return null; // ✅ 변환 실패 시 null 반환하여 필터링에서 제거되도록 함
        }
    }

    //  공연장 필터링
    public List<TicketResponse> filterByPlaces(List<String> places, int page, int size) {
        List<TicketResponse> filteredList = repository.findAll().stream()
                .filter(ticket -> places == null || places.isEmpty() || places.contains(ticket.getPlace()))
                .sorted(Comparator.comparing(TicketData::getEndDate).reversed())
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return getPagedList(filteredList, page, size);
    }

    // 지역 필터링
    public List<TicketResponse> filterByAreas(List<String> areas, int page, int size) {
        List<TicketResponse> filteredList = repository.findAll().stream()
                .filter(ticket -> areas == null || areas.isEmpty() || areas.contains(ticket.getArea()))
                .sorted(Comparator.comparing(TicketData::getEndDate).reversed())
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return getPagedList(filteredList, page, size);
    }

    //단건 조회
    public Optional<TicketResponse> getTicketDetail(String id) {
        return repository.findById(id)
                .map(this::toDetailResponseDTO);
    }

    // 상세 정보를 포함한 Response DTO 변환
    private TicketResponse toDetailResponseDTO(TicketData ticketData) {
        return TicketResponse.builder()
                .seq((ticketData.getSeq()))
                .title(ticketData.getTitle())
                .discountRate(ticketData.getDiscountRate())
                .price(ticketData.getPrice())
                .startDate(ticketData.getStartDate())
                .endDate(ticketData.getEndDate())
                .place(ticketData.getPlace())
                .area(ticketData.getArea())
                .img(ticketData.getImg())
                .build();
    }

    // 엔티티 → Response DTO 변환
    private TicketResponse toResponseDTO(TicketData ticketData) {
        return TicketResponse.builder()
                .seq((ticketData.getSeq()))
                .title(ticketData.getTitle())
                .discountRate(ticketData.getDiscountRate())
                .price(ticketData.getPrice())
                .startDate(ticketData.getStartDate())
                .endDate(ticketData.getEndDate())
                .place(ticketData.getPlace())
                .img(ticketData.getImg())
                .build();
    }
}
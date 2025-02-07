package com.example.INFO.domain.ticket.service;

import com.example.INFO.domain.ticket.domain.TicketData;
import com.example.INFO.domain.ticket.domain.TicketProperties;
import com.example.INFO.domain.ticket.domain.repository.TicketDataRepository;
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
import java.util.ArrayList;
import java.util.List;

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
}
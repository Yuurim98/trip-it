package com.example.tripit.schedule.service;

import com.example.tripit.error.CustomException;
import com.example.tripit.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.example.tripit.error.ErrorCode.NO_CONTENT;

@Service
public class ApiConnection {

    //properties 주입
    @Value("${openapi.serviceKey}")
    private String serviceKey;

    @Value("${openapi.urlEndPoint}")
    private String urlEndPoint;

    @Value("${openapi.dataType}")
    private String dataType;

    @Value("${openapi.os}")
    private String os;

    public ResponseEntity<Object> apiResult(String apiUrl) {

        URL url = null;
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder(); //응답 데이터를 저장하기 위한 StringBuilder

        try {
            url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection(); //URL 연결열고 HTTP 연결 만든다
            connection.setRequestMethod("GET"); //HTTP 요청 GET으로 설정
            connection.setRequestProperty("Content-type", "application/json"); //요청 헤더 Content-type을 json으로 설정

            BufferedReader rd = null; //응답 데이터를 읽기 위한 BufferedReader 객체 선언

            if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300) {  //HTTP 응답 코드를 가져오는데 코드가 200~300 사이면
                rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //응답 데이터 담기
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> dataMap = objectMapper.readValue(rd, Map.class);
                Map<String, Object> responseMap = (Map<String, Object>) dataMap.get("response");
                Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
                Map<String, Object> itemsMap = (Map<String, Object>) bodyMap.get("items");
                List<Map<String, Object>> itemList = (List<Map<String, Object>>) itemsMap.get("item");

                for (Map<String, Object> item : itemList) {
                    String jsonResponse = objectMapper.writeValueAsString(item); //item을 JSON 문자열로 변환하여 사용
                    sb.append(jsonResponse);
                }
                rd.close(); //BufferedReader 닫기
                return ResponseEntity.ok(sb.toString()); //성공 반환
            } else {
                //실패 시 에러
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    errorResponse.append(line);
                }
                rd.close();
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(NO_CONTENT);
        } finally {
            if (connection != null) {
                connection.disconnect(); //HTTP 연결 닫기
            }
        }
    }

    public ResponseEntity<Object> cultureFacilityApi(String metroId, String pageNo, String contentTypeId)  {
        int numOfRows = 8; //가져올 갯수

        //URL 생성
        String apiUrl = urlEndPoint + "areaBasedSyncList1?serviceKey=" + serviceKey + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo + os + "&areaCode=" + metroId + "&contentTypeId=" + contentTypeId + dataType;

        return apiResult(apiUrl);

    }//cultureFacilityApi 끝

    public ResponseEntity<Object> detailApi(String contentId) {
        //URL 생성
        String apiUrl = urlEndPoint + "detailCommon1?serviceKey=" + serviceKey + os +  dataType + "&contentId=" + contentId + "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y";

        return apiResult(apiUrl);
    }

    public ResponseEntity<Object> searchApi(String metroId, String pageNo, String contentTypeId, String keyword) throws UnsupportedEncodingException {
        int numOfRows = 8; //가져올 갯수
        //int pageNo = 1; //페이지 넘버
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString()); //인코딩 작업

        //URL 생성
        String apiUrl = urlEndPoint + "searchKeyword1?serviceKey=" + serviceKey + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo + os + "&areaCode=" + metroId + "&contentTypeId=" + contentTypeId + "&keyword=" + encodedKeyword + dataType;

        return apiResult(apiUrl);
    }



}
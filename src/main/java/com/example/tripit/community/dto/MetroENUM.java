package com.example.tripit.community.dto;


import lombok.Getter;

@Getter
public enum MetroENUM {

    SEOUL("1", "서울"),
    BUSAN("6", "부산"),
    DAEGU("4", "대구"),
    INCHEON("2", "인천"),
    GWANGJU("5", "광주"),
    DAEJEON("3", "대전"),
    ULSAN("7", "울산"),
    SEJONG("8", "세종"),
    GYEONGGI("31", "경기"),
    GANGWON("32", "강원"),
    CHUNGBUK("33", "충북"),
    CHUNGNAM("34", "충남"),
    GYEONGBUK("35", "경북"),
    GYEONGNAM("36", "경남"),
    JEONBUK("37", "전북"),
    JEONNAM("38", "전남"),
    JEJU("39", "제주");

    private final String id;
    private final String name;

    MetroENUM(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String getNameById(String id) {
        for (MetroENUM metro : values()) {
            if (metro.id.equals(id)) {
                return metro.name;
            }
        }
        throw new IllegalArgumentException("Invalid metroId: " + id);
    }
}

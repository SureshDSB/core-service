package com.jet.core.studio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jet.core.District;
import com.jet.core.State;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

public class StudioEnumMain {
    public static void main(String[] args) throws IOException {

        String fileName = "studio.csv";
        Map<Integer, CityInfo> map = read(fileName);
        //System.out.println(map);

        // skip header
        List<CityInfo> cityInfos = map.values().stream().skip(1).toList();
        System.out.println(cityInfos);

        Map<String,Map<String,Map<String,List<String>>>> cmap = createGroup(cityInfos);
        System.out.println(cmap);

        Map<String,Map<String,Map<String,Long>>> countMap = createGroupCount(cityInfos);

        System.out.println(countMap);
        String json = json(cmap);
        System.out.println("json = ");
        System.out.println(json);

        Map<String,Map<String,Map<String,List<String>>>> jsonMap = parse(json);
        System.out.println("jsonMap = ");
        System.out.println(jsonMap);

        Comparator<CityInfo> stateComaparator = Comparator.comparing(CityInfo::getState);
        Comparator<CityInfo> cityComparator = Comparator.comparing(CityInfo::getCity);
        Comparator<CityInfo> districtComparator = Comparator.comparing(CityInfo::getDistrict);
        Comparator<CityInfo> studioComparator = Comparator.comparing(CityInfo::getStudio);
        Comparator<CityInfo> comparator = stateComaparator.thenComparing(districtComparator).thenComparing(cityComparator).thenComparing(studioComparator);
        List<CityInfo> cityInfos1 = new ArrayList<>(cityInfos);
        cityInfos1.sort(comparator);
        System.out.println("Sorted List=");
        System.out.println(cityInfos1);
        Map<State, Map<District, Map<String, List<String>>>> eMap = createGroupEnum(cityInfos1);
        System.out.println("eMap = ");
        System.out.println(eMap);
    }

    private static Map<String, Map<String, Map<String, List<String>>>> createGroup(List<CityInfo> cityInfos) {
        return cityInfos.stream().collect(
                Collectors.groupingBy(CityInfo::getState,
                        Collectors.groupingBy(CityInfo::getDistrict,
                                Collectors.groupingBy(CityInfo::getCity,
                                        Collectors.mapping(CityInfo::getStudio, toList())
                                        ))));
    }

    private static Map<State , List<CityInfo>> createGroupEnumC(List<CityInfo> cityInfos) {
        final Function<CityInfo, State> toState = x->State.valueOf(x.getState());
        return cityInfos.stream().collect(Collectors.groupingBy(toState));

    }

    private static Map<State, Map<District, Map<String, List<String>>>> createGroupEnum(List<CityInfo> cityInfos) {
        final Function<CityInfo, State> toState = x->State.valueOf(x.getState());
        final Function<CityInfo, District> toDistrict = x-> District.valueOf(x.getDistrict());
        return cityInfos.stream().collect(
                Collectors.groupingBy(toState,
                        Collectors.groupingBy(toDistrict,
                                Collectors.groupingBy(CityInfo::getCity,
                                        Collectors.mapping(CityInfo::getStudio, toList())
                                ))));

    }

 private static Map<String, Map<String, Map<String, Long>>> createGroupCount(List<CityInfo> cityInfos) {
        return cityInfos.stream().collect(
                Collectors.groupingBy(CityInfo::getState,
                        Collectors.groupingBy(CityInfo::getDistrict,
                                Collectors.groupingBy(CityInfo::getCity,
                                        Collectors.mapping(CityInfo::getStudio, counting())
                                        ))));
    }


    public static Map<Integer, CityInfo> read(String fileName) throws IOException {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);
        List<String> lines = Files.readAllLines(Paths.get(String.valueOf(file.toPath())));
        Map<Integer, CityInfo> map = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] split = line.split(",");
            CityInfo cityInfo = new CityInfo();
            cityInfo.setState(split[0]);
            cityInfo.setDistrict(split[1]);
            cityInfo.setCity(split[2]);
            cityInfo.setStudio(split[3]);
            map.put(i, cityInfo);
        }
        return map;
    }

    private static String json(Map<String, Map<String, Map<String, List<String>>>>  map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Map<String, Map<String, List<String>>>> parse(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

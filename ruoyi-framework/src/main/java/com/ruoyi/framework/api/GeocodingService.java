package com.ruoyi.framework.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.ruoyi.framework.util.SnCal;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author dss
 * @version 1.0.0
 * @description 百度地图api接口调用
 * @className GeocodingService.java
 * @createTime 2020年12月26日 21:21:00
 */
@Service("geocodingService")
@Transactional
public class GeocodingService {

    private static final Logger LOG = LoggerFactory.getLogger(GeocodingService.class);

    private static final Double PI = Math.PI;

    private static final Double PK = 180 / PI;

    private static final String AK = "yp7vMC6ZZsDe2KVGfIU8lXVchKihyEm5";
    private static final String MAP_URL= "http://api.map.baidu.com/geocoding/v3/?ak="+AK+"&output=json&address=";

    /**
     * 根据地址获取经纬度
     * @param address
     * @return
     */
    public Map<String,Double> getLatAndLngByAddress(String address){
        Map<String,Double> poiMap = null;
        String result = null;
        try {
//
            result = Request.Get(MAP_URL+ address)
                    .connectTimeout(1000).socketTimeout(1000)
                    .execute().returnContent().asString();
        } catch (IOException e) {
            LOG.error("调用百度地图API获取{}的经纬度，抛错{}",address,e);
        }
        if (StringUtils.isNotBlank(result) && "0".equals(JSON.parseObject(result).get("status") + "")){
            String lat = result.substring(result.indexOf("\"lat\":")
                    + ("\"lat\":").length(), result.indexOf("},\"precise\""));
            String lng = result.substring(result.indexOf("\"lng\":")
                    + ("\"lng\":").length(), result.indexOf(",\"lat\""));
            poiMap = ImmutableMap.of("lat",Double.parseDouble(lat),"lng",Double.parseDouble(lng));
        }
        return poiMap;
    }

    /**
     * 计算两个地址的距离（米）
     * @param address
     * @param otherAddress
     * @return
     */
    public Double getDistanceFromTwoPlaces(String address,String otherAddress){
        Double distance = null;
        if (StringUtils.isNotBlank(address) && StringUtils.isNotBlank(otherAddress)){
            address = address.trim();
            otherAddress = otherAddress.trim();
            if (address.equals(otherAddress)){
                return 0.0d;
            }
            Map pointA = getLatAndLngByAddress(address);
            Map pointB = getLatAndLngByAddress(otherAddress);
            distance = getDistanceFromTwoPoints(pointA,pointB);
        }
        return distance;
    }

    /**
     * 获取两个经纬度之间的距离（单位：米）
     * @param pointA
     * @param pointB
     * @return
     */
    public Double getDistanceFromTwoPoints(Map pointA, Map pointB) {
        Double distance = null;
        if (pointA != null && !pointA.isEmpty() && pointB != null && !pointB.isEmpty()){
            double lat_a = (double) pointA.get("lat");
            double lng_a = (double) pointA.get("lng");
            double lat_b = (double) pointB.get("lat");
            double lng_b = (double) pointB.get("lng");

            if (lat_a == lat_b && lng_a == lng_b){
                return 0.0d;
            }

            double t1 = Math.cos(lat_a / PK) * Math.cos(lng_a / PK) * Math.cos(lat_b / PK) * Math.cos(lng_b / PK);
            double t2 = Math.cos(lat_a / PK) * Math.sin(lng_a / PK) * Math.cos(lat_b / PK) * Math.sin(lng_b / PK);
            double t3 = Math.sin(lat_a / PK) * Math.sin(lat_b / PK);

            double tt = Math.acos(t1 + t2 + t3);
            distance = 6366000 * tt;
        }
        return distance;
    }
}

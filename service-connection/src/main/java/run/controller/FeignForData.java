package run.controller;

import feign.Headers;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import run.bean.RTU;
import run.feign.FeignConf;

import java.util.Map;

@FeignClient(name="service-data",configuration= FeignConf.class)
public interface FeignForData {

    @RequestLine("POST /rtuDraw")
    @Headers("Content-Type: application/json")
    public Map<String,Object> rtuDraw(Map<String, Object> map);

}

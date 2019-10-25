package run.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import run.service.DataService;
import java.util.*;

@RestController
public class DataController {
    @Autowired
    private DataService dataService;
    @Autowired
    private FeignForStructure feignForStructure;

    @RequestMapping(value = "/healthTop5",method = RequestMethod.GET)
    public Map<String,Object> healthTop5(){
        List<Map<String,Object>> list1 = dataService.healthTop5A();
        List<Map<String,Object>> list2 = dataService.healthTop5B();
        List<Map<String,Object>> l1 = dataService.riskTop5A();
        List<Map<String,Object>> l2 = dataService.riskTop5B();
        Map<String,Object> reslutMap = new HashMap<>();
        reslutMap.put("list1",list1);
        reslutMap.put("list2",list2);
        reslutMap.put("l1",l1);
        reslutMap.put("l2",l2);
        return reslutMap;
    }

    @RequestMapping(value = "/rtuDraw",method = RequestMethod.POST)
    public Map<String,Object> rtuDraw(@RequestBody Map<String,Object> map){
        String rtu_id = map.get("rtu_id")+"";
        Map<String,Object> reslutMap = new HashMap<>();
        reslutMap.put("health",dataService.rtuHealth(rtu_id));
        reslutMap.put("risk",dataService.rtuRisk(rtu_id));
        return reslutMap;
    }
}

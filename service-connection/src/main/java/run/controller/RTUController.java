package run.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import run.bean.RTU;
import run.service.RTUService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@RestController
public class RTUController {
    @Autowired
    private RTUService rtuService;
    @Autowired
    FeignForSPD feignForSPD;
    @Autowired
    FeignForETCR feignForETCR;
    @Autowired
    FeignForLightning feignForLightning;
    @Autowired
    FeignForStatic feignForStatic;
    @Autowired
    FeignForRsws feignForRsws;
    @Autowired
    FeignForSvt feignForSvt;
    @Autowired
    FeignForHc feignForHc;
    @Autowired
    FeignForStray feignForStray;
    @Autowired
    FeignForCat feignForCat;
    @Autowired
    FeignForMQ feignForMQ;
    @Autowired
    AsyncController asyncController;
    @Autowired
    private StructureController structureController;
    @Autowired
    FeignForData feignForData;

    @RequestMapping(value = "/selectAllRTU",method = RequestMethod.GET)
    public Map<String,Object> selectAllRTU (HttpServletRequest req, HttpServletResponse resp) {
        String structure = req.getParameter("structure");
        List<Integer> strList = structureController.foreachIdAndPIdForConnection(Integer.parseInt(structure));
        System.out.println("strList : ++++++++++++"+strList);
        int start;
        if (req.getParameter("start") != null && !"".equals(req.getParameter("start"))) {
            start = Integer.parseInt(req.getParameter("start"));
        } else {
            start = -1;
        }
        int limit;
        if (req.getParameter("limit") != null && !"".equals(req.getParameter("limit"))) {
            limit = Integer.parseInt(req.getParameter("limit"));
        } else {
            limit = -1;
        }
        int site_id;
        if (req.getParameter("site_id") != null && req.getParameter("site_id") != "") {
            site_id = Integer.parseInt(req.getParameter("site_id"));
        } else {
            site_id = -1;
        }
        int connect_type;
        if (req.getParameter("connect_type") != null) {
            connect_type = Integer.parseInt(req.getParameter("connect_type"));
            if (connect_type == 2) {
                connect_type = -1;
            }
        } else {
            connect_type = -1;
        }

        String rtu_ip = req.getParameter("rtu_ip");
        String rtu_netmask = req.getParameter("rtu_netmask");
        String rtu_gateway = req.getParameter("rtu_gateway");
        String status = req.getParameter("status");
        //System.out.println(start+"=="+limit+"=="+site_id+"=="+connect_type+"=="+rtu_ip+"=="+rtu_netmask+"=="+rtu_gateway);
        Map<String, Object> param = new HashMap<>();
        param.put("site_id", site_id);
        param.put("connect_type", connect_type);
        param.put("rtu_ip", rtu_ip);
        param.put("rtu_netmask", rtu_netmask);
        param.put("rtu_gateway", rtu_gateway);
        param.put("start",start);
        param.put("limit",limit);
        param.put("strList",strList);
        List<Map<String, Object>> rtuList = rtuService.selectAllRTU(param);

        Map<String, Object> rtuListMap = new HashMap<>();

        if (start == -1 || limit == -1) {
            rtuListMap.put("items", rtuList);
            rtuListMap.put("totals", rtuList.size());
            return rtuListMap;
        } else {
            //遍历rtuList集合
            if (rtuList.size() > 0) {
                for (int i = 0; i < rtuList.size(); i++) {
                    Map<String, Object> map = rtuList.get(i);
                    int rtu_id = Integer.parseInt(map.get("rtu_id") + "");
                    int deviceWarning = rtuService.selectDeviceWarningCount(rtu_id);
                    System.out.println("deviceWarning : " + deviceWarning);
                    int rtu_state = -1;
                    if(map.get("rtu_state") != null && map.get("rtu_state") != ""){
                        rtu_state = Integer.parseInt(map.get("rtu_state") + "");
                    }else{
                        rtu_state = 1;
                    }
                    if (deviceWarning > 0 && rtu_state == 0) {
                        //异常
                        map.put("rtu_state", 2);
                    }
                }
            }

            if (status != null && !"".equals(status)) {
                int temp = Integer.parseInt(status);
                List<Map<String, Object>> finalList = new LinkedList<>();
                int count = 0;
                for (int i = start; i < rtuList.size(); i++) {
                    Map<String, Object> map = rtuList.get(i);
                    if (count == limit) {
                        break;
                    }
                    int s = Integer.parseInt(map.get("status") + "");
                    if (s == temp) {
                        finalList.add(map);
                        count++;
                    }

                }

                int size = 0;
                for (int i = start; i < rtuList.size(); i++) {
                    Map<String, Object> map = rtuList.get(i);
                    int s = Integer.parseInt(map.get("status") + "");
                    if (s == temp) {
                        size++;
                    }

                }
                rtuListMap.put("items", finalList);
                rtuListMap.put("totals", size);
                return rtuListMap;
            } else {

                List<Map<String, Object>> finalList = new LinkedList<>();
                int count = 0;
                for (int i = start; i < rtuList.size(); i++) {
                    Map<String, Object> map = rtuList.get(i);
                    if (count == limit) {
                        break;
                    }
                    finalList.add(map);
                    count++;

                }

                rtuListMap.put("items", finalList);
                rtuListMap.put("totals", rtuList.size());
                return rtuListMap;
            }
        }
    }

    @RequestMapping(value = "/selectAllRTUDraw",method = RequestMethod.GET)
    public Map<String,Object> selectAllRTUDraw (@RequestParam String site_id) {
        Map<String,Object> param = new HashMap<>();
        param.put("site_id",site_id);
        List<Map<String, Object>> rtuList = rtuService.selectAllRTUDrawBySiteId(param);
        Map<String, Object> rtuListMap = new HashMap<>();
        for(Map<String,Object> map : rtuList){
            int rtu_id = Integer.parseInt(map.get("rtu_id") + "");
            int deviceWarning = rtuService.selectDeviceWarningCount(rtu_id);
            System.out.println("deviceWarning : " + deviceWarning);
            int rtu_state = -1;
            if(map.get("rtu_state") != null && map.get("rtu_state") != ""){
                rtu_state = Integer.parseInt(map.get("rtu_state") + "");
            }else{
                rtu_state = 1;
            }
            if (deviceWarning > 0 && rtu_state == 0) {
                //异常
                map.put("rtu_state", 2);
            }

            Map<String,Object> p = new HashMap<>();
            p.put("rtu_id",rtu_id);
            Map<String,Object> dataMap = feignForData.rtuDraw(p);
            Map<String,Object> healthMap = (Map)dataMap.get("health");
            Map<String,Object> riskMap = (Map)dataMap.get("risk");
            map.put("health",healthMap.get("score"));
            map.put("risk",riskMap.get("score"));
        }
        rtuListMap.put("items", rtuList);
        rtuListMap.put("totals", rtuList.size());
        rtuListMap.put("siteInfo",rtuService.selectSiteInfo(param));
        return rtuListMap;
    }

    @RequestMapping(value = "/selectRTUById",method = RequestMethod.GET)
    public Map<String,Object> selectRTUById (@RequestParam int id){
        return rtuService.selectRTUById(id);
    }

    @RequestMapping(value = "/rtuDraw",method = RequestMethod.GET)
    public Map<String,Object> rtuDraw (@RequestParam int id){
        Map<String,Object> param = new HashMap<>();
        param.put("rtu_id",id+"");
        Map<String,Object> resultMap = feignForData.rtuDraw(param);
        Map<String,Object> map = rtuService.selectRTUById(id);

        int rtu_id = Integer.parseInt(id + "");
        int deviceWarning = rtuService.selectDeviceWarningCount(rtu_id);
        int rtu_state = -1;
        if(map.get("rtu_state") != null && map.get("rtu_state") != ""){
            rtu_state = Integer.parseInt(map.get("rtu_state") + "");
        }else{
            rtu_state = 1;
        }
        if (deviceWarning > 0 && rtu_state == 0) {
            //异常
            map.put("rtu_state", 2);
        }

        resultMap.put("rtuInfo",map);

        Map<String,Object> spdMap = rtuService.selectSPDStatusById(id);
        if(spdMap != null){
            String spdTemp = spdMap.get("status")+"";
            if("0".equals(spdTemp)){
                resultMap.put("spdStatus",0);
            }else {
                resultMap.put("spdStatus",1);
            }
        }

        Map<String,Object> etcrMap = rtuService.selectETCRStatusById(id);
        if(etcrMap != null){
            String etcrStatus = etcrMap.get("status")+"";
            String etcrAlarm = etcrMap.get("alarm")+"";
            if("1".equals(etcrStatus)){
                resultMap.put("etcrStatus",1);
            }else if("0".equals(etcrStatus) && Integer.parseInt(etcrAlarm) > 0){
                resultMap.put("etcrStatus",2);
            }else if("0".equals(etcrStatus) && "0".equals(etcrAlarm)){
                resultMap.put("etcrStatus",0);
            }
        }

        return resultMap;
    }

    @RequestMapping(value = "/selectAllRTUPosition",method = RequestMethod.GET)
    public List<Map<String,Object>> selectAllRTUPosition (HttpServletRequest req){
        String structure = req.getParameter("structure");
        List<Integer> strList = structureController.foreachIdAndPIdForConnection(Integer.parseInt(structure));
        System.out.println("strList : ++++++++++++"+strList);
        Map<String,Object> param = new HashMap<>();
        param.put("strList",strList);
        return rtuService.selectAllRTUPosition(param);
    }

    @RequestMapping(value = "/insertRTU", method = RequestMethod.POST)
    public Map<String, Object> insertRTU (@RequestBody RTU rtu){
        int result = rtuService.insertRTU(rtu);
        Map<String,Object> map = new HashMap<>();
        if(result>0){
            map.put("success",true);
            map.put("message","成功添加了RTU");
            Map<String,Object> param = new HashMap<>();
            param.put("rtu",rtu);
            param.put("op",0);
            asyncController.asyncSendRtuDelNewConf(param);
            asyncController.asyncSendRTUConf(rtu);
        }else {
            map.put("success",false);
            map.put("message","添加RTU失败");
        }
        return map;
    }

    @RequestMapping("/deleteRTU")
    public Map<String, Object> deleteRTU(HttpServletRequest req, HttpServletResponse resp){
        int id = Integer.parseInt(req.getParameter("id"));
        int res = rtuService.deleteRTU(id);
        Map<String,Object> param = new HashMap<>();
        RTU rtu = new RTU();
        rtu.setRtu_id(id);
        param.put("rtu",rtu);
        param.put("op",1);
        asyncController.asyncSendRtuDelNewConf(param);
        //删除redis
        asyncController.delRTUConfByRedis(rtu.getRtu_id()+"");
        //删除相关设备配置
        asyncController.asyncDelDeviceByRTUID(id);
        Map<String,Object> map = new HashMap<>();
        if(res>0){
            map.put("success",true);
            map.put("message","成功删除了RTU");
        }else {
            map.put("success",false);
            map.put("message","删除RTU失败");
        }
        return map;
    }

    @RequestMapping(value = "/updateRTU", method = RequestMethod.POST)
    public Map<String, Object> updateRTU(@RequestBody RTU rtu){
        int result = rtuService.updateRTU(rtu);
        Map<String,Object> map = new HashMap<>();
        if(result>0){
            map.put("success",true);
            map.put("message","成功更新了RTU");
            Map<String,Object> param = new HashMap<>();
            param.put("rtu",rtu);
            param.put("op",0);
            asyncController.asyncSendRtuDelNewConf(param);
            asyncController.asyncSendRTUConf(rtu);
        }else {
            map.put("success",false);
            map.put("message","更新RTU失败");
        }
        return map;
    }

}

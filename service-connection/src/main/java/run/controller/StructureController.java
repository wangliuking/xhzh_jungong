package run.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import run.bean.Node;
import run.service.StructureService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StructureController {
    @Autowired
    private StructureService structureService;

    @RequestMapping(value = "/selectAllStructure")
    public List<Node> selectAllStructure (){

        List<Node> nodeList = structureService.selectAll();
        TreeUtil treeUtil = new TreeUtil();
        List<Node> resultList = treeUtil.getInfiniteLevelTree(nodeList);
        return resultList;

    }

    @RequestMapping(value = "/insertStructure")
    public Map<String, Object> insertStructure (@RequestBody Node node){
        Map<String,Object> params = new HashMap<>();
        System.out.println("node : "+node);
        int result = structureService.insert(node);
        Map<String,Object> map = new HashMap<>();
        if(result>0){
            map.put("success",true);
            map.put("message","成功添加了一个单位");
        }else {
            map.put("success",false);
            map.put("message","添加单位失败");
        }
        return map;
    }

    @RequestMapping(value = "/deleteStructure")
    public Map<String, Object> deleteStructure(HttpServletRequest req, HttpServletResponse resp){
        int id = Integer.parseInt(req.getParameter("id"));
        int res = structureService.delete(id);
        Map<String,Object> map = new HashMap<>();
        if(res>0){
            map.put("success",true);
            map.put("message","成功删除了该单位");
        }else {
            map.put("success",false);
            map.put("message","删除单位失败");
        }
        return map;
    }

    @RequestMapping(value = "/updateStructure")
    public Map<String, Object> updateStructure(HttpServletRequest req){
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        param.put("name",name);
        int result = structureService.update(param);
        Map<String,Object> map = new HashMap<>();
        if(result>0){
            map.put("success",true);
            map.put("message","成功更新了该单位");
        }else {
            map.put("success",false);
            map.put("message","更新该单位失败");
        }
        return map;
    }
}

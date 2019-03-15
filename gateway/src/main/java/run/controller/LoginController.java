package run.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import run.EncryptUtil;
import run.exportWord.ZipUtils;
import run.redis.RedisTest;
import run.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@RestController
public class LoginController {
    @Autowired
    LoginService loginService;

    @RequestMapping("/login")
    public Map<String,Object> login(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Map<String,Object> param = new HashMap<>();
        System.out.println("username为： "+username+"====="+"password为： "+ EncryptUtil.encrypt(password));
        param.put("username",username);
        param.put("password", EncryptUtil.encrypt(password));
        List<Map<String,Object>> list = loginService.selectUser(param);
        System.out.println("返回的userlist为： "+list);
        Map<String,Object> resultMap = new HashMap<>();
        if(list.size()>0){
            RedisTest.addLoginUser(req.getSession().getId(),username);

            Map<String,Object> logParam = new HashMap<>();
            logParam.put("type","登录操作");
            logParam.put("userId",username);
            logParam.put("ip",getIpAddress(req));
            logParam.put("content","登录平台");
            loginService.insertLog(logParam);

            resultMap.put("result","登录成功");
            return resultMap;
        }else{
            resultMap.put("result","登录失败");
            return resultMap;
        }
    }

    @RequestMapping("/loginOut")
    public Map<String,Object> loginOut(HttpServletRequest req) {
        RedisTest.removeLoginUser(req.getSession().getId());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result","退出成功");
        return resultMap;
    }

    @RequestMapping("/getLoginUser")
    public Map<String,Object> getLoginUser(HttpServletRequest req) {
        String userId = RedisTest.getLoginUser(req.getSession().getId());
        System.out.println("userId : "+userId);
        Map<String,Object> param = new HashMap<>();
        param.put("userId",userId);
        return loginService.selectUserPower(param);
    }

    @RequestMapping(value = "/selectLogList",method = RequestMethod.GET)
    public Map<String,Object> selectLogList(HttpServletRequest req) {
        int start = Integer.parseInt(req.getParameter("start"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String type = req.getParameter("type");
        String userId = req.getParameter("userId");
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");

        Map<String,Object> result = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        params.put("start",start);
        params.put("limit",limit);
        params.put("type",type);
        params.put("userId",userId);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        result.put("items",loginService.selectLogList(params));
        result.put("totals",loginService.selectLogListCount(params));
        return result;
    }

    /**
     * 日志调用服务
     */
    @RequestMapping(value = "/insertLog",method = RequestMethod.GET)
    public void insertLog(HttpServletRequest req) {
        String sessionId = req.getSession().getId();
        System.out.println("sessionId : "+sessionId);
        String userId = RedisTest.getLoginUser(sessionId);
        System.out.println("userId : "+userId);
        Map<String,Object> logParam = new HashMap<>();
        logParam.put("type",req.getParameter("type"));
        logParam.put("userId",userId);
        logParam.put("ip",getIpAddress(req));
        logParam.put("content",req.getParameter("content"));
        loginService.insertLog(logParam);
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     *
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     *
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}

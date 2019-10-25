package run.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.mapper.DataMapper;

import java.util.List;
import java.util.Map;

@Service
public class DataService {
    @Autowired
    DataMapper dataMapper;

    public Map<String,Object> rtuHealth(String rtu_id){
        return dataMapper.rtuHealth(rtu_id);
    }

    public Map<String,Object> rtuRisk(String rtu_id){
        return dataMapper.rtuRisk(rtu_id);
    }

    public List<Map<String,Object>> healthTop5A(){
        return dataMapper.healthTop5A();
    }

    public List<Map<String,Object>> healthTop5B(){
        return dataMapper.healthTop5B();
    }

    public List<Map<String,Object>> riskTop5A(){
        return dataMapper.riskTop5A();
    }

    public List<Map<String,Object>> riskTop5B(){
        return dataMapper.riskTop5B();
    }

}

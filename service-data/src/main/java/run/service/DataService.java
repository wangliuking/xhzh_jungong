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

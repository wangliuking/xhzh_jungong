package run.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataMapper {
    @Select("<script>" +
            "select * from health_output where rtu_id=#{rtu_id} order by create_at desc limit 1"+
            "</script>")
    Map<String,Object> rtuHealth(String rtu_id);

    @Select("<script>" +
            "select * from risk_output where rtu_id=#{rtu_id} order by create_at desc limit 1"+
            "</script>")
    Map<String,Object> rtuRisk(String rtu_id);

    @Select("<script>" +
            "select * from (select * from health_output order by create_at desc limit 100) t group by rtu_id order by score limit 5"+
            "</script>")
    List<Map<String,Object>> healthTop5A();

    @Select("<script>" +
            "select * from (select * from health_output order by create_at desc limit 100) t group by rtu_id order by score desc limit 5"+
            "</script>")
    List<Map<String,Object>> healthTop5B();

    @Select("<script>" +
            "select * from (select * from risk_output order by create_at desc limit 100) t group by rtu_id order by score limit 5"+
            "</script>")
    List<Map<String,Object>> riskTop5A();

    @Select("<script>" +
            "select * from (select * from risk_output order by create_at desc limit 100) t group by rtu_id order by score desc limit 5"+
            "</script>")
    List<Map<String,Object>> riskTop5B();
}

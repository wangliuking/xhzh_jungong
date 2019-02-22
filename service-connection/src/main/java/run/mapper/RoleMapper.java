package run.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import run.bean.Role;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMapper {

    @Select("select * from xhzh.role where id = #{id}")
    Role selectRoleById(int id);

    @Select("<script>" +
            "select * from xhzh.role where 1=1 " +
            "<if test=\"param != null and param != ''\">" +
            "and name like concat('%',#{param},'%')" +
            "</if>" +
            "limit #{start},#{limit}"+
            "</script>")
    List<Role> selectRoleList(Map<String, Object> param);

    @Select("<script>" +
            "select count(*) from xhzh.role where 1=1 " +
            "<if test=\"param != null and param != ''\">" +
            "and name like concat('%',#{param},'%')" +
            "</if>" +
            "</script>")
    int selectRoleListCount(Map<String, Object> param);

    @Insert("insert into xhzh.role(name) values(#{name})")
    int insertRole(Role role);

    /*@Insert("insert into xhzh.role(name,menu_protect,menu_security,menu_fire," +
            "menu_weather,menu_culture,menu_manage,menu_protect_conf," +
            "menu_protect_search,menu_protect_count,menu_protect_alarm," +
            "menu_protect_maintain,menu_protect_conf_site,menu_protect_conf_rtu," +
            "menu_protect_conf_device,menu_protect_search_now,menu_protect_search_history," +
            "menu_protect_search_chart,menu_manage_structure,menu_manage_user," +
            "menu_manage_group,menu_manage_role,menu_manage_log) " +
            "values(#{name},#{menu_protect},#{menu_security},#{menu_fire}," +
            "#{menu_weather},#{menu_culture},#{menu_manage},#{menu_protect_conf}," +
            "#{menu_protect_search},#{menu_protect_count},#{menu_protect_alarm}," +
            "#{menu_protect_maintain},#{menu_protect_conf_site},#{menu_protect_conf_rtu}," +
            "#{menu_protect_conf_device},#{menu_protect_search_now},#{menu_protect_search_history}," +
            "#{menu_protect_search_chart},#{menu_manage_structure},#{menu_manage_user}," +
            "#{menu_manage_group},#{menu_manage_role},#{menu_manage_log})")
    int insertRole(Role role);*/

    @Update("update xhzh.role set menu_protect=#{menu_protect},menu_security=#{menu_security}," +
            "menu_fire=#{menu_fire},menu_weather=#{menu_weather},menu_culture=#{menu_culture},menu_manage=#{menu_manage}," +
            "menu_protect_conf=#{menu_protect_conf},menu_protect_search=#{menu_protect_search},menu_protect_count=#{menu_protect_count},menu_protect_alarm=#{menu_protect_alarm}," +
            "menu_protect_maintain=#{menu_protect_maintain},menu_protect_conf_site=#{menu_protect_conf_site},menu_protect_conf_rtu=#{menu_protect_conf_rtu},menu_protect_conf_device=#{menu_protect_conf_device}," +
            "menu_protect_search_now=#{menu_protect_search_now},menu_protect_search_history=#{menu_protect_search_history},menu_protect_search_chart=#{menu_protect_search_chart},menu_manage_structure=#{menu_manage_structure}," +
            "menu_manage_user=#{menu_manage_user},menu_manage_group=#{menu_manage_group},menu_manage_role=#{menu_manage_role},menu_manage_log=#{menu_manage_log}" +
            " where id=#{id}")
    int updateRole(Role role);

    @Delete("delete from xhzh.role where id=#{id}")
    int deleteRole(int id);
}

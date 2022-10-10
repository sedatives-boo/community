package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * 在本类中，学习使用注解实现sql，不是xml
 *
 */
@Mapper
public interface LoginTicketMapper {

    //登入成功后要插入凭证//需要声明主键自动生成，@Options，且需要将生成的值注入给对象，keyProperty = "id"
    @Insert({
            "insert into login_ticket (user_id,ticket,status,expired) ",//加个空格断开
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    //查询方法：围绕ticket
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    //修改凭证状态：不删除
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateStatus(String ticket,int status);
    //学习：假如需要动态sql时
    /*@Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\">",
            "and 1 =1",
            "</if>",
            "</script>"
    })*/
}

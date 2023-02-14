package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DiscussPostMapper {

    /**
     *
     * @param userId    在首页查询不需要id，而以后的个人主页查询需要id，因此开发成动态sql
     * @param offset    起始行行号
     * @param limit     每页最多显示的数据条数
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId,int offset, int limit);

    int selectDiscussPostRows(@Param("userId") int userId);//如果需要动态拼条件，且方法有且只有一个条件，则这个参数前必须起别名

    int insertDiscussPost(DiscussPost discussPost);

}

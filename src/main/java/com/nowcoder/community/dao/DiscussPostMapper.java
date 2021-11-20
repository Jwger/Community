package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    //分页查询帖子 返回集合 集合里面是返回的对象
    //动态 当userid是0 就不管 不是0就去
    //起始行 行号 和每一页最多显示多少条数据 limit 为分页做准备
    List<DiscussPost> selectDiscussPost(int userId,int offset,int limit);

    //查询出表里一共有多少条数据
    //param 起别名
    //如果在circle里动态的条件(在<if>里面使用) 需要用到的条件 如果只有这一个 就必须起别名
    int selectDiscussPostRows(@Param("userId")int userId) ;

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);
}

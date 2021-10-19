package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

//标识Bean
@Mapper
/*
@Repository也可以
*/
public interface UserMapper {
    //mybatis底层会帮我们自动生成实现类
    /*增删改查的方法
    * */
    //根据ID查询的方法
    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);

    //增加一个用户 返回一个整数 插入数据的行数
    int insertUser(User user);
    //对user做修改 修改状态  ，要有条件（ID） 返回修改的条数 几行数据
    int updateStatus(int id,int status);

    //更新头像
    int updateHeader(int id , String headerUrl);

    //更新密码
    int updatePassword(int id , String password);


}

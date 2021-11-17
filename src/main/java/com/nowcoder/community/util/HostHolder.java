package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

//持有用户的信息 用于代替session对象
//服务器面对请求是多线程的，每个线程都要隔离，隔离存
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}

package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//业务组件
@Service
//@Scope("prototype") 多例 很少用
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    //构造器
    public AlphaService(){
        System.out.println("实例化AlphaService");
    }
    //这个方法在构造器之后调用
    @PostConstruct
    public void init(){
        System.out.println("初始化AlphaService");
    }
    @PreDestroy
    //在销毁对象之前调用他
    public void destroy(){
        System.out.println("销毁AlphaService");
    }
    public String find(){
       return alphaDao.select();
    }
}

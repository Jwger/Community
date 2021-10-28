package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.UUID;

@Controller
@RequestMapping("/alpha")//起个名
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "Hello Spring boot";
    }
    @RequestMapping("/date")
    @ResponseBody
    public String getDate(){
        return alphaService.find();
    }
    @RequestMapping("/http")
    //请求对象，响应对象
    public void http(HttpServletRequest request, HttpServletResponse response
    ){
        //获取请求数据
        //方式
        System.out.println(request.getMethod());
        //路径
        System.out.println(request.getServletPath());
        //
        Enumeration<String> enumeration = request.getHeaderNames();
         while (enumeration.hasMoreElements()){
             String name = enumeration.nextElement();
             String value = request.getHeader(name);
             System.out.println(name+":"+value);
         }
        System.out.println(request.getParameter("code"));
         //返回响应数据
        response.setContentType("text/html;charset = utf-8");
        try ( PrintWriter writer = response.getWriter();
        ){
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //请求的数据怎么处理 简便方式 GET
    //查询所有学生，分页显示 需要告诉服务器 当前是第几夜，每一页最多显示多少条件
    //students?current=1 &limit =20
    //强制必须是GET请求才可以访问到
    @RequestMapping(path = "/students",method = RequestMethod.GET)
    //响应主体
    @ResponseBody
    public String getStudent(
            //意思是 request中的名为current的值 给下面这个current,也可以不穿 后一句意思，默认值是1
            //响应属性
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false,defaultValue = "10")  int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some student";
    }
    //根据学生的ID 查询一个学生，
    // 路径为/student/123
    //是个变量 用大括号括起来
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    //PathVeriable路径变量
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";
    }

    //post请求
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //响应 一个动态的html
    @RequestMapping(path = "/teacher",method = RequestMethod.GET )
    //不加@ResponseBody 默认返回Html
    public ModelAndView getTeacher(){
        ModelAndView mav = new ModelAndView();
        //模板里面需要多少个变量 就加多少个数据进去
        mav.addObject("name","张三");
        mav.addObject("age","30");
        //设置模板的路径和名字 模板是html文件 是固定的方式 后缀不用写 写成文件名就行了
        //放到templates下一个叫demo的文件
        mav.setViewName("/demo/view");
        return mav;
    }
    //响应的稍简单版
    @RequestMapping(path = "/school",method = RequestMethod.GET)
    //DIspatcherSerlet发现有model 就会自动实例化传过来
    public String getSchool(Model model){
        model.addAttribute("name","北京大学");
        model.addAttribute("age","80");
        //返回的类型是String     return 的是view的路径
        //跟上面的不一样的是 这个方法把model放在这个对象里 把view直接返回
        //返回的值给了DIspatcherSerlet
        return "/demo/view";
    }
    //响应JSON数据 在异步请求中 需要响应 比如说要注册B站 注册用户名显示被占用
    //肯定是判断有没有用，访问服务器，服务器访问数据库 但是页面没有被刷新，这就叫异步
    //网页没刷新就访问了服务器
    //Java对象 返回给浏览器 浏览器解析对用 用的是JS语言 可以通过JS实现兼容
    //Java语言 --》 JSON字符串 --》 JS对象
    //客户端需要返回一个局部验证的结果 是成功还是失败
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age","23");
        emp.put("salary","8000.00");
        return emp;
    }
    //返回所有员工 响应
    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age","23");
        emp.put("salary","8000.00");
        list.add(emp);
        emp = new HashMap<>();
        emp.put("name","李四");
        emp.put("age","24");
        emp.put("salary","9000.00");
        list.add(emp);
        emp = new HashMap<>();
        emp.put("name","王五");
        emp.put("age","25");
        emp.put("salary","10000.00");
        list.add(emp);
        return list;

    }

    //cookie
    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        //创建Cookie对象
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置Cookie生效范围
        cookie.setPath("/community/alpha");
        //cookie生存时间
        cookie.setMaxAge(60*10);
        //发送Cookie
        response.addCookie(cookie);
        return "set Cookie";
    }
    @RequestMapping(path = "/cookie/get",method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        System.out.println(code);
        return "get Cookie";
    }
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id",1);
        session.setAttribute("name","Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }
}

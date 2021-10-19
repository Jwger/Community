package com.nowcoder.community.entity;
/*
* 封装分页相关信息*/

public class Page {
    //当前页码
    private int current =1;;
    //显示上限
    private int limit = 10;

    //数据的总数（计算总页数）
    private int rows;
    //查询路径(复用分页链接)
    private String path;

    public int getCurrent() {

        return current;
    }

    public void setCurrent(int current) {
        //需要有判断 不能是一个很大 或者负数
        if (current >=1){
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <=100){
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0){
            this.rows = rows;
        }
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //数据库查询的时候需要用到  或页面显示需要用到
/**
* 获取当前页的起始行
 *
* @return int
*/
    public int getoffset(){
        //current *limit -limit
        return (current -1) *limit;
    }
    /**
    * 获取总的页数
     *
    * @return int
    */
    public int getTotal(){
        //rows /limit {+1}
        if (rows%limit==0){
            return rows/limit;
        }else {
            return rows / limit + 1;
        }
    }
    /**
    *获取起始页码
     *
    * @return int
    */
    public int getFrom(){
        int from = current-2;
        return Math.max(from, 1);
    }

    /**
    * 获取结束页码
    * @return
    */
    public int getTo(){
        int to = current +2;
        //最后i页
        int total = getTotal();

        return Math.min(to, total);
    }
}

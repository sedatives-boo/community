package com.nowcoder.community.entity;

public class Page {
    private int current = 1;//当前页码
    private int limit =10;//显示上限
    private int rows;//数据总数（用于计算总页数）
    private String path;//查询路径
    //获取当前页的起始行
    public int getOffset(){
        return (current-1)*limit;
    }
    //获取总的叶数
    public int getTotal(){
        if(rows%limit==0){
           return rows/limit;
        }else{
            return rows/limit+1;
        }
    }
    //从哪开始，从哪结束
    public int getFrom(){
        int from = current-2;
        return Math.max(from, 1);
    }
    public int getTo(){
        int to = current+2;
        int total = getTotal();
        return Math.min(to, total);
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current>=1){
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit>=1&&limit<=100){
            this.limit = limit;
        }

    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows>=0){
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

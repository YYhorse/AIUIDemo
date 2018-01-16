package com.aiuidemo.www;

import java.util.List;

/**
 * Created by yy on 2018/1/15.
 */
public class AiUiJson {
    private Answer answer;
//    private Data data;
//    private Semantic semantic;
    private String dialog_stat,text,service;
    private int rc;             //0 操作成功  1输入异常 2系统内部异常 3业务操作失败   4文本没有匹配的技能场景

    public Answer getAnswer(){return this.answer;}
//    public Data getData(){return this.data;}
//    public Semantic getSemantic() {return this.semantic;}
    public String getDialog_stat() {return this.dialog_stat;}
    public String getText() {return this.text;}
    public String getService() {return this.service;}
    public int getRc() {return this.rc;}

    public class Answer{
        private String text;
        public String getText() {return this.text;}
    }

    public class Data{
        private List<Result> result;
        public List<Result> getResult() {return this.result;}
    }
    public class Result{
//        private String content,name,source,url;
//        public String getContent() {return this.content;}
//        public String getName() {return this.name;}
//        public String getSource() {return this.source;}
//        public String getUrl() {return this.url;}
    }
    public class Semantic{

    }
}
/*
{
        "answer" : {
            "text" : "\"合肥\"今天\"中雨\", \"15℃\", \"东北风微风\", 雨天出行记得准备带伞"
        },
        "data" : {
            "result" : [
                {
                    ......
                }
            ]
        },
        "dialog_stat" : "DataValid",
        "text" : "合肥的天气",
        "service" : "weather",
        "rc" : 0,
        "semantic" : [
            {
                "intent" :  "QUERY",
                "slots" : {
                     ......
                }
            }
        ]
    }
 */

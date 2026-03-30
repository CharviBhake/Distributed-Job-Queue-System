package com.example.demo.Entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class Job implements Serializable {
    @Id
    private String id;
    private String type;
    private String payload;
    private int retryCount;
    private String priority;
    public Job(){}
    public Job(String id,String type,String payload){
        this.id=id;
        this.type=type;
        this.payload=payload;
    }
    public String getId(){
        return id;
    }
    public String getType(){
        return type;
    }
    public String getPayload(){
        return payload;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setPayload(String payload ){
        this.payload=payload;
    }
    public int getRetryCount(){
        return retryCount;
    }
    public void setRetryCount(int retryCount){
        this.retryCount=retryCount;
    }
    public String getPriority(){
        return priority;
    }
    public void setPriority(String Priority){ this.priority=priority;}
}

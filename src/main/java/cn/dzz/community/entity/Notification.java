package cn.dzz.community.entity;

import java.io.Serializable;

/**
 * (Notification)实体类
 *
 * @author makejava
 * @since 2020-04-04 13:05:31
 */
public class Notification implements Serializable {
    private static final long serialVersionUID = 716468904010682128L;
    
    private Long id;
    
    private String receiverName;
    
    private Integer receiver;
    
    private Integer notifier;
    
    private String notifierName;
    /**
    * 0问题1回复
    */
    private Integer type;
    /**
    * 0未读1已读
    */
    private Integer status;
    
    private String title;
    
    private Long outerId;
    
    private Long gmtCreate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public Integer getNotifier() {
        return notifier;
    }

    public void setNotifier(Integer notifier) {
        this.notifier = notifier;
    }

    public String getNotifierName() {
        return notifierName;
    }

    public void setNotifierName(String notifierName) {
        this.notifierName = notifierName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getOuterId() {
        return outerId;
    }

    public void setOuterId(Long outerId) {
        this.outerId = outerId;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

}
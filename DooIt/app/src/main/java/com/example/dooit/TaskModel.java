package com.example.dooit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskModel implements Serializable {

    String desc;
    Boolean status;

    public TaskModel(String desc, Boolean status) {
        this.desc = desc;
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

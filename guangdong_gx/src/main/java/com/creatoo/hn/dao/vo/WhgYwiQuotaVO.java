package com.creatoo.hn.dao.vo;

import com.creatoo.hn.dao.model.WhgYwiQuota;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "群文配额管理")
public class WhgYwiQuotaVO extends WhgYwiQuota implements Serializable {

    private String name;

    private String usedstate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsedstate() {
        return usedstate;
    }

    public void setUsedstate(String usedstate) {
        this.usedstate = usedstate;
    }
}
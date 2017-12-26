package com.example.zhaojian.ExcelToXml.dataEntity;

/**
 * Created by zhaojian on 2017/12/25.
 */

public class BaseEntity
{
    /* 中文名称 */
    protected String name;
    /* 对应id */
    protected String enName;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
    }
}

package com.example.zhaojian.ExcelToXml.dataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaojian on 2017/12/25.
 * 二级节点，表示input、radio、date等信息
 */

public class SubSectionEntity extends BaseEntity
{

    /*文本输入*/
    public static final int TYPE_INPUT_TXT = 0;
    /*数字输入*/
    public static final int TYPE_INPUT_NUMBER = 1;
    /*单选*/
    public static final int TYPE_RADIO = 2;
    /*多选*/
    public static final int TYPE_SELECT = 3;
    /*日期*/
    public static final int TYPE_DATE = 4;


    //public static final int TYPE_DATE = 5;

    /* 字段属性 用于区分input、radio、date */
    private int type;
    /*是否显示*/
    private boolean show = true;
    /*是否可编辑*/
    private boolean enable = true;
    /* 关联数据库字段 */
    private String dataBaseLink;
    /*对应的值域*/
    private List<ValueEntity> valueEntityList = new ArrayList<>();

    /*受其他二级节点的影响*/
    private String linkedStr;
    /*受其他二级节点影响对应值域*/
    private String linkedValueStr;
    /*影响其他二级节点*/
    private List<SubSectionEntity> linkList = new ArrayList<>();
    /*影响其他二级节点对应值域*/
    private List<ValueEntity> linkValueEntityList = new ArrayList<>();



    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public boolean isShow()
    {
        return show;
    }

    public void setShow(boolean show)
    {
        this.show = show;
    }

    public boolean isEnable()
    {
        return enable;
    }

    public void setEnable(boolean enable)
    {
        this.enable = enable;
    }

    public String getDataBaseLink()
    {
        return dataBaseLink;
    }

    public void setDataBaseLink(String dataBaseLink)
    {
        this.dataBaseLink = dataBaseLink;
    }

    public List<ValueEntity> getValueEntityList()
    {
        return valueEntityList;
    }

    public void setValueEntityList(List<ValueEntity> valueEntityList)
    {
        this.valueEntityList = valueEntityList;
    }

    public String getLinkedStr()
    {
        return linkedStr;
    }

    public void setLinkedStr(String linkedStr)
    {
        this.linkedStr = linkedStr;
    }

    public String getLinkedValueStr()
    {
        return linkedValueStr;
    }

    public void setLinkedValueStr(String linkedValueStr)
    {
        this.linkedValueStr = linkedValueStr;
    }

    public List<SubSectionEntity> getLinkList()
    {
        return linkList;
    }

    public void setLinkList(List<SubSectionEntity> linkList)
    {
        this.linkList = linkList;
    }

    public List<ValueEntity> getLinkValueEntityList()
    {
        return linkValueEntityList;
    }

    public void setLinkValueEntityList(List<ValueEntity> linkValueEntityList)
    {
        this.linkValueEntityList = linkValueEntityList;
    }

    public void addLinkSubSection(SubSectionEntity subSectionEntity)
    {
        linkList.add(subSectionEntity);
    }

    public void addLinkValueEntity(ValueEntity valueEntity)
    {
        linkValueEntityList.add(valueEntity);
    }
}

package com.example.zhaojian.test2.dataEntity;

import java.util.List;

/**
 * Created by zhaojian on 2017/12/25.
 */

public class SectionEntity extends BaseEntity
{
    /* 每个Section中需要显示的二级节点项 */
    private List<SubSectionEntity> subSectionList;

    public List<SubSectionEntity> getSubSectionList()
    {
        return subSectionList;
    }

    public void setSubSectionList(List<SubSectionEntity> subSectionList)
    {
        this.subSectionList = subSectionList;
    }
}

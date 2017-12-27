package com.example.zhaojian.ExcelToXml;

import android.content.Context;
import android.text.TextUtils;

import com.example.zhaojian.ExcelToXml.dataEntity.SubSectionEntity;
import com.example.zhaojian.ExcelToXml.utils.ExcelUtils;

import java.util.HashMap;

/**
 * Created by zhaojian on 2017/12/26.
 */

public class DataManager
{
    private static DataManager dataManager;
    /*二级节点类型表*/
    private HashMap<String,Integer> subSectionMap;
    /*字段对应的字段id*/
    private HashMap<String,String> sectionIdMap;
    /*xml中节点名称*/
    private HashMap<Integer,String> xmlSectionMap;

    public static DataManager getIns()
    {
        if (dataManager == null)
        {
            dataManager = new DataManager();
        }
        return dataManager;
    }

    private DataManager()
    {
        initSubSectionMap();
        initXmlMap();
    }

    private void initSubSectionMap()
    {
        subSectionMap = new HashMap<>();
        subSectionMap.put("文本", 0);
        subSectionMap.put("数字", 1);
        subSectionMap.put("单选", 2);
        subSectionMap.put("多选", 3);
        subSectionMap.put("日期", 4);
        subSectionMap.put("搜索", 5);
        subSectionMap.put("列表", 6);
    }

    private void initXmlMap()
    {
        xmlSectionMap = new HashMap<>();
        xmlSectionMap.put(SubSectionEntity.TYPE_INPUT_TXT, "input");
        xmlSectionMap.put(SubSectionEntity.TYPE_INPUT_NUMBER, "input");
        xmlSectionMap.put(SubSectionEntity.TYPE_RADIO, "radio");
        xmlSectionMap.put(SubSectionEntity.TYPE_SELECT, "select");
        xmlSectionMap.put(SubSectionEntity.TYPE_DATE, "date");
    }

    public int getSubSectionType(String typeStr)
    {
        if (TextUtils.isEmpty(typeStr) || typeStr.length() < 2)
        {
            return -1;
        }
        String subStr = typeStr.substring(0, 2);
        return subSectionMap.get(subStr);
    }

    public String getSectionId(String sectionName)
    {
        if (TextUtils.isEmpty(sectionName) || !sectionIdMap.containsKey(sectionName))
        {
            return "";
        }
        return sectionIdMap.get(sectionName).toLowerCase();
    }

    public String getXmlSectionName(int subSectionType)
    {
        if (xmlSectionMap.containsKey(subSectionType))
        {
            return xmlSectionMap.get(subSectionType);
        }
        return "unknow";
    }

    public void initIdMap(Context context, String fileName)
    {
        sectionIdMap = ExcelUtils.getIdMap(context, fileName);
    }
}

package com.example.zhaojian.ExcelToXml.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.zhaojian.ExcelToXml.DataManager;
import com.example.zhaojian.ExcelToXml.dataEntity.SectionEntity;
import com.example.zhaojian.ExcelToXml.dataEntity.SubSectionEntity;
import com.example.zhaojian.ExcelToXml.dataEntity.ValueEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by zhaojian on 2017/12/25.
 */

public class ExcelUtils
{
    public static List<SectionEntity> parseExcel(Context context, String fileName)
    {
        jxl.Workbook workbook;
        List<SectionEntity> sectionEntityList = new ArrayList<>();
        try
        {
            InputStream inputStream = context.getAssets().open(fileName);
            workbook = Workbook.getWorkbook(inputStream);
            Sheet readSheet = workbook.getSheet(0);
            int rsColumns = readSheet.getColumns();
            int rsRows = readSheet.getRows();

            for (int i = 2; i < rsRows;)
            {
                SectionEntity sectionEntity = new SectionEntity();
                Cell sectionCell = readSheet.getCell(0, i);
                sectionEntity.setName(sectionCell.getContents());
                sectionEntity.setEnName(ChineseInital.getAllFirstLetter(sectionCell.getContents()));

                List<SubSectionEntity> subSectionEntityList = new ArrayList<>();
                while (true)
                {
                    SubSectionEntity subSectionEntity = new SubSectionEntity();

                    /*字段名称*/
                    Cell cell1 = readSheet.getCell(1, i);
                    subSectionEntity.setName(cell1.getContents());

                    //先通过xml表中配置的id查询，如果没有，则使用中文名的首字母缩写
                    String sectionId = DataManager.getIns().getSectionId(cell1.getContents());
                    if (TextUtils.isEmpty(sectionId))
                    {
                        subSectionEntity.setEnName("**未匹配**"+ChineseInital.getAllFirstLetter(cell1.getContents()));
                    }
                    else
                    {
                        subSectionEntity.setEnName(sectionId);
                    }

                    if (cell1.getContents().length() == 4)
                    {
                        switch (cell1.getContents().substring(2, 4))
                        {
                            case "隐藏":
                                subSectionEntity.setShow(false);
                                break;
                            case "显示":
                                subSectionEntity.setEnable(false);
                                break;
                            default:
                                break;
                        }
                    }

                    /*字段属性*/
                    cell1 = readSheet.getCell(2, i);
                    subSectionEntity.setType(getType(cell1.getContents()));

                    /*值域*/
                    cell1 = readSheet.getCell(3, i);
                    subSectionEntity.setValueEntityList(getValueEntityList(cell1.getContents()));
                    subSectionEntityList.add(subSectionEntity);

                    /*link*/
                    cell1 = readSheet.getCell(4, i);
                    parseLink(subSectionEntity, cell1.getContents());

                    i++;
                    if (i >= rsRows || !TextUtils.isEmpty(readSheet.getCell(0, i).getContents()))
                    {
                        break;
                    }
                }
                sectionEntity.setSubSectionList(subSectionEntityList);
                relateLink(sectionEntity);
                sectionEntityList.add(sectionEntity);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (BiffException e)
        {
            e.printStackTrace();
        }
        return sectionEntityList;
    }

    public static HashMap<String, String> getIdMap(Context context, String fileName)
    {
        HashMap<String, String> idMap = new HashMap<>();
        try
        {
            InputStream inputStream = context.getAssets().open(fileName);
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet readSheet = workbook.getSheet(1);
            if (readSheet == null)
            {
                return idMap;
            }
            int rsColumns = readSheet.getColumns();
            int rsRows = readSheet.getRows();
            for (int i = 3; i < rsRows; i++)
            {
                String name = readSheet.getCell(0, i).getContents();
                String id = readSheet.getCell(1, i).getContents();
                idMap.put(name, id);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (BiffException e)
        {
            e.printStackTrace();
        }

        return idMap;
    }

    private static int getType(String typeStr)
    {
        return DataManager.getIns().getSubSectionType(typeStr);
    }


    private static List<ValueEntity> getValueEntityList(String content)
    {
        if (TextUtils.isEmpty(content))
        {
            return null;
        }
        List<ValueEntity> valueEntityList = new ArrayList<>();
        String[] values = content.split("\n");
        for (String value : values)
        {
            String[] value1 = value.split(" ");
            ValueEntity valueEntity = new ValueEntity();
            if (value1.length == 1)
            {
                valueEntity.setName(value1[0]);
            }
            else if (value1.length == 2)
            {
                valueEntity.setIndex(Integer.parseInt(value1[0]));
                valueEntity.setName(value1[1]);
            }
            valueEntityList.add(valueEntity);
        }
        return valueEntityList;
    }

    private static void parseLink(SubSectionEntity subSectionEntity, String content)
    {
        if (TextUtils.isEmpty(content))
        {
            return;
        }
        String[] data = content.split("=");
        if (data.length == 2)
        {
            subSectionEntity.setLinkedStr(data[0]);
            subSectionEntity.setLinkedValueStr(data[1]);
        }
    }

    private static void relateLink(SectionEntity sectionEntity)
    {
        List<SubSectionEntity> subSectionList = sectionEntity.getSubSectionList();
        for (SubSectionEntity subSectionEntity : subSectionList)
        {
            String linkedStr = subSectionEntity.getLinkedStr();
            if (!TextUtils.isEmpty(linkedStr))
            {
                for (int i = 0; i < subSectionList.size(); i++)
                {
                    SubSectionEntity subSectionEntityTem = subSectionList.get(i);
                    if (subSectionEntity != subSectionEntityTem && linkedStr.equals(subSectionEntityTem.getName()))
                    {
                        subSectionEntityTem.addLinkSubSection(subSectionEntity);
                    }
                    List<ValueEntity> valueEntityList = subSectionEntityTem.getValueEntityList();
                    String linkedValue = subSectionEntity.getLinkedValueStr();
                    if (valueEntityList == null || valueEntityList.size() == 0)
                    {
                        continue;
                    }
                    for (ValueEntity valueEntity : valueEntityList)
                    {
                        if (linkedValue.equals(valueEntity.getName()))
                        {
                            subSectionEntityTem.addLinkValueEntity(valueEntity);
                            break;
                        }
                    }
                }
            }
        }
    }

}

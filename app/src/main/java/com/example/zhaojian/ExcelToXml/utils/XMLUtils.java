package com.example.zhaojian.ExcelToXml.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.example.zhaojian.ExcelToXml.DataManager;
import com.example.zhaojian.ExcelToXml.dataEntity.SectionEntity;
import com.example.zhaojian.ExcelToXml.dataEntity.SubSectionEntity;
import com.example.zhaojian.ExcelToXml.dataEntity.ValueEntity;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhaojian on 2017/12/25.
 */

public class XMLUtils
{
    public static boolean createXML(List<SectionEntity> sectionEntityList)
    {
        Element root = new Element("views");
        Document document = new Document(root);

        for (SectionEntity sectionEntity : sectionEntityList)
        {
            Element element = new Element("section");
            element.setAttribute("name", sectionEntity.getEnName())
                    .setAttribute("label", sectionEntity.getName());
            List<SubSectionEntity> subSectionEntityList = sectionEntity.getSubSectionList();
            for (SubSectionEntity subSectionEntity : subSectionEntityList)
            {
                Element subElement = new Element(DataManager.getIns().getXmlSectionName(subSectionEntity.getType()));
                subElement.setAttribute("name", subSectionEntity.getEnName())
                        .setAttribute("label", subSectionEntity.getName());
                if (!subSectionEntity.isEnable())
                {
                    subElement.setAttribute("enable", "false");
                }
                if (!subSectionEntity.isShow())
                {
                    subElement.setAttribute("show", "false");
                }

                List<SubSectionEntity> linkSubSectionList = subSectionEntity.getLinkList();
                if (linkSubSectionList.size() > 0)
                {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < linkSubSectionList.size(); i++)
                    {
                        SubSectionEntity linkSubSection = linkSubSectionList.get(i);
                        stringBuilder.append(linkSubSection.getEnName()+",");
                    }
                    String linkStr = stringBuilder.toString().substring(0, stringBuilder.length() - 1);

                    subElement.setAttribute("link", linkStr);
                }

                switch (subSectionEntity.getType())
                {
                    case SubSectionEntity.TYPE_INPUT_TXT:
                        if (subSectionEntity.getValueEntityList() != null && subSectionEntity.getValueEntityList().size() == 1)
                        {
                            subElement.setAttribute("unit", subSectionEntity.getValueEntityList().get(0).getName());
                        }
                        break;
                    case SubSectionEntity.TYPE_INPUT_NUMBER:
                        subElement.setAttribute("inputType", "number");
                        if (subSectionEntity.getValueEntityList() != null && subSectionEntity.getValueEntityList().size() == 1)
                        {
                            subElement.setAttribute("unit", subSectionEntity.getValueEntityList().get(0).getName());
                        }
                        break;
                    case SubSectionEntity.TYPE_RADIO:
                        List<ValueEntity> valueEntityList = subSectionEntity.getValueEntityList();
                        List<ValueEntity> linkValueEntityList = subSectionEntity.getLinkValueEntityList();
                        List<SubSectionEntity> linkSubSectionEntityList = subSectionEntity.getLinkList();

                        for (int i = 0; i < valueEntityList.size(); i++)
                        {
                            ValueEntity valueEntity = valueEntityList.get(i);
                            Element subSubElement = new Element("item");
                            subSubElement.setAttribute("name", valueEntity.getName())
                                    .setAttribute("code", valueEntity.getIndex() + "");
                            //if (subSectionEntity.get)
                            subElement.addContent(subSubElement);
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int j = 0; j < linkValueEntityList.size(); j++)
                            {
                                SubSectionEntity linkSubSectionEntity = linkSubSectionEntityList.get(j);
                                ValueEntity linkValueEntity = linkValueEntityList.get(j);
                                if (linkValueEntity.getName().equals(valueEntity.getName()))
                                {
                                    stringBuilder.append(linkSubSectionEntity.getEnName() + ",");
                                }
                            }
                            String result = stringBuilder.toString();
                            if (!TextUtils.isEmpty(result))
                            {
                                subSubElement.setAttribute("show", result.substring(0, result.length() - 1));
                            }
                        }
                        break;
                    case SubSectionEntity.TYPE_SELECT:
                        List<ValueEntity> valueEntityList1 = subSectionEntity.getValueEntityList();
                        for (ValueEntity valueEntity : valueEntityList1)
                        {
                            Element subSubElement = new Element("item");
                            subSubElement.setAttribute("name", valueEntity.getName())
                                    .setAttribute("code", valueEntity.getIndex() + "");
                            subElement.addContent(subSubElement);
                        }
                        break;
                    case SubSectionEntity.TYPE_DATE:
                        subElement.setAttribute("defaultValue", "today");
                        break;
                    default:
                        break;
                }

                element.addContent(subElement);
            }
            root.addContent(element);
        }
        Format format = Format.getPrettyFormat();
        XMLOutputter xmlOutputter = new XMLOutputter(format);
        try
        {
            xmlOutputter.output(document, new FileOutputStream(Environment.getExternalStorageDirectory() + "/xmlResult.xml"));
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

package com.example.zhaojian.ExcelToXml.utils;

import android.os.Environment;

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
                Element subElement;

                switch (subSectionEntity.getType())
                {
                    case SubSectionEntity.TYPE_INPUT_TXT:
                        subElement = new Element("input");
                        if (subSectionEntity.getValueEntityList() != null && subSectionEntity.getValueEntityList().size() == 1)
                        {
                            subElement.setAttribute("unit", subSectionEntity.getValueEntityList().get(0).getName());
                        }
                        break;
                    case SubSectionEntity.TYPE_INPUT_NUMBER:
                        subElement = new Element("input");
                        subElement.setAttribute("inputType", "number");
                        if (subSectionEntity.getValueEntityList() != null && subSectionEntity.getValueEntityList().size() == 1)
                        {
                            subElement.setAttribute("unit", subSectionEntity.getValueEntityList().get(0).getName());
                        }
                        break;
                    case SubSectionEntity.TYPE_RADIO:
                        subElement = new Element("radio");
                        List<ValueEntity> valueEntityList = subSectionEntity.getValueEntityList();
                        for (ValueEntity valueEntity : valueEntityList)
                        {
                            Element subSubElement = new Element("item");
                            subSubElement.setAttribute("name", valueEntity.getName())
                                    .setAttribute("code", valueEntity.getIndex() + "");
                            subElement.addContent(subSubElement);
                        }
                        break;
                    case SubSectionEntity.TYPE_SELECT:
                        subElement = new Element("select");
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
                        subElement = new Element("date");
                        subElement.setAttribute("defaultValue", "today");
                        break;
                    default:
                        subElement = new Element("unknow");
                        break;
                }
                subElement.setAttribute("name", subSectionEntity.getEnName())
                        .setAttribute("label", subSectionEntity.getName());
                element.addContent(subElement);
                if (!subSectionEntity.isEnable())
                {
                    subElement.setAttribute("enable", "false");
                }
                if (!subSectionEntity.isShow())
                {
                    subElement.setAttribute("show", "false");
                }
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

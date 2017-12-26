package com.example.zhaojian.ExcelToXml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.zhaojian.ExcelToXml.dataEntity.SectionEntity;
import com.example.zhaojian.ExcelToXml.utils.ExcelUtils;
import com.example.zhaojian.ExcelToXml.utils.XMLUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DataManager.getIns().initIdMap(MainActivity.this, "宜兴精神卫生22.xls");
                List<SectionEntity> sectionEntityList = ExcelUtils.parseExcel(MainActivity.this, "宜兴精神卫生22.xls");
                boolean success = XMLUtils.createXML(sectionEntityList);
            }
        });
    }

    private void readExcel()
    {
        jxl.Workbook readwb = null;
        try
        {
            InputStream inputStream = getAssets().open("Test1.xls");
            //InputStream inputStream = new FileInputStream("D:/Test1.xls");
            readwb = Workbook.getWorkbook(inputStream);
            Sheet readSheet = readwb.getSheet(0);
            int rsColumns = readSheet.getColumns();
            int rsRows = readSheet.getRows();
            for (int i = 0;i<rsRows;i++)
            {
                for (int j = 0;j<rsColumns;j++)
                {
                    Cell cell = readSheet.getCell(j, i);
                    System.out.print(cell.getContents() + "");
                }
                System.out.println();
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

    }
}

package nu.mine.kino.projects.utils;

/******************************************************************************
 * Copyright (c) 2010 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/

import static nu.mine.kino.projects.utils.PoiUtils.getDataFirstRowNum;
import static nu.mine.kino.projects.utils.PoiUtils.getDataLastRowNum;
import static nu.mine.kino.projects.utils.PoiUtils.getDate;
import static nu.mine.kino.projects.utils.PoiUtils.getHeaderIndex;
import static nu.mine.kino.projects.utils.PoiUtils.getTaskId;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Assert;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.bbreak.excella.core.util.PoiUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class POITest {

    FileInputStream in = null;

    Workbook workbook = null;

    // //////////////////////
    @Test
    public void test7() {
        System.out.println("--- test7 ---");
        Sheet sheet = workbook.getSheetAt(0);
        System.out.println(getHeaderIndex(sheet));
        System.out.println(getDataFirstRowNum(sheet));
        System.out.println(getDataLastRowNum(sheet));
        System.out.println("--- test7 ---");

    }

    // イナズマ線の基準日を取得する際、日付かどうかをチェックしている。
    @Test
    public void test3() throws InvalidFormatException, IOException {
        Sheet sheet = workbook.getSheetAt(0);
        Name name = workbook.getName("雷線基準日");
        CellReference cellRef = new CellReference(name.getRefersToFormula());
        Row row = sheet.getRow(cellRef.getRow());
        Cell baseDateCell = row.getCell(cellRef.getCol());
        System.out.println("cellが日付か:"
                + PoiUtil.isCellDateFormatted(baseDateCell));
        Date baseDate = baseDateCell.getDateCellValue();
        System.out.println(baseDate);
    }

    // 該当レンジでの最下行をさがす。
    @Test
    public void test4() throws InvalidFormatException, IOException {
        Sheet sheet = workbook.getSheetAt(0);
        System.out.println("最終行:" + PoiUtil.getLastRowNum(sheet, 0, 0));
    }

    // 数値関係の確認
    @Test
    public void test5() throws InvalidFormatException, IOException {
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> e = sheet.rowIterator();
        System.out.println("--- 数値系のテスト ---");
        while (e.hasNext()) {
            Row row = e.next();
            Cell taskIdCell = row.getCell(1);
            String taskId = getTaskId(taskIdCell);

            System.out.printf("[%s],[%s],[%s],[%s],[%s]\n", taskId,
                    row.getCell(15), row.getCell(22), row.getCell(23),
                    row.getCell(24));

        }
        System.out.println("--- 数値系のテスト ---");

    }

    // 日付関係の確認
    @Test
    public void test6() throws InvalidFormatException, IOException {
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> e = sheet.rowIterator();
        System.out.println("--- 日付系のテスト ---");
        int index=0;
        while (e.hasNext()) {
            Row row = e.next();
            Cell taskIdCell = row.getCell(1);
            String taskId = getTaskId(taskIdCell);

            Cell scheduledSDateCell = row.getCell(16);
            Date sDate = getDate(scheduledSDateCell);
            Cell scheduledEDateCell = row.getCell(17);
            Date eDate = getDate(scheduledEDateCell);

            String pattern = "yyyy/MM/dd";
            System.out.printf("[%s],[%s],[%s]\n", taskId,
                    Utils.date2Str(sDate, pattern),
                    Utils.date2Str(eDate, pattern));

            index++;
        }
        System.out.println(index);
        System.out.println("--- 日付系のテスト ---");

    }

    // @Test
    public void test2() throws InvalidFormatException, IOException {
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> e = sheet.rowIterator();
        while (e.hasNext()) {
            Row row = e.next();
            Cell cell = row.getCell(0);

            if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
            } else {
                System.out.println(cell.getStringCellValue());
            }
        }

    }

    // とりあえずいじってみている。
    @Test
    public void test1() throws InvalidFormatException, IOException {
        Sheet sheet = workbook.getSheetAt(0);
        // int[] rowBreaks = sheet.getRowBreaks();

        Row row = sheet.getRow(25);
        Cell cell = row.getCell(24);
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_NUMERIC:
            System.out.println(cell.getNumericCellValue());
            break;
        case Cell.CELL_TYPE_STRING:
            System.out.println(cell.getStringCellValue());
            break;
        default:
            System.out.println("cellType=" + cell.getCellType());
            break;
        }
        System.out.println("value: " + cell);
        System.out.println(sheet.getRow(46).getCell(4));

        final int rowMax = sheet.getLastRowNum();
        System.out.println(rowMax);

    }

    public static void main(String[] args) {
        Workbook workbook = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(new java.io.File(
                    "project_management_tools.xls"));
            workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            // int[] rowBreaks = sheet.getRowBreaks();

            Row row = sheet.getRow(25);
            Cell cell = row.getCell(24);
            switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                System.out.println(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                System.out.println(cell.getStringCellValue());
                break;
            default:
                System.out.println("cellType=" + cell.getCellType());
                break;
            }
            System.out.println("value: " + cell);
            System.out.println(sheet.getRow(46).getCell(4));

            final int rowMin = sheet.getFirstRowNum();
            System.out.println(rowMin);
            final int rowMax = sheet.getLastRowNum();
            System.out.println(rowMax);

        } catch (InvalidFormatException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
        }

    }

    @Before
    public void before() {
        try {
            in = new FileInputStream(new java.io.File(
                    "project_management_tools.xls"));
            workbook = WorkbookFactory.create(in);
        } catch (FileNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (InvalidFormatException e) {
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void after() {
        if (in != null)
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail(e.getMessage());
            }
    }
}

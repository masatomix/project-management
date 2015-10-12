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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.bbreak.excella.core.util.PoiUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * POIの挙動を確認するただのサンプル。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class POI2Test {

    FileInputStream in = null;

    FileOutputStream out = null;

    Workbook workbook = null;

    // @Test
    public void test3() throws InvalidFormatException, IOException {

        // String range = "A1:C5";
        // CellRangeAddress address = CellRangeAddress.valueOf(range);
        // int firstRow = address.getFirstRow();
        // int lastRow = address.getLastRow();
        // int firstColumn = address.getFirstColumn();
        // int lastColumn = address.getLastColumn();
        // System.out.println(firstRow);
        // System.out.println(lastRow);
        // System.out.println(lastColumn);
        // System.out.println(firstColumn);

        // 名前指定でセルをを取得する
        Sheet sheet = workbook.getSheetAt(0);
        Name name = workbook.getName("雷線基準日");
        CellReference cellRef = new CellReference(name.getRefersToFormula());

        Row row = sheet.getRow(cellRef.getRow());
        Cell baseDateCell = row.getCell(cellRef.getCol());
        System.out.println("cellが日付か:"
                + PoiUtil.isCellDateFormatted(baseDateCell));
        Date baseDate = baseDateCell.getDateCellValue();
        System.out.println(baseDate);
        System.out.println(baseDateCell.getNumericCellValue());

        // 名前指定でセルを取得する。
        final Name DATA_AREA = workbook.getName("DATA_AREA");
        final AreaReference areaReference = new AreaReference(
                DATA_AREA.getRefersToFormula());
        final CellReference firstCell = areaReference.getFirstCell();
        final CellReference lastCell = areaReference.getLastCell();

        Row fRow = sheet.getRow(firstCell.getRow());
        Row lRow = sheet.getRow(lastCell.getRow());

        System.out.printf("先頭行Index %s\n", firstCell.getRow());
        System.out.printf("最終行Index %s\n", lastCell.getRow());

        Cell fCell = fRow.getCell(firstCell.getCol());
        Cell lCell = lRow.getCell(lastCell.getCol());
        System.out.println(fCell);
        System.out.println(lCell);

        for (int index = firstCell.getRow(); index <= lastCell.getRow(); index++) {
            Row tmpRow = sheet.getRow(index);
            Cell tmpCell = tmpRow.getCell(firstCell.getCol()); // ココで指定するColumnNumberがわからん。
            System.out.println(tmpCell);
        }

    }

    // @Test
    public void test4() throws InvalidFormatException, IOException {

        // 名前指定でセルをを取得する
        Sheet sheet = workbook.getSheetAt(0);
        Name name = workbook.getName("雷線基準日");
        CellReference cellRef = new CellReference(name.getRefersToFormula());

        Row row = sheet.getRow(cellRef.getRow());
        Cell baseDateCell = row.getCell(cellRef.getCol());
        System.out.println("cellが日付か:"
                + PoiUtil.isCellDateFormatted(baseDateCell));
        Date baseDate = baseDateCell.getDateCellValue();
        System.out.println(baseDate);
        System.out.println(baseDateCell.getNumericCellValue());

        // 名前指定でセルを取得する。
        final Name DATA_AREA = workbook.getName("DATA_AREA");
        final AreaReference areaReference = new AreaReference(
                DATA_AREA.getRefersToFormula());
        final CellReference firstCell = areaReference.getFirstCell();
        final CellReference lastCell = areaReference.getLastCell();

        Row fRow = sheet.getRow(firstCell.getRow());
        Row lRow = sheet.getRow(lastCell.getRow());

        System.out.printf("先頭行Index %s\n", firstCell.getRow());
        System.out.printf("最終行Index %s\n", lastCell.getRow());

        Cell fCell = fRow.getCell(firstCell.getCol());
        Cell lCell = lRow.getCell(lastCell.getCol());
        System.out.println(fCell);
        System.out.println(lCell);

        for (int index = firstCell.getRow(); index <= lastCell.getRow(); index++) {
            Row tmpRow = sheet.getRow(index);
            Cell tmpCell = tmpRow.getCell(firstCell.getCol()); // ココで指定するColumnNumberがわからん。
            tmpCell.setCellValue(index * 100);
            System.out.println(tmpCell);
        }

        try {
            out = new FileOutputStream(new java.io.File("testdata4.xls"));
            workbook.write(out);
        } catch (FileNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    // @Test
    public void test5() throws InvalidFormatException, IOException {

        // 名前指定でセルをを取得する
        Sheet sheet = workbook.getSheetAt(1);
        final Name DATA_AREA = workbook.getName("DATA_AREA2");
        final AreaReference areaReference = new AreaReference(
                DATA_AREA.getRefersToFormula());
        final CellReference firstCell = areaReference.getFirstCell();
        final CellReference lastCell = areaReference.getLastCell();

        Row fRow = sheet.getRow(firstCell.getRow());
        Row lRow = sheet.getRow(lastCell.getRow());

        System.out.printf("先頭行Index %s\n", firstCell.getRow());
        System.out.printf("最終行Index %s\n", lastCell.getRow());

        Cell fCell = fRow.getCell(firstCell.getCol());
        Cell lCell = lRow.getCell(lastCell.getCol());
        System.out.println(fCell);
        System.out.println(lCell);

        for (int index = firstCell.getRow(); index <= lastCell.getRow(); index++) {
            Row tmpRow = sheet.getRow(index);
            Cell tmpCell = tmpRow.getCell(firstCell.getCol()); // ココで指定するColumnNumberがわからん。
            tmpCell.setCellValue(index * 100);
            System.out.println(tmpCell);
        }
        sheet.shiftRows(lastCell.getRow(), lastCell.getRow(), 3);

        try {
            out = new FileOutputStream(new java.io.File("testdata4.xls"));
            workbook.write(out);
        } catch (FileNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void test6() throws InvalidFormatException, IOException {
        int dataCount = 10;
        expandRange(workbook, "データシート", "DATA_PV", dataCount - 2);

        try {
            out = new FileOutputStream(new java.io.File("testdata6.xls"));
            workbook.write(out);
        } catch (FileNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    private void expandRange(Workbook workbook, String sheetName,
            String dataName, int dataCount) {

        // 名前指定でセルをを取得する
        Sheet sheet = workbook.getSheet(sheetName);
        final Name DATA_AREA = workbook.getName(dataName);
        final AreaReference areaReference = new AreaReference(
                DATA_AREA.getRefersToFormula());
        // final CellReference firstCell = areaReference.getFirstCell();
        final CellReference lastCell = areaReference.getLastCell();

        // System.out.printf("先頭行Index %s\n", firstCell.getRow());
        System.out.printf("最終行Index %s\n", lastCell.getRow());

        sheet.shiftRows(lastCell.getRow(), lastCell.getRow(), dataCount);
    }

    @Before
    public void before() {
        try {
            in = new FileInputStream(new java.io.File("testdata2.xls"));
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
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail(e.getMessage());
            }
        }

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail(e.getMessage());
            }
        }
    }
}

package com.banmatrip.alert.constant;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Miracle Xu on 2017/8/31.
 */
public class DownloadNotByTemplate {

    static private DownloadNotByTemplate downloadNotByTemplate = new DownloadNotByTemplate();

    private DownloadNotByTemplate() {
    }

    public static DownloadNotByTemplate getInstance() {
        return downloadNotByTemplate;
    }

     /**
      * data             =>      导出内容List集合
      * header           =>      表头集合,有则写,无则不写
      * sheetName        =>      Sheet索引名(默认0)
      * isXSSF           =>      是否Excel2007以上
      * targetPath       =>      导出文件路径
      * os               =>      导出文件流
      **/

    //传入参数：数据，表头，表名，文件格式,文件分色(true)
    public void exportObjectsToExcel(List<?> data, List<String> header, boolean isXSSF, OutputStream os,boolean color) throws Exception {

        exportExcelNoModuleHandler(data, header, null, isXSSF,color).write(os);
    }

    //传入参数：数据，表头，表名，文件格式,文件不分色
    public void exportObjectsToExcel(List<?> data, List<String> header, boolean isXSSF, OutputStream os) throws Exception {

        exportExcelNoModuleHandler(data, header, null, isXSSF).write(os);
    }

    //传入参数：数据，表头，表名，文件格式
    public void exportDailyDetailExcel(Map<String,List<Map<String,Object>>> data, List<String> header, String sheetNameDaily,String sheetNameMonth, boolean isXSSF,OutputStream os) throws Exception {

        exportExcelDaily(data, header, sheetNameDaily,sheetNameMonth,isXSSF).write(os);

     }

    //excel换行不分色
    private Workbook exportExcelNoModuleHandler(List<?> data, List<String> header, String sheetName, boolean isXSSF) throws Exception {

        Workbook workbook;
        //文件格式处理
        if (isXSSF) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
        Sheet sheet;
        //文件名
        if (null != sheetName && !"".equals(sheetName)) {
            sheet = workbook.createSheet(sheetName);
        } else {
            sheet = workbook.createSheet();
        }
        int rowIndex = 0;
        if (null != header && header.size() > 0) {
            // 写标题
            Row row = sheet.createRow(rowIndex);
            for (int i = 0; i < header.size(); i++) {
                row.createCell(i, Cell.CELL_TYPE_STRING).setCellValue(header.get(i));
            }
            rowIndex++;
        }
        //写入内容
        for (Object object : data) {
            Row row = sheet.createRow(rowIndex);
            if (object.getClass().isArray()) {
                for (int j = 0; j < Array.getLength(object); j++) {
                    row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue(Array.get(object, j).toString());
                }
            } else if ((object instanceof TreeMap)) {
                TreeMap<String,Object> items = (TreeMap) object;
                int j = 0;
                for (String key : items.keySet()){
                    String dataTmp = items.get(key) == null ? "" : items.get(key).toString();
                    row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue(dataTmp);
                    j++;
                }
            } else if ((object instanceof HashMap)) {
                HashMap<String,Object> items = (HashMap) object;
                int j = 0;
                for (String key : items.keySet()){
                    String tmp = items.get(key) == null ? "" : items.get(key).toString();
                    row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue(tmp);
                    j++;
                }
            } else {
                row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(object.toString());
            }
            sheet.autoSizeColumn((short)rowIndex);
            rowIndex++;
        }
        return workbook;
    }

    //excel换行分色
    private Workbook exportExcelNoModuleHandler(List<?> data, List<String> header, String sheetName, boolean isXSSF,boolean color) throws Exception {

        Workbook workbook = null;
        if(color) {  //确认excel换行分色
            //文件格式处理
            if (isXSSF) {
                workbook = new XSSFWorkbook();
            } else {
                workbook = new HSSFWorkbook();
            }
            //单元格背景
            CellStyle style1 = workbook.createCellStyle();
            style1.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style1.setBorderBottom((short) 1);
            style1.setBorderRight((short) 1);
            CellStyle style2 = workbook.createCellStyle();
            style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style2.setBorderBottom((short) 1);
            style2.setBorderRight((short) 1);

            Sheet sheet;
            //文件名
            if (null != sheetName && !"".equals(sheetName)) {
                sheet = workbook.createSheet(sheetName);
            } else {
                sheet = workbook.createSheet();
            }
            int rowIndex = 0;
            if (null != header && header.size() > 0) {
                // 写标题
                Row row = sheet.createRow(rowIndex);
                for (int i = 2; i < header.size(); i++) {
                    Cell cell = row.createCell(i-2, Cell.CELL_TYPE_STRING);
                    cell.setCellValue(header.get(i));
                    cell.setCellStyle(style1);
                    sheet.setColumnWidth(i-2, 5000);
                }
                rowIndex++;
            }

            //写入内容
            List<Integer> colorTmp = new ArrayList();
            CellStyle style = workbook.createCellStyle();
            for (Object object : data) {
                Row row = sheet.createRow(rowIndex);
                if (object.getClass().isArray()) {
                    for (int j = 0; j < Array.getLength(object); j++) {
                        Cell cell = row.createCell(j, Cell.CELL_TYPE_STRING);
                        cell.setCellValue(Array.get(object, j).toString());
                        cell.setCellStyle(style);
                    }
                } else if ((object instanceof TreeMap)) {
                    TreeMap<String, Object> items = (TreeMap) object;
                    int j = 0;
                    for (String key : items.keySet()) {
                        String dataTmp = items.get(key) == null ? "" : items.get(key).toString();
                        Cell cell = row.createCell(j, Cell.CELL_TYPE_STRING);
                        cell.setCellValue(dataTmp);
                        cell.setCellStyle(style);
                        j++;
                    }
                } else if ((object instanceof HashMap)) {
                    HashMap<String, Object> items = (HashMap) object;
                    int vision = 0;
                    if(null != items.get("vision_detail")) {
                        vision = Integer.parseInt(items.get("vision_detail").toString());//获取分析视角
                    }
                    if(colorTmp.size() == 0){
                        colorTmp.add(vision);
                        style = style2;
                    }else{
                        if(!colorTmp.contains(vision)){     //如果分析视角改变了，则更换excel底色
                            colorTmp.add(vision);
                            if(style == style1){
                                style = style2;
                            }else{
                                style = style1;
                            }
                        }
                    }
                    int j = 0;
                    for (String key : items.keySet()) {
                        if("vision_detail".equals(key) || "condition_detail".equals(key)){
                            continue;
                        }
                        String dataTmp = items.get(key) == null ? "" : items.get(key).toString();
                        Cell cell = row.createCell(j, Cell.CELL_TYPE_STRING);
                        cell.setCellValue(dataTmp);
                        cell.setCellStyle(style);
                        j++;
                    }
                } else {
                    Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
                    cell.setCellValue(object.toString());
                    cell.setCellStyle(style);
                }
                rowIndex++;
            }
        }
        return workbook;
    }


    /**
     * @Description: 销售日报详情表格下载
     * @author guoyong
     * @create 2017-10-30
     * @Copyright: 2017 www.banmatrip.com All rights reserved.
     **/
    private Workbook exportExcelDaily(Map<String,List<Map<String,Object>>> data, List<String> header, String sheetNameDaily,String sheetNameMonth, boolean isXSSF) throws Exception {

        Workbook workbook;
        //文件格式处理
        if (isXSSF) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
        Sheet sheetDaily;
        Sheet sheetMonth;
        //文件名
        if (null != sheetNameDaily && !"".equals(sheetNameDaily)) {
            sheetDaily = workbook.createSheet(sheetNameDaily);
        } else {
            sheetDaily = workbook.createSheet();
        }
        if (null != sheetNameMonth && !"".equals(sheetNameMonth)) {
            sheetMonth = workbook.createSheet(sheetNameMonth);
        } else {
            sheetMonth = workbook.createSheet();
        }
        int rowIndex = 0;
        int rowIndexMon = 0;
        if (null != header && header.size() > 0) {
            // 写标题
            Row row = sheetDaily.createRow(rowIndex);
            for (int i = 0; i < header.size(); i++) {
                row.createCell(i, Cell.CELL_TYPE_STRING).setCellValue(header.get(i));
            }
            rowIndex++;
        }
        if (null != header && header.size() > 0) {
            // 写标题
            Row row = sheetMonth.createRow(rowIndexMon);
            for (int i = 0; i < header.size(); i++) {
                row.createCell(i, Cell.CELL_TYPE_STRING).setCellValue(header.get(i));
            }
            rowIndexMon++;
        }
        //写入内容
        List<Map<String,Object>> dailyData = data.get("dailyData");
        List<Map<String,Object>> monthData = data.get("monthData");
        for (Object object : dailyData) {
            Row row = sheetDaily.createRow(rowIndex);
            if (object.getClass().isArray()) {
                for (int j = 0; j < Array.getLength(object); j++) {
                    row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue(Array.get(object, j).toString());
                }
            } else if ((object instanceof TreeMap)) {
                TreeMap<String,Object> items = (TreeMap) object;
                int j = 0;
                for (String key : items.keySet()){
                    row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue((String) items.get(key));
                    j++;
                }
            } else if ((object instanceof HashMap)) {
                HashMap<String,Object> items = (HashMap) object;
                int j = 0;
                for (String key : items.keySet()){
                    row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue((String) items.get(key));
                    j++;
                }
            } else {
                row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(object.toString());
            }
            sheetDaily.autoSizeColumn((short)rowIndex);
            rowIndex++;
        }
        for (Object object : monthData) {
            Row row = sheetMonth.createRow(rowIndexMon);
            if (object.getClass().isArray()) {
                for (int j = 0; j < Array.getLength(object); j++) {
                    row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue(Array.get(object, j).toString());
                }
            } else if ((object instanceof TreeMap)) {
                TreeMap<String,Object> items = (TreeMap) object;
                int j = 0;
                for (String key : items.keySet()){
                    row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue((String) items.get(key));
                    j++;
                }
            } else if ((object instanceof HashMap)) {
                HashMap<String,Object> items = (HashMap) object;
                int j = 0;
                for (String key : items.keySet()){
                    row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue((String) items.get(key));
                    j++;
                }
            } else {
                row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(object.toString());
            }
            sheetDaily.autoSizeColumn((short)rowIndexMon);
            rowIndexMon++;
        }
        return workbook;
    }
}

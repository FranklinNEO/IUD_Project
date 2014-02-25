package com.knight.excel;

import com.google.gson.Gson;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NEO on 13-12-13.
 */
public class ExcelToJson {

    private static ArrayList<String> readExcel(String url) {
        try {
            ArrayList<ReportInfo> infolist = new ArrayList<ReportInfo>();
            ArrayList<AreaInfo> arealist = new ArrayList<AreaInfo>();
            ArrayList<ConstructInfo> constructlist = new ArrayList<ConstructInfo>();
            ReportInfo reportinfo = null;
            AreaInfo areainfo = null;
            ConstructInfo constructinfo = null;
            InputStream is = new FileInputStream(url);
            Workbook rwb = Workbook.getWorkbook(is);
            //获得总的Sheets，得到sheet的层数
            Sheet[] sheets = rwb.getSheets();
            int sheetLen = sheets.length;
            //获得第一个Sheets 的结果
            for (int index = 0; index < sheetLen; index++) {
                jxl.Sheet rs = rwb.getSheet(index);
                int num_row = rs.getRows();//得到行数
                int num_column = rs.getColumns();//得到列数
                if (num_row != 0 && num_column != 0) {
                    for (int j = 0; j < num_row; j++) {
                        Cell[] cell = rs.getRow(j);//得到第j行的所有值
                        for (int column_index = 0; column_index < num_column; column_index++) {
                            String value = cell[column_index].getContents();//得到第j行，第column_indexs列的值
                            System.out.print("" + "" + value + "   ");
                        }
                        switch (index) {
                            case 0:
                                reportinfo = new ReportInfo(
                                        rs.getRow(j)[4].getContents().substring(0, 2),
                                        rs.getRow(j)[4].getContents().substring(2, 4),
                                        rs.getRow(j)[4].getContents().substring(4, 6),
                                        (rs.getRow(j)[3].getContents().trim().equals("") ? "0000" : rs.getRow(j)[4].getContents().substring(6, 10)),
                                        rs.getRow(j)[4].getContents(),
                                        rs.getRow(j)[5].getContents(),
                                        rs.getRow(j)[6].getContents(),
                                        rs.getRow(j)[7].getContents(),
                                        rs.getRow(j)[8].getContents(),
                                        rs.getRow(j)[9].getContents(),
                                        rs.getRow(j)[10].getContents());
                                infolist.add(reportinfo);
                                break;
                            case 1:
                                areainfo = new AreaInfo(
                                        rs.getRow(j)[0].getContents(),
                                        rs.getRow(j)[1].getContents(),
                                        rs.getRow(j)[2].getContents(),
                                        rs.getRow(j)[3].getContents(),
                                        rs.getRow(j)[0].getContents().substring(0, 2),
                                        rs.getRow(j)[0].getContents().substring(2, 4),
                                        rs.getRow(j)[0].getContents().substring(4, 6),
                                        rs.getRow(j)[4].getContents(),
                                        (rs.getRow(j)[5].getContents().trim().equals("")) ? "0000" : rs.getRow(j)[5].getContents(),
                                        (rs.getRow(j)[6].getContents().trim().equals("")) ? "000000" : rs.getRow(j)[6].getContents(),
                                        rs.getRow(j)[7].getContents());

                                arealist.add(areainfo);
                                break;
                            case 2:
                                String id;
                                if ((rs.getRow(j)[2].getContents().length() == 9)) {
                                    id = rs.getRow(j)[3].getContents().substring(0, 4);
                                } else if ((rs.getRow(j)[2].getContents().length() == 4)) {
                                    id = rs.getRow(j)[3].getContents().substring(0, 4);
                                } else {
                                    id = rs.getRow(j)[3].getContents().substring(rs.getRow(j)[3].getContents().length() - 4, rs.getRow(j)[3].getContents().length());
                                }
                                constructinfo = new ConstructInfo(
                                        id,
                                        rs.getRow(j)[1].getContents(),
                                        rs.getRow(j)[2].getContents(),
                                        rs.getRow(j)[3].getContents(),
                                        rs.getRow(j)[4].getContents());
                                constructlist.add(constructinfo);
                                break;
                            default:
                                break;
                        }
                        System.out.println("");
                    }
                    System.out.println("");
                }


            }
            Gson gson = new Gson();
            String reportinfodata = gson.toJson(infolist);
            System.out.println(reportinfodata);
            String areainfodata = gson.toJson(arealist);
            System.out.println(areainfodata);
            String constructinfodata = gson.toJson(constructlist);
            System.out.println(constructinfodata);
            ArrayList<String> result = new ArrayList<String>();
            result.add(reportinfodata);
            result.add(areainfodata);
            result.add(constructinfodata);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            ArrayList<String> data = readExcel("d://RedInfo/IUD/Report/天津服务站/天津.xls");
            FileOutputStream report = new FileOutputStream("d://RedInfo/IUD/Report/天津服务站/report_data.txt", true);
            String reportstr = data.get(0);
            report.write(reportstr.getBytes());
            report.close();

            FileOutputStream area = new FileOutputStream("d://RedInfo/IUD/Report/天津服务站/area_data.txt", true);
            String areastr = data.get(1);
            area.write(areastr.getBytes());
            area.close();

            FileOutputStream construct = new FileOutputStream("d://RedInfo/IUD/Report/天津服务站/construct_data.txt", true);
            String constructstr = data.get(2);
            construct.write(constructstr.getBytes());
            construct.close();
        } catch (Exception e) {

        }
    }
}

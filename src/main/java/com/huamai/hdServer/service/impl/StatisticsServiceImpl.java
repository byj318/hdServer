package com.huamai.hdServer.service.impl;

import com.huamai.hdServer.mapper.HistoryDialyseRecordMapper;
import com.huamai.hdServer.service.StatisticsService;
import com.huamai.hdServer.util.DownloadUtil;
import com.huamai.hdServer.util.PoiUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: 旭燃
 * @Date: 2019/9/17 09:25
 * @Description:
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private HistoryDialyseRecordMapper historyDialyseRecordMapper;

    @Override
    public Map<String, Object> statisticsBfz(String startTime, String endTime, String courtyardArea) {
        List<String> stringList = historyDialyseRecordMapper.statisticsBfz(startTime, endTime, courtyardArea);
        List<String> result = new ArrayList<>();
        stringList.forEach(e -> {
            String[] split = e.split(",");
            result.addAll(Arrays.asList(split));
        });
        Map<String, Long> collect = result.stream().collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        List<String> dictoryList = historyDialyseRecordMapper.getDictory();
        Map<String, Object> resultMap = new HashMap<>();
        dictoryList.forEach(e -> {
            resultMap.put(e, 0L);
        });
        resultMap.put("其他", 0L);

        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            String key = entry.getKey();
            Long value = Long.parseLong(entry.getValue().toString());
            Iterator<Map.Entry<String, Long>> iterator = collect.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry2 = iterator.next();
                String key1 = entry2.getKey();
                if (StringUtils.isEmpty(key1)) {
                    iterator.remove();
                }
                Long value1 = entry2.getValue();
                if (key1.contains(key)) {
                    value += value1;
                    iterator.remove();
                }
            }
            if (value == 0) {
                resultMap.put(key, value);
            } else {
                resultMap.put(key, "<a style=\"color: blue;\" onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color='blue'\" href=\"javascript:void(0)\" onclick=\"getPatientInfo('" + key + "');\">" + value + "</a>");
            }
        }
        long sum = collect.values().stream().mapToLong(Long::intValue).sum();
        if (sum > 0) {
            resultMap.put("其他", "<a style=\"color: blue;\" onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color='blue'\" href=\"javascript:void(0)\" onclick=\"getPatientInfo('其他');\">" + sum + "</a>");
        } else {
            resultMap.put("其他", 0);
        }
        //resultMap.put("其他",sum);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("recordsTotal", 1);
        List<Map<String, Object>> resList = new ArrayList<>();
        resList.add(resultMap);
        resMap.put("data", resList);
        return resMap;
    }

    @Override
    public List<Map<String, String>> getTableHeader() {
        List<Map<String, String>> dictoryToTableHeader = historyDialyseRecordMapper.getDictoryToTableHeader();
        Map<String, String> qtMap = new HashMap<>();
        qtMap.put("data", "其他");
        qtMap.put("title", "其他");
        dictoryToTableHeader.add(qtMap);
        return dictoryToTableHeader;
    }

    @Override
    public Map<String, Object> listPatientInfo(String startTime, String endTime, String courtyardArea, String type) {
        List<Map<String, Object>> maps;
        if ("其他".equals(type)) {
            List<String> dictoryList = historyDialyseRecordMapper.getDictory();
            maps = historyDialyseRecordMapper.listPatientInfo(startTime, endTime, courtyardArea, type, dictoryList);
        } else {
            maps = historyDialyseRecordMapper.listPatientInfo(startTime, endTime, courtyardArea, type, null);
        }

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("recordsTotal", maps.size());
        resMap.put("data", maps);
        return resMap;
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
                            String courtyardArea) {
        HSSFWorkbook tpWorkbook = null;
        HSSFWorkbook workBook = null;
        ByteArrayOutputStream outputStream = null;
        try {
            workBook = new HSSFWorkbook();
            CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
            Sheet sheet = workBook.createSheet();
            String courtyardAreaStr = "";
            if ("0".equals(courtyardArea)) {
                courtyardAreaStr = "全院";
            } else if ("1".equals(courtyardArea)) {
                courtyardAreaStr = "新院";
            } else if ("2".equals(courtyardArea)) {
                courtyardAreaStr = "老院";
            }
            workBook.setSheetName(0, courtyardAreaStr + "并发症统计");
            List<String> stringList = historyDialyseRecordMapper.statisticsBfz(startTime, endTime, courtyardArea);
            List<String> result = new ArrayList<>();
            stringList.forEach(e -> {
                String[] split = e.split(",");
                result.addAll(Arrays.asList(split));
            });
            Map<String, Long> collect = result.stream().collect(Collectors.groupingBy(String::toString, Collectors.counting()));
            List<String> dictoryList = historyDialyseRecordMapper.getDictory();
            Map<String, Object> resultMap = new HashMap<>();
            dictoryList.forEach(e -> {
                resultMap.put(e, 0L);
            });
            resultMap.put("其他", 0L);

            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                Long value = Long.parseLong(entry.getValue().toString());
                Iterator<Map.Entry<String, Long>> iterator = collect.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Long> entry2 = iterator.next();
                    String key1 = entry2.getKey();
                    if (StringUtils.isEmpty(key1)) {
                        iterator.remove();
                    }
                    Long value1 = entry2.getValue();
                    if (key1.contains(key)) {
                        value += value1;
                        iterator.remove();
                    }
                }
                resultMap.put(key, value);
            }
            long sum = collect.values().stream().mapToLong(Long::intValue).sum();
            resultMap.put("其他", sum);

            Row row;
            Cell cell;
            row = sheet.createRow(0);
            int i = 0;
            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                cell = row.createCell(i++);
                cell.setCellStyle(hcs);
                cell.setCellValue(entry.getKey());
            }
            i = 0;
            row = sheet.createRow(1);
            CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                cell = row.createCell(i++);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(entry.getValue().toString());
            }
            outputStream = new ByteArrayOutputStream();

            workBook.write(outputStream);
            DownloadUtil downloadUtil = new DownloadUtil();
            downloadUtil.download(outputStream, response, courtyardAreaStr + "并发症统计" + ".xls", request.getHeader("user-agent"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (tpWorkbook != null) {
                try {
                    tpWorkbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void secondExportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea, String type) {
        HSSFWorkbook tpWorkbook = null;
        HSSFWorkbook workBook = null;
        ByteArrayOutputStream outputStream = null;
        try {
            workBook = new HSSFWorkbook();
            CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
            Sheet sheet = workBook.createSheet();
            String courtyardAreaStr = "";
            if ("0".equals(courtyardArea)) {
                courtyardAreaStr = "全院";
            } else if ("1".equals(courtyardArea)) {
                courtyardAreaStr = "新院";
            } else if ("2".equals(courtyardArea)) {
                courtyardAreaStr = "老院";
            }
            workBook.setSheetName(0, courtyardAreaStr + "并发症-" + type.replace("/", "") + "统计");
            List<Map<String, Object>> maps;
            if ("其他".equals(type)) {
                List<String> dictoryList = historyDialyseRecordMapper.getDictory();
                maps = historyDialyseRecordMapper.listPatientInfo(startTime, endTime, courtyardArea, type, dictoryList);
            } else {
                maps = historyDialyseRecordMapper.listPatientInfo(startTime, endTime, courtyardArea, type, null);
            }
            Row row;
            Cell cell;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellStyle(hcs);
            cell.setCellValue("姓名");
            cell = row.createCell(1);
            cell.setCellStyle(hcs);
            cell.setCellValue("性别");
            cell = row.createCell(2);
            cell.setCellStyle(hcs);
            cell.setCellValue("身份证");
            cell = row.createCell(3);
            cell.setCellStyle(hcs);
            cell.setCellValue("病历本号");
            cell = row.createCell(4);
            cell.setCellStyle(hcs);
            cell.setCellValue("并发症");
            cell = row.createCell(5);
            cell.setCellStyle(hcs);
            cell.setCellValue("时间");
            CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
            for (int i = 0; i < maps.size(); i++) {
                Map<String, Object> data = maps.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("name").toString());

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("gender").toString());

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("IdentityCard").toString());

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("CaseCode").toString());

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("SYMPTOM").toString());

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("CureTime").toString());
            }
            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 5000);
            sheet.setColumnWidth(2, 5000);
            sheet.setColumnWidth(3, 5000);
            sheet.setColumnWidth(4, 5000);
            sheet.setColumnWidth(5, 5000);
            outputStream = new ByteArrayOutputStream();

            workBook.write(outputStream);
            DownloadUtil downloadUtil = new DownloadUtil();
            downloadUtil.download(outputStream, response, courtyardAreaStr + "并发症-" + type.replace("/", "") + "统计" + ".xls", request.getHeader("user-agent"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (tpWorkbook != null) {
                try {
                    tpWorkbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public Map<String, Object> statisticsYsjb(String startTime, String endTime, String courtyardArea) {
        List<Map<String, Object>> maps = historyDialyseRecordMapper.statisticsYsjb(startTime, endTime, courtyardArea);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("recordsTotal", maps.size());
        resMap.put("data", maps);
        return resMap;
    }

    @Override
    public Map<String, Object> statisticsHsjb(String startTime, String endTime, String courtyardArea) {
        List<Map<String, Object>> maps = historyDialyseRecordMapper.statisticsHsjb(startTime, endTime, courtyardArea);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("recordsTotal", maps.size());
        resMap.put("data", maps);
        return resMap;
    }

    @Override
    public void exportExcelYsjb(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea) {
        HSSFWorkbook tpWorkbook = null;
        HSSFWorkbook workBook = null;
        ByteArrayOutputStream outputStream = null;
        try {
            workBook = new HSSFWorkbook();
            CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
            Sheet sheet = workBook.createSheet();
            String courtyardAreaStr = "";
            if ("0".equals(courtyardArea)) {
                courtyardAreaStr = "全院";
            } else if ("1".equals(courtyardArea)) {
                courtyardAreaStr = "新院";
            } else if ("2".equals(courtyardArea)) {
                courtyardAreaStr = "老院";
            }
            workBook.setSheetName(0, courtyardAreaStr + "医生交班");
            List<Map<String, Object>> maps = historyDialyseRecordMapper.statisticsYsjb(startTime, endTime, courtyardArea);
            Row row;
            Cell cell;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellStyle(hcs);
            cell.setCellValue("姓名");
            cell = row.createCell(1);
            cell.setCellStyle(hcs);
            cell.setCellValue("性别");
            cell = row.createCell(2);
            cell.setCellStyle(hcs);
            cell.setCellValue("身份证");
            cell = row.createCell(3);
            cell.setCellStyle(hcs);
            cell.setCellValue("病历本号");
            cell = row.createCell(4);
            cell.setCellStyle(hcs);
            cell.setCellValue("诊断");
            cell = row.createCell(5);
            cell.setCellStyle(hcs);
            cell.setCellValue("病情情况");
            cell = row.createCell(6);
            cell.setCellStyle(hcs);
            cell.setCellValue("时间");
            cell = row.createCell(7);
            cell.setCellStyle(hcs);
            cell.setCellValue("医生签名");
            CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
            for (int i = 0; i < maps.size(); i++) {
                Map<String, Object> data = maps.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("name").toString());

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("gender").toString());

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("IdentityCard").toString());

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("CaseCode").toString());

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("DefineName").toString());

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("YiShengZongJie").toString());

                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("setuptime").toString());

                cell = row.createCell(7);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("Yisheng").toString());
            }
            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 5000);
            sheet.setColumnWidth(2, 5000);
            sheet.setColumnWidth(3, 5000);
            sheet.setColumnWidth(4, 35000);
            sheet.setColumnWidth(5, 35000);
            sheet.setColumnWidth(6, 5000);
            sheet.setColumnWidth(7, 5000);
            outputStream = new ByteArrayOutputStream();

            workBook.write(outputStream);
            DownloadUtil downloadUtil = new DownloadUtil();
            downloadUtil.download(outputStream, response, courtyardAreaStr + "医生交班.xls", request.getHeader("user-agent"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (tpWorkbook != null) {
                try {
                    tpWorkbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void exportExcelHsjb(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea) {
        HSSFWorkbook workBook = null;
        ByteArrayOutputStream outputStream = null;
        try {
            workBook = new HSSFWorkbook();
            CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
            Sheet sheet = workBook.createSheet();
            String courtyardAreaStr = "";
            if ("0".equals(courtyardArea)) {
                courtyardAreaStr = "全院";
            } else if ("1".equals(courtyardArea)) {
                courtyardAreaStr = "新院";
            } else if ("2".equals(courtyardArea)) {
                courtyardAreaStr = "老院";
            }
            workBook.setSheetName(0, courtyardAreaStr + "护士交班");
            List<Map<String, Object>> maps = historyDialyseRecordMapper.statisticsHsjb(startTime, endTime, courtyardArea);
            Row row;
            Cell cell;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellStyle(hcs);
            cell.setCellValue("姓名");
            cell = row.createCell(1);
            cell.setCellStyle(hcs);
            cell.setCellValue("性别");
            cell = row.createCell(2);
            cell.setCellStyle(hcs);
            cell.setCellValue("身份证");
            cell = row.createCell(3);
            cell.setCellStyle(hcs);
            cell.setCellValue("病历本号");
            cell = row.createCell(4);
            cell.setCellStyle(hcs);
            cell.setCellValue("诊断");
            cell = row.createCell(5);
            cell.setCellStyle(hcs);
            cell.setCellValue("病情情况");
            cell = row.createCell(6);
            cell.setCellStyle(hcs);
            cell.setCellValue("时间");
            cell = row.createCell(7);
            cell.setCellStyle(hcs);
            cell.setCellValue("护士签名");
            CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
            for (int i = 0; i < maps.size(); i++) {
                Map<String, Object> data = maps.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("name").toString());

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("gender").toString());

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("IdentityCard").toString());

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("CaseCode").toString());

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("DefineName").toString());

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("HuShiZongJie").toString());

                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("setuptime").toString());

                cell = row.createCell(7);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.get("ZHushi").toString());
            }
            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 5000);
            sheet.setColumnWidth(2, 5000);
            sheet.setColumnWidth(3, 5000);
            sheet.setColumnWidth(4, 35000);
            sheet.setColumnWidth(5, 35000);
            sheet.setColumnWidth(6, 5000);
            sheet.setColumnWidth(7, 5000);
            outputStream = new ByteArrayOutputStream();

            workBook.write(outputStream);
            DownloadUtil downloadUtil = new DownloadUtil();
            downloadUtil.download(outputStream, response, courtyardAreaStr + "护士交班.xls", request.getHeader("user-agent"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

package com.huamai.hdServer.service.impl;

import com.huamai.hdServer.domain.Gjzb;
import com.huamai.hdServer.domain.GjzbPatientTj;
import com.huamai.hdServer.domain.Gjzbtj;
import com.huamai.hdServer.domain.Xgtl;
import com.huamai.hdServer.enums.CourtyardAreaEnum;
import com.huamai.hdServer.enums.HospitalEnum;
import com.huamai.hdServer.mapper.GjzbtjMapper;
import com.huamai.hdServer.service.GjzbtjService;
import com.huamai.hdServer.util.DataUtil;
import com.huamai.hdServer.util.DateUtil;
import com.huamai.hdServer.util.DownloadUtil;
import com.huamai.hdServer.util.PoiUtil;
import com.huamai.hdServer.vo.GjzbtjVO;
import com.huamai.hdServer.vo.ResultVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 关键指标统计 业务逻辑层
 *
 * @author bbd
 */
@Service
public class GjzbtjServiceImpl implements GjzbtjService {
    @Autowired
    private GjzbtjMapper gjzbtjMapper;

    /**
     * 分页查询 透析液电解质监测记录 page : 起始数据索引 非页数 size 每页数据大小
     */
    public ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea, HttpServletRequest request, String loginName) throws Exception {

        LinkedBlockingQueue<Runnable> objects = new LinkedBlockingQueue<Runnable>(7);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(7, 7, 3L, TimeUnit.SECONDS, objects);
        executorService.prestartAllCoreThreads();
        GjzbtjVO gjzbtjVO = new GjzbtjVO();
        //血管通路
        Future<Gjzbtj> xgtlFuture = executorService.submit(() -> {
            Integer index = 0;
            if (CourtyardAreaEnum.NEW_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.NEW_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.OLD_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.OLD_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.FULL_DISTRICT.getValue();
            }
            List<Xgtl> gjzbs = gjzbtjMapper.listXgtls2(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), index);
            if (gjzbs.size() == 0) {
                return new Gjzbtj("0", "0", "0", "0");
            }
            List<String> xgtlZsList = gjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("xgtlZsList" + courtyardArea + loginName, xgtlZsList);
            //达标数据
            List<Xgtl> dbGjzbs = gjzbs.stream().filter((e) -> {
                return e.getVal().contains("内瘘") || e.getVal().contains("內瘘") || e.getVal().contains("AVF") || e.getVal().contains("移植");
            }).collect(Collectors.toList());
            List<String> xgtlDbsList = dbGjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("xgtlDbsList" + courtyardArea + loginName, xgtlDbsList);

            //达标率
            String bfb = DataUtil.formatPercentage(dbGjzbs.size() * 1.0 / gjzbs.size());
            return new Gjzbtj(dbGjzbs.size() + "", gjzbs.size() + "", null, bfb + "");
        });
        //血红蛋白
        Future<Gjzbtj> xhdbFuture = executorService.submit(() -> {
            Integer index = 0;
            if (CourtyardAreaEnum.NEW_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.NEW_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.OLD_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.OLD_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.FULL_DISTRICT.getValue();
            }
            //总数据
            List<Gjzb> gjzbs = gjzbtjMapper.listObjects2(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), 9.0, 1000.0, 178, index);
            if (gjzbs.size() == 0) {
                return new Gjzbtj("0", "0", "0", "0");
            }
            List<String> xhdbZsList = gjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("xhdbZsList" + courtyardArea + loginName, xhdbZsList);
            //达标数据
            List<Gjzb> dbGjzbs = gjzbs.stream().filter((e) -> {
                return e.getVal() >= 100;
            }).collect(Collectors.toList());
            List<String> xhdbDbsList = dbGjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("xhdbDbsList" + courtyardArea + loginName, xhdbDbsList);
            //求平均值
            DoubleSummaryStatistics doubleSummaryStatistics = gjzbs.stream().mapToDouble((e) -> e.getVal()).summaryStatistics();
            String average = DataUtil.formatDouble(doubleSummaryStatistics.getAverage());
            //达标率
            String bfb = DataUtil.formatPercentage(dbGjzbs.size() * 1.0 / gjzbs.size());
            return new Gjzbtj(dbGjzbs.size() + "", gjzbs.size() + "", average, bfb + "");
        });
        //白蛋白
        Future<Gjzbtj> bdbFuture = executorService.submit(() -> {
            Integer index = 0;
            if (CourtyardAreaEnum.NEW_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.NEW_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.OLD_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.OLD_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.FULL_DISTRICT.getValue();
            }
            List<Gjzb> gjzbs = gjzbtjMapper.listObjects2(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), 5.0, 70.0, 154, index);
            if (gjzbs.size() == 0) {
                return new Gjzbtj("0", "0", "0", "0");
            }
            List<String> bdbZsList = gjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("bdbZsList" + courtyardArea + loginName, bdbZsList);
            //达标数据
            List<Gjzb> dbGjzbs = gjzbs.stream().filter((e) -> {
                return e.getVal() >= 40;
            }).collect(Collectors.toList());
            List<String> bdbDbsList = dbGjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("bdbDbsList" + courtyardArea + loginName, bdbDbsList);

            //求平均值
            DoubleSummaryStatistics doubleSummaryStatistics = gjzbs.stream().mapToDouble((e) -> e.getVal()).summaryStatistics();
            String average = DataUtil.formatDouble(doubleSummaryStatistics.getAverage());
            //达标率
            String bfb = DataUtil.formatPercentage(dbGjzbs.size() * 1.0 / gjzbs.size());
            return new Gjzbtj(dbGjzbs.size() + "", gjzbs.size() + "", average, bfb + "");
        });
        //血钙
        Future<Gjzbtj> caFuture = executorService.submit(() -> {
            Integer index = 0;
            if (CourtyardAreaEnum.NEW_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.NEW_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.OLD_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.OLD_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.FULL_DISTRICT.getValue();
            }
            List<Gjzb> gjzbs = gjzbtjMapper.listObjects2(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), 0.1, 10.0, 410, index);
            if (gjzbs.size() == 0) {
                return new Gjzbtj("0", "0", "0", "0");
            }
            List<String> caZsList = gjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("caZsList" + courtyardArea + loginName, caZsList);
            //达标数据
            List<Gjzb> dbGjzbs = gjzbs.stream().filter((e) -> {
                return e.getVal() >= 2.1 && e.getVal() <= 2.54;
            }).collect(Collectors.toList());
            List<String> caDbsList = dbGjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("caDbsList" + courtyardArea + loginName, caDbsList);
            //求平均值
            DoubleSummaryStatistics doubleSummaryStatistics = gjzbs.stream().mapToDouble((e) -> e.getVal()).summaryStatistics();
            String average = DataUtil.formatDouble(doubleSummaryStatistics.getAverage());
            //达标率
            String bfb = DataUtil.formatPercentage(dbGjzbs.size() * 1.0 / gjzbs.size());
            return new Gjzbtj(dbGjzbs.size() + "", gjzbs.size() + "", average, bfb + "");
        });
        //血磷
        Future<Gjzbtj> pFuture = executorService.submit(() -> {
            Integer index = 0;
            if (CourtyardAreaEnum.NEW_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.NEW_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.OLD_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.OLD_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.FULL_DISTRICT.getValue();
            }
            List<Gjzb> gjzbs = gjzbtjMapper.listObjects2(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), 0.1, 10.0, 198, index);
            if (gjzbs.size() == 0) {
                return new Gjzbtj("0", "0", "0", "0");
            }
            List<String> pZsList = gjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("pZsList" + courtyardArea + loginName, pZsList);
            //达标数据
            List<Gjzb> dbGjzbs = gjzbs.stream().filter((e) -> {
                return e.getVal() >= 1.13 && e.getVal() <= 1.78;
            }).collect(Collectors.toList());
            List<String> pDbsList = dbGjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("pDbsList" + courtyardArea + loginName, pDbsList);

            //求平均值
            DoubleSummaryStatistics doubleSummaryStatistics = gjzbs.stream().mapToDouble((e) -> e.getVal()).summaryStatistics();
            String average = DataUtil.formatDouble(doubleSummaryStatistics.getAverage());
            //达标率
            String bfb = DataUtil.formatPercentage(dbGjzbs.size() * 1.0 / gjzbs.size());
            return new Gjzbtj(dbGjzbs.size() + "", gjzbs.size() + "", average, bfb + "");
        });
        //甲状旁腺激素
        Future<Gjzbtj> jzpxjsFuture = executorService.submit(() -> {
            Integer index = 0;
            if (CourtyardAreaEnum.NEW_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.NEW_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.OLD_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.OLD_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.FULL_DISTRICT.getValue();
            }
            List<Gjzb> gjzbs = gjzbtjMapper.listObjects2(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), null, 10000.0, 471, index);
            if (gjzbs.size() == 0) {
                return new Gjzbtj("0", "0", "0", "0");
            }
            List<String> jzpxjsZsList = gjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("jzpxjsZsList" + courtyardArea + loginName, jzpxjsZsList);
            //达标数据
            List<Gjzb> dbGjzbs = gjzbs.stream().filter((e) -> {
                return e.getVal() >= 100 && e.getVal() <= 600;
            }).collect(Collectors.toList());
            List<String> jzpxjsDbsList = dbGjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("jzpxjsDbsList" + courtyardArea + loginName, jzpxjsDbsList);
            //求平均值
            DoubleSummaryStatistics doubleSummaryStatistics = gjzbs.stream().mapToDouble((e) -> e.getVal()).summaryStatistics();
            String average = DataUtil.formatDouble(doubleSummaryStatistics.getAverage());
            //达标率
            String bfb = DataUtil.formatPercentage(dbGjzbs.size() * 1.0 / gjzbs.size());
            return new Gjzbtj(dbGjzbs.size() + "", gjzbs.size() + "", average, bfb + "");
        });
        //KT/V
        Future<Gjzbtj> ktvFuture = executorService.submit(() -> {
            Integer index = 0;
            if (CourtyardAreaEnum.NEW_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.NEW_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.OLD_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.OLD_DISTRICT.getValue();
            } else if (CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)) {
                index = HospitalEnum.FULL_DISTRICT.getValue();
            }
            List<Gjzb> gjzbs = gjzbtjMapper.listObjects2(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), null, 10.0, 308, index);
            if (gjzbs.size() == 0) {
                return new Gjzbtj("0", "0", "0", "0");
            }
            List<String> ktvZsList = gjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("ktvZsList" + courtyardArea + loginName, ktvZsList);
            //达标数据
            List<Gjzb> dbGjzbs = gjzbs.stream().filter((e) -> {
                return e.getVal() >= 1.2;
            }).collect(Collectors.toList());
            List<String> ktvDbsList = dbGjzbs.stream().map((e) -> e.getPatientId()).collect(Collectors.toList());
            request.getSession().setAttribute("ktvDbsList" + courtyardArea + loginName, ktvDbsList);
            //求平均值
            DoubleSummaryStatistics doubleSummaryStatistics = gjzbs.stream().mapToDouble((e) -> e.getVal()).summaryStatistics();
            String average = DataUtil.formatDouble(doubleSummaryStatistics.getAverage());
            //达标率
            String bfb = DataUtil.formatPercentage(dbGjzbs.size() * 1.0 / gjzbs.size());
            return new Gjzbtj(dbGjzbs.size() + "", gjzbs.size() + "", average, bfb + "");
        });
        Gjzbtj xgtlGjzbtj = xgtlFuture.get();
        Gjzbtj xhdbGjzbtj = xhdbFuture.get();
        Gjzbtj bdbGjzbtj = bdbFuture.get();
        Gjzbtj caGjzbtj = caFuture.get();
        Gjzbtj pGjzbtj = pFuture.get();
        Gjzbtj jzpxjsGjzbtj = jzpxjsFuture.get();
        Gjzbtj ktvGjzbtj = ktvFuture.get();
        gjzbtjVO.setXgtlDbs(xgtlGjzbtj.getA1()).setXgtlZs(xgtlGjzbtj.getA2()).setXgtlDbBfb(xgtlGjzbtj.getBfb());
        gjzbtjVO.setXhdbDbs(xhdbGjzbtj.getA1()).setXhdbZs(xhdbGjzbtj.getA2()).setXhdbPjs(xhdbGjzbtj.getA3()).setXhdbDbBfb(xhdbGjzbtj.getBfb());
        gjzbtjVO.setBdbDbs(bdbGjzbtj.getA1()).setBdbZs(bdbGjzbtj.getA2()).setBdbPjs(bdbGjzbtj.getA3()).setBdbDbBfb(bdbGjzbtj.getBfb());
        gjzbtjVO.setCaDbs(caGjzbtj.getA1()).setCaZs(caGjzbtj.getA2()).setCaPjs(caGjzbtj.getA3()).setCaDbBfb(caGjzbtj.getBfb());
        gjzbtjVO.setPDbs(pGjzbtj.getA1()).setPZs(pGjzbtj.getA2()).setPPjs(pGjzbtj.getA3()).setPDbBfb(pGjzbtj.getBfb());
        gjzbtjVO.setJzpxjsDbs(jzpxjsGjzbtj.getA1()).setJzpxjsZs(jzpxjsGjzbtj.getA2()).setJzpxjsPjs(jzpxjsGjzbtj.getA3()).setJzpxjsDbBfb(jzpxjsGjzbtj.getBfb());
        gjzbtjVO.setKtvDbs(ktvGjzbtj.getA1()).setKtvZs(ktvGjzbtj.getA2()).setKtvPjs(ktvGjzbtj.getA3()).setKtvDbBfb(ktvGjzbtj.getBfb());
        executorService.shutdown();
        List<GjzbtjVO> result = new ArrayList<>();
        result.add(gjzbtjVO);
        request.getSession().setAttribute("gjzbtjVO" + courtyardArea + loginName, gjzbtjVO);
        return new ResultVO(1L, result);
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
                            String courtyardArea, String loginName) {
        InputStream is = null;
        HSSFWorkbook tpWorkbook = null;
        HSSFWorkbook workBook = null;
        ByteArrayOutputStream outputStream = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream("static/excel/血透指标统计.xls");
            tpWorkbook = new HSSFWorkbook(is);
            workBook = new HSSFWorkbook();
            workBook = tpWorkbook;
            CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
            Sheet sheet = workBook.getSheetAt(0);
            workBook.setSheetName(0, courtyardArea + "血透关键指标质量统计");

            Row row;
            Cell cell;
            // GjzbtjVO gjzbtjVO = GjzbtjVO.class.cast(listAll(1, 1, startTime, endTime, courtyardArea).getData().get(0));
            GjzbtjVO gjzbtjVO = GjzbtjVO.class.cast(request.getSession().getAttribute("gjzbtjVO" + courtyardArea + loginName));
            CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
            row = sheet.createRow(3);

            cell = row.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getXgtlDbs());
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getXgtlZs());
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getXgtlDbBfb());

            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getXhdbDbs());
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getXhdbZs());
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getXhdbPjs());
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getXhdbDbBfb());

            cell = row.createCell(7);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getBdbDbs());
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getBdbZs());
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getBdbPjs());
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getBdbDbBfb());

            cell = row.createCell(11);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getCaDbs());
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getCaZs());
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getCaPjs());
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getCaDbBfb());

            cell = row.createCell(15);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getPDbs());
            cell = row.createCell(16);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getPZs());
            cell = row.createCell(17);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getPPjs());
            cell = row.createCell(18);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getPDbBfb());

            cell = row.createCell(19);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getJzpxjsDbs());
            cell = row.createCell(20);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getJzpxjsZs());
            cell = row.createCell(21);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getJzpxjsPjs());
            cell = row.createCell(22);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getJzpxjsDbBfb());

            cell = row.createCell(23);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getKtvDbs());
            cell = row.createCell(24);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getKtvZs());
            cell = row.createCell(25);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getKtvPjs());
            cell = row.createCell(26);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(gjzbtjVO.getKtvDbBfb());

            outputStream = new ByteArrayOutputStream();

            workBook.write(outputStream);
            DownloadUtil downloadUtil = new DownloadUtil();
            String fileName = courtyardArea + "血透关键指标质量统计.xls";
            downloadUtil.download(outputStream, response, fileName, request.getHeader("user-agent"));
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
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public ResultVO listPatientInfo(HttpServletRequest request, String startTime, String endTime, String loginName, String searchType, String courtyardArea) {
        List<String> list = (List<String>) request.getSession().getAttribute(searchType + courtyardArea + loginName);
        List<GjzbPatientTj> result = new ArrayList<>();
        if ("xgtlDbsList".equals(searchType) || "xgtlZsList".equals(searchType)) {
            result = gjzbtjMapper.getXgtlPatientinfo(list);
        } else {
            List<Gjzb> hy = getGjzbPatientTjData(startTime, endTime, searchType, list);
            Map<String, List<Gjzb>> collect = hy.stream().collect(Collectors.groupingBy(Gjzb::getPatientId));
            List<Gjzb> gjzbs = new ArrayList<>();
            for (Map.Entry<String, List<Gjzb>> entry : collect.entrySet()) {
                String key = entry.getKey();
                List<Gjzb> value = entry.getValue();
                if (CollectionUtils.isEmpty(value)) {
                    continue;
                }
                List<String> values = value.stream().map(Gjzb::getValue).collect(Collectors.toList());
                Gjzb gjzb = new Gjzb();
                gjzb.setPatientId(key).setValue(String.join(",", values));
                gjzbs.add(gjzb);
            }
            List<GjzbPatientTj> patientinfos = gjzbtjMapper.getPatientinfo(list);
            result = patientinfos.stream()
                    .flatMap(x -> gjzbs.stream()
                            .filter(y -> x.getPatientId().equals(y.getPatientId()))
                            .map(y -> new GjzbPatientTj(x.getName(), x.getCasecode(), x.getIdentitycard(), x.getFirstdialysetime(), y.getValue(), x.getHospitalId())))
                    .collect(Collectors.toList());
        }
        return new ResultVO(Long.valueOf(result.size()), result);
    }

    private List<Gjzb> getGjzbPatientTjData(String startTime, String endTime, String searchType, List<String> list) {
        List<Gjzb> hyPatientinfo = new ArrayList<>();
        if ("xhdbDbsList".equals(searchType) || "xhdbZsList".equals(searchType)) {
            hyPatientinfo = gjzbtjMapper.getHy(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), 9.0, 1000.0, 178, list);
        } else if ("bdbDbsList".equals(searchType) || "bdbZsList".equals(searchType)) {
            hyPatientinfo = gjzbtjMapper.getHy(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), 5.0, 70.0, 154, list);
        } else if ("caDbsList".equals(searchType) || "caZsList".equals(searchType)) {
            hyPatientinfo = gjzbtjMapper.getHy(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), 0.1, 10.0, 410, list);
        } else if ("pDbsList".equals(searchType) || "pZsList".equals(searchType)) {
            hyPatientinfo = gjzbtjMapper.getHy(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), 0.1, 10.0, 198, list);
        } else if ("jzpxjsDbsList".equals(searchType) || "jzpxjsZsList".equals(searchType)) {
            hyPatientinfo = gjzbtjMapper.getHy(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), null, 10000.0, 471, list);
        } else if ("ktvDbsList".equals(searchType) || "ktvZsList".equals(searchType)) {
            hyPatientinfo = gjzbtjMapper.getHy(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), null, 10.0, 308, list);
        }
        return hyPatientinfo;
    }

    @Override
    public void secondExportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea, String loginName, String searchType) {
        HSSFWorkbook tpWorkbook = null;
        HSSFWorkbook workBook = null;
        ByteArrayOutputStream outputStream = null;
        try {
            String sheetName = "";
            String fileName = "";
            switch (searchType) {
                case "xgtlDbsList":
                    sheetName = "血管通路";
                    fileName = "具体病人信息列表血管通路达标数";
                    break;
                case "xgtlZsList":
                    sheetName = "血管通路";
                    fileName = "具体病人信息列表血管通路总数";
                    break;
                case "xhdbDbsList":
                    sheetName = "血红蛋白";
                    fileName = "具体病人信息列表血红蛋白达标数";
                    break;
                case "xhdbZsList":
                    sheetName = "血红蛋白";
                    fileName = "具体病人信息列表血红蛋白总数";
                    break;
                case "bdbDbsList":
                    sheetName = "白蛋白";
                    fileName = "具体病人信息列表白蛋白达标数";
                    break;
                case "bdbZsList":
                    sheetName = "白蛋白";
                    fileName = "具体病人信息列表白蛋白总数";
                    break;
                case "caDbsList":
                    sheetName = "钙";
                    fileName = "具体病人信息列表钙达标数";
                    break;
                case "caZsList":
                    sheetName = "钙";
                    fileName = "具体病人信息列表钙总数";
                    break;
                case "pDbsList":
                    sheetName = "磷";
                    fileName = "具体病人信息列表磷达标数";
                    break;
                case "pZsList":
                    sheetName = "磷";
                    fileName = "具体病人信息列表磷总数";
                    break;
                case "jzpxjsDbsList":
                    sheetName = "甲状旁腺激素";
                    fileName = "具体病人信息列表甲状旁腺激素达标数";
                    break;
                case "jzpxjsZsList":
                    sheetName = "甲状旁腺激素";
                    fileName = "具体病人信息列表甲状旁腺激素总数";
                    break;
                case "ktvDbsList":
                    sheetName = "透析充分性";
                    fileName = "具体病人信息列表透析充分性达标数";
                    break;
                case "ktvZsList":
                    sheetName = "透析充分性";
                    fileName = "具体病人信息列表透析充分性总数";
                    break;

            }
            workBook = new HSSFWorkbook();
            CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
            Sheet sheet = workBook.createSheet();
            workBook.setSheetName(0, courtyardArea + sheetName);
            List<String> list = (List<String>) request.getSession().getAttribute(searchType + courtyardArea + loginName);
            List<GjzbPatientTj> hyPatientinfo = listPatientInfo(request, startTime, endTime, loginName, searchType, courtyardArea).getData();
            int len = 1;
            for (GjzbPatientTj gjzbPatientTj : hyPatientinfo) {
                String[] split = gjzbPatientTj.getVal().split(",");
                len = len >= split.length ? len : split.length;
            }
            int line = 1;
            Row row;
            Cell cell;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellStyle(hcs);
            cell.setCellValue("姓名");
            cell = row.createCell(1);
            cell.setCellStyle(hcs);
            cell.setCellValue("病历本号");
            cell = row.createCell(2);
            cell.setCellStyle(hcs);
            cell.setCellValue("身份证号");
            cell = row.createCell(3);
            cell.setCellStyle(hcs);
            cell.setCellValue("首次透析日期");
            cell = row.createCell(4);
            cell.setCellStyle(hcs);
            cell.setCellValue("院区");
            for (int i = 0, j = 0; i < len; i++, j += 2) {
                cell = row.createCell(5 + j);
                cell.setCellStyle(hcs);
                cell.setCellValue("检测日期");
                cell = row.createCell(6 + j);
                cell.setCellStyle(hcs);
                cell.setCellValue("检测结果");
            }
            CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
            for (GjzbPatientTj gjzbPatientTj : hyPatientinfo) {
                row = sheet.createRow(line++);
                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(gjzbPatientTj.getName());

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(gjzbPatientTj.getCasecode());

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(gjzbPatientTj.getIdentitycard());

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(gjzbPatientTj.getFirstdialysetime());

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(gjzbPatientTj.getHospitalId());
                if (searchType.contains("xgtl")) {
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(gjzbPatientTj.getSfrq());

                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(gjzbPatientTj.getVal());
                } else {
                    String[] split = gjzbPatientTj.getVal().split(",");
                    for (int i = 0, j = 0; i < split.length; i++, j += 2) {
                        String[] s = split[i].split(": ");

                        cell = row.createCell(5 + j);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(s[0]);

                        cell = row.createCell(6 + j);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(s[1]);

                    }
                }

            }
            sheet.setColumnWidth(2,5000);
            sheet.setColumnWidth(3,5000);
            outputStream = new ByteArrayOutputStream();

            workBook.write(outputStream);
            DownloadUtil downloadUtil = new DownloadUtil();
            downloadUtil.download(outputStream, response, courtyardArea + fileName + ".xls", request.getHeader("user-agent"));
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
}

package com.huamai.hdServer.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huamai.hdServer.domain.Grjcap;
import com.huamai.hdServer.mapper.GrjcapMapper;
import com.huamai.hdServer.service.GrjcapService;
import com.huamai.hdServer.util.DateUtil;
import com.huamai.hdServer.util.DownloadUtil;
import com.huamai.hdServer.util.PoiUtil;
import com.huamai.hdServer.vo.ResultVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 感染监测安排 业务逻辑层
 *
 * @author bbd
 */
@Service
public class GrjcapServiceImpl implements GrjcapService {
    @Autowired
    private GrjcapMapper grjcapMapper;

    private static final SimpleDateFormat simpleDateFormatHour = new SimpleDateFormat("HH:mm");

    /**
     * 分页查询 透析液电解质监测记录 page : 起始数据索引 非页数 size 每页数据大小
     */
    public ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea) {
        PageHelper.startPage(page / size + 1, size);
        Page<Grjcap> data = (Page<Grjcap>) grjcapMapper.getAll(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                DateUtil.stringToLocalDateForYYYYMMDD(endTime), courtyardArea);
        List<Grjcap> result = data.getResult();
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(page);
        result.forEach((e) -> {
            e.setNumber(atomicInteger.incrementAndGet());
        });
        return new ResultVO(data.getTotal(), result);
    }

    /**
     * 删除数据 删除数据的inIds字符串 eg:1,2,3
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class) // 事务
    public void deleteDate(String inIds) throws Exception {
        String[] ids = inIds.split(",");
        for (String id : ids) {
            grjcapMapper.deleteDate(new BigDecimal(id));
        }
    }

    /**
     * 新增数据
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void insertData(Grjcap grjcap) throws Exception {
        grjcap.setIsDelete(false);
        String jqbmResult = grjcap.getJqbmResult();
        if(!StringUtils.isEmpty(jqbmResult)) {
            jqbmResult = jqbmResult + "(" + grjcap.getSwsResultTemp() + ")";
            grjcap.setJqbmResult(jqbmResult);
        }

        String txyaRysResult = grjcap.getTxyaRysResult();
        if(!StringUtils.isEmpty(txyaRysResult)) {
            txyaRysResult = txyaRysResult + "(" + grjcap.getTxyaRysResultTemp() + ")";
            grjcap.setTxyaRysResult(txyaRysResult);
        }

        String txybRysResult = grjcap.getTxybRysResult();
        if(!StringUtils.isEmpty(txybRysResult)) {
            txybRysResult = txybRysResult + "(" + grjcap.getTxybRysResultTemp() + ")";
            grjcap.setTxybRysResult(txybRysResult);
        }

        String txycRysResult = grjcap.getTxycRysResult();
        if(!StringUtils.isEmpty(txycRysResult)) {
            txycRysResult = txycRysResult + "(" + grjcap.getTxycRysResultTemp() + ")";
            grjcap.setTxycRysResult(txycRysResult);
        }

        String txydRysResult = grjcap.getTxydRysResult();
        if(!StringUtils.isEmpty(txydRysResult)) {
            txydRysResult = txydRysResult + "(" + grjcap.getTxydRysResultTemp() + ")";
            grjcap.setTxydRysResult(txydRysResult);
        }

        String txyeRysResult = grjcap.getTxyeRysResult();
        if(!StringUtils.isEmpty(txyeRysResult)) {
            txyeRysResult = txyeRysResult + "(" + grjcap.getTxyeRysResultTemp() + ")";
            grjcap.setTxyeRysResult(txyeRysResult);
        }

        String txyfRysResult = grjcap.getTxyfRysResult();
        if(!StringUtils.isEmpty(txyfRysResult)) {
            txyfRysResult = txyfRysResult + "(" + grjcap.getTxyfRysResultTemp() + ")";
            grjcap.setTxyfRysResult(txyfRysResult);
        }

        String txygRysResult = grjcap.getTxygRysResult();
        if(!StringUtils.isEmpty(txygRysResult)) {
            txygRysResult = txygRysResult + "(" + grjcap.getTxygRysResultTemp() + ")";
            grjcap.setTxygRysResult(txygRysResult);
        }

        String txyhRysResult = grjcap.getTxyhRysResult();
        if(!StringUtils.isEmpty(txyhRysResult)) {
            txyhRysResult = txyhRysResult + "(" + grjcap.getTxyhRysResultTemp() + ")";
            grjcap.setTxyhRysResult(txyhRysResult);
        }
        grjcapMapper.insertData(grjcap);
    }

    /**
     * 修改数据
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateData(Grjcap grjcap) throws Exception {
        String jqbmResult = grjcap.getJqbmResult();
        if(!StringUtils.isEmpty(jqbmResult)) {
            jqbmResult = jqbmResult + "(" + grjcap.getSwsResultTemp() + ")";
            grjcap.setJqbmResult(jqbmResult);
        }

        String txyaRysResult = grjcap.getTxyaRysResult();
        if(!StringUtils.isEmpty(txyaRysResult)) {
            txyaRysResult = txyaRysResult + "(" + grjcap.getTxyaRysResultTemp() + ")";
            grjcap.setTxyaRysResult(txyaRysResult);
        }

        String txybRysResult = grjcap.getTxybRysResult();
        if(!StringUtils.isEmpty(txybRysResult)) {
            txybRysResult = txybRysResult + "(" + grjcap.getTxybRysResultTemp() + ")";
            grjcap.setTxybRysResult(txybRysResult);
        }

        String txycRysResult = grjcap.getTxycRysResult();
        if(!StringUtils.isEmpty(txycRysResult)) {
            txycRysResult = txycRysResult + "(" + grjcap.getTxycRysResultTemp() + ")";
            grjcap.setTxycRysResult(txycRysResult);
        }

        String txydRysResult = grjcap.getTxydRysResult();
        if(!StringUtils.isEmpty(txydRysResult)) {
            txydRysResult = txydRysResult + "(" + grjcap.getTxydRysResultTemp() + ")";
            grjcap.setTxydRysResult(txydRysResult);
        }

        String txyeRysResult = grjcap.getTxyeRysResult();
        if(!StringUtils.isEmpty(txyeRysResult)) {
            txyeRysResult = txyeRysResult + "(" + grjcap.getTxyeRysResultTemp() + ")";
            grjcap.setTxyeRysResult(txyeRysResult);
        }

        String txyfRysResult = grjcap.getTxyfRysResult();
        if(!StringUtils.isEmpty(txyfRysResult)) {
            txyfRysResult = txyfRysResult + "(" + grjcap.getTxyfRysResultTemp() + ")";
            grjcap.setTxyfRysResult(txyfRysResult);
        }

        String txygRysResult = grjcap.getTxygRysResult();
        if(!StringUtils.isEmpty(txygRysResult)) {
            txygRysResult = txygRysResult + "(" + grjcap.getTxygRysResultTemp() + ")";
            grjcap.setTxygRysResult(txygRysResult);
        }

        String txyhRysResult = grjcap.getTxyhRysResult();
        if(!StringUtils.isEmpty(txyhRysResult)) {
            txyhRysResult = txyhRysResult + "(" + grjcap.getTxyhRysResultTemp() + ")";
            grjcap.setTxyhRysResult(txyhRysResult);
        }
        grjcapMapper.updateData(grjcap);
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
                            String courtyardArea) {
        InputStream is = null;
        HSSFWorkbook tpWorkbook = null;
        HSSFWorkbook workBook = null;
        ByteArrayOutputStream outputStream = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream("static/excel/感染监测安排.xls");
            tpWorkbook = new HSSFWorkbook(is);
            workBook = new HSSFWorkbook();
            workBook = tpWorkbook;
            CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
            Sheet sheet = workBook.getSheetAt(0);
            workBook.setSheetName(0, "感染监测安排");

            int line = 3;
            Row row;
            Cell cell;
            List<Grjcap> datas = grjcapMapper.getAll(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
                    DateUtil.stringToLocalDateForYYYYMMDD(endTime), courtyardArea);
            CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
            for (Grjcap data : datas) {
                row = sheet.createRow(line++);

                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(DateUtil.localDateToStringForYYYYMMDD(data.getRqsj()));

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getSwsResult());

                Date swsTime = data.getSwsTime();
                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                if (swsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(swsTime));
                }

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getJqbmResult());

                Date jqbmTime = data.getJqbmTime();
                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                if (jqbmTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(jqbmTime));
                }

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getZls1Result());

                Date zls1Time = data.getZls1Time();
                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                if (zls1Time != null) {
                    cell.setCellValue(simpleDateFormatHour.format(zls1Time));
                }

                cell = row.createCell(7);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getZls2Result());

                Date zls2Time = data.getZls2Time();
                cell = row.createCell(8);
                cell.setCellStyle(cellStyle);
                if (zls2Time != null) {
                    cell.setCellValue(simpleDateFormatHour.format(zls2Time));
                }

                cell = row.createCell(9);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getFssaRysResult());

                Date fssaRysTime = data.getFssaRysTime();
                cell = row.createCell(10);
                cell.setCellStyle(cellStyle);
                if (fssaRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(fssaRysTime));
                }

                cell = row.createCell(11);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getFssaXjsResult());

                Date fssaXjsTime = data.getFssaXjsTime();
                cell = row.createCell(12);
                cell.setCellStyle(cellStyle);
                if (fssaXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(fssaXjsTime));
                }

                cell = row.createCell(13);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getFssbRysResult());

                Date fssbRysTime = data.getFssbRysTime();
                cell = row.createCell(14);
                cell.setCellStyle(cellStyle);
                if (fssbRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(fssbRysTime));
                }

                cell = row.createCell(15);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getFssbXjsResult());

                Date fssbXjsTime = data.getFssbXjsTime();
                cell = row.createCell(16);
                cell.setCellStyle(cellStyle);
                if (fssbXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(fssbXjsTime));
                }

                cell = row.createCell(17);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxyaRysResult());

                Date txyaRysTime = data.getTxyaRysTime();
                cell = row.createCell(18);
                cell.setCellStyle(cellStyle);
                if (txyaRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txyaRysTime));
                }

                cell = row.createCell(19);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxyaXjsResult());

                Date txyaXjsTime = data.getTxyaXjsTime();
                cell = row.createCell(20);
                cell.setCellStyle(cellStyle);
                if (txyaXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txyaXjsTime));
                }


                cell = row.createCell(21);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxybRysResult());

                Date txybRysTime = data.getTxybRysTime();
                cell = row.createCell(22);
                cell.setCellStyle(cellStyle);
                if (txybRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txybRysTime));
                }

                cell = row.createCell(23);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxybXjsResult());

                Date txybXjsTime = data.getTxybXjsTime();
                cell = row.createCell(24);
                cell.setCellStyle(cellStyle);
                if (txybXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txybXjsTime));
                }


                cell = row.createCell(25);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxycRysResult());

                Date txycRysTime = data.getTxycRysTime();
                cell = row.createCell(26);
                cell.setCellStyle(cellStyle);
                if (txycRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txycRysTime));
                }

                cell = row.createCell(27);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxycXjsResult());

                Date txycXjsTime = data.getTxycXjsTime();
                cell = row.createCell(28);
                cell.setCellStyle(cellStyle);
                if (txycXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txycXjsTime));
                }


                cell = row.createCell(29);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxydRysResult());

                Date txydRysTime = data.getTxydRysTime();
                cell = row.createCell(30);
                cell.setCellStyle(cellStyle);
                if (txydRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txydRysTime));
                }

                cell = row.createCell(31);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxydXjsResult());

                Date txydXjsTime = data.getTxydXjsTime();
                cell = row.createCell(32);
                cell.setCellStyle(cellStyle);
                if (txydXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txydXjsTime));
                }


                cell = row.createCell(33);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxyeRysResult());

                Date txyeRysTime = data.getTxyeRysTime();
                cell = row.createCell(34);
                cell.setCellStyle(cellStyle);
                if (txyeRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txyeRysTime));
                }

                cell = row.createCell(35);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxyeXjsResult());

                Date txyeXjsTime = data.getTxyeXjsTime();
                cell = row.createCell(36);
                cell.setCellStyle(cellStyle);
                if (txyeXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txyeXjsTime));
                }

                cell = row.createCell(37);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxyfRysResult());

                Date txyfRysTime = data.getTxyfRysTime();
                cell = row.createCell(38);
                cell.setCellStyle(cellStyle);
                if (txyfRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txyfRysTime));
                }

                cell = row.createCell(39);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxyfXjsResult());

                Date txyfXjsTime = data.getTxyfXjsTime();
                cell = row.createCell(40);
                cell.setCellStyle(cellStyle);
                if (txyfXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txyfXjsTime));
                }


                cell = row.createCell(41);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxygRysResult());

                Date txygRysTime = data.getTxygRysTime();
                cell = row.createCell(42);
                cell.setCellStyle(cellStyle);
                if (txygRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txygRysTime));
                }

                cell = row.createCell(43);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxygXjsResult());

                Date txygXjsTime = data.getTxygXjsTime();
                cell = row.createCell(44);
                cell.setCellStyle(cellStyle);
                if (txygXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txygXjsTime));
                }

                cell = row.createCell(45);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxyhRysResult());

                Date txyhRysTime = data.getTxyhRysTime();
                cell = row.createCell(46);
                cell.setCellStyle(cellStyle);
                if (txyhRysTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txyhRysTime));
                }

                cell = row.createCell(47);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getTxyhXjsResult());

                Date txyhXjsTime = data.getTxyhXjsTime();
                cell = row.createCell(48);
                cell.setCellStyle(cellStyle);
                if (txyhXjsTime != null) {
                    cell.setCellValue(simpleDateFormatHour.format(txyhXjsTime));
                }

                cell = row.createCell(49);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getCccyr());

                cell = row.createCell(50);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getFccyr());

                cell = row.createCell(51);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getRemark());

                cell = row.createCell(52);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getCourtyardArea());

            }

            outputStream = new ByteArrayOutputStream();

            workBook.write(outputStream);
            DownloadUtil downloadUtil = new DownloadUtil();
            String fileName = "感染监测安排.xls";
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
    public List<String> queryDictionary(String dictionaryClassName, String dictionaryTableName) {
        return grjcapMapper.queryDictionary(dictionaryClassName, dictionaryTableName);
    }

}

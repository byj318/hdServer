package com.huamai.hdServer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huamai.hdServer.domain.Ansypzjl;
import com.huamai.hdServer.enums.CourtyardAreaEnum;
import com.huamai.hdServer.enums.ExceptionEnum;
import com.huamai.hdServer.exception.MyException;
import com.huamai.hdServer.mapper.AnsypzjlMapper;
import com.huamai.hdServer.service.AnsypzjlService;
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
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A浓缩液配置记录 业务逻辑层
 *
 * @author bbd
 */
@Service
public class AnsypzjlServiceImpl implements AnsypzjlService {
    @Autowired
    private AnsypzjlMapper ansypzjlMapper;

    /**
     * 分页查询 透析液电解质监测记录 page : 起始数据索引 非页数 size 每页数据大小
     */
    public ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea) {
        PageHelper.startPage(page / size + 1, size);
        //过滤
        Example example = new Example(Ansypzjl.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDelete",false);
        if(!StringUtils.isEmpty(startTime)){
            criteria.andGreaterThanOrEqualTo("pzsj",DateUtil.stringToLocalDateForYYYYMMDD(startTime));
        }
        if(!StringUtils.isEmpty(endTime)){
            criteria.andLessThanOrEqualTo("pzsj",DateUtil.stringToLocalDateForYYYYMMDD(endTime));
        }
        if(!StringUtils.isEmpty(courtyardArea) && !CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)){
            criteria.andEqualTo("courtyardArea",courtyardArea);
        }

        //排序
        example.setOrderByClause("pzsj DESC");

        //查询
        List<Ansypzjl> result = ansypzjlMapper.selectByExample(example);

        PageInfo<Ansypzjl> pageInfo = new PageInfo<>(result);
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(page);
        result.forEach((e) -> {
            e.setNumber(atomicInteger.incrementAndGet());
        });
        return new ResultVO(pageInfo.getTotal(), result);
    }

    /**
     * 删除数据 删除数据的inIds字符串 eg:1,2,3
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class) // 事务
    public void deleteDate(String inIds) throws Exception {
        String[] ids = inIds.split(",");
        for (String id : ids) {
           ansypzjlMapper.deleteDate(new BigDecimal(id));
        }
    }

    /**
     * 新增数据
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void insertData(Ansypzjl ansypzjl) throws Exception {
        ansypzjl.setIsDelete(false);
        int i = ansypzjlMapper.insertSelective(ansypzjl);
        if(i == 0){
            throw new MyException(ExceptionEnum.ANSYPZJL_CREATE_FAILED);
        }
    }

    /**
     * 修改数据
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateData(Ansypzjl ansypzjl) throws Exception {
        ansypzjlMapper.updateData(ansypzjl);
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
                            String courtyardArea) {
        InputStream is = null;
        HSSFWorkbook tpWorkbook = null;
        HSSFWorkbook workBook = null;
        ByteArrayOutputStream outputStream = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream("static/excel/A浓缩液配置记录.xls");
            tpWorkbook = new HSSFWorkbook(is);
            workBook = new HSSFWorkbook();
            workBook = tpWorkbook;
            CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
            Sheet sheet = workBook.getSheetAt(0);
            workBook.setSheetName(0, "A浓缩液配置记录");

            int line = 1;
            Row row;
            Cell cell;
            //过滤
            Example example = new Example(Ansypzjl.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("isDelete",false);
            if(!StringUtils.isEmpty(startTime)){
                criteria.andGreaterThanOrEqualTo("pzsj",DateUtil.stringToLocalDateForYYYYMMDD(startTime));
            }
            if(!StringUtils.isEmpty(endTime)){
                criteria.andLessThanOrEqualTo("pzsj",DateUtil.stringToLocalDateForYYYYMMDD(endTime));
            }
            if(!StringUtils.isEmpty(courtyardArea) && !CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)){
                criteria.andEqualTo("courtyardArea",courtyardArea);
            }

            //排序
            example.setOrderByClause("pzsj DESC");

            //查询
            List<Ansypzjl> datas = ansypzjlMapper.selectByExample(example);
            CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
            for (Ansypzjl data : datas) {
                row = sheet.createRow(line++);
                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(DateUtil.localDateTimeToStringForYYYYMMDDHHMM(data.getPzsj()));

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getPzfs());

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getGfxh());

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getGfph());

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getJstj());

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getDrtj());

                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getZxddz());

                cell = row.createCell(7);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getCzr());

                cell = row.createCell(8);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getFhr());

                cell = row.createCell(9);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getRemark());

                cell = row.createCell(10);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(data.getCourtyardArea());

            }

            outputStream = new ByteArrayOutputStream();

            workBook.write(outputStream);
            DownloadUtil downloadUtil = new DownloadUtil();
            String fileName = "A浓缩液配置记录.xls";
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

}

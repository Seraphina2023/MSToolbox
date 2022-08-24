package com.msop.core.report.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msop.core.report.entity.ReportFileEntity;
import com.msop.core.report.mapper.ReportFileMapper;
import com.msop.core.report.service.IReportFileService;
import org.springframework.stereotype.Service;

/**
 * UReport Service 实现
 *
 * @author ruozhuliufeng
 */
@Service
public class ReportFileServiceImpl extends ServiceImpl<ReportFileMapper, ReportFileEntity> implements IReportFileService {
}

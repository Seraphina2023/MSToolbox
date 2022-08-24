package com.msop.core.report.endpoint;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msop.core.mp.support.Condition;
import com.msop.core.mp.support.Query;
import com.msop.core.report.entity.ReportFileEntity;
import com.msop.core.report.service.IReportFileService;
import com.msop.core.tool.model.Result;
import com.msop.core.tool.utils.Func;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * UReport API端点
 *
 * @author ruozhuliufeng
 */
@ApiIgnore
@RestController
@AllArgsConstructor
@RequestMapping("/report/rest")
public class ReportEndpoint {
    private final IReportFileService service;

    /**
     * 详情
     */
    @GetMapping("/detail")
    public Result<ReportFileEntity> detail(ReportFileEntity file) {
        ReportFileEntity detail = service.getOne(Condition.getQueryWrapper(file));
        return Result.succeed(detail);
    }

    /**
     * 分页
     */
    @GetMapping("/list")
    public Result<IPage<ReportFileEntity>> list(@RequestParam Map<String, Object> file, Query query) {
        IPage<ReportFileEntity> pages = service.page(Condition.getPage(query), Condition.getQueryWrapper(file, ReportFileEntity.class));
        return Result.succeed(pages);
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    public Result remove(@RequestParam String ids) {
        boolean temp = service.removeByIds(Func.toLongList(ids));
        if (temp) {
            return Result.succeed();
        } else {
            return Result.failed();
        }
    }
}

package com.msop.core.report.endpoint;

import com.msop.core.launch.constant.AppConstant;
import com.msop.core.report.service.IReportFileService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping(AppConstant.APPLICATION_REPORT_NAME + "/report/rest")
public class ReportBootEndpoint extends ReportEndpoint{
    public ReportBootEndpoint(IReportFileService service){
        super(service);
    }
}

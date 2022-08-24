package com.core.core.sms.template;


import com.core.core.sms.model.SmsCode;
import com.core.core.sms.model.SmsData;
import com.core.core.sms.model.SmsResponse;
import com.core.core.sms.properties.SmsProperties;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.msop.core.redis.cache.MsRedis;
import com.msop.core.tool.constant.StringConstant;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;

/**
 * 腾讯云短信发送类
 * 
 * @author ruozhuliufeng 
 */
@AllArgsConstructor
public class TencentSmsTemplate implements SmsTemplate {

    private static final int SUCCESS = 0;
    private static final String NATION_CODE = "86";
    private final SmsProperties smsProperties;
    private final SmsMultiSender smsSender;
    private final MsRedis msRedis;


    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        try {
            Collection<String> values = smsData.getParams().values();
            String[] params = StringUtil.toStringArray(values);
            SmsMultiSenderResult senderResult = smsSender.sendWithParam(
                    NATION_CODE,
                    StringUtil.toStringArray(phones),
                    Func.toInt(smsProperties.getTemplateId()),
                    params,
                    smsProperties.getSignName(),
                    StringConstant.EMPTY, StringConstant.EMPTY
            );
            return new SmsResponse(senderResult.result == SUCCESS, senderResult.result, senderResult.toString());
        } catch (HTTPException | IOException e) {
            e.printStackTrace();
            return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Override
    public SmsCode sendValidate(SmsData smsData, String phone) {
        SmsCode smsCode = new SmsCode();
        boolean temp = sendSingle(smsData, phone);
        if (temp && StringUtil.isNotBlank(smsData.getKey())) {
            String id = StringUtil.randomUUID();
            String value = smsData.getParams().get(smsData.getKey());
            msRedis.setEx(cacheKey(phone, id), value, Duration.ofMinutes(30));
            smsCode.setId(id).setValue(value);
        } else {
            smsCode.setSuccess(Boolean.FALSE);
        }
        return smsCode;
    }

    @Override
    public boolean validateMessage(SmsCode smsCode) {
        String id = smsCode.getId();
        String value = smsCode.getValue();
        String phone = smsCode.getPhone();
        String cache = msRedis.get(cacheKey(phone, id));
        if (StringUtil.isNotBlank(value) && StringUtil.equalsIgnoreCase(cache, value)) {
            msRedis.del(cacheKey(phone, id));
            return true;
        }
        return false;
    }
}

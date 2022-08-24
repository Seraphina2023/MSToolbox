package com.core.core.sms.template;

import com.core.core.sms.model.SmsCode;
import com.core.core.sms.model.SmsData;
import com.core.core.sms.model.SmsInfo;
import com.core.core.sms.model.SmsResponse;
import com.msop.core.tool.constant.StringConstant;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;

import static com.core.core.sms.constant.SmsConstant.CAPTCHA_KEY;

/**
 * 短信通用封装
 *
 * @author ruozhuliufeng
 */
public interface SmsTemplate {

    /**
     * 缓存键值
     *
     * @param phone 手机号
     * @param id    键值
     * @return 缓存键值返回
     */
    default String cacheKey(String phone, String id) {
        return CAPTCHA_KEY + phone + StringConstant.COLON + id;
    }

    /**
     * 发送短信
     *
     * @param smsInfo 短信信息
     * @return 发送返回
     */
    default boolean send(SmsInfo smsInfo) {
        return sendMulti(smsInfo.getSmsData(), smsInfo.getPhones());
    }

    /**
     * 发送短信
     *
     * @param smsData 短信内容
     * @param phone   手机号
     * @return 发送返回
     */
    default boolean sendSingle(SmsData smsData, String phone) {
        if (StringUtils.isEmpty(phone)) {
            return Boolean.FALSE;
        }
        return sendMulti(smsData, Collections.singletonList(phone));
    }

    /**
     * 发送短信
     *
     * @param smsData 短信内容
     * @param phones  手机号列表
     * @return 发送返回
     */
    default boolean sendMulti(SmsData smsData, Collection<String> phones) {
        SmsResponse response = sendMessage(smsData, phones);
        return response.isSuccess();
    }

    /**
     * 发送短信
     *
     * @param smsData 短信内容
     * @param phones  手机号列表
     * @return 发送返回
     */
    SmsResponse sendMessage(SmsData smsData, Collection<String> phones);

    /**
     * 发送验证码
     *
     * @param smsData 短信内容
     * @param phone   手机号
     * @return 发送返回
     */
    SmsCode sendValidate(SmsData smsData, String phone);

    /**
     * 校验验证码
     *
     * @param smsCode 验证码内容
     * @return 是否校验成功
     */
    boolean validateMessage(SmsCode smsCode);
}

package com.msop.core.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.msop.core.mybatis.properties.MybatisPlusAutoFillProperties;
import com.msop.core.secure.model.MsUser;
import com.msop.core.secure.util.AuthUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自定义填充公共字段
 *
 * @author ruozhuliufeng
 */
public class DateMetaObjectHandler implements MetaObjectHandler {

    private MybatisPlusAutoFillProperties autoFillProperties;

    public DateMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties) {
        this.autoFillProperties = autoFillProperties;
    }

    /**
     * 是否开启了插入填充
     *
     * @return 是否
     */
    @Override
    public boolean openInsertFill() {
        return autoFillProperties.getEnableInsertFill();
    }

    /**
     * 是否开启了更新填充
     *
     * @return 是否
     */
    @Override
    public boolean openUpdateFill() {
        return autoFillProperties.getEnableUpdateFill();
    }

    /**
     * 插入填充，字段为空自动填充
     *
     * @param metaObject object
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        MsUser msUser = AuthUtils.getUser();
        Long userId = 0L;
        if (msUser != null) {
            userId = msUser.getUserId();
        }
        setFieldValByName(autoFillProperties.getCreateTimeField(), new Date(), metaObject);
        setFieldValByName(autoFillProperties.getCreateUserField(), userId, metaObject);
        setFieldValByName(autoFillProperties.getUpdateTimeField(), new Date(), metaObject);
        setFieldValByName(autoFillProperties.getUpdateUserField(), userId, metaObject);

    }

    /**
     * 更新字段填充
     *
     * @param metaObject object
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        MsUser msUser = AuthUtils.getUser();
        Long userId = 0L;
        if (msUser != null) {
            userId = msUser.getUserId();
        }
        setFieldValByName(autoFillProperties.getUpdateTimeField(), new Date(), metaObject);
        setFieldValByName(autoFillProperties.getUpdateUserField(), userId, metaObject);
    }
}

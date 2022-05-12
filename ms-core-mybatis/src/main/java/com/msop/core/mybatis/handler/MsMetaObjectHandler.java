package com.msop.core.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.msop.core.secure.utils.SecureUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

public class MsMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = SecureUtil.getUserId();
        Date date = new Date();
        fillStrategy(metaObject,"createUser",userId);
        fillStrategy(metaObject,"createTime",date);
        fillStrategy(metaObject,"updateUser",userId);
        fillStrategy(metaObject,"updateTime",date);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = SecureUtil.getUserId();
        Date date = new Date();
        fillStrategy(metaObject,"updateUser",userId);
        fillStrategy(metaObject,"updateTime",date);
    }
}

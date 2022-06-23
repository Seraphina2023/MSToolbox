package com.msop.core.mp.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.StringUtil;

/**
 * 分页工具
 */
public class Condition {

    public static <T> IPage<T> getPage(Query query) {
        Page<T> page = new Page<>(Func.toInt(query.getCurrent(), 1), Func.toInt(query.getSize(), 10));
        String[] ascArr = Func.toStrArray(query.getAscs());
        for (String asc : ascArr) {
            page.addOrder(OrderItem.asc(StringUtil.cleanIdentifier(asc)));
        }
        String[] descArr = Func.toStrArray(query.getDescs());
        for (String desc : descArr) {
            page.addOrder(OrderItem.desc(StringUtil.cleanIdentifier(desc)));
        }
        return page;
    }
}
package tech.msop.mybatis.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页模型
 *
 * @param <T> 泛型
 * @author ruozhuliufeng
 */
@Data
public class MsPage<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();
    /**
     * 总数
     */
    private long total = 0;
    /**
     * 每页显示条数，默认10
     */
    private long size = 10;
    /**
     * 当前页
     */
    private long current = 1;

    /**
     * mybatis-plus 分页模型转换
     *
     * @param page 分页实体类
     * @param <T>  泛型
     * @return MsPage<T>
     */
    public static <T> MsPage<T> of(IPage<T> page) {
        MsPage<T> msPage = new MsPage<>();
        msPage.setRecords(page.getRecords());
        msPage.setTotal(page.getTotal());
        msPage.setSize(page.getSize());
        msPage.setCurrent(page.getCurrent());
        return msPage;
    }
}

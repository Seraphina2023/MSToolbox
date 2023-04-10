package tech.msop.mybatis.injector;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import tech.msop.mybatis.injector.methods.InsertIgnore;
import tech.msop.mybatis.injector.methods.Replace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义的 sql 注入
 *
 * @author ruozhuliufeng
 */
public class MsSqlInjector extends DefaultSqlInjector {

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
		List<AbstractMethod> methodList = new ArrayList<>();
		methodList.add(new InsertIgnore());
		methodList.add(new Replace());
		methodList.add(new InsertBatchSomeColumn(i -> i.getFieldFill() != FieldFill.UPDATE));
		methodList.addAll(super.getMethodList(mapperClass, tableInfo));
		return Collections.unmodifiableList(methodList);
	}
}

package tech.msop.mybatis.injector.methods;


import tech.msop.mybatis.injector.MsSqlMethod;

/**
 * 插入一条数据（选择字段插入）插入如果中已经存在相同的记录，则忽略当前新数据
 *
 * @author ruozhuliufeng
 */
public class InsertIgnore extends AbstractInsertMethod {

	public InsertIgnore() {
		super(MsSqlMethod.INSERT_IGNORE_ONE);
	}
}

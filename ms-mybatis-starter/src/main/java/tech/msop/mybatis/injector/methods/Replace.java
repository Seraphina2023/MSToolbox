package tech.msop.mybatis.injector.methods;


import tech.msop.mybatis.injector.MsSqlMethod;

/**
 * 插入一条数据（选择字段插入）
 * <p>
 * 表示插入替换数据，需求表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
 * </p>
 *
 * @author ruozhuliufeng
 */
public class Replace extends AbstractInsertMethod {

	public Replace() {
		super(MsSqlMethod.REPLACE_ONE);
	}
}


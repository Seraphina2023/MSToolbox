package tech.msop.core.db.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 数据库类型 枚举
 */
@Getter
@AllArgsConstructor
public enum MsDbEnum {
    /**
     * MySQL 数据库
     */
    MySQL("mysql", "SELECT table_name FROM information_schema.tables WHERE table_schema = '%s'", "SELECT column_name FROM information_schema.columns WHERE table_schema = '%s' AND table_name = '%s'"),
    /**
     * Oracle 数据库
     */
    Oracle("oracle","",""),

    ;

    /**
     * 数据库类型
     */
    final String type;
    /**
     * 查询数据库的所有表 SQL
     */
    final String tableSql;
    /**
     * 查询表内的所有列 SQL
     */
    final String columnSql;

    /**
     * 根据类型获取数据
     *
     * @param type 数据库类型
     * @return 枚举
     */
    public MsDbEnum of(String type) {
        MsDbEnum db = null;
        for (MsDbEnum dbEnum : values()) {
            if (dbEnum.type.equals(type)) {
                db = dbEnum;
            }
        }
        return db;
    }
}

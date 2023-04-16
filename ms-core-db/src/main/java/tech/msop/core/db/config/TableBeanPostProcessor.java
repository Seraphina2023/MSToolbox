package tech.msop.core.db.config;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import tech.msop.core.db.annotation.MsTable;
import tech.msop.core.db.annotation.MsTableColumn;
import tech.msop.core.db.annotation.MsTableId;
import tech.msop.core.db.exception.MsTableException;
import tech.msop.core.db.properties.MsDbProperties;
import tech.msop.core.tool.utils.CollectionUtil;
import tech.msop.core.tool.utils.ObjectUtil;
import tech.msop.core.tool.utils.StringUtil;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据库自动创建表
 */
@AutoConfiguration
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(value = "ms.db.enabled", havingValue = "true")
public class TableBeanPostProcessor implements BeanPostProcessor {
    /**
     * 自定义配置类
     */
    private final MsDbProperties properties;
    /**
     * JDBC
     */
    private final JdbcTemplate jdbcTemplate;
    /**
     * 数据库连接字符串配置类
     */
    private final DataSourceProperties dataSourceProperties;
    public static final Map<String, String> classTypeMappingColumnType = Maps.newHashMap();

    static {
        classTypeMappingColumnType.put("int", "integer");
        classTypeMappingColumnType.put("java.lang.Integer", "integer");
        classTypeMappingColumnType.put("java.lang.String", "varchar(255)");
        classTypeMappingColumnType.put("long", "Long");
        classTypeMappingColumnType.put("java.lang.Long", "Long");
    }

    @PostConstruct
    public void init() {
        // 创建表
        verifyAndComplementTable();
    }


    private void verifyAndComplementTable() {
        log.info("start----------------->校验并完善数据库表结构");
        Set<Class<?>> tableList = getTableList();
        // 校验表数据
        validEntryDefinition(tableList);
        String queryTableNameSql = "select name from sqlite_master where type='table' order by name ";
        //获取数据库中所有的表
        List<String> existTableNme = jdbcTemplate.queryForList(queryTableNameSql, String.class);
        Map<String, String> existTableNmeMap = existTableNme.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
        for (Class clazz : tableList) {
            // 获取MsTable注解
            MsTable table = (MsTable) clazz.getAnnotation(MsTable.class);
            String tableName = table.value();
            if (StringUtil.isBlank(tableName)) {
                // 表名为空，取值 配置表前缀+类名下划线
                tableName = clazz.getName();
                if (properties.getCamelCase()) {
                    tableName = StringUtil.humpToUnderline(tableName);
                }
                if (StringUtil.isNotBlank(properties.getTablePrefix())) {
                    tableName = properties.getTablePrefix() + tableName;
                }
            }
            if (existTableNmeMap.containsKey(tableName)) {
                // 表已存在
                //获取表的列
                List<String> tableColumn = getTableColumn(tableName);
                Map<String, String> tableColumnMap = tableColumn.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String columnName = field.getName();
                    if (properties.getCamelCase()) {
                        columnName = StringUtil.humpToUnderline(columnName);
                    }
                    MsTableColumn column = field.getAnnotation(MsTableColumn.class);
                    // 列不存在，不处理
                    if (!ObjectUtil.isEmpty(column)
                            && !column.exists()) {
                        continue;
                    }
                    if (!ObjectUtil.isEmpty(column)
                            && StringUtil.isNotBlank(column.value())) {
                        columnName = column.value();
                    }
                    // 已经存在的列
                    if (tableColumnMap.containsKey(columnName)) {
                        continue;
                    }
                    // 数据库中不存在的列需要添加到数据库
                    Class<?> type = field.getType();
                    String columnType = classTypeMappingColumnType.get(type.getName());
                    String sql = "ALTER TABLE ".concat(tableName).concat(" ADD COLUMN ").concat(columnName).concat(" ").concat(columnType);
                    log.info("--------> 添加表结构列，table={},columnName={},sql = {}", tableName, columnName, sql);
                    jdbcTemplate.update(sql);
                }
            } else {
                // 不存在此表，创建表
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE ").append(tableName).append(" ( ");
                Field[] fields = clazz.getDeclaredFields();
                String primaryKey = "PRIMARY KEY (";
                for (Field field : fields) {
                    field.setAccessible(true);
                    String columnName = field.getName();
                    if (properties.getCamelCase()) {
                        columnName = StringUtil.humpToUnderline(columnName);
                    }
                    MsTableColumn tableColumn = field.getAnnotation(MsTableColumn.class);
                    // 列不存在，不处理
                    if (!ObjectUtil.isEmpty(tableColumn)
                            && !tableColumn.exists()) {
                        continue;
                    }
                    if (!ObjectUtil.isEmpty(tableColumn)
                            && StringUtil.isNotBlank(tableColumn.value())) {
                        columnName = tableColumn.value();
                    }
                    String columnType = classTypeMappingColumnType.get(field.getType().getName());
                    MsTableId tableId = field.getAnnotation(MsTableId.class);
                    if (!Objects.isNull(tableId)) {
                        sb.append(columnName).append(" ").append(columnType).append(" NOT NULL, ");
                        primaryKey = primaryKey + columnName + ") ";
                    } else {
                        sb.append(columnName).append(" ").append(columnType).append(" , ");
                    }
                }
                sb.append(primaryKey);
                sb.append("); ");
                log.info("--------> 添加表 table={},sql = {}", tableName, sb.toString());
                jdbcTemplate.update(sb.toString());
            }
        }
        log.info("end----------------->校验并完善数据库表结构");
    }

    /**
     * 查询数据表的所有列
     *
     * @param tableName 数据表名
     * @return 列名列表
     */
    private List<String> getTableColumn(String tableName) {
        return null;
    }


    /**
     * 获取包下包含 @MsTable 注解的所有类
     *
     * @return 类列表
     */
    private Set<Class<?>> getTableList() {
        Set<Class<?>> tableList = new HashSet<>();
        List<String> packageList = Arrays.asList(properties.getPackages());
        // 指定包列表
        if (CollectionUtil.isNotEmpty(packageList)) {
            packageList.forEach(path -> {
                Reflections reflections = new Reflections(path);
                tableList.addAll(reflections.getTypesAnnotatedWith(MsTable.class));
            });
        }
        return tableList;
    }

    /**
     * 校验表数据
     *
     * @param tableList 表列表
     */
    @SneakyThrows
    private void validEntryDefinition(Set<Class<?>> tableList) {
        Map<String, Class> validRepetitionMap = Maps.newHashMap();
        for (Class clazz : tableList) {
            // 获取MsTable注解
            MsTable table = (MsTable) clazz.getAnnotation(MsTable.class);
            if (ObjectUtil.isEmpty(table)) {
                // 注解为空,不处理该类
                continue;
            }
            String tableName = table.value();
            if (StringUtil.isBlank(tableName)) {
                // 表名为空，取值 配置表前缀+类名下划线
                tableName = clazz.getName();
                if (properties.getCamelCase()) {
                    tableName = StringUtil.humpToUnderline(tableName);
                }
                if (StringUtil.isNotBlank(properties.getTablePrefix())) {
                    tableName = properties.getTablePrefix() + tableName;
                }
            }
            if (validRepetitionMap.containsKey(tableName)) {
                throw new MsTableException("存在相同的表名称! 类名ClassName:" + clazz.getName() + ",表名:" + tableName);
            }
            validRepetitionMap.put(tableName, clazz);
            // 验证实体中的列名
            Field[] fields = clazz.getDeclaredFields();
            if (CollectionUtil.isEmpty(Arrays.asList(fields)) || fields.length == 0) {
                throw new MsTableException("类不存在相关的属性! 类名ClassName:" + clazz.getName());
            }
            Set<String> columnNames = new HashSet<>();
            MsTableId tableId = null;
            for (Field field : fields) {
                String fieldName = field.getName();
                if (properties.getCamelCase()) {
                    fieldName = StringUtil.humpToUnderline(fieldName);
                }
                MsTableColumn tableColumn = field.getAnnotation(MsTableColumn.class);
                // 列不存在，不处理
                if (!ObjectUtil.isEmpty(tableColumn)
                        && !tableColumn.exists()) {
                    continue;
                }
                if (!ObjectUtil.isEmpty(tableColumn)
                        && StringUtil.isNotBlank(tableColumn.value())) {
                    fieldName = tableColumn.value();
                }
                MsTableId id = field.getAnnotation(MsTableId.class);
                if (ObjectUtil.isNotEmpty(id)) {
                    tableId = id;
                }
                if (columnNames.contains(fieldName)) {
                    throw new MsTableException("存在相同的字段名称!类属性:" + field.getName() + ",数据库字段名:" + fieldName);
                }
                columnNames.add(fieldName);
            }
            if (ObjectUtil.isEmpty(tableId)) {
                throw new MsTableException("表主键ID不能为空! 类名ClassName:" + clazz.getName());
            }
            if (CollectionUtil.isEmpty(columnNames)) {
                throw new MsTableException("表的列不能为空! 类名ClassName:" + clazz.getName());
            }
        }
    }
}

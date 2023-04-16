# MS-CORE-DB
> 实现数据库自动建表、自动维护表结构
> 根据连接字符串判断数据库类型
> 适用于 不想使用JPA但是需要自动维护表结构数据
> 不适用于 多数据源维护表结构
## 配置说明

```yaml
ms:
  db:
    enabled: true # 允许自动生成表
    table-prefix: tb_ # 生成表的前缀
    camel-case: true # 允许将驼峰格式的类名转换为下划线格式的数据库表名
    packages: "tech.msop.data.entity" # 包名列表
```

## 使用说明

- 在实体类上添加注解 @MsTable
  - @MsTable 注解说明
  - value: 数据库表名，若不存在该值，将实体类转换为下划线格式的数据库表名，加上指定的前缀，组成该表的数据库表名
- 实体类上使用@Id 表明主键
- 在不需要的字段上添加注解 @MsTableColumn(exist=false)
  - value：数据库表字段名，若不存在该值，默认为字段名转换为下划线格式的数据库字段名
  - exists：数据库表是否存在该字段，为false时,不处理该字段
  - description：数据库表字段描述

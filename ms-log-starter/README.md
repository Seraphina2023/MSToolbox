# MS-LOG 工具类

## 配置信息

```yaml
ms:
  audit:
    log:
      enabled: true # 是否开启审计日志
      error-log: true # 是否开启异常日志推送
      console: true # 是否在控制台打印日志
      log-type: logger # 日志类型 logger: 控制台打印日志  db: 保存到数据库中 feign: 自定义类实现Feign接口保存到自定义的程序中 es: 保存到ES中(未实现) redis: 保存到Redis中(未实现) 
      level: None # 请求日志记录级别 None 不打印请求日志 BASIC 打印请求接口与返回信息  HEADERS 打印请求接口、Header与返回信息 BODY 打印请求接口、Header、Body数据与返回信息
      datasource: # 日志类型为db时,可选自定义MySQL数据库,若不指定,将获取默认配置的数据库信息
        driver: xxxxx
        user: xxxxx
        password: xxxxx
        url: xxxxx
```

## 使用说明

- 使用 @AuditLog 注解

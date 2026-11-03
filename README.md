# 读书笔记分享系统

基于 Struts2.5 + JSP + 原生 JDBC 的读书笔记分享平台，支持书籍投稿审核、封面上传审核、书单管理与笔记互动。

## 技术栈

| 分类 | 技术 |
| --- | --- |
| 后端 | Struts2.5、原生 JDBC |
| 前端 | JSP、JSTL、Bootstrap 3 |
| 数据库 | MySQL 5.7 / 8.0（utf8mb4） |
| 构建 | Maven |
| 运行环境 | JDK 8、Tomcat 9 |

## 功能

1. **用户模块** —— 注册登录，区分管理员与普通用户，未登录访问受保护页面自动跳转登录
2. **书籍分享审核** —— 普通用户提交的书籍为待审核状态，管理员审核通过后才对外展示，驳回则不公开
3. **封面上传审核** —— 仅书籍分享者可上传封面，普通用户上传的封面需管理员单独审核；书籍审核与封面审核互不干扰
4. **书单管理** —— 创建公开或私有书单，将书籍加入书单，公开书单在书单广场展示
5. **笔记互动** —— 撰写读书笔记，支持收藏书单 / 笔记与评论

## 环境要求

- JDK 8
- Maven 3.6+
- MySQL 5.7 / 8.0
- Tomcat 9

## 部署步骤

### 1. 初始化数据库

导入项目根目录的 `reading_share.sql`，脚本会自动创建 `reading_share` 库（字符集 utf8mb4），并完成建表与演示数据初始化。

```bash
mysql -uroot -p --default-character-set=utf8mb4 < reading_share.sql
```

也可使用 Navicat 等工具直接执行该脚本。

### 2. 修改数据库配置

编辑 `src/main/resources/db.properties`，改为本机 MySQL 账号密码：

```properties
jdbc.url=jdbc:mysql://localhost:3306/reading_share?useUnicode=true&characterEncoding=utf8&useSSL=false
jdbc.username=root
jdbc.password=123456
```

### 3. 编译并启动

```bash
mvn clean compile
```

在 IDEA 中配置 Tomcat 9 运行，部署 `war exploded`，Application context 设为 `/JavaEEwork`，启动后访问：

```
http://localhost:8080/JavaEEwork/
```

### 4. 预设账号

| 账号 | 密码 | 角色 |
| --- | --- | --- |
| admin | admin123 | 管理员 |
| zhangsan | 123456 | 普通用户 |
| lisi | 123456 | 普通用户 |
| wangwu | 123456 | 普通用户 |

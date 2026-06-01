# 部署与演示说明

## 1. 环境要求
- Node.js 20+
- JDK 17+
- Maven 3.9+
- MySQL 8.x

## 2. 数据库初始化
1. 创建数据库：
   - `CREATE DATABASE cellular_automata DEFAULT CHARACTER SET utf8mb4;`
2. 导入表结构：
   - 执行 `backend/schema.sql`

## 3. 后端部署
1. 进入目录：`backend/`
2. 打包：`mvn -DskipTests package`
3. 运行：
   - `java -jar target/backend-0.0.1-SNAPSHOT.jar`
4. 环境变量（按需覆盖）：
   - `DB_HOST`
   - `DB_PORT`
   - `DB_NAME`
   - `DB_USER`
   - `DB_PASSWORD`

## 4. 前端部署
1. 进入目录：`frontend/`
2. 安装依赖：`npm install`
3. 构建：`npm run build`
4. 将 `frontend/dist/frontend/browser`（Angular 默认构建目录）部署到 Nginx 静态目录。
5. 配置 Nginx 反向代理 `/api` 到后端 `8080`。

示例反向代理（Nginx）：
```nginx
location /api/ {
    proxy_pass http://127.0.0.1:8080/api/;
}
```

## 5. 演示建议流程
1. 进入页面后先演示生命游戏点击初始化和随机初始化。
2. 演示单步、连续、暂停、回退、重置。
3. 演示规则编辑（摩尔/冯诺依曼、B/S 参数）对结果的影响。
4. 演示教学面板：点击某个细胞查看邻域解释。
5. 演示 5 个评测场景加载与自动判定。
6. 演示预设模型库（生命游戏、Rule30、Rule110）。

## 6. 答辩材料建议
- 项目分析、设计图、关键代码说明、部署步骤、使用说明。
- 组员分工与贡献比例。
- 若使用 AI 工具，补充“使用位置 + 提示词摘要 + 人工复核说明”。

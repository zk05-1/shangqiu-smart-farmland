# 商丘市农业农村局智能农田管理系统

集农田档案、种植管理、智能灌溉、精准施肥、病虫害防治、气象监测、IoT 传感器数据采集、AI 产量预测于一体的智慧农业管理平台。

## 技术栈

- **前端**：Vue 3 + Element Plus + ECharts + 高德地图 + Vite
- **后端**：Spring Boot 3.2.5 + Spring Security + Spring Data JPA
- **数据库**：MySQL 8.0（开发）/ PostgreSQL（部署）
- **认证**：JWT 无状态认证 + RBAC 权限模型

## 本地运行

### 后端
```bash
cd 框架/server
./mvnw spring-boot:run
```
默认运行在 http://localhost:8080

### 前端
```bash
cd 框架/client
npm install
npm run dev
```
默认运行在 http://localhost:5173

## 默认账号
- 账号：`admin`
- 密码：`123456`

## 部署

- 前端部署到 Vercel，后端部署到 Render
- 详见 `render.yaml` 和 `框架/client/vercel.json`

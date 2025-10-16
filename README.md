# Spring AI Demo

基于Spring AI和Ollama的AI聊天应用示例项目。

## 项目简介

这是一个使用Spring Boot 3.5和Spring AI构建的AI聊天应用演示项目。项目集成了Ollama作为AI模型后端，提供了流式和阻塞两种聊天接口，支持实时响应和传统同步响应模式。

## 技术栈

- **Spring Boot 3.5.6** - 应用框架
- **Spring WebFlux** - 响应式Web框架
- **Spring AI** - AI集成框架
- **Ollama** - 本地AI模型运行平台
- **Java 17** - 运行环境
- **Gradle** - 构建工具
- **Lombok** - 代码简化工具

## 环境要求

- Java 17+
- Gradle 7+
- Ollama服务 (需提前安装并运行)
- 至少一个Ollama模型 (如: llama3)

## 快速开始

### 1. 克隆项目

````bash 
git clone <项目地址> 
cd spring-ai-demo
````

### 2. 启动Ollama服务

确保Ollama服务正在运行，并拉取所需模型：
````bash
ollama -m llama3
````

### 3. 构建项目
````bash 
./gradlew build
````


### 4. 运行应用

````bash 
./gradlew bootRun
````
应用将在 `http://localhost:8080` 启动。

## API接口

### 流式聊天接口

````http 
POST /chat Content-Type: application/json
{ "prompt": "你好，介绍一下你自己" }
````
返回SSE流式响应，实时输出AI回答内容。

### 阻塞聊天接口

````http
POST /blockingChat Content-Type: application/json
{ "prompt": "你好，介绍一下你自己" }
````

返回完整的AI回答内容。

## 配置说明

主要配置项位于 [application.properties](file:///Users/bob/ai/spring-ai-demo/src/main/resources/application.properties) 文件中：

- `spring.ai.chat.chunk-size`: 控制流式输出的文本块大小，默认为100字符

## 核心组件

- [SpringAiDemoApplication](file:///Users/bob/ai/spring-ai-demo/src/main/java/com/wangyousong/practice/springaidemo/SpringAiDemoApplication.java#L5-L12) - 应用启动类
- [ChatController](file:///Users/bob/ai/spring-ai-demo/src/main/java/com/wangyousong/practice/springaidemo/controller/ChatController.java#L14-L32) - 聊天控制器，提供REST API接口
- [ChatService](file:///Users/bob/ai/spring-ai-demo/src/main/java/com/wangyousong/practice/springaidemo/service/ChatService.java#L9-L68) - 聊天服务，处理AI交互逻辑
- [ChatRequest](file:///Users/bob/ai/spring-ai-demo/src/main/java/com/wangyousong/practice/springaidemo/request/ChatRequest.java#L6-L10) - 聊天请求数据传输对象

## 项目结构

src/ ├── main/ 
│ ├── java/ 
│ │ └── com/wangyousong/practice/springaidemo/ 
│ │ ├── controller/ # 控制器层 
│ │ ├── request/ # 请求DTO 
│ │ ├── service/ # 业务服务层 
│ │ └── SpringAiDemoApplication.java # 启动类 
│ └── resources/ 
│ └── application.properties # 配置文件 
└── test/ # 测试代码

## 测试

运行单元测试：

````bash
./gradlew test
````

## 部署

构建可执行JAR包：

````bash 
./gradlew bootJar
````

运行构建的JAR包：
````bash
java -jar build/libs/spring-ai-demo-0.0.1-SNAPSHOT.jar
````
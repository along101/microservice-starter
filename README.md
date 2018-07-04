# 微服务starter

### starter简介

微服务starter是基础框架部研发的一款基于spring-boot的starter。

开发人员只需在工程中引入microservice-starter的jar包，编写一个main方法，即可启动应用。

微服务starter是接入拍拍贷基础设施的启动工程，集成了：

    microservice-starter-apollo集成配置中心
    microservice-starter-cat集成cat监控
    microservice-starter-eureka集成服务发现中心
    microservice-starter-log集成日志
    microservice-starter-metric集成metric监控
    microservice-starter-archaius集成archaius动态配置
    microservice-starter-cachecloud集成缓存云
    microservice-starter-raptor集成rpc框架raptor
    
### starter包含：

1. spring-boot基础包的引用

2. 基础框架团队开发的二方包

3. 标准的三方包

4. 基础框架团队开发的UTIL工具类

### 注意项：

##### 应用中的必须包含内容：

1. spring boot环境配置中，且其中必须包含com.along101.appId和spring.application.name两个配置项。





# team_oa
为[葫芦侠社区](http://www.huluxia.com/)技术分享板块提供的团队管理系统。

使用地址：https://oa.jsls9.top

本仓库为后端仓库，前端仓库为[team_oa_web](https://github.com/subei12/team_oa_web)

### 技术栈
springboot、shiro、mybatis等

### 部署脚本
- [sql脚本](/oa.sql)


### 前端
使用百度开源项目：[AMIS](https://github.com/baidu/amis)

Nginx配置：
```bash
#team-oa
server {
    listen       8082;
    server_name  localhost;

    charset utf-8;

    #access_log  logs/host.access.log  main;
    
    location / {
        root   D:/wordspace_idea/git/Team-OA-WEB;
        index  index.html index.htm;
    }
    location /api {
        proxy_set_header Host $host;
        proxy_set_header X-Forwarded-Host $server_name;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        #开启错误拦截配置,一定要开启
        #proxy_intercept_errors on;
        #error_page 404 500 502 503 504 =200 http://localhost:8082/login.html;
        proxy_pass http://localhost:8081/api/;
        root   html;
        index  index.html index.htm;
    }
}
```

主要功能：

1. 帖子管理（结算）
2. 我的信息
3. 团队人员管理
4. 积分兑换
5. 团队预算管理
6. 上报管理

更新日志：
见commits

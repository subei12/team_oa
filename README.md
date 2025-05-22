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

## Docker Compose Deployment (Frontend + Backend)

This is the recommended way to deploy the application as it includes both the frontend (served by Nginx) and the backend API.

### Prerequisites

*   Docker and Docker Compose installed on your system.
*   The backend application JAR file built (e.g., by running `mvn package` if you haven't built the backend image separately).
*   A backend configuration file `application.yaml` placed in the root of this project.
    *   **Important:** Ensure settings within `application.yaml` are correct for a Dockerized environment. For example, if your database is also running in Docker on the same Docker network, the database host might be the service name of the database container (e.g., `jdbc:mysql://db_container_name/team_oa`). If the database is external, ensure it's accessible from the Docker containers.
*   (Optional) An Nginx configuration file named `nginx.conf` placed in the root of this project if you wish to override the default Nginx settings provided in `frontend/default.conf`. For most cases, the default should suffice.

### Building and Running

1.  Navigate to the project's root directory (where `docker-compose.yml` is located).
2.  Run the following command to build the images (if not already built) and start the services:

    ```bash
    docker-compose up --build -d
    ```
    *   `--build`: Forces Docker Compose to build the images before starting the containers. This is useful if you've made changes to the Dockerfiles or application code.
    *   `-d`: Runs the containers in detached mode (in the background).

### Accessing the Application

Once the containers are running, the application should be accessible via the Nginx frontend at:
`http://localhost:8082`

API requests to `http://localhost:8082/api/...` will be proxied by Nginx to the backend service.

### Managing the Services

*   **View Logs:**
    *   For the frontend (Nginx): `docker-compose logs frontend`
    *   For the backend API: `docker-compose logs backend`
    *   Combined logs: `docker-compose logs`
*   **Stop Services:**
    ```bash
    docker-compose down
    ```
    This command stops and removes the containers, networks, and volumes defined in the `docker-compose.yml` (unless volumes are declared as external).
*   **Start Services (without rebuilding):**
    ```bash
    docker-compose up -d
    ```
*   **Stop Services (without removing):**
    ```bash
    docker-compose stop
    ```

## Docker Deployment (Backend Only)

This application can be deployed using Docker.

### Prerequisites

*   Docker installed on your system.
*   The application JAR file built (e.g., by running `mvn package`).

### Building the Image

1.  Navigate to the project's root directory (where the `Dockerfile` is located).
2.  Run the following command to build the Docker image:

    ```bash
    docker build -t team_oa .
    ```
    (You can replace `team_oa` with your preferred image name/tag).

### Running the Container

To run the application in a Docker container, you'll need your `application.yaml` configuration file.

1.  Place your `application.yaml` file in a directory on your host machine (e.g., `/path/to/your/config/`).
2.  Create a directory on your host machine for logs (e.g., `/path/to/your/logs/`).
3.  Run the following command:

    ```bash
    docker run -d \
        -p 8081:8081 \
        -v /path/to/your/config/application.yaml:/app/config/application.yaml \
        -v /path/to/your/logs:/app/logs \
        --name team_oa_app \
        team_oa
    ```

    **Explanation of the command:**
    *   `-d`: Runs the container in detached mode (in the background).
    *   `-p 8081:8081`: Maps port 8081 of the host to port 8081 of the container.
    *   `-v /path/to/your/config/application.yaml:/app/config/application.yaml`: Mounts your local `application.yaml` into the container at `/app/config/application.yaml`. **Remember to replace `/path/to/your/config/application.yaml` with the actual path to your file.**
    *   `-v /path/to/your/logs:/app/logs`: Mounts a local directory for storing logs from the application. **Remember to replace `/path/to/your/logs` with the actual path to your desired log directory.**
    *   `--name team_oa_app`: Assigns a name to the running container for easier management.
    *   `team_oa`: The name of the image to run (which you used when building).

### Accessing the Application

Once the container is running, you should be able to access the application at `http://localhost:8081/api` (or your configured server address and port if different).

### Managing the Container

*   To view logs: `docker logs team_oa_app`
*   To stop the container: `docker stop team_oa_app`
*   To start a stopped container: `docker start team_oa_app`
*   To remove the container (after stopping): `docker rm team_oa_app`

更新日志：
见commits

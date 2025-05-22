# Use an official Nginx image as a parent image
FROM nginx:alpine

# Install git
RUN apk add --no-cache git

# Clone the frontend static content
# IMPORTANT: This clones the master branch by default. If a specific tag/branch is needed, adjust the command.
RUN git clone https://github.com/subei12/team_oa_web.git /usr/share/nginx/html/app_content     && mv /usr/share/nginx/html/app_content/* /usr/share/nginx/html/     && rm -rf /usr/share/nginx/html/app_content

# Copy the Nginx configuration file
# This file (default.conf) will be created in the next step and placed in the frontend directory locally
COPY default.conf /etc/nginx/conf.d/default.conf

# Expose port 8082 (as specified by the user)
EXPOSE 8082

# Command to run Nginx
CMD ["nginx", "-g", "daemon off;"]

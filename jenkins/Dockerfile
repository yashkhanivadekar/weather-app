FROM jenkins/jenkins:lts

USER root

# Install docker and docker-compose
RUN apt-get update && \
    apt-get install -y docker.io docker-compose curl && \
    curl -SL https://github.com/docker/compose/releases/download/v2.24.6/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose && \
    apt-get clean

# Allow docker.sock access from host
VOLUME /var/run/docker.sock

USER jenkins
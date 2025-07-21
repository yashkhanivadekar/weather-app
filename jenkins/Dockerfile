FROM jenkins/jenkins:lts

USER root

# Install docker and docker-compose
RUN apt-get update && \
    apt-get install -y docker.io docker-compose curl && \
    usermod -aG docker jenkins && \
    chown -R jenkins:jenkins /var/run/docker.sock && \
    apt-get clean

# Allow docker.sock access from host
VOLUME /var/run/docker.sock

USER jenkins
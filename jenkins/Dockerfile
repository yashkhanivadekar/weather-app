FROM jenkins/jenkins:lts

USER root

# Install docker and docker-compose
RUN apt-get update && \
    apt-get install -y docker.io docker-compose curl && \
    apt-get clean

# Allow docker.sock access from host
VOLUME /var/run/docker.sock

USER jenkins
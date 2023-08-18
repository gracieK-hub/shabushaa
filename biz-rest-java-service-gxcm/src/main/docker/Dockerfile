FROM java:8
ADD demo-java-service.tar.gz /
ADD entrypoint.sh /
RUN chmod +x /entrypoint.sh
EXPOSE 9001
ENTRYPOINT ["/entrypoint.sh"]
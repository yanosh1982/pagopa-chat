FROM java:8
WORKDIR /
ADD target/pagopa-chat-shaded.jar pagopa-chat.jar
EXPOSE 10000
CMD java -jar pagopa-chat.jar
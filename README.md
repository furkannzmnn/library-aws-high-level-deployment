Uygulamayı çalıştırmak için aşağıdaki adımları takip ediniz.

1 ) AWS CLI indirin. https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html

2 ) Projenin olduğu diziye gelip sırasıyla şu komutları çalıştırın.

    - mvn clean install -D skipTest
    - docker build -t lib .
    - docker-compose up -d
    - Terminalden çalıştırın .\init-aws.sh
    - docker-compose up -d

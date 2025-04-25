# t1-repo
для запуска проекта необходимо выполнить 2 команды в терминале: 

```mvn clean package -DskipTests```
* Maven соберет ваш проект и создаст JAR-файл в папке target/.


```docker-compose up --build```
* Создаст Docker-образы для PostgreSQL и вашего приложения.
* Запустит их в контейнерах.

Покрытие тестами : ![img_1.png](img_1.png)



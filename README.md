# Getting Started

## Requirements

## Startup

## API testing
If no users are specified in any repositories, the following users will be populated
* username: bilbo, password: p
* username frodo, password: p

One can attempt to test functionality with cURL

You can get a session started and saved to a file with
`curl -i -H "Content-type: application/x-www-form-urlencoded" -c cookies.txt -X POST http://localhost:8080/login -d "username=bilbo&password=p"`
Then use the same session to use the API
`curl -i -b cookies.txt -X GET http://localhost:8080/employees`


# Used guides
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.2/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-jpa-and-spring-data)

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

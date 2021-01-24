# Getting Started

This project is for a technical assessment made by 
Vytautas Vėgėlė (vytautasveg at gmail dot com)

## Requirements

[Maven](https://maven.apache.org/download.cgi) is required to setup and run this project.
Here is the full environment used with the Java version:
```
Apache Maven 3.6.3
Maven home: /usr/share/maven
Java version: 11.0.9.1, vendor: Ubuntu, runtime: /usr/lib/jvm/java-11-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.8.0-40-generic", arch: "amd64", family: "unix"
```

This project uses Spring Boot version 2.4.2. With some included configurations, the project uses the following technologies:
* H2 used as a data base in memory (default)
* Hibernate is used to communicate with the database (default)
* User sessions are used 



## Startup

Simply run it with maven using this command in project root where `pom.xml` is located:

`mvn spring-boot:run`


## API testing (using cURL)

### Default data
If no users are specified in any repositories, the following users will be populated
* username: user1, password: p
* username user2, password: p

One can attempt to test functionality with cURL

### Register user
You can register a new user with
```
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data \
  '{"email": "test1", "password": "p"}' "http://localhost:8080/register"
```

### Login user
You can get a session started and saved to a file with
```
curl -i \
-H "Content-type: application/x-www-form-urlencoded" \
-c cookies.txt \
-X POST http://localhost:8080/login -d "username=test1&password=p"
```

### Create blog entry
Then use the same session to use the API. Start with creating a blog entry
```
curl -i -b cookies.txt \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data \
'{"title": "testpost", "content": "sample content text"}' "http://localhost:8080/blogs"
```

### Browse blog entries
Review your blog entries
```
curl -i -b cookies.txt \
-H "Accept: application/json" \
-X GET http://localhost:8080/blogs
```
This, due to requirements, will only return the blog entries owned by your currently logged-in user.

### Update blog entries
Update the title and content of your blog entry. If no other entries were added,
the id and the path should include the number '4'
```
curl -i -b cookies.txt \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X PUT --data \
'{"title": "testpost", "content": "changed sample content text"}' \
"http://localhost:8080/blogs/4"
```

### Delete blog entry
```
curl -i -b cookies.txt \
-H "Accept: application/json" \
-X DELETE http://localhost:8080/blogs/4
```

# Used guides
These are most of the data sources used in learning Spring and setting up this application:

* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

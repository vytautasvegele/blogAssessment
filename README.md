# Getting Started

This project is for a technical assessment made by 
Vytautas Vėgėlė (vytautasveg@gmail.com)

## Requirements



## Startup



## API testing

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
'{"title": "testpost", "content": "sample content text"}' "http://localhost:8080/post"
```

### Browse blog entries
Review your blog entries
```
curl -i -b cookies.txt \
-H "Accept: application/json" \
-X GET http://localhost:8080/blogs
```
This, due to requirements, will only return the blog entries owned by your currently logged-in user.

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

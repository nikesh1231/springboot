
## Coding Standards


### Naming conventions

#### Code

- Use camel case for classes with a capital e.g. VelocityResponseWriter
- Use lower cases for packages e.g. com.company.project.ui
- Use camel case for variables with no capital e.g. velocityResponseWriter
- All caps for constants e.g. MAX_PARAMETER_COUNT
- Avoid using snake case _ except for enums and constants

#### REST api input and output

- Use CamelCase
- Do not use _

```
{
  "thisPropertyIsAnIdentifier": "identifier value"
}
```

### Database connections

Use reactive hiberate to connect to the database. Do not add
- jdbc
- r2dbc

### Authentication

All endpoints require an authentication token (jwt) except:
- /api/login
- /api/users/signup
- /api/health

### Endpoint - RouterFunction vs Controller

Use router functions. 

### Block operation

Avoid using block where possible because it stops the operation. Webflux only has a fixed number of threads and this will degrade performance. 
Use flatmap with mono and flux.

Example:
```
Api1Response response = webClient1.getApi1(request.getId()).block()
```

https://stackoverflow.com/questions/52744418/spring-webflux-difference-between-block-flatmap-and-subscribe

### Swagger

The swagger will be available on your machine
http://localhost:5000/webjars/swagger-ui/index.html

It is important that all endpoints and fields are documented because these are used by other developers building front end and mobile.

There are three places to manage documentation
1. handler - documents the api -> See UserHandler.java
2. response object (or handler response parameter) -> See UserDetails.java
3. request object (or handler request parameter) -> See CreateUserCommand.java

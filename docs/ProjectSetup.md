
## Project Setup

### What to expect at startup
Hibernate will configure all tables and database relationships.

The data initializer will pre populate data

### Environment Variables

The following environment variables will need to be configured (intellij / eclipse / vscode):

MYSQL_URL=localhost:3306/ocpp

MYSQL_DB_USERNAME=dbadmin

MYSQL_DB_PASSWORD=password

The values provided are examples

### Java
Install java SDK Amazon Coretto 11

https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html

*Reason*

We will be deploying to AWS using Amazon Coretto 11

### Database
Install MySQL 8.0. The minor version on AWS is 8.0.28.

A mysql database will need to be setup
- Create a database called the same as the database in the username e.g. ocpp
- Create a user with the same username and password as the environment variables below. e.g. dbadmin and password
- Provide privledges to the user _GRANT ALL PRIVILEGES ON ocpp.* TO 'dbadmin'@'localhost'_

### Database Connection

Use hibernate reactive where possible.

### Swagger

Front end development relies upon these endpoints and it is important they can access the swagger

# credit-card-validator
A microservice for user data access

## Motivation
Expedia Technical test

## Installation
### Pre-request 
- Maven installation

### Build setup
Open a command window, as administrator, navigate to the root directory of the project,  and run 
> mvn clean install

## Tests

> mvn test

## Usage
The application can be run with SpringBoot, there is no need to use a separate Tomcat.

The simplest way to run the application is

> mvn spring-boot:run

In the web browser type in the following URL;
http://localhost:8080/api/credit-card/4547422007055103?expiry_date=04%2f19



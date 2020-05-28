# The application is using Java 8, Spring Boot and Maven
A simple mvn spring-boot:run from the project folder should be sufficient to get it running.

# Real Life Scenario testing
Open a terminal after the application started and run:
    
    curl -X POST http://127.0.0.1:8080/register -H 'Content-Type: application/json' -d '{"username": "BobFrench", "password": "Password1", "dob": "1980-02-21", "paymentCardNumber": "349293081054422"}'

This should create an entry in HS that can be verified by accessing H2 console in:
    
    http://localhost:8080/h2-console/
    
Remembering to replace the JDBC URL field with
    
    jdbc:h2:mem:cashierdb

# Persistance
To add a bit more realism an H2 database is in use which could be easily replaced by a more robust data base 
as Spring DATA JPA was used for decoupling the persistance layer. 
With H2 being an in memory DB that is started with the spring boot start up process the
data will be lost when the application is down.

# Validations and HTTP return status codes
- For the basic validations, Spring will return the required HTTP 400 code 
``with additional information in the JSON format for the following:

    ·​ ​Username - alphanumeric, no spaces;
    ·​ ​Password – min length 4, at least one upper case letter & number;
    ·​ ​DoB (Date of Birth) - ISO 8601 format;
    ·​ ​Payment Card Number – between 15 and 19 digits;
    

- For the business rules the same will happen with the following status codes:
    
    . 403 for under aged users (under 18);
    . 409 for users trying to register with an already registered username;
    . 406 for users with a blocked INN credit card;
    
#INN Blocked List
The list with the INN numbers that should be rejected can be configured via the application.properties. 
Although in a full size application I would definitely have a separate property file for a case like this. 
For this specific case I decided to keep it concise to one property file.

#Error Messages
It is not clear whether the error messages are required to be thrown along with the HTTP status code, 
if these messages were supposed to be used by the client application these should be encapsulated in a message bundle.
I decided to keep it simple for this tech test purpose. 

#Integration Tests
The integration tests is implemented using REST-assured.
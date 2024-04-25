Project Description:
- Provide API's to register a user.
- Verification with OTPs to phone/email

The Architecture of the project:

![Registration-Architecture.svg](./Registration-Architecture.svg)

Architecture explained:
1. [x] Client sends a request with the appropriate details to /api/v1/singup route
2. [x] Then Server send the Client details to Messaging Server
3. [x] Now the Messaging Server generates an OTP and adds to Messaging Queue of Redis 
and send back a response request is added to Message queue
4. [x] There will be workers(Other Application) to processes the MessageNode(which is pushed by Messaging Server)
5. [x] Now these workers HSET to Redis with User details and OTP. And sends Email to the client with OTP.
6. [x] There are two conditions here:
	1. If client enters the OTP: 
		- 7.1: Then the HTTP server pops out the user details from Redis
	2. If client doesn't enter: 
		- 7.2: As the response comes back in step 3. A setTimeOut is added for 5 mins
	   This will pops out the user details from Redis.
8. [x] Now the User data is added to Database after verification.

Prerequisites:
    - Java
    - maven
    - node
    - docker
    - less secure gmail account

Setup:
1. Install all the packages in every sub-directory:
    1. **api:**
        mvn clean install (or you can use any IDE to install packages)
    2. **message-server:**
        - for npm:
            npm i
        - for pnpm:
            pnpm i
    3. **message-worker:**
        - for npm:
            npm i
        - for pnpm:
            pnpm i
        
2. Run the program:
    1. run the containers in docker:
        - docker run --name <db_name> -e POSTGRES_PASSWORD=<your_db_password> -d -p 5431:5432 postgres 
        - docker run -d -p 6379:6379 redis 
    2. change the values in ./api/src/main/resources/application.properties
        - change spring.datasource.url=jdbc:postgresql://localhost:5431/postgres
        - change spring.datasource.password=<your_db_password>
    3. add the less secure google account:
        - create a file: ./message-worker/.env
        - add EMAIL_USER=<less_secure_gmail_id>
        - add EMAIL_PASS=<less_secure_gmail_password>
    4. run all the sub-directories:
        api:

            cd api
            mvn compile (better to use an IDE like intellij IDEA)
            mvn package
            java -jar target/gs-maven-0.1.0.jar

        message-server:
            
            cd ../message-server
            tsc -b
            node ./dist/index.js

        message-worker:

            cd ../message-worker
            tsc -b
            node .dist/index.js

That's it you now start all the server:
you can use

    1.  {base-url}/api/v1/signup
        Description: to get the otp to signup
        type: POST
        body: 
        {
            "username": <username>,
            "email": <email>,
            "password": <password>, // This should be pre-hashed in frontend
            "mobile": <mobile_number>
        }

    2.  {base-url}/api/v1/VerifyOTP
        Description: to verify the otp sent to email
        type: POST
        body:
        {
            "user":{
                "username": <username>,
                "email": <email>,
                "password": <password>, // This should be pre-hashed in frontend
                "mobile": <mobile_number>
            },
            "otp": <otp_sent>
        }
       
    3.  {base-url}/api/v1/getUsers
        Description: this is for just Development purpose, delete in production
        type: GET

Future-work:
1. Add rate-limiting
2. Add recapture

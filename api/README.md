Project Description:
- Provide API's to register a user.
- Verification with OTPs to phone/email

The Architecture of the project:

![Registration-Architecture.svg](./Registration-Architecture.svg)


Architecture explained:
1. [x] Client sends a request with the appropriate details to /api/v1/singup route
2. [x] Then Server [send](https://blog.tericcabrel.com/http-requests-springboot-rest-template/) the Client details to Messaging Server
3. [x] The following things are done in step 3:
   1. [x] Generate OTP
   2. [x] Add to Messaging Queue(Redis)
   3. [x] Send back a response request is added to Message queue
4. [x] Workers get Message Node from Message queue
5. The following things are done in step 5:
   1. [x] SET to Redis
   2. [x] Send [Emails](https://nodemailer.com/) to client with OTP
6. There are two conditions here:
   1. If client enters the OTP:
      - 7.1: Then the HTTP server pops out the user details from Redis
   2. If client doesn't enter:
      - [x] 7.2: As the response comes back in step 3. A [setTimeOut](https://stackoverflow.com/questions/26311470/what-is-the-equivalent-of-javascript-settimeout-in-java) is added for 5 mins
            This will pops out the user details from Redis.
7. Now the User data is [added](https://github.com/ManikantaMandala/learningJava/tree/day7) to Database after verification.
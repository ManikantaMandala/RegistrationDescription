Project Description:
- Provide API's to register a user.
- Verification with OTPs to phone/email

The Architecture of the project:

![Registration-Architecture.svg](./Registration-Architecture.svg)

Architecture explained:
1. Client sends a request with the appropriate details to /api/v1/singup route
2. Then Server send the Client details to Messaging Server
3. Now the Messaging Server generates an OTP and adds to Messaging Queue of Redis 
and send back a response request is added to Message queue
4. There will be workers(Other Application) to processes the MessageNode(which is pushed by Messaging Server)
5. Now these workers HSET to Redis with User details and OTP. And sends Email to the client with OTP.
6. There are two conditions here:
	1. If client enters the OTP: 
		- 7.1: Then the HTTP server pops out the user details from Redis
	2. If client doesn't enter: 
		- 7.2: As the response comes back in step 3. A setTimeOut is added for 5 mins
	   This will pops out the user details from Redis.
8. Now the User data is added to Database after verification.

 

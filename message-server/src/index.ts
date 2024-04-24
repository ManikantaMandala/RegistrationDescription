import express from 'express';
import { createClient } from 'redis';

const app = express();
const redisClient = createClient();
const port = 4000

app.use(express.json());

app.post("/otp", async function ForOtp(req, res){
	// Generate OTP of 6 letters
	let otp = generateOTP();
	while(otp < 1000){
		otp = generateOTP();
	}
	const {username, mobile, email, password} = req.body;
	
	try{
		// Push the OTP and userDetail to Redis Queue
		// use redisClient.isReady to check whether it is connected or not
		if(!redisClient.isReady){
			throw new Error("Redis is not connected");
		}
		const value = JSON.stringify({
			username,
			mobile,
			email,
			password,
			otp
		});
		await redisClient.lPush(`message`, value);
		res.status(200).json({
			message: `OTP is pushed to the queue`
		});
	}
	catch(e){
		console.log(e);
	}
	
});

function generateOTP(){
	let otp = 0;
	for(let i=0; i<4;i++){
		otp = otp * 10 + Math.floor(Math.random() * 10);
	}
	console.log(otp);
	return otp;
}

async function StartMessageServer(){
	try{
		await redisClient.connect();

		app.listen(port , () => {
			console.log(`Server is running on port ${port}`);
		});
	}
	catch(e){
		console.log(e);
	}
}
StartMessageServer();

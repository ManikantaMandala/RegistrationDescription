import { createClient } from "redis";
import nodemailer from "nodemailer";
import dotenv from "dotenv";

const client = createClient();
dotenv.config();

// TODO: Change host and auth (See options)
const transporter = nodemailer.createTransport({
	host: "smtp.gmail.com",
	port: 465,
	secure: true,
	auth: {
		user: process.env.EMAIL_USER,
		pass: process.env.EMAIL_PASS
	},
});

async function createWorker(){
	try{
		await client.connect();
		while(true){
			// 4.RPOP from the queue
			const messageNode = await client.brPop("message", 0);
			// Set Redis key
			// @ts-ignore
			const {username, email, otp} = JSON.parse(messageNode?.element);
			// @ts-ignore
			processNode(messageNode?.element, username, email);

			// 5. Send to user
			sendEmail(email, username, otp);
		}
	}catch(e){
		console.log(e);
	}
}

async function sendEmail(userEmail:string, user:string, otp: number){
	try{
		const info = await transporter.sendMail({
			from: '"Maddison Foo Koch 👻" <maddison53@ethereal.email>', // sender address
			to: userEmail, // list of receivers
			subject: "Here is the OTP", // Subject line
			html: `
			<div>
				<header>
					<b>Hello ${user}</b>
				</header>
				<main>
				Here is the OTP: <b>${otp}</b>
				</main>
			</div>
			`,
		});
		console.log(info.messageId);
	}catch(e){
		console.log(e);
	}
};

async function processNode(node:string, userName:string, email:string){
	try{
		// make this format string
		console.log(`before setting ${userName}| ${email}| ${node}`);
		// 5. Set the key in Redis
		const result = await client.set(`${userName}:${email}`, node);
		console.log(result);
		console.log(`after setting ${userName}| ${email}`);
		return;
	}catch(e){
		console.log(e);
	}
};

createWorker();

package com.areksoft.api.services;

import com.areksoft.api.db.UserDb;
import com.areksoft.api.models.OTPRequest;
import com.areksoft.api.models.OTPResponse;
import com.areksoft.api.models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private UserDb db;
    RestTemplate restTemplate;
    HttpHeaders headers;

    private String redisUrl;
    private int redisPort;

    @Autowired
    UserService(UserDb db){
        this.db = db;
        this.restTemplate = new RestTemplate();

        this.redisUrl = "localhost";
        this.redisPort = 6379;

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

//    public static void setTimeout(Runnable runnable, int delay){
//        new Thread(() -> {
//            try {
//                Thread.sleep(delay);
//                runnable.run();
//            }
//            catch (Exception e){
//                System.err.println(e);
//            }
//        }).start();
//    }

    public String SignUp(User user){
        // Send to message server to message OTP and add to redis
        // Send a HTTP request to localhost:4000/otp
        // get the response
        // Start a setTimeout

        OTPRequest otpRequest = new OTPRequest();
        otpRequest.setUsername(user.getUsername());
        otpRequest.setEmail(user.getEmail());
        otpRequest.setPassword(user.getPassword());
        otpRequest.setMobile(user.getMobile());

        HttpEntity<OTPRequest> req = new HttpEntity<>(otpRequest, headers);
        String url = generateUrl("/otp");
        System.out.println(url);

        ResponseEntity<OTPResponse> res = restTemplate.postForEntity(url, req, OTPResponse.class);
        OTPResponse body = res.getBody();

        System.out.println(body);

        int delay = 45;
        CompletableFuture
                .delayedExecutor(delay, TimeUnit.SECONDS)
                .execute(()->{
                    // Del the key value pair from redis
                    // Key : ${username}:${email}
                    JedisPool jedisPooled = new JedisPool(redisUrl, redisPort);
                    try(Jedis jedis = jedisPooled.getResource()){
                        long returnValue = jedis.del(user.getUsername()+":"+user.getEmail());
                        System.out.println(returnValue + "After " + delay + " seconds" );
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                });

        assert body != null;

        return body.getMessage();
    }
    private String generateUrl(String route){
        String baseUrl = "http://localhost:4000/";
        return baseUrl + route;
    }
    public String VerifyOTP(User user, Integer otp){
        // Verify the OTP from the Redis and the params
        // after verification of OTP
        // check whether it satisfies the unique requirements of properties
        // call addUser();
        JedisPool pool = new JedisPool(redisUrl, redisPort);
        try(Jedis jedis = pool.getResource()){
            // get key-value pair from redis and parse to JSON
            String result = jedis.get(user.getUsername()+":"+user.getEmail());
            long resultDel = jedis.del(user.getUsername()+":"+user.getEmail());
            System.out.println("Del Result" +  resultDel);
            if(result == null){
                return "Please try again! one more time \ndoesn't have your key-value pair";
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(result);
            Integer RedisOtp = node.get("otp").asInt();
            if(RedisOtp.equals((otp))){
                System.out.println("Before adding user"+ user);
                User newUser = addUser(user);
                if(newUser == null){
                    return "User already exist";
                }
                else{
                    return "Verification successful, You are add to database";
                }
            }
            else{
                return "Verification failed";
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return "Connection failed with redis, statusCode: 500 -> Internal error";
    }

    private User addUser(User user){
        try{
            return db.save(user);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    public List<User> getUsers(){
        return db.findAll();
    }
}

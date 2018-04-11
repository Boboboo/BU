package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import redis.clients.jedis.Jedis;

public class redisTest {

	public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        
        try {
            File f = new File("/Users/air/Desktop/l.txt");
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";
            System.out.println("Reading Start...");
            
            while ((readLine = b.readLine()) != null) {
                String [] array=readLine.split(" ");
                	System.out.println(array[0]+" "+array[1]);
                	jedis.lpush("list", array[1]);  
            }
		    
        } catch (IOException e) {
            e.printStackTrace();
        }	

        List<String> list = jedis.lrange("list", 0 ,1);
        for(int i=0; i<list.size(); i++) {
            System.out.println("列表项为: "+list.get(i));
        }
	}
	

}

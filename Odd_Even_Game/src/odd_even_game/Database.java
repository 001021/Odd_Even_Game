package odd_even_game;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Database {

	boolean checkDuplicate(String s) {
      try {
    	  String[] array;
    	  String fileName = "userDB.txt";
    	  FileReader fr = new FileReader(fileName);
    	  String line="";
    	  BufferedReader br = new BufferedReader(fr);
    	  
    	  while((line=br.readLine())!=null){
    		  array=line.split("/");
    		  if(s.equals(array[0])==true) {
    			  return true;
    		  }
    	  }
    	  
    	  br.close();
         }catch(IOException e) {
            System.out.println("File not found");
         }
      return false;
    }
    
    
    //사용자 정보 추가
    boolean addUser(String id, String Password, String name, String nickName, String email, String sns, String ip, String time) {
       
       if(checkDuplicate(id)) {
          System.out.println("Duplicate id");
          return false;
       }
       
       try {
    	   BufferedWriter bw = new BufferedWriter(new FileWriter("userDB.txt",true));
    	   bw.write("\n"+id+"/"+Password+"/"+name+"/"+nickName+"/"+email+"/"+sns+"/"+ip+"/"+time+"/1/0/0");
    	   bw.close();
       }catch(IOException e) {
    	   System.out.println("File not found");
    	   return false;
       }
       return true;
    }
    
    
    //로그인 id,pw 매칭
    static boolean IsValid(String id, String password){
       
       try {
    	   String[] array;
    	   String fileName = "userDB.txt";
    	   FileReader fr = new FileReader(fileName);
    	   
    	   String line="";
    	   BufferedReader br = new BufferedReader(fr);
    	   
    	   while((line=br.readLine())!=null){
    		   array=line.split("/");
    		   if(id.equals(array[0])==true) {
    			   if(password.equals(array[1])==true) {
    				   System.out.println("로그인 성공");
    				   return true;
    			   }
    			   else {
    				   System.out.println("로그인 실패");
    				   return false;
    			   }
    		   }
    	   }
       } catch(IOException e) {
            System.out.println("File not found");
            return false;
       }
       return false;
    }
    
    String getUserInfo(String nickName){
       try {
    	   String[] array;
    	   String result;
    	   
    	   String fileName = "userDB.txt";
    	   FileReader fr = new FileReader(fileName);
    	   
    	   String line="";
    	   BufferedReader br = new BufferedReader(fr);
    	   
    	   while((line=br.readLine())!=null){
    		   array=line.split("/");
               if(nickName.equals(array[3])==true) {
                  result=nickName+": "+array[9]+"/"+array[10];
                  return result;
               }
    	   }
       }catch(IOException e) {
            System.out.println("File not found");
            return null;
       }
       return null;
    }
}
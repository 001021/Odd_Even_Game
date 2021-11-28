package odd_even_game;

import java.io.*;
import java.net.InetAddress;

public class Database {

   //ID, 닉네임 중복검사
   boolean checkDuplicate(String id, String nickName) {
      try {
         String[] array;
         String fileName = "userDB.txt";
         FileReader fr = new FileReader(fileName);
         String line="";
         BufferedReader br = new BufferedReader(fr);
         
         while((line=br.readLine())!=null){
            array=line.split("/");
            if(id.equals(array[0]) || nickName.equals(array[3])) {
               return true;
            }
         }
         
         br.close();
         }catch(IOException e) {
            System.out.println(e);
         }
      return false;
    }
    
    
    // 회원가입 정보 추가
    boolean addUser(String id, String password, String name, String nickName, String email, String sns, InetAddress inetAddress, String time) {
    	EnPassword secu = new EnPassword();
    	password = secu.encryptSHA256(password);
       
       if(id==null||password==null||name==null||nickName==null||email==null||sns==null||inetAddress==null)
          return false;
       
       if(checkDuplicate(id, nickName)) {
          System.out.println("Duplicate id, nickName");
          return false;
       }
       
       try {
          BufferedWriter bw = new BufferedWriter(new FileWriter("userDB.txt",true));
          bw.write(id+"/"+password+"/"+name+"/"+nickName+"/"+email+"/"+sns+"/"+inetAddress+"/"+time+"/1/0/0"+"\n");
          bw.close();
       }catch(IOException e) {
          System.out.println(e);
          return false;
       }
       return true;
    }
    
    
    //로그인
    boolean IsValid(String id, String password){
    	EnPassword secu = new EnPassword();
    	password = secu.encryptSHA256(password);
       
       try {
          String[] array;
          String fileName = "userDB.txt";
          FileReader fr = new FileReader(fileName);
          
          String line="";
          BufferedReader br = new BufferedReader(fr);
          
          while((line=br.readLine())!=null){
             array=line.split("/");
             if(id.equals(array[0])) {
                if(password.equals(array[1])) {
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
            System.out.println(e);
            return false;
       }
       return false;
    }
    
    //승패 조회
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
            System.out.println(e);
            return null;
       }
       return null;
    }
    
    //win+1
    void updateWin(String nickName){
        try {
            String[] array;
            
            BufferedReader br = new BufferedReader(new FileReader("userDB.txt"));
            
            String line = null;
            String temp = "";
            
            while((line = br.readLine()) != null) {
             array=line.split("/");
               if(nickName.equals(array[3])) {
                int win=Integer.parseInt(array[9])+1;
                array[9]=Integer.toString(win);
                temp += array[0]+"/"+array[1]+"/"+array[2]+"/"+array[3]+"/"+array[4]+"/"+array[5]+"/"+array[6]+"/"+array[7]+"/"+array[8]+"/"+array[9]+"/"+array[10]+"\n";
                continue;
                }
               temp += line+"\n";
               }
            BufferedWriter bw = new BufferedWriter(new FileWriter("userDB.txt"));
            bw.write(temp);
            bw.close();
              
            br.close();
            }catch(IOException e) {
              System.out.println(e);
         }
    }
    
    
    //lose+1
    void updateLose(String nickName){
       try {
         String[] array;
         
         BufferedReader br = new BufferedReader(new FileReader("userDB.txt"));
         
         String line = null;
         String temp = "";
         
         while((line = br.readLine()) != null) {
          array=line.split("/");
            if(nickName.equals(array[3])) {
             int lose=Integer.parseInt(array[10])+1;
             array[10]=Integer.toString(lose);
             temp += array[0]+"/"+array[1]+"/"+array[2]+"/"+array[3]+"/"+array[4]+"/"+array[5]+"/"+array[6]+"/"+array[7]+"/"+array[8]+"/"+array[9]+"/"+array[10]+"\n";
             continue;
             }
            temp += line+"\n";
            }
         BufferedWriter bw = new BufferedWriter(new FileWriter("userDB.txt"));
         bw.write(temp);
         bw.close();
           
         br.close();
         }catch(IOException e) {
           System.out.println(e);
           }
    }
    
    //마지막 접속시간, 방문횟수 업데이트
    void updateConnection(String nickName, String time){
       try {
             String[] array;
             
             BufferedReader br = new BufferedReader(new FileReader("userDB.txt"));
             
             String line = null;
             String temp = "";
             
             while((line = br.readLine()) != null) {
              array=line.split("/");
                if(nickName.equals(array[3])) {
                 array[7]=time;
                 int count=Integer.parseInt(array[8])+1;
                array[8]=Integer.toString(count);
                 temp += array[0]+"/"+array[1]+"/"+array[2]+"/"+array[3]+"/"+array[4]+"/"+array[5]+"/"+array[6]+"/"+array[7]+"/"+array[8]+"/"+array[9]+"/"+array[10]+"\n";
                 continue;
                 }
                temp += line+"\n";
                }
             BufferedWriter bw = new BufferedWriter(new FileWriter("userDB.txt"));
             bw.write(temp);
             bw.close();
               
             br.close();
             }catch(IOException e) {
               System.out.println(e);
               }
       }
    
    //닉네임검색
    String getNickName(String id){
       try {
          String[] array;
          String result;
           
          String fileName = "userDB.txt";
          FileReader fr = new FileReader(fileName);
           
          String line="";
          BufferedReader br = new BufferedReader(fr);
           
          while((line=br.readLine())!=null){
             array=line.split("/");
             if(id.equals(array[0])==true) {
                result=array[3];
                return result;
                }
             }
          }catch(IOException e) {
             System.out.println(e);
             return null;
             }
       return null;
    }
}
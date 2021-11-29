package odd_even_game;

import java.security.*;

public class EnPassword {

   public String encryptSHA256(String pw) { 	//Get the pw before encrypting
      String sha =""; 	//Where you receive encryption variables.
      try {
         MessageDigest sh = MessageDigest.getInstance("SHA-256");
         sh.update(pw.getBytes());
         byte byteData[]=sh.digest();
         StringBuffer sb = new StringBuffer();
         
         for(int i=0;i<byteData.length;i++) {	//Encryption in for
            sb.append(Integer.toString((byteData[i]&0xff)+0x100, 16).substring(1));
         }
         
         sha = sb.toString();
      }catch(NoSuchAlgorithmException e) {
         System.out.println(e);		//print Error
         sha=null;
      }
      return sha;
   }
}
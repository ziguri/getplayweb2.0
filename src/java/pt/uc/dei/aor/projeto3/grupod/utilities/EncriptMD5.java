

package pt.uc.dei.aor.projeto3.grupod.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EncriptMD5 {
    private static MessageDigest md;
    
    /**
     * Encript a string
     * @param pass
     * @return the encripted string
     */

   public static String cryptWithMD5(String pass){
    try {
        md = MessageDigest.getInstance("MD5");
        byte[] passBytes = pass.getBytes();
        md.reset();
        byte[] digested = md.digest(passBytes);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<digested.length;i++){
            sb.append(Integer.toHexString(0xff & digested[i]));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(EncriptMD5.class.getName()).log(Level.SEVERE, null, ex);
    }
        return null;


   }
}

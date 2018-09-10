package mx.gob.seguropopulartlax.supervision;

import android.util.Base64;

public class Tools {

    public String encode(String pass) {
        String s ="";
        try{
            byte[] b = pass.getBytes("UTF-8");
            s = Base64.encodeToString(b, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

}

package wings.app.microblog.util;

import java.util.HashMap;
import java.util.Map;

public class Http {

    public  static Map<String,Object> standardResponse(Object obj, Integer status, String success){
        Map<String,Object> re=new HashMap<>();
        re.put("data",obj);
        re.put("status",status);
        re.put("msg",success);
        return re;
    }

    public  static Map<String,Object> standardResponse(Integer status, String success){
        Map<String,Object> re=new HashMap<>();
        re.put("data",null);
        re.put("status",status);
        re.put("msg",success);
        return re;
    }

    public  static Map<String,Object> standardResponse(Object obj){
        Map<String,Object> re=new HashMap<>();
        re.put("data",obj);
        re.put("status",200);
        re.put("msg","success");
        return re;
    }

    public  static Map<String,Object> standardResponse(){
        Map<String,Object> re=new HashMap<>();
        re.put("data",null);
        re.put("status",200);
        re.put("msg","success");
        return re;
    }


}

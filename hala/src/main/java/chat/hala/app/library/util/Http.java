package chat.hala.app.library.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by astropete on 2017/12/27.
 */
public class Http {

    public static String requestPost(String requestUrl, Map<String, String> requestProperty, String bodyParameter) throws Exception, IOException, ProtocolException {
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        for(Map.Entry<String, String> m : requestProperty.entrySet()){
            conn.setRequestProperty(m.getKey(), m.getValue());
        }
        setBodyParameter(bodyParameter, conn);
        return returnResult(conn);
    }

    public static Map<String, String> getFacebookMember(String token) throws Exception {

        String request = "https://graph.facebook.com/me?access_token=" + token;
        URL url;
        url = new URL(request);
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        System.out.println("****** Content of the URL ********");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input;
        StringBuffer sb = new StringBuffer();
        while ((input = br.readLine()) != null){
            sb.append(input);
        }
        br.close();
        String result = sb.toString();
        Map<String, String> re = new HashMap<>();
        re.put("name", Json.getByKey(result, "name"));
        re.put("fid", Json.getByKey(result, "id"));
        return re;
    }


    public static Map<String, String> getGoogleMember(String token) throws Exception {
        String request = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token="+token;
        URL url=new URL(request);
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input;
        StringBuffer sb = new StringBuffer();
        while ((input = br.readLine()) != null){
            sb.append(input);
        }
        br.close();
        String result = sb.toString();
        Map<String,String> re =new HashMap<>();
        re.put("name", Json.getByKey(result, "name"));
        re.put("picture",Json.getByKey(result,"picture"));
        return re;
    }


   /* public static Map<String, String> getInstagramMember(String token) throws Exception {
        String request = "https://api.instagram.com/v1/users/self/media/recent/?access_token="+token;
        URL url=new URL(request);
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        System.out.println("****** Content of the URL ********");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input;
        StringBuffer sb = new StringBuffer();
        while ((input = br.readLine()) != null){
            sb.append(input);
        }
        br.close();
        String result = sb.toString();
        Map<String, String> re = new HashMap<>();
        re.put("name", Json.getByKey(result, "name"));
        re.put("picture",Json.getByKey(result,"picture"));
        return re;
    }*/



    public static JsonObject requestApple(String url, String receipt) throws Exception{
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setAllowUserInteraction(false);
        PrintStream ps = new PrintStream(connection.getOutputStream());
        ps.print("{\"receipt-data\": \"" + receipt + "\"}");
        ps.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String str;
        StringBuffer sb = new StringBuffer();
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        br.close();
        String resultStr = sb.toString();
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(resultStr, JsonElement.class);
        JsonObject result = element.getAsJsonObject();
        if (result != null && result.get("status").getAsInt() == 21007) {
            return requestApple(Constant.ApplePaySandboxUrl, receipt);
        }
        return result;
    }

    public static void setBodyParameter(String str, HttpURLConnection conn) throws IOException {
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(str.getBytes("utf-8"));
        out.flush();
        out.close();
    }

    public static String returnResult(HttpURLConnection conn) throws Exception, IOException {
        InputStream input = null;
        if (conn.getResponseCode() == 200) {
            input = conn.getInputStream();
        } else {
            input = conn.getErrorStream();
        }
        String result = new String(readInputStream(input), "UTF-8");
        return result;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    public static Map<String, String> generalResponse(){
        Map<String, String> re = new HashMap<>();
        re.put("status", "success");
        return re;
    }

    public static Map<String, Object> resultResponse(Object o){
        Map<String, Object> re = new HashMap<>();
        re.put("result", o);
        return re;
    }

    public static Map<String, Boolean> verifyFailed(){
        Map<String, Boolean> re = new HashMap<>();
        re.put("verified", false);
        return re;
    }

    public static Map<String, Object> coinResponse(Object o, Integer coins){
        Map<String, Object> re = new HashMap<>();
        re.put("entity", o);
        re.put("coins", coins);
        return re;
    }

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

package wings.app.microblog.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Json {


        private static final String KEYSEPERATOR = ".";

        public static String getByKey(String json, String key){
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(json, JsonElement.class);
            JsonObject j = element.getAsJsonObject();
            String index = "";
            if(key.contains(KEYSEPERATOR)) {
                String[] keys = key.split("\\.");
                int i = 0;
                for (String k : keys) {
                    if (i + 1 == keys.length) break;
                    j = (JsonObject) j.get(k);
                    i += 1;
                }
                index = keys[keys.length - 1];
            }else{
                index = key;
            }
            return j.get(index).getAsString();
        }


}

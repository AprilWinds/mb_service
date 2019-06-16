package chat.hala.app.library;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by astropete on 2018/3/19.
 */
public class Qiniu {

    private static final String ACCESS_KEY = "rL_wG6fxCwk_DlEuttqG81JeHBBFg7uHGxuzfkqj";
    private static final String SECRET_KEY = "WJMvASOFMEQGmucRPOVkC19k6iiftKvMEyIrYMpK";
    private static final String MEMBER_BUCKET = "member";
    private static final String ROOM_BUCKET = "room";
    private static final String MATERIAL_BUCKET = "material";
    private static final String ROOMSTYLE_BUCKET="roomstyle";




    public static Map issueToken(){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String memberToken = auth.uploadToken(MEMBER_BUCKET, (String)null, 864000L, (StringMap)null);
        String roomToken = auth.uploadToken(ROOM_BUCKET, (String)null, 864000L, (StringMap)null);
        String materialToken = auth.uploadToken(MATERIAL_BUCKET, (String)null, 864000L, (StringMap)null);
        String roomStyleToken=auth.uploadToken(ROOMSTYLE_BUCKET,(String) null,864000L,(StringMap) null );
        Map<String, String> result = new HashMap<String, String>();
        result.put("member", memberToken);
        result.put("room", roomToken);
        result.put("material", materialToken);
        result.put("roomStyle", roomStyleToken);
        return result;
    }
}

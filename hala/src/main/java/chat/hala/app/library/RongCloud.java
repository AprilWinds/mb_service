package chat.hala.app.library;

import chat.hala.app.entity.Gift;
import chat.hala.app.entity.Member;
import chat.hala.app.library.util.Encryption;
import chat.hala.app.library.util.Http;
import chat.hala.app.library.util.Json;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by astropete on 2017/12/27.
 */
public class RongCloud {

    private static final String APIURL = "http://api.cn.ronghub.com";
    private static final String APPKEY = "RC-App-Key";
    private static final String NONCE = "RC-Nonce";
    private static final String TIMESTAMP = "RC-Timestamp";
    private static final String SIGNATURE = "RC-Signature";
    private static final String HALAKEY = "e0x9wycfet5tq";
    private static final String HALASEC = "7eJLGVR8JkL";
    private static final String UTF8 = "UTF-8";
    private static final String OKCODE = "200";

    public Object getToken(Member m) throws Exception{
        String rb = this.tokenData(m);
        Map<String, String> rp = this.getRequestParam();
        String ru = APIURL + "/user/getToken.json";
        String re = Http.requestPost(ru, rp, rb);
        return Json.getByKey(re, "token");
    }

    public Boolean getOnline(Member m) throws Exception{
        String rb = "userId=" + m.getCharacterId();
        Map<String, String> rp = this.getRequestParam();
        String ru = APIURL + "/user/checkOnline.json";
        String re = Http.requestPost(ru, rp, rb);
        String online = Json.getByKey(re, "status");
        return online.equals("1");
    }

    public Boolean sendInOutNotice(Member m, Long roomId, Boolean inout) throws Exception{
        String message = m.getNickname() + " " + (inout ? "enter" : "leave") + " this room";
        String rb = "content={\"message\":\""+message+"\",\"extra\":\"\"}&fromUserId=0&toChatroomId="+String.valueOf(roomId)+"&objectName=RC:InfoNtf";
        Map<String, String> rp = this.getRequestParam();
        String ru = APIURL + "/message/chatroom/publish.json";
        String re = Http.requestPost(ru, rp, rb);
        return re.contains(OKCODE);
    }

    public Boolean sendRoomBlock(Member m, Long roomId) throws Exception{
        String rb = "userId="+m.getCharacterId()+"&chatroomId="+String.valueOf(roomId)+"&minute=1440";
        Map<String, String> rp = this.getRequestParam();
        String ru = APIURL + "/chatroom/user/block/add.json";
        String re = Http.requestPost(ru, rp, rb);
        return re.contains(OKCODE);
    }

    public Boolean sendRoomSilence(Member m, Long roomId) throws Exception{
        String rb = "userId="+m.getCharacterId()+"&chatroomId="+String.valueOf(roomId)+"&minute=30";
        Map<String, String> rp = this.getRequestParam();
        String ru = APIURL + "/chatroom/user/gag/add.json";
        String re = Http.requestPost(ru, rp, rb);
        return re.contains(OKCODE);
    }

    public Boolean sendRoomGift(Member m, Member t, Long roomId, Gift gift) throws Exception{
        String rb = "content={\"from\":\""+m.getNickname()+"\",\"to\":\""+t.getNickname()+"\",\"fromId:\":\""+m.getId().toString()+"\",\"toId\":\""+t.getId().toString()+"\",\"gid\":\""+gift.getId().toString()+"\",\"img\":\""+gift.getAvatarUrl()+"\",\"gif\":\""+ gift.getGifUrl()+"\"}&fromUserId="+m.getCharacterId()+"&toChatroomId="+String.valueOf(roomId)+"&objectName=CM:GiftInRoom";
        Map<String, String> rp = this.getRequestParam();
        String ru = APIURL + "/message/chatroom/publish.json";
        String re = Http.requestPost(ru, rp, rb);
        return re.contains(OKCODE);
    }



    public Boolean sendCustomerMessage(String message, String fromId, String toId) throws Exception{
        String rb = "content={\"content\":\""+message+"\"}&fromUserId="+fromId+"&toUserId="+toId+"&objectName=RC:TxtMsg&isIncludeSender=0";
        Map<String, String> rp = this.getRequestParam();
        String ru = APIURL + "/message/private/publish.json";
        String re = Http.requestPost(ru, rp, rb);
        return re.contains(OKCODE);
    }

    private String tokenData(Member m) throws IOException{
        StringBuilder sb = new StringBuilder();
        sb.append("&userId=").append(URLEncoder.encode(m.getCharacterId(), UTF8));
        sb.append("&name=").append(URLEncoder.encode(m.getNickname(), UTF8));
        sb.append("&portraitUri=").append(URLEncoder.encode(m.getAvatarUrl(), UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }
        return body;
    }

    private Map<String, String> getRequestParam(){
        Map<String, String> rp= new HashMap<>();
        rp.put(APPKEY, HALAKEY);
        rp.put(NONCE, String.valueOf(Math.random() * 1000000));
        rp.put(TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
        StringBuilder toSign = new StringBuilder(HALASEC).append(rp.get(NONCE)).append(rp.get(TIMESTAMP));
        rp.put(SIGNATURE, Encryption.SHA1ed(toSign.toString()));
        return rp;
    }


}

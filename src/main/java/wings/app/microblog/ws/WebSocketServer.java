package wings.app.microblog.ws;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.Msg;
import wings.app.microblog.entity.Notification;
import wings.app.microblog.repository.MemberRepository;
import wings.app.microblog.repository.MsgRepository;
import wings.app.microblog.repository.NotificationRepository;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint(value = "/wss/{mid}")
@RestController
public class WebSocketServer {


    private static MsgRepository    msgRepo;
    private static MemberRepository memberRepo;
    private static NotificationRepository notificationRepo;

    @Autowired
    public void setMemberRepo(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }
    @Autowired
    public void setMsgRepo(MsgRepository msgRepo) {
        this.msgRepo = msgRepo;
    }
    @Autowired
    public  void setNotificationRepo(NotificationRepository notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    public static CopyOnWriteArraySet<Session> set = new CopyOnWriteArraySet<Session>();
    public static Map<Long,Session>  map=new ConcurrentHashMap<Long, Session>();


    @OnOpen
    public void onOpen(@PathParam("mid")Long mid, Session session) {
        map.put(mid,session);
        set.add(session);
        log.warn("开启时连接数"+map.size());
    }


    @OnClose
    public void onClose(Session closeSession) {
        set.remove(closeSession);
        log.info("关闭时连接数"+map.size());
        boolean contains = set.contains(closeSession);
        log.info("set是否包含"+contains);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        forwardMsg(message);
    }


    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误");
        set.remove(session);
        error.printStackTrace();
    }

    public void forwardMsg(String message){
        Msg msg = new Gson().fromJson(message, Msg.class);
        Member member = memberRepo.findById(msg.getFromId()).orElse(null);
        msg.setType(1);
        msg.setFromAvatar(member.getAvatarUrl());
        msg.setFromNickname(member.getNickname());
        msg.setTime(new Date());
        log.info("来自客户端的消息:" + msg.getMsg());
        Session receiver = map.get(msg.getToId());
        msgRepo.saveAndFlush(msg);
        if (receiver==null){
            log.info("目标未上线 存入数据库中");
        }else{
            if (set.contains(receiver)){
                String s = new Gson().toJson(msg);
            try {
                receiver.getBasicRemote().sendText(s);
            } catch (IOException e) {
                e.printStackTrace();
                map.remove(msg.getToId());
            }
            }else{
                map.remove(msg.getToId());
            }
        }

    }

    public void sendMsg(Msg msg){

        Session receiver = map.get(msg.getToId());
        if (receiver!=null) {
            String s = new Gson().toJson(msg);
            log.info(s);
            try {
                receiver.getBasicRemote().sendText(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            map.remove(msg.getToId());

            log.info("持久化  待拉取");
        }

    }

    public void sendAll(Notification msg){
        List<Long> allId = memberRepo.findAllId();
        allId.forEach(x->{
            Session session = map.get(x);
            if (session!=null){
                try {
                    session.getBasicRemote().sendText(new Gson().toJson(msg));
                } catch (IOException e) {
                    e.printStackTrace();
                    Notification notification=new Notification();
                    notification.setMsg(msg.getMsg());
                    notification.setType(2);
                    notification.setTime(msg.getTime());
                    notification.setReceiveId(x);
                    notificationRepo.saveAndFlush(notification);
                }
            }else{
                Notification notification=new Notification();
                notification.setMsg(msg.getMsg());
                notification.setType(2);
                notification.setTime(msg.getTime());
                notification.setReceiveId(x);
                notificationRepo.saveAndFlush(notification);
            }
        });
    }

}
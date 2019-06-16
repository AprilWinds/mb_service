package wings.app.microblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.Msg;
import wings.app.microblog.entity.Notification;
import wings.app.microblog.repository.MsgRepository;
import wings.app.microblog.repository.NotificationRepository;

import java.util.List;


@Service
public class MsgService {

    @Autowired
    private MsgRepository msgRepo;

    @Autowired
    private NotificationRepository notificationRepo;

    public List<Msg> getMsgList(Member member) {
        List<Msg> ls = msgRepo.findByOffLine(member.getId());
        return ls;
    }

    public List<Notification> getNotificationList(Member member) {

        return  notificationRepo.findNotificationList(member.getId());
    }

    public void delNotification(Long nid) {
        notificationRepo.deleteById(nid);
    }
}

package wings.app.microblog.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.Msg;
import wings.app.microblog.entity.Notification;
import wings.app.microblog.service.MsgService;
import wings.app.microblog.util.Http;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/msg")
public class MsgResource {

    @Autowired
    private MsgService msgService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request) {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/",method = RequestMethod.GET,  produces = "application/json")
    public  Object msgList(@ModelAttribute("member") Member member){
        List<Msg> msgList = msgService.getMsgList(member);
        return Http.standardResponse(msgList);
    }
    @RequestMapping(value = "/notification",method = RequestMethod.GET,  produces = "application/json")
    public  Object notificationList(@ModelAttribute("member") Member member){
        List<Notification> ls =msgService.getNotificationList(member);
        return Http.standardResponse(ls);
    }
    @RequestMapping(value = "/notification/{nid}",method = RequestMethod.DELETE,  produces = "application/json")
    public  Object delNotification(@PathVariable("nid") Long nid){
        msgService.delNotification(nid);
        return Http.standardResponse();
    }


}

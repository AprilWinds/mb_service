package chat.hala.app.restful;

import chat.hala.app.entity.Member;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.restful.exception.OutboundException;
import chat.hala.app.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by astropete on 2017/12/27.
 */
@RestController
@RequestMapping("/chat")
public class ChatResource {

    @Autowired
    private ChatService chatService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request)
    {
        return (Member) request.getAttribute("member");
    }


    @RequestMapping(value = "/token", method = RequestMethod.GET, produces = "application/json")
    public Object getRongToken(@ModelAttribute("member") Member member) throws OutboundException {
        try{
            return chatService.requestRongToken(member);
        }catch (Exception e){
            throw new OutboundException(ErrorMsg.RONG_CLOUD_ERR);
        }
    }
}

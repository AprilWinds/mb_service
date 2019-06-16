package chat.hala.app.restful;

import chat.hala.app.restful.wrapper.MemberStatus;
import chat.hala.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
public class OnLineResource {

    @Autowired
    private MemberService memberService;



    @RequestMapping(value = "/online",method = RequestMethod.POST,produces = "application/json")
    public Object status(@RequestBody List<MemberStatus> ls) {

        return memberService.cloudUpdateStatus(ls);
    }
}

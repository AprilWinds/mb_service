package chat.hala.app.restful;

import chat.hala.app.entity.GiftGiving;
import chat.hala.app.entity.Member;
import chat.hala.app.restful.exception.NotAcceptableException;
import chat.hala.app.service.GiftService;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by astropete on 2018/1/26.
 */

@RestController
@RequestMapping("/gift")
public class GiftResource {

    @Autowired
    private GiftService giftService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request)
    {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public Object getGift(@ModelAttribute("member") Member member,
                          HttpServletRequest request){
        String language = request.getHeader("Accept-Language");
        return giftService.getAllGift(language);
    }

    @RequestMapping(value = "/give", method = RequestMethod.POST, produces = "application/json")
    public Object giveGift(@ModelAttribute("member") Member member,
                           @RequestBody GiftGiving giving) throws NotAcceptableException{
        try{
            return giftService.giveMemberGift(member, giving);
        } catch (PreconditionNotQualifiedException e){
            throw new NotAcceptableException(e.getMessage());
        }
    }
}

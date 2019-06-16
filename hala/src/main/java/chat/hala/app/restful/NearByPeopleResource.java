package chat.hala.app.restful;

import chat.hala.app.entity.Member;
import chat.hala.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/nearbyPeople")
@RestController
public class NearByPeopleResource {


    @Autowired
    private MemberService memberService;


    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request)
    {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/",method = RequestMethod.GET,produces = "application/json")
    public Object getNearbyPeople(@ModelAttribute("member")Member member,
                                  @RequestParam("lng") Double lng,
                                  @RequestParam("lat") Double lat,
                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                  @RequestParam(value = "flag",required =false,defaultValue ="%")String flag){


        return  memberService.findNearbyPeople(member.getId(),lng,lat,page,size,flag);
    }

    @RequestMapping(value = "/online",method = RequestMethod.GET,produces = "application/json")
    public Object getNearbyPeopleOnline(@ModelAttribute("member")Member member,
                                        @RequestParam("lng") Double lng,
                                        @RequestParam("lat") Double lat,
                                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
        return  memberService.findOnlinePeople(member.getId(),lng,lat,page,size);
    }

}

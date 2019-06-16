package chat.hala.app.restful;

import chat.hala.app.entity.Fonding;
import chat.hala.app.entity.Member;
import chat.hala.app.service.FondingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by astropete on 2018/1/7.
 */
@RestController
@RequestMapping("/fonding")
public class FondingResource {

    @Autowired
    private FondingService fondingService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request)
    {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public Object addFonding(@ModelAttribute("member") Member member,
                             @RequestBody Fonding fonding){
        return fondingService.createFonding(fonding);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public Object getFonding(@ModelAttribute("member") Member member,
                             HttpServletRequest request,
                             @RequestParam("parentId") Long parentId,
                             @PageableDefault(value = 10, sort = {"usedCount"}, direction = Sort.Direction.DESC) Pageable pageable){
        String language = request.getHeader("Accept-Language");
        return fondingService.findFondingByParentId(parentId, pageable, language);
    }
}

package chat.hala.app.restful;

        import chat.hala.app.service.RankService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.domain.Sort;
        import org.springframework.data.web.PageableDefault;
        import org.springframework.web.bind.annotation.*;

/**
 * Created by astropete on 2018/1/29.
 */

@RestController
@RequestMapping("/rank")
public class RankResource {

    @Autowired
    private RankService rankService;

    @RequestMapping(value = "/gift", method = RequestMethod.GET, produces = "application/json")
    public Object getGiftRanking(@RequestParam("type") String type,
                                 @PageableDefault(value = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
        return rankService.getGiftRanking(type, pageable);
    }

    @RequestMapping(value = "/wealth", method = RequestMethod.GET, produces = "application/json")
    public Object getWealthRanking(@RequestParam("type") String type,
                                   @PageableDefault(value = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
        return rankService.getWealthRanking(type, pageable);
    }

    @RequestMapping(value = "/room", method = RequestMethod.GET, produces = "application/json")
    public Object getRoomRanking(@RequestParam("type") String type,
                                 @PageableDefault(value = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
        return rankService.getRoomRanking(type, pageable);
    }

    @RequestMapping(value = "/room/{roomId}/member", method = RequestMethod.GET, produces = "application/json")
    public Object getRoomMemberRanking(@RequestParam("type") String type,
                                       @PathVariable("roomId") Long roomId,
                                       @PageableDefault(value = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
        return rankService.getRoomMemberRanking(type, roomId, pageable);
    }
}

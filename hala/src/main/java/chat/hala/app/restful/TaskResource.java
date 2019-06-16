package chat.hala.app.restful;

import chat.hala.app.entity.Member;
import chat.hala.app.library.util.General;
import chat.hala.app.library.util.Http;
import chat.hala.app.restful.exception.ConflictException;
import chat.hala.app.restful.exception.PreconditionFailedException;
import chat.hala.app.service.TaskService;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by astropete on 2018/3/7.
 */

@RestController
@RequestMapping("/task")
public class TaskResource {

    @Autowired
    private TaskService taskService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request)
    {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/member", method = RequestMethod.GET, produces = "application/json")
    public Object getMemberTask(@ModelAttribute("member") Member member,
                                HttpServletRequest request){
        String language = request.getHeader("Accept-Language");
        return taskService.getMemberFinishedWithTask(member, language);
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST, produces = "application/json")
    public Object memberCompleteTask(@ModelAttribute("member") Member member,
                                     @RequestParam("symbol") String symbol) throws ConflictException{
        try{
            taskService.memberCompleteTask(member, symbol);
            return Http.generalResponse();
        }catch (ObjectAlreadyExistedException e){
            throw new ConflictException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{taskId}/reward", method = RequestMethod.POST, produces = "application/json")
    public Object memberGetTaskReward(@ModelAttribute("member") Member member,
                                      @PathVariable("taskId") Long taskId) throws PreconditionFailedException, ConflictException{
        try{
            return taskService.memberGetRewardTask(member, taskId);
        } catch (PreconditionNotQualifiedException e){
            throw new PreconditionFailedException(e.getMessage());
        } catch (ObjectAlreadyExistedException e){
            throw new ConflictException(e.getMessage());
        }
    }

}

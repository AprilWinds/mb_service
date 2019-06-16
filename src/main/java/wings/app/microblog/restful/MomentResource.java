package wings.app.microblog.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wings.app.microblog.Exception.InexistenceException;
import wings.app.microblog.entity.Comment;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.Moment;
import wings.app.microblog.entity.Report;
import wings.app.microblog.service.MomentService;
import wings.app.microblog.util.ErrorCode;
import wings.app.microblog.util.Http;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/moment")
public class MomentResource {

    @Autowired
    private MomentService momentService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request) {
        return (Member) request.getAttribute("member");
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object upload(@RequestParam(value = "files",required = false) MultipartFile[] files, @ModelAttribute("member") Member member) throws IOException {

        String rs="";
        if (files.length>0) {
            for (MultipartFile file : files) {
                int i=0;
                File path = null;

                path=new File("D://up/");
                /* path = new File(ResourceUtils.getURL("src/main/resources/static/up/").getPath());*/

                String id = member.getId().toString();
                String date = String.valueOf(new Date().getTime());
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                File f = new File(path.getAbsolutePath(), id + date + suffix);
                try {
                    file.transferTo(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rs+=(id+date+suffix+"-");
            }
        }
        return Http.standardResponse(rs);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public Object createMoment(@ModelAttribute("member") Member member, @RequestBody Moment moment) {
        momentService.createMoment(member.getId(), moment);
        return Http.standardResponse();
    }

    @RequestMapping(value = "/{momentId}/prise", method = RequestMethod.POST, produces = "application/json")
    public Object  priseMoment(@ModelAttribute("member") Member member, @PathVariable("momentId")Long momentId) {
        Integer integer=null;
        try {
            integer= momentService.priseMoment(member.getId(), momentId);
        } catch (InexistenceException e) {
            e.printStackTrace();
            return Http.standardResponse(ErrorCode.NO_MOMENT.status,ErrorCode.NO_MOMENT.msg);
        }
        return Http.standardResponse(integer);
    }

    @RequestMapping(value = "/{momentId}/collect", method = RequestMethod.POST, produces = "application/json")
    public Object  collectMoment(@ModelAttribute("member") Member member, @PathVariable("momentId")Long momentId) {
        Integer integer = momentService.collectMoment(member.getId(), momentId);
        return Http.standardResponse(integer);
    }



    @RequestMapping(value = "/comment",method=RequestMethod.POST,produces = "application/json")
    public Object commentMoment(@ModelAttribute("member") Member member, @RequestBody Comment comment){
        try {
            System.out.println(comment.getContent());
            momentService.addComment(member,comment);
        } catch (InexistenceException e) {
            e.printStackTrace();
            return  Http.standardResponse(ErrorCode.NO_MOMENT.status,ErrorCode.NO_MOMENT.msg);
        }
        return Http.standardResponse();
    }


    @RequestMapping(value = "/{momentId}/comments",method=RequestMethod.GET,produces = "application/json")
    public Object getCommentList(@PathVariable("momentId")Long momentId, @PageableDefault(value = 10) Pageable pageable){
        List<Comment> cl=null;
        try {
            cl = momentService.getCommentList(momentId, pageable);
        } catch (InexistenceException e) {
            e.printStackTrace();
            return Http.standardResponse(ErrorCode.NO_MOMENT.status,ErrorCode.NO_MOMENT.msg);
        }
        return Http.standardResponse(cl);
    }

    @RequestMapping(value = "/{momentId}/{privacy}",method=RequestMethod.PUT,produces = "application/json")
    public Object updateMomentPrivacy(@PathVariable("momentId")Long momentId, @PathVariable("privacy")Integer visibility){
        try {
            momentService.updateMomentPrivacy(momentId,visibility);
        } catch (InexistenceException e) {
            e.printStackTrace();
            return Http.standardResponse(ErrorCode.NO_MOMENT.status,ErrorCode.NO_MOMENT.msg);
        }
        return Http.standardResponse();
    }


    @RequestMapping(value = "/all",method=RequestMethod.GET,produces = "application/json")
    public Object getAllVisibleMoments(@ModelAttribute("member")Member member,@PageableDefault(value = 10)Pageable pageable){
        List<Moment> ms = momentService.getAllVisibleMoments(member.getId(),member, pageable);
        return Http.standardResponse(ms);
    }

    @RequestMapping(value = "/report",method=RequestMethod.POST,produces = "application/json")
    public Object getAllVisibleMoments(@ModelAttribute("member")Member member,@RequestBody Report report){
        momentService.createReport(member.getId(),report);
        return Http.standardResponse();
    }




  /*  @RequestMapping(value = "/by", method = RequestMethod.POST)
    public Object by(@RequestParam("name") String name, @RequestParam("pswd") String pswd) {

        return Http.standardResponse(name + pswd);
    }

    @RequestMapping(value = "/gy", method = RequestMethod.GET)
    public Object gy(@RequestBody Test test) {

        return Http.standardResponse(test);
    }*/

}

package chat.hala.app.service.library;

import chat.hala.app.entity.Member;
import chat.hala.app.entity.MemberTaskLog;
import chat.hala.app.entity.Task;
import chat.hala.app.library.util.General;
import chat.hala.app.repository.MemberRepository;
import chat.hala.app.repository.MemberTaskLogRepository;
import chat.hala.app.repository.TaskRepository;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by astropete on 2018/3/7.
 */
@Component
public class TaskHandler {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private MemberTaskLogRepository tlRepo;

    public MemberTaskLog completeTask(Member member, String symbol){
        Task task = taskRepo.findBySymbol(symbol);
        MemberTaskLog existed = null;
        if(task.getDaily()){
            existed = tlRepo.findByMemberIdAndTaskIdAndCreatedAtAfter(member.getId(), task.getId(), General.date("daily"));
        }else{
            existed = tlRepo.findByMemberIdAndTaskId(member.getId(), task.getId());
        }
        if(existed != null) return null;
        MemberTaskLog mtl = new MemberTaskLog();
        mtl.setMemberId(member.getId());
        mtl.setTaskId(task.getId());
        mtl.setCreatedAt(new Date());
        mtl.setUpdatedAt(new Date());
        tlRepo.saveAndFlush(mtl);
        return mtl;
    }

}

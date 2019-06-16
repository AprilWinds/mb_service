package chat.hala.app.service.implementation;

import chat.hala.app.entity.CoinTransaction;
import chat.hala.app.entity.Member;
import chat.hala.app.entity.MemberTaskLog;
import chat.hala.app.entity.Task;
import chat.hala.app.library.util.Constant;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.library.util.General;
import chat.hala.app.library.util.Http;
import chat.hala.app.repository.CoinTransactionRepository;
import chat.hala.app.repository.MemberRepository;
import chat.hala.app.repository.MemberTaskLogRepository;
import chat.hala.app.repository.TaskRepository;
import chat.hala.app.service.TaskService;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import chat.hala.app.service.library.CoinTransactionRecorder;
import chat.hala.app.service.library.TaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by astropete on 2018/3/7.
 */
@Service
public class TaskServiceImplementation implements TaskService {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private MemberTaskLogRepository tlRepo;

    @Autowired
    private TaskHandler taskHandler;

    @Autowired
    private CoinTransactionRepository ctRepo;

    @Autowired
    private CoinTransactionRecorder ctRecorder;

    @Autowired
    private MemberRepository mRepo;

    @Override
    public Object getMemberFinishedWithTask(Member member, String language){
        List<Task> tasks = taskRepo.findAll();
        List<Long> dailyTIDs = new ArrayList<>();
        List<Long> nonDailyTIDs = new ArrayList<>();
        for(Task t : tasks){
            if(t.getDaily()) dailyTIDs.add(t.getId());
            if(!t.getDaily()) nonDailyTIDs.add(t.getId());
        }
        List<BigInteger> dailyTaskLogIds = tlRepo.findMemberCompletedDailyTaskIds(member.getId(), dailyTIDs, General.date("daily"));
        List<BigInteger> nonDailyTaskLogIds = tlRepo.findMemberCompletedTaskIds(member.getId(), nonDailyTIDs);
        List<BigInteger> dailyTaskRewardedIds = ctRepo.findMemberRewardedDailyTaskIds(member.getId(), dailyTIDs, General.date("daily"));
        List<BigInteger> nonDailyTaskRewardedIds = ctRepo.findMemberRewardedTaskIds(member.getId(), nonDailyTIDs);
        for(Task t: tasks){
            t.setState(Task.TaskState.undo);
            if(dailyTaskLogIds.contains(BigInteger.valueOf(t.getId().intValue())) || nonDailyTaskLogIds.contains(BigInteger.valueOf(t.getId()))) t.setState(Task.TaskState.done);
            if(dailyTaskRewardedIds.contains(BigInteger.valueOf(t.getId().intValue())) || nonDailyTaskRewardedIds.contains(BigInteger.valueOf(t.getId()))) t.setState(Task.TaskState.rewarded);
        }
        for(Task t : tasks){
            if(language.equals(Constant.LAN_ARAB)){
                t.setDescription(t.getArabicDescription());
            }
            t.setArabicDescription(null);
        }
        return tasks;
    }

    @Override
    public void memberCompleteTask(Member member, String symbol) throws ObjectAlreadyExistedException{
        MemberTaskLog mtl = taskHandler.completeTask(member, symbol);
        if(mtl == null) throw new ObjectAlreadyExistedException(ErrorMsg.TASK_ALREADY_COMPLETED);
    }

    @Override
    public Object memberGetRewardTask(Member member, Long taskId) throws PreconditionNotQualifiedException, ObjectAlreadyExistedException{
        Task task = taskRepo.findOne(taskId);
        CoinTransaction ct = ctRepo.findTop1ByMemberIdAndReferenceIdAndFromActionOrderByCreatedAtDesc(member.getId(), taskId, CoinTransaction.FromAction.task);
        if((!task.getDaily() && ct != null) || (task.getDaily() && ct != null && ct.getCreatedAt().getTime() > General.date("daily").getTime())) throw new ObjectAlreadyExistedException(ErrorMsg.TASK_ALREADY_REWARDED);
        MemberTaskLog log = tlRepo.findTop1ByMemberIdAndTaskIdOrderByCreatedAtDesc(member.getId(), taskId);
        if((!task.getDaily() && log == null) || (task.getDaily() && log == null) || (task.getDaily() && log != null && log.getCreatedAt().getTime() < General.date("daily").getTime())) throw new PreconditionNotQualifiedException(ErrorMsg.TASK_NOT_COMPLETED);
        ctRecorder.recordTaskIncome(member.getId(), task);
        member.setCoins(member.getCoins() + task.getCoins());
        mRepo.saveAndFlush(member);
        return Http.coinResponse(task, member.getCoins());
    }
}

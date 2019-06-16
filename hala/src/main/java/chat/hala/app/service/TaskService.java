package chat.hala.app.service;

import chat.hala.app.entity.Member;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;

/**
 * Created by astropete on 2018/3/7.
 */
public interface TaskService {
    public Object getMemberFinishedWithTask(Member member, String language);
    public void memberCompleteTask(Member member, String symbol) throws ObjectAlreadyExistedException;
    public Object memberGetRewardTask(Member member, Long taskId) throws ObjectAlreadyExistedException, PreconditionNotQualifiedException;
}

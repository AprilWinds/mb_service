package wings.app.microblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.Msg;
import wings.app.microblog.repository.MsgRepository;

import java.util.List;

@Service
public class MsgService {

    @Autowired
    private MsgRepository msgRepo;

    public List<Msg> getMsgList(Member member) {

        return  msgRepo.findByOffLine(member.getId());
    }
}

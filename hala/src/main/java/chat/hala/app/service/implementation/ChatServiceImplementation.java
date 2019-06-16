package chat.hala.app.service.implementation;

import chat.hala.app.entity.Member;
import chat.hala.app.library.RongCloud;
import chat.hala.app.repository.MemberRepository;
import chat.hala.app.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by astropete on 2017/12/27.
 */

@Service
public class ChatServiceImplementation implements ChatService {

    @Autowired
    private MemberRepository memberRepo;

    @Override
    public Object requestRongToken(Member member) throws Exception{
        Map<String, String> m = new HashMap<>();
        if(member.getRongToken() != null){
            m.put("token", member.getRongToken());
        }else{
            RongCloud r = new RongCloud();
            String token = r.getToken(member).toString();
            m.put("token", token);
            member.setRongToken(token);
            member.setUpdatedAt(new Date());
            memberRepo.saveAndFlush(member);
        }
        return m;
    }
}

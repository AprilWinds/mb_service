package chat.hala.app.service;

import chat.hala.app.entity.Member;

/**
 * Created by astropete on 2017/12/27.
 */
public interface ChatService {
    public Object requestRongToken(Member member) throws Exception;
}

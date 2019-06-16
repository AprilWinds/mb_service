package chat.hala.app.service;

import chat.hala.app.entity.GiftGiving;
import chat.hala.app.entity.Member;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;

/**
 * Created by astropete on 2018/1/26.
 */
public interface GiftService {
    public Object getAllGift(String language);
    public Object giveMemberGift(Member member, GiftGiving giving) throws PreconditionNotQualifiedException;
}

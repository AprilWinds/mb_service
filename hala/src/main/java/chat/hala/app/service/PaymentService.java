package chat.hala.app.service;

import chat.hala.app.entity.Member;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;

/**
 * Created by astropete on 2018/4/2.
 */
public interface PaymentService {
    public Object verifyAppleReceipt(Member member, String receipt) throws ObjectAlreadyExistedException, Exception;
}

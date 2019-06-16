package chat.hala.app.restful;

import chat.hala.app.entity.Member;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.restful.exception.ConflictException;
import chat.hala.app.restful.exception.OutboundException;
import chat.hala.app.restful.wrapper.AppleReceipt;
import chat.hala.app.service.PaymentService;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by astropete on 2018/4/2.
 */

@RestController
@RequestMapping(value = "/payment")
public class PaymentResource {

    @Autowired
    private PaymentService payService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request)
    {
        return (Member) request.getAttribute("member");
    }


    @RequestMapping(value = "/verify/apple", method = RequestMethod.POST, produces = "application/json")
    public Object verifyApplePay(@ModelAttribute("member") Member member,
                                 @RequestBody AppleReceipt receipt) throws ConflictException, OutboundException{
        try {
            return payService.verifyAppleReceipt(member, receipt.getReceiptData());
        }catch (ObjectAlreadyExistedException e){
            throw new ConflictException(e.getMessage());
        }catch (Exception e){
            throw new OutboundException(ErrorMsg.APPLE_SERVICE_ERROR);
        }
    }
}

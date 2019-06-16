package chat.hala.app.service.implementation;

import chat.hala.app.entity.Member;
import chat.hala.app.entity.Recharge;
import chat.hala.app.library.Identifier;
import chat.hala.app.library.util.Constant;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.library.util.Http;
import chat.hala.app.repository.MemberRepository;
import chat.hala.app.repository.RechargeRepository;
import chat.hala.app.service.PaymentService;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import chat.hala.app.service.library.CoinTransactionRecorder;
import chat.hala.app.service.library.TaskHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by astropete on 2018/4/2.
 */

@Service
public class PaymentServiceImplementation implements PaymentService {

    @Autowired
    private TaskHandler taskHandler;

    @Autowired
    private RechargeRepository rRepo;

    @Autowired
    private CoinTransactionRecorder ctr;

    @Autowired
    private MemberRepository mRepo;

    @Override
    public Object verifyAppleReceipt(Member member, String receipt) throws ObjectAlreadyExistedException, Exception{
        JsonObject result = Http.requestApple(Constant.ApplePayBuyUrl, receipt);
        if(result.get("status").getAsInt() == 0){
            JsonArray purchased = result.get("receipt").getAsJsonObject().get("in_app").getAsJsonArray();
            Integer totalCoin = 0;
            for(JsonElement p : purchased){
                String productId = p.getAsJsonObject().get("product_id").getAsString();
                String transactionId = p.getAsJsonObject().get("transaction_id").getAsString();
                Recharge r = rRepo.findByOutTransactionCode(transactionId);
                if(r != null) throw new ObjectAlreadyExistedException(ErrorMsg.RECHARGE_EXISTED);
                String relatedCoinSpent = productId.substring(2);
                List<Integer> items = Arrays.stream(relatedCoinSpent.split("_")).map(Integer::parseInt).collect(Collectors.toList());
                Integer coins = items.get(0);
                Double amount = Double.valueOf(items.get(1)) - 0.01;
                totalCoin += coins;
                Recharge rg = this.addRecharge(member.getId(), amount, transactionId, Recharge.PayThrough.apppay);
                taskHandler.completeTask(member, "recharge");
                ctr.recordRecharge(member.getId(), coins ,rg.getId(), false);
            }
            member.setCoins(member.getCoins() + totalCoin);
            mRepo.saveAndFlush(member);
            return Http.coinResponse(null, member.getCoins());
        }else{
            return Http.verifyFailed();
        }
    }

    private Recharge addRecharge(Long memberId, Double amount, String outCode, Recharge.PayThrough through){
        Recharge r = new Recharge();
        r.setAmount(amount);
        r.setMemberId(memberId);
        r.setOutTransactionCode(outCode);
        r.setThrough(through);
        Identifier idg = new Identifier(8, "T", false);
        r.setTransactionId(idg.generate());
        r.setState(Recharge.OState.success);
        r.setCreatedAt(new Date());
        r.setUpdatedAt(new Date());
        return rRepo.saveAndFlush(r);
    }


}

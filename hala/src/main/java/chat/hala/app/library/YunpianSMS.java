package chat.hala.app.library;

import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by astropete on 2017/12/25.
 */
public class YunpianSMS {

    private final Logger logger = LoggerFactory.getLogger(YunpianSMS.class);
    private static final String apiKey = "89e193e44d19dbff7e5b08c4bd4693a8";
    private static final int successCode = 0;
    private YunpianClient client;
    private String mobile;
    private String content;

    public YunpianSMS(){
        this.client = new YunpianClient(apiKey).init();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean send(){
        Map<String, String> param = client.newParam(2);
        param.put(YunpianClient.MOBILE, this.mobile);
        param.put(YunpianClient.TEXT, this.content);
        Result<SmsSingleSend> r = client.sms().single_send(param);
        if(r.getCode() != successCode){
            logger.error(r.getDetail());
            return false;
        }else{
            return true;
        }
    }

    public void destroy(){
        this.client.close();
    }

}

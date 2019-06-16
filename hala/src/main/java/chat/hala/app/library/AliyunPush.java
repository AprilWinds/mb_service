package chat.hala.app.library;

import chat.hala.app.entity.Member;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.*;

/**
 * Created by astropete on 2018/3/20.
 */
public class AliyunPush {

    private static String ACCESS_KEY_ID = "akid";
    private static String ACCESS_KEY_SEC = "aksec";
    private static String REGION = "cn-hangzhou";
    private static Long APPLE_APP_KEY = 24701053L;
    private PushRequest pr;
    private DefaultAcsClient client;

    public AliyunPush(){
        IClientProfile profile = DefaultProfile.getProfile(REGION, ACCESS_KEY_ID, ACCESS_KEY_SEC);
        this.client = new DefaultAcsClient(profile);
        this.pr = new PushRequest();
    }

    public void applePush(Member m, String title, String body, String params) throws Exception {

        PushNoticeToiOSRequest iOSRequest = new PushNoticeToiOSRequest();
        //安全性比较高的内容建议使用HTTPS
        iOSRequest.setProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        iOSRequest.setMethod(MethodType.POST);
        iOSRequest.setAppKey(APPLE_APP_KEY);
        // iOS的通知是通过APNS中心来发送的，需要填写对应的环境信息. DEV :表示开发环境, PRODUCT: 表示生产环境
        iOSRequest.setApnsEnv("PRODUCT");
        iOSRequest.setTarget("DEVICE");
        iOSRequest.setTargetValue(m.getApToken());
        iOSRequest.setTitle(title);
        iOSRequest.setBody(body);
        iOSRequest.setExtParameters(params);


        PushNoticeToiOSResponse pushNoticeToiOSResponse = client.getAcsResponse(iOSRequest);
        System.out.printf("RequestId: %s, MessageId: %s\n",
                pushNoticeToiOSResponse.getRequestId(), pushNoticeToiOSResponse.getMessageId());
    }
}

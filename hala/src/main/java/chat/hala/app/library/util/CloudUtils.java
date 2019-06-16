package chat.hala.app.library.util;

import io.rong.RongCloud;
import io.rong.messages.TxtMessage;
import io.rong.models.message.PrivateMessage;
import org.springframework.stereotype.Component;

@Component
public class CloudUtils {

        //private static final String appKey = "82hegw5u8d4dx";
        //private static final String appSecret = "SwGqiJSc1P";

        private static final String appKey = "e0x9wycfet5tq";
        private static final String appSecret = "7eJLGVR8JkL";
        private static RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        private static io.rong.methods.message._private.Private Private = rongCloud.message.msgPrivate;



        public  void  systemNotice(String msg,String[] target,String extra) throws Exception {

               TxtMessage txtMessage = new TxtMessage(msg,extra);
               PrivateMessage privateMessage = new PrivateMessage()
                    .setSenderId(Constant.HALA_NOTICE_CHARACTERID)
                    .setTargetId(target)
                    .setObjectName(txtMessage.getType())
                    .setContent(txtMessage)
                    .setPushContent(msg) //ios 展示 // .setPushData("{\"pushData\":\"helloaaaaaaa\"}")
                    .setIsPersisted(0)
                    .setIsIncludeSender(0);

                    Private.send(privateMessage);
        }



}

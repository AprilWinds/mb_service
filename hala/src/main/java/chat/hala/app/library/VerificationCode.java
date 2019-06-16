package chat.hala.app.library;

/**
 * Created by astropete on 2017/12/25.
 */
public class VerificationCode {

    private static final Integer CODE_LENGTH = 6;
    private static final Integer EFFECTIVE_PERIOD = 600;

    private String code;
    private String mobileNumber;

    public VerificationCode(String mobileNumber){
        this.mobileNumber = mobileNumber;
    }

    public String getCurrentCode(){
        this.code = generateCode(0);
        return this.code;
    }

    public String getLastPeriodCode(){
        return generateCode(-1);
    }

    private String generateCode(Integer adjust){
        Integer periodNumber = getPeriodNumber() + adjust;
        Integer pepper = (periodNumber + getMobileDigit()) % 10;
        Integer magic = (periodNumber / (pepper + 1)) % ((int)(Math.pow(10, CODE_LENGTH)));
        return padStartWithString(magic.toString(), pepper.toString());
    }

    private Integer getPeriodNumber(){
        Integer timestamp = (int) (System.currentTimeMillis() / 1000);
        return timestamp / EFFECTIVE_PERIOD;
    }

    private Integer getMobileDigit(){
        return Integer.parseInt(this.mobileNumber.substring(0,3) + this.mobileNumber.substring(this.mobileNumber.length() - 4, this.mobileNumber.length()));
    }

    private String padStartWithString(String str, String pad){
        Integer restLength = CODE_LENGTH - str.length();
        StringBuffer prefix = new StringBuffer("");
        char[] padChar = pad.toCharArray();
        while(prefix.length() < restLength){
            int i = 0;
            prefix.append(padChar[i%pad.length()]);
            i++;
        }
        return prefix.toString() + str;
    }

}

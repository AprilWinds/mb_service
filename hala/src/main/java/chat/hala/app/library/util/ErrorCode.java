package chat.hala.app.library.util;

public enum  ErrorCode {


    TOKEN_INVALID(0,"The aptoken is invalid"),
    //REGISTER(200,"register"),
    NO_DYNAMIC(4001,"No such dynamic"),
    No_IMG_CONTENT(4002,"Pictures or text do not exist"),
    THIRD_PARTY_ERROR(4003,"Third party token fails"),
    CANNOT_FOLLOW(4005,"Cannot follow this member, blocking relationship existed"),
    NO_MEMBER(4004,"No such member, id or characterId error"),
    BLOKED(4006,"Blocked from viewing information"),
    No_DYNAMIC_UPDATE_PRIVILEGE(4007,"No permission to change dynamic"),
    NUMBER_LESS_THAN(4008,"The number of less than"),
    RONGCLOUD_ERROR(4009,"Rong cloud information error");


    private  Integer  status;
    private  String  msg;

    ErrorCode(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

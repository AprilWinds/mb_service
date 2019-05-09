package wings.app.microblog.util;

public enum  ErrorCode {

    LOGIN_ERROR(100,"用户名不存在或密码错误"),
    BANNED(101,"该账户已被封禁"),
    BLOCKED(102,"已被该用户拉黑"),
    NO_MOMENT(103,"目标帖子不存在"),
    USERNAME_REPETITION(104,"用户名已存在");



    public Integer status;
    public String  msg;

    ErrorCode(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}

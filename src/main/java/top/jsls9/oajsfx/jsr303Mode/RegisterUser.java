package top.jsls9.oajsfx.jsr303Mode;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/** 用作注册校验
 * @author bSu
 * @date 2021/5/25 - 21:44
 */
public class RegisterUser {

    @NotNull(message = "密码不能为空")
    @Length(min = 6 ,max = 18, message = "密码需要>=6位，且<=18位")
    private String passWord;

    @NotNull(message = "密码不能为空")
    @Length(min = 6 ,max = 18, message = "密码需要>=6位，且<=18位")
    private String passWord1;

    @NotNull(message = "绑定QQ不可为空")
    private String qq;

    @NotNull(message = "验证码不可为空")
    private String code;

    public RegisterUser(String passWord, String passWord1, String qq) {
        this.passWord = passWord;
        this.passWord1 = passWord1;
        this.qq = qq;
    }

    public RegisterUser() {
    }

    public String getPassWord() {
        return passWord;
    }

    public String getPassWord1() {
        return passWord1;
    }

    public String getQq() {
        return qq;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setPassWord1(String passWord1) {
        this.passWord1 = passWord1;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

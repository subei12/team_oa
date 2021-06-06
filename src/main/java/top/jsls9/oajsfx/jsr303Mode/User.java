package top.jsls9.oajsfx.jsr303Mode;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/** 用作登录校验，很奇怪为什么控制台不打印BindException异常，log4j抽风呢吗
 * @author bSu
 * @date 2021/3/9 - 22:43
 */
public class User {

    @NotNull(message = "账号不能为空")
    @Length(min = 6 ,max = 18, message = "账号需要>=6位，且<=18位")
    private String userName;

    private String passWord;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }
}

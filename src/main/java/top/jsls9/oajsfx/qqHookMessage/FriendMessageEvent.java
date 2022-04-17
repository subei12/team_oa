package top.jsls9.oajsfx.qqHookMessage;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jsls9.oajsfx.annotation.HookMessage;
import top.jsls9.oajsfx.enums.EventType;
import top.jsls9.oajsfx.hlxPojo.qqPojo.Content;
import top.jsls9.oajsfx.hlxPojo.qqPojo.MessageChain;
import top.jsls9.oajsfx.hlxPojo.qqPojo.MessageRootBean;
import top.jsls9.oajsfx.hlxPojo.qqPojo.SendRootBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bSu
 * @date 2022/4/11 - 18:39
 */
public class FriendMessageEvent {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 试例
     * 使用HookMessage的方法必须使用一个参数
     * @return
     */
    @HookMessage(event = {EventType.FRIENDMESSAGE, EventType.GROUPMESSAGE}, plain = "!!!test")
    public SendRootBean test01(MessageRootBean messageRootBean){
        System.out.println("111111111");
        logger.info("当前消息参数："+ JSON.toJSONString(messageRootBean));
        SendRootBean sendRootBean = new SendRootBean();
        //命令行
        sendRootBean.setCommand("sendFriendMessage");//仅私聊发给测试人，多种类型懒得判断了

        //消息
        MessageChain messageChain = new MessageChain();
        messageChain.setType("Plain");
        messageChain.setText("hellow world! \n");

        MessageChain messageChain1 = new MessageChain();
        messageChain1.setType("Plain");
        messageChain1.setText("测试成功！ \n");

        MessageChain messageChain2 = new MessageChain();
        messageChain2.setType("Image");
        messageChain2.setUrl("https://s2.ax1x.com/2020/01/01/l85Eef.jpg");

        List<MessageChain> list = new ArrayList<>();
        list.add(messageChain);
        list.add(messageChain1);
        list.add(messageChain2);

        //命令内容
        Content content = new Content();
        content.setTarget(messageRootBean.getSender().getId());
        content.setMessageChain(list);
        sendRootBean.setContent(content);
        return sendRootBean;
    }

}

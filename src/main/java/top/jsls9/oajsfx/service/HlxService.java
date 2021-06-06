package top.jsls9.oajsfx.service;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author bSu
 * @date 2021/5/15 - 17:59
 */
public interface HlxService {

    //结算
    Object settlement(String hlxUserId,String postId) throws ParseException, IOException;

}

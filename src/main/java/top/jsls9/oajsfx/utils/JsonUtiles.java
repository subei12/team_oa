package top.jsls9.oajsfx.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author bSu
 * @date 2022/1/8 - 18:42
 */
public class JsonUtiles {

    //判断一个字符串是不是数字
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //将keyPath分割成数组
    public static String[] customSplit(String oldStr) {
        List<String> strings = new ArrayList<>();
        String[] split = oldStr.split("\\.");
        for (String s : split) {
            if (s.contains("[")) {
                String[] subS = s.split("\\[|\\]");
                Collections.addAll(strings, subS);
            } else {
                strings.add(s);
            }
        }
        return strings.toArray(new String[0]);
    }

    /** <p>通过key链之间获取JSON中的值</p></br>
     * <p>最终的value不一定是string，但返回string具有最大的普适性，相比返回JSON或Object更方便使用</p>
     * @param jsonStr
     * @param keyPath
     * @return
     */
    public static String getJsonString(String jsonStr, String keyPath) {
        String[] keyArray = customSplit(keyPath);
        JSON curResult = null;
        JSON curJson;
        if (isInteger(keyArray[0])) {
            curJson = JSON.parseArray(jsonStr);
        } else {
            curJson = JSON.parseObject(jsonStr);
        }
        for (int i = 0; i < keyArray.length; i++) {
            int j = i + 1;
            //判断父Json当前类型,JSONObject
            if (curJson instanceof JSONObject) {
                //判断子Json类型,分两种情况，最后一个key是无法确认其value类型的，默认返回string
                if (j == keyArray.length) {
                    return ((JSONObject) curJson).getString(keyArray[i]);
                }
                //如果不是最后一个key,可以根据其后的key的类型来判断当前key对应的value类型
                if (isInteger(keyArray[j])) {
                    curResult = ((JSONObject) curJson).getJSONArray(keyArray[i]);
                } else {
                    curResult = ((JSONObject) curJson).getJSONObject(keyArray[i]);
                }
            } //判断父Json当前类型,JSONArray
            else {
                //判断子Json类型，最后一个key默认返回string
                if (j == keyArray.length) {
                    return ((JSONArray) curJson).getString(Integer.parseInt(keyArray[i]));
                }
                if (isInteger(keyArray[j])) {
                    curResult = ((JSONArray) curJson).getJSONArray(Integer.parseInt(keyArray[i]));
                } else {
                    curResult = ((JSONArray) curJson).getJSONObject(Integer.parseInt(keyArray[i]));
                }
            }
            curJson = curResult;
        }
        return curResult.toJSONString();
    }

}

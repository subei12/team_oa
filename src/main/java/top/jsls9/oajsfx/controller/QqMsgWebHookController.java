package top.jsls9.oajsfx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jsoup.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.jsls9.oajsfx.annotation.HookMessage;
import top.jsls9.oajsfx.enums.EventType;
import top.jsls9.oajsfx.hlxPojo.PostsJsonRootBean;
import top.jsls9.oajsfx.hlxPojo.qqPojo.MessageChain;
import top.jsls9.oajsfx.hlxPojo.qqPojo.MessageRootBean;
import top.jsls9.oajsfx.hlxPojo.qqPojo.SendRootBean;
import top.jsls9.oajsfx.utils.JsonUtiles;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author bSu
 * @date 2021/8/22 - 10:03
 */
@Api(tags = "QqMsgWebHook接口")
@RestController
public class QqMsgWebHookController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    final static String MESSAGE_TYPE_PLAIN = "Plain";

    @PostMapping("/qq/webhook")
    public Object qqWebHook(@RequestBody MessageRootBean messageRootBean){
    /*public Object qqWebHook(@RequestBody Map map){
        JSONObject json = new JSONObject();
        MessageRootBean messageRootBean = json.parseObject(json.toJSONString(map), MessageRootBean.class);*/
        System.out.println(JSON.toJSON(messageRootBean));
        String str = "{\"command\":\"sendFriendMessage\",\n" +
                "\"content\":{\n" +
                "  \"target\":2622798046,\n" +
                "  \"messageChain\":[\n" +
                "    { \"type\":\"Plain\", \"text\":\"hello\\n\" },\n" +
                "    { \"type\":\"Plain\", \"text\":\"world\\n\" },\n" +
                "    { \"type\":\"Plain\", \"text\":\"我是中文\" },\n" +
                "\t{ \"type\":\"Image\", \"url\":\"https://i0.hdslb.com/bfs/album/67fc4e6b417d9c68ef98ba71d5e79505bbad97a1.png\" }\n" +
                "  ]\n" +
                "}\n" +
                "}";

        //str = "{\"command\":\"sendFriendMessage\",\"content\":{\"group\":0,\"messageChain\":[{\"faceId\":0,\"groupId\":0,\"id\":0,\"length\":0,\"senderId\":0,\"size\":0,\"target\":0,\"targetId\":0,\"text\":\"测试成功\",\"time\":0,\"type\":\"Plain\",\"value\":0}],\"qq\":0,\"subject\":0,\"target\":2622798046}}";
        /*if ( true){
            return str;
        }*/
        //找到定义的注解
        //Class<HookMessage> hookMessageClass = HookMessage.class;
        try {
            String event = messageRootBean.getType();
            List<MessageChain> messageChains = messageRootBean.getMessageChain();
            if(messageChains == null || messageChains.size() <= 0){
                return null;
            }
            for(MessageChain messageChain : messageChains){
                String type = messageChain.getType();
                if(StringUtils.isBlank(type) || !MESSAGE_TYPE_PLAIN.equals(type)){
                    continue;
                }
                //关键词匹配仅限Plain类型（文本）
                SendRootBean hookMessagesMethods = (SendRootBean) getHookMessagesMethods(event, messageChain.getText(), messageRootBean);
                logger.info("webHook路由返回信息: "+ JSON.toJSONString(hookMessagesMethods));
                //仅匹配成功一次路由
                if(hookMessagesMethods != null){//暂时先这样，后续可能会有表示来区分是否匹配的到路由
                    return hookMessagesMethods;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("qqWebHook，上报失败: "+e.getMessage());
        }

        //getHookMessagesMethods(event,);

        return null;
    }

    @HookMessage(event = {EventType.FRIENDMESSAGE})
    public String test1(){
        EventType friendmessage = EventType.FRIENDMESSAGE;
        return null;
    }




    /**
     * 从包package中获取所有的Class
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClasses(String packageName) throws Exception{

        // 第一个class类的集合
        //List<Class<?>> classes = new ArrayList<Class<?>>();
        Set<Class<?>> classes = new HashSet<>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中，以下俩种方法都可以
                    //网上的第一种方法，
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                    //网上的第二种方法
                    //addClass(classes,filePath,packageName);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    public void addClass(Set<Class<?>> classes, String filePath, String packageName) throws Exception{
        File[] files=new File(filePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile()&&file.getName().endsWith(".class"))||file.isDirectory();
            }
        });
        for(File file:files){
            String fileName=file.getName();
            if(file.isFile()){
                String classsName=fileName.substring(0,fileName.lastIndexOf("."));
                if(! StringUtil.isBlank(packageName)){
                    classsName=packageName+"."+classsName;
                }
                doAddClass(classes,classsName);
            }

        }
    }

    public void doAddClass(Set<Class<?>> classes, final String classsName) throws Exception{
        ClassLoader classLoader=new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return super.loadClass(name);
            }
        };
        //Class<?> cls= ClassLoader.loadClass(classsName);
        classes.add(classLoader.loadClass(classsName));
    }

    //找也用了Controller注解的类
    private static Set<Class<?>> hookMessages;

    public static Set<Class<?>> getHookMessages() throws Exception{
        if (hookMessages == null) {
            hookMessages = new HashSet<>();
            Set<Class<?>> clsList = getClasses("top.jsls9.oajsfx.qqHookMessage");
            if (clsList != null && clsList.size() > 0) {
                for (Class<?> cls : clsList) {
                    hookMessages.add(cls);
                }
            }
        }
        return hookMessages;
    }

    /**
     * 通过事件和文本消息找到对应的路由
     * @param event 事件
     * @param plain 文本消息
     * @param args 去量的消息对象
     * @throws Exception
     */
    public Object getHookMessagesMethods(String event, String plain, MessageRootBean args) throws Exception{
        for (Class<?> cls : hookMessages) {
            Method[] methods = cls.getMethods();
            for (Method method : methods) {
                HookMessage annotation = method.getAnnotation(HookMessage.class);
                if (annotation != null) {
                    EventType[] events = annotation.event();//找到RequestMapping的注入value值
                    /*if (value.equals("/about")) {//判断是不是/about，是的话，就调用about(args)方法
                        method.invoke(cls.newInstance(), "args"); //第二个参数是方法里的参数
                    }*/
                    for(EventType e : events){
                        String value = e.getValue();
                        //判断是否与当前事件一致
                        if (StringUtils.isNotBlank(event) && value.equals(event)){
                            //事件一致时，判断关键字是否一致
                            String key = annotation.plain();
                            if(StringUtils.isNotBlank(plain) && plain.equals(key)){//欢迎来到嵌套低于
                                Object invoke = method.invoke(cls.newInstance(), args);
                                return invoke;
                            }
                            logger.info("plain：{}未匹配到相应的路由", key);
                        }
                        logger.info("event：{}未匹配到相应的路由", event);
                    }

                }
            }
        }
        return null;
    }
    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    //classes.add(Class.forName(packageName + '.' + className));
                    //经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }

    //初始化class
    static {
        try {
            getHookMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)throws Exception {

    }


}

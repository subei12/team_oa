package top.jsls9.oajsfx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import top.jsls9.oajsfx.dao.IntegralMallDao;
import top.jsls9.oajsfx.dao.IntegralMallLogDao;
import top.jsls9.oajsfx.dao.UserDao;
import top.jsls9.oajsfx.model.IntegralMall;
import top.jsls9.oajsfx.model.IntegralMallLog;
import top.jsls9.oajsfx.model.User;
import top.jsls9.oajsfx.service.IntegralMallService;
import top.jsls9.oajsfx.utils.RespBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 取错名字了，应该叫商品表 goods，现在积分商店显得有点尴尬
 * @author bSu
 * @date 2021/6/14 - 18:33
 */
@Service
public class IntegralMallServiceImpl implements IntegralMallService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IntegralMallDao integralMallDao;
    @Autowired
    private IntegralMallLogDao integralMallLogDao;

    @Autowired
    private UserDao userDao;


    @Override
    public Map<String,Object> integralMallList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<IntegralMall> integralMalls = integralMallDao.integralMallList();
        PageInfo<IntegralMall> pageInfo = new PageInfo<IntegralMall>(integralMalls);
        Map<String,Object> map = new HashMap<>();
        List<IntegralMall> list = pageInfo.getList();
        map.put("count",pageInfo.getTotal());
        map.put("rows",list);
        return map;
    }

    /**
     * 商品兑换
     * @param id 商品id
     * @param user 操作人
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)//修改为读已提交，不然mysql默认的可重复度会使乐观锁读不到最新版本号；但也需要可以把重试阶段使用spring的传播机制时每次重试都是新的事务，这样应该可以读到最新版本号了吧（猜想）
    public Object buyGoosd(String id, User user) {
        logger.info("+++++++++++++++++++++++++++++++");
        logger.info("商品兑换开始...");
        int i = 0;
        do {
            //查询商品
            IntegralMall integralMall = integralMallDao.selectByPrimaryKey(id);
            logger.info("商品ID："+integralMall.getIntegralMallId()+"；商品库存："+integralMall.getGoodsCount()+"；此商品当前版本号："+integralMall.getVersion());
            if(integralMall.getGoodsCount()<=0){
                logger.info("库存不足，兑换失败。");
                return RespBean.error("库存不足，请更换商品兑换或者下个月再来吧。");
            }
            if(integralMall.getGoodsPrice()>user.getIntegral()){
                logger.info("积分不足，兑换失败。");
                return RespBean.error("积分不足，请获取足够积分后再来吧。");
            }
            //库存充足的情况下，带上版本号去更新商品库存；CAS 乐观锁
            int i1 = integralMallDao.updateGoodsCountByCas(integralMall);
            if(i1>=1){
                //更新成功，插入兑换记录
                IntegralMallLog integralMallLog = new IntegralMallLog();
                integralMallLog.setCount(1);//目前单个商城每月只能兑换一次
                integralMallLog.setUserId(user.getId());
                integralMallLog.setIntegralMallId(id);
                integralMallLogDao.insert(integralMallLog);
                //减用户积分
                user.setIntegral(-integralMall.getGoodsPrice());
                userDao.updateIntegralByHlxUserId(user);
                return RespBean.success("兑换成功。");
            }
            i++;
            //并发时重试三次，仍未兑换成功，返回兑换频繁。
        }while (i<=3);
        logger.info("重试三次后仍未兑换成功。");
        return RespBean.error("当前兑换繁忙，请稍后重试。");

    }

    @Override
    public IntegralMall selectByPrimaryKey(String id) {
        return integralMallDao.selectByPrimaryKey(id);
    }

}

package com.lyon.search;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:代理查询类，充当代理主题角色     <br>
 * Date 9/27/2018 2:42 PM                    <br>
 */
public class ProxySearch implements ISearch{
    private Logger logger;
    private AccessValidator accessValidator;
    private RealSearch realSearch = new RealSearch();

    /**
     * 搜索方法接口
     *
     * @param user    用户名
     * @param keyword 关键词
     * @return 结果
     */
    @Override
    public String search(String user, String keyword) {
        if (this.validate(user)) {
            // 调用真实主题对象的查询方法
            String result = realSearch.search(user, keyword);
            // 记录查询日志
            this.log(user);
            // 返回查询结果
            return result;
        } else {
            return null;
        }
    }

    public boolean validate(String user) {
        accessValidator = new AccessValidator();
        return accessValidator.validate(user);
    }

    public void log(String user) {
        logger = new Logger();
        logger.log(user);
    }
}

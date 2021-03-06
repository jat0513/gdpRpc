package com.gdprpc.registry;

import com.gdprpc.common.bean.ServerInfo;

/**
 * Created by 我是金角大王 on 2017-10-25.
 */
public interface RegistryService {
    /***
     * 链接到zookeeper服务器
     */
    public void connentToRegistryService();

    /***
     * 断开zookeeper服务器链接
     */
    public void destroy();

    /***
     * 注册服务
     * @param serverName 服务名称
     */
    public void register(String serverName);

    /***
     * 获取服务
     * @param serverName 服务名称
     * @return 服务地址
     */
    public ServerInfo discover(String serverName);
}

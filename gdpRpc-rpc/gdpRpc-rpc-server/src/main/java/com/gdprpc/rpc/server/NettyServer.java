package com.gdprpc.rpc.server;

import com.gdprpc.common.bean.RpcRequest;
import com.gdprpc.common.bean.RpcResponse;
import com.gdprpc.common.codec.RpcDecoder;
import com.gdprpc.common.codec.RpcEncoder;
import com.gdprpc.registry.zookeeper.ZookeeperRegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by 我是金角大王 on 2017-10-22.
 */
public class NettyServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline()
                            .addLast(new RpcDecoder(RpcRequest.class)) // 解码 RPC 请求
                            .addLast(new RpcEncoder(RpcResponse.class)) // 编码 RPC 响应
                            .addLast(new ServerChannelInboundHandler());
                }
            });
            b.option(ChannelOption.SO_BACKLOG, 1);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 服务器绑定端口监听
            ChannelFuture f = b.bind(8099).sync();
            ZookeeperRegistryService registryservice = new ZookeeperRegistryService();
            registryservice.connentToRegistryService();
            registryservice.register("com.gdprpc.rpc.client.User");
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

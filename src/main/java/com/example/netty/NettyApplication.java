package com.example.netty;

import com.example.netty.echoServer.channelHandlers.EchoServerChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class NettyApplication {

	public static void main(String[] args) throws InterruptedException {
//		SpringApplication.run(NettyApplication.class, args);
		int port = 8080;

		new EchoServer(port).start();
	}

	public static final class EchoServer {
		private final int port;

		public EchoServer(int port) {
			this.port = port;
		}

		public void start() throws InterruptedException {
			final EchoServerChannelHandler echoServerChannelHandler = new EchoServerChannelHandler();
			try (EventLoopGroup eventLoopGroup = new NioEventLoopGroup()) {
				ServerBootstrap bootstrap = new ServerBootstrap();
				bootstrap.group(eventLoopGroup)
						.channel(NioServerSocketChannel.class)
						.localAddress(port)
						.childHandler(new ChannelInitializer<SocketChannel>() {
							@Override
							protected void initChannel(SocketChannel ch) throws Exception {
								ch.pipeline().addLast(echoServerChannelHandler);
							}
						});
				ChannelFuture channelFuture = bootstrap.bind().sync();
				channelFuture.channel().closeFuture().sync();
			}

		}
	}

}

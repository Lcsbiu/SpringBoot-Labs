package cn.iocoder.springboot.lab67.nettyserverdemo.messagehandler.auth;

import cn.iocoder.springboot.lab67.nettycommondemo.codec.Invocation;
import cn.iocoder.springboot.lab67.nettycommondemo.dispacher.MessageHandler;
import cn.iocoder.springboot.lab67.nettyserverdemo.message.auth.AuthRequest;
import cn.iocoder.springboot.lab67.nettyserverdemo.message.auth.AuthResponse;
import cn.iocoder.springboot.lab67.nettyserverdemo.server.NettyChannelManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthRequestHandler implements MessageHandler<AuthRequest> {

    @Autowired
    private NettyChannelManager nettyChannelManager;

    @Override
    public void execute(Channel channel, AuthRequest authRequest) {
        // 如果未传递 accessToken
        if (StringUtils.isEmpty(authRequest.getAccessToken())) {
            AuthResponse authResponse = new AuthResponse().setCode(1).setMessage("认证 accessToken 未传入");
            channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
            return;
        }

        // 添加到 WebSocketUtil 中
        // 考虑到代码简化，我们先直接使用 accessToken 作为 User
        nettyChannelManager.addUser(channel, authRequest.getAccessToken());

        // 判断是否认证成功。这里，假装直接成功
        AuthResponse authResponse = new AuthResponse().setCode(0);
        channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }

}
//package tech.msop.auth.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import tech.msop.auth.util.AuthUtils;
//
//import javax.websocket.server.ServerEndpointConfig;
//
///**
// * webSocket鉴权配置
// *
// * @author ruozhuliufeng
// */
//@Slf4j
//public class WcAuthConfigurator extends ServerEndpointConfig.Configurator {
//    @Override
//    public boolean checkOrigin(String originHeaderValue) {
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        try {
//            //检查token有效性
//            AuthUtils.checkAccessToken(servletRequestAttributes.getRequest());
//        } catch (Exception e) {
//            log.error("WebSocket-auth-error", e);
//            return false;
//        }
//        return super.checkOrigin(originHeaderValue);
//    }
//}

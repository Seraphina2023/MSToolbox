package tech.msop.auth.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.msop.auth.constants.SecurityConstants;
import tech.msop.auth.model.MsUser;
import tech.msop.core.tool.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取当前登录人工具类
 *
 * @author ruozhuliufeng
 */
public class LoginUserUtil {

    /**
     * 获取当前登录人
     *
     * @param request 请求
     * @param isFull  是否获取全部信息
     * @return 登录人信息
     */
    public static MsUser getCurrentUser(HttpServletRequest request, boolean isFull) {
        MsUser user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)){
            Object principal = authentication.getPrincipal();
            // 客户端模式只返回一个clientId
            if (principal instanceof MsUser){
                user = (MsUser) principal;
            }
        }
        if (user == null){
            String userId = request.getHeader(SecurityConstants.USER_ID_HEADER);
            String username = request.getHeader(SecurityConstants.USER_HEADER);
            // 处理用户信息
            user = new MsUser();
            user.setUserId(Long.valueOf(userId));
            user.setUserName(username);
        }
        return user;
    }

    public static MsUser getCurrentUser(boolean isFull){
        HttpServletRequest request = WebUtil.getRequest();
        return getCurrentUser(request,isFull);
    }


}

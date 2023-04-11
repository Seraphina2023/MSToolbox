package tech.msop.auth.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import tech.msop.auth.model.MsUser;

/**
 * 登录用户holder
 *
 * @author ruozhuliufeng
 */
public class LoginUserContextHolder {
    private static final ThreadLocal<MsUser> CONTEXT = new TransmittableThreadLocal<>();

    public static void setUser(MsUser user) {
        CONTEXT.set(user);
    }

    public static MsUser getUser() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}

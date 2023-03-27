package tech.msop.core.tool.constant;

/**
 * 系统默认角色
 *
 * @author ruozhuliufeng
 */
public interface RoleConstant {

    String ADMINISTRATOR = "administrator";
    String ADMIN = "admin";

    String HAS_ROLE_ADMINISTRATOR = "hasRole('" + ADMINISTRATOR + "')";
    String HAS_ROLE_ADMIN = "hasRole('" + ADMINISTRATOR + "," + ADMIN + "')";

    String USER = "user";

    String HAS_ROLE_USER = "hasRole('" + USER + "')";

    String TEST = "test";

    String HAS_ROLE_TEST = "hasRole('" + TEST + "')";
}

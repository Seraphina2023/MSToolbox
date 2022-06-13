package com.msop.core.secure.registry;

import com.msop.core.secure.properties.AuthSecure;
import com.msop.core.secure.properties.BasicSecure;
import com.msop.core.secure.properties.ClientSecure;
import com.msop.core.secure.properties.SignSecure;
import com.msop.core.secure.provider.HttpMethod;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 安全框架统一设置
 *
 * @author ruozhuliufeng
 */
@Data
public class SecureRegistry {
    /**
     * 是否开启鉴权
     */
    private boolean enabled = false;
    /**
     * 是否开启授权
     */
    private boolean authEnabled = true;
    /**
     * 是否开启基础认证
     */
    private boolean basicEnabled = true;
    /**
     * 是否开启签名认证
     */
    private boolean signEnabled = true;
    /**
     * 是否开启客户端认证
     */
    private boolean clientEnabled = true;
    /**
     * 默认放行规则
     */
    private final List<String> defaultExcludePatterns = new ArrayList<>();
    /**
     * 自定义放行规则
     */
    private final List<String> excludePatterns = new ArrayList<>();
    /**
     * 自定义授权集合
     */
    private final List<AuthSecure> authSecures = new ArrayList<>();

    /**
     * 基础认证配置
     */
    private final List<BasicSecure> basicSecures = new ArrayList<>();

    /**
     * 签名配置
     */
    private final List<SignSecure> signSecures = new ArrayList<>();

    /**
     * 客户端
     */
    private final List<ClientSecure> clientSecures = new ArrayList<>();

    public SecureRegistry() {
        this.defaultExcludePatterns.add("/actuator/health/**");
        this.defaultExcludePatterns.add("/v2/api-docs/**");
        this.defaultExcludePatterns.add("/auth/**");
        this.defaultExcludePatterns.add("/token/**");
        this.defaultExcludePatterns.add("/log/**");
        this.defaultExcludePatterns.add("/menu/routes");
        this.defaultExcludePatterns.add("/menu/auth-routes");
        this.defaultExcludePatterns.add("/menu/top-menu");
        this.defaultExcludePatterns.add("/process/resource-view");
        this.defaultExcludePatterns.add("/process/diagram-view");
        this.defaultExcludePatterns.add("/manager/check-upload");
        this.defaultExcludePatterns.add("/error/**");
        this.defaultExcludePatterns.add("/assets/**");
    }

    /**
     * 设置单个放行API
     *
     * @param pattern API
     */
    public SecureRegistry excludePathPattern(String pattern) {
        this.excludePatterns.add(pattern);
        return this;
    }

    /**
     * 设置放行API 集合
     */
    public SecureRegistry excludePathPatterns(String... patterns) {
        this.excludePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    /**
     * 设置放行API
     */
    public SecureRegistry excludePathPatterns(List<String> patterns) {
        this.excludePatterns.addAll(patterns);
        return this;
    }

    /**
     * 设置单个自定义授权
     */
    public SecureRegistry addAuthPattern(HttpMethod method,String pattern,String expression){
        this.authSecures.add(new AuthSecure(method,pattern,expression));
        return this;
    }

    /**
     * 设置自定义授权集合
     */
    public SecureRegistry addAuthPatterns(List<AuthSecure> authSecures){
        this.authSecures.addAll(authSecures);
        return this;
    }

    /**
     * 返回自定义授权集合
     */
    public List<AuthSecure> getAuthSecures(){
        return this.authSecures;
    }
    /**
     * 设置单个基础认证
     */
    public SecureRegistry addBasicPattern(HttpMethod method,String pattern,String username,String password){
        this.basicSecures.add(new BasicSecure(method,pattern,username,password));
        return this;
    }

    /**
     * 设置基础认证集合
     */
    public SecureRegistry addBasicPatterns(List<BasicSecure> basicSecures){
        this.basicSecures.addAll(basicSecures);
        return this;
    }

    /**
     * 返回基础认证集合
     */
    public List<BasicSecure> getBasicSecures(){
        return this.basicSecures;
    }
    /**
     * 设置单个签名认证
     */
    public SecureRegistry addSignPattern(HttpMethod method,String pattern,String crypto){
        this.signSecures.add(new SignSecure(method,pattern,crypto));
        return this;
    }

    /**
     * 设置签名认证集合
     */
    public SecureRegistry addSignPatterns(List<SignSecure> signSecures){
        this.signSecures.addAll(signSecures);
        return this;
    }

    /**
     * 返回签名认证集合
     */
    public List<SignSecure> getSignSecures(){
        return this.signSecures;
    }
}

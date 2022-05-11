package com.msop.core.secure.provider;

public interface IClientDetailsService {
    /**
     * 根据clientId 获取Client 详情
     * @param clientId 客户端ID
     * @return IClientDetails
     */
    IClientDetails loadClientByClientId(String clientId);
}

package com.msop.core.secure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("ms.secure")
public class MsSecureProperties {
    /**
     * 客户端
     */
    private final List<ClientSecure> client = new ArrayList<>();
    /**
     * 放行URL
     */
    private final List<String> skipUrl = new ArrayList<>();
}

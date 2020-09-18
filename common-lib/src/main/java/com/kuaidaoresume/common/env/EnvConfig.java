package com.kuaidaoresume.common.env;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * environment configuration
 *
 * @author Aaron Liu
 */
@Data
@Builder
public class EnvConfig {

    private String name;
    private boolean debug;
    private String externalApex;
    private String internalApex;
    private String scheme;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static Map<String, EnvConfig> map;

    static {
        map = new HashMap<String, EnvConfig>();
        EnvConfig envConfig = EnvConfig.builder().name(EnvConstant.ENV_DEV)
                .debug(true)
                .externalApex("kuaidaoresume-v2.local")
                .internalApex(EnvConstant.ENV_DEV)
                .scheme("http")
                .build();
        map.put(EnvConstant.ENV_DEV, envConfig);

        envConfig = EnvConfig.builder().name(EnvConstant.ENV_TEST)
                .debug(true)
                .externalApex("kuaidaoresume-v2.local")
                .internalApex(EnvConstant.ENV_DEV)
                .scheme("http")
                .build();
        map.put(EnvConstant.ENV_TEST, envConfig);

        // for China aliyun k8s , enable debug and use http and kuaidaoresume-uat.local
        // in real world, disable debug and use http and kuaidaoresume-uat.com in UAT environment
        envConfig = EnvConfig.builder().name(EnvConstant.ENV_UAT)
                .debug(true)
                .externalApex("kuaidaoresume-uat.local")
                .internalApex(EnvConstant.ENV_UAT)
                .scheme("http")
                .build();
        map.put(EnvConstant.ENV_UAT, envConfig);

        envConfig = EnvConfig.builder().name(EnvConstant.ENV_CA_PREPROD)
                .debug(false)
                .externalApex("ca.smartresume.careers")
                .internalApex(EnvConstant.ENV_CA_PREPROD)
                .scheme("http") // CA might need here
                .build();
        map.put(EnvConstant.ENV_CA_PREPROD, envConfig);

        envConfig = EnvConfig.builder().name(EnvConstant.ENV_US_PREPROD)
                .debug(false)
                .externalApex("us.smartresume.careers")
                .internalApex(EnvConstant.ENV_US_PREPROD)
                .scheme("http") // CA might need here
                .build();
        map.put(EnvConstant.ENV_US_PREPROD, envConfig);

        envConfig = EnvConfig.builder().name(EnvConstant.ENV_PROD)
                .debug(false)
                .externalApex("kuaidaoresume.com") // real domain for kuai dao in prod
                .internalApex(EnvConstant.ENV_PROD)
                .scheme("https") // CA needed here
                .build();
        map.put(EnvConstant.ENV_PROD, envConfig);
    }

    public static EnvConfig getEnvConfg(String env) {
        EnvConfig envConfig = map.get(env);
        if (envConfig == null) {
            envConfig = map.get(EnvConstant.ENV_DEV);
        }
        return envConfig;
    }
}

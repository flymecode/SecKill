package com.xupt.seckill.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11AprProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 *  当 spring 容器内没有 TomcatEmbeddedServletContainerFactory 这个 bean 时，会把此bean加载进 spring 容器中
 * @author maxu
 * @date 2019/6/23
 */
@Component
public class WebServerConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 使用对应工厂类提供给我们的接口定制化我们的 tomcat connector
        ((TomcatServletWebServerFactory)factory).addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                Http11AprProtocol protocol = (Http11AprProtocol)connector.getProtocolHandler();
                // 设置3秒之后不响应则断开keeplive
                protocol.setKeepAliveTimeout(3000);
                // 设置10000次请求之后断开keeplive
                protocol.setMaxKeepAliveRequests(10000);
            }
        });
    }
}

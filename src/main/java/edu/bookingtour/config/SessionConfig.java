package edu.bookingtour.config;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.InvalidSessionStrategy;

@Configuration
public class SessionConfig {

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    
    @Bean
    public InvalidSessionStrategy invalidSessionStrategy() {
        return (request, response) -> {
            jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("JSESSIONID", "");
            cookie.setMaxAge(0);
            cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
            response.addCookie(cookie);
            response.sendRedirect(request.getContextPath() + "/");
        };
    }

    
    @Bean
    public ApplicationListener<ApplicationReadyEvent> invalidateExistingSessionsOnStartup() {
        return event -> {
            if (!(event.getApplicationContext() instanceof ServletWebServerApplicationContext webContext)) {
                return;
            }
            if (!(webContext.getWebServer() instanceof TomcatWebServer tomcatWebServer)) {
                return;
            }

            Tomcat tomcat = tomcatWebServer.getTomcat();
            for (Container hostChild : tomcat.getHost().findChildren()) {
                if (!(hostChild instanceof Context tomcatContext)) {
                    continue;
                }
                Manager manager = tomcatContext.getManager();
                if (manager == null) {
                    continue;
                }
                for (Session session : manager.findSessions()) {
                    try {
                        session.expire();
                    } catch (IllegalStateException ignored) {
                    }
                }
            }
        };
    }
}

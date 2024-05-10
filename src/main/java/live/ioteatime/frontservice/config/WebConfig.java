package live.ioteatime.frontservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.interceptor.UserInfoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserAdaptor userAdaptor;

    public WebConfig(@Lazy UserAdaptor userAdaptor) {
        this.userAdaptor = userAdaptor;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor(userAdaptor))
                .addPathPatterns(List.of("/admin", "/admin/**", "/sensors/**", "/change-password",
                        "/daily-report/**"));
    }
}

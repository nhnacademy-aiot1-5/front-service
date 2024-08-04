package live.ioteatime.frontservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.interceptor.UserInfoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web application을 설정 하는 Config 클래스 입니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UserAdaptor userAdaptor;

    public WebConfig(@Lazy UserAdaptor userAdaptor) {
        this.userAdaptor = userAdaptor;
    }

    /**
     * ObjectMapper 클래스의 인스턴스를 생성하고 반환합니다.
     * ObjectMapper는 java 객체를 json으로 변환 하거나 java 객체를 json으로 변환 하는데 사용 됩니다.
     * JavaTimeModule은 날짜/시간 타입들을 직렬화 하거나 역직렬화 하기 위해 사용 됩니다.
     *
     * @return JavaTimeModule이 등록된 ObjectMapper 클래스의 인스턴스를 반환 합니다.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * InterceptorRegistry에 interceptor들을 추가합니다.
     * UserInfoInterceptor는 사용자 정보 관련 처리를 담당 하는 interceptor 입니다.
     * 요청이 처리되고 controller 메서드가 실행된 후 interceptor가 호출 됩니다.
     *
     * @param registry interceptor가 추가될 InterceptorRegistry 입니다.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor(userAdaptor))
                .addPathPatterns(List.of("/admin", "/admin/**", "/sensors/**", "/change-password",
                        "/daily-report/**", "/monthly-report/**", "/places/**", "/sse"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}

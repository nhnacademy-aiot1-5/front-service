package live.ioteatime.frontservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("authentication/login");
        registry.addViewController("/register").setViewName("authentication/register");
        registry.addViewController("/mypage").setViewName("mypage/mypage");
        registry.addViewController("/mypage/electronic").setViewName("mypage/electronic");
        registry.addViewController("/mypage/electronic/bottom").setViewName("mypage/electronic-bottom");
    }

}

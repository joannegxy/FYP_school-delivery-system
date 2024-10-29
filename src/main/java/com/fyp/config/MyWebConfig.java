package com.fyp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/logintoadmin",
                        "/logintocustomer",
                        "/logintoshopworker",
                        "/logintoshopowner",
                        "/logintodeliveryman",
                        "/login_admin",
                        "/login_customer",
                        "/index",
                        "/login_shopowner",
                        "/login_shopworker",
                        "/login_deliveryman",
                        "/register",
                        "/loginpage_admin.html",
                        "/loginpage_customer.html",
                        "/loginpage_deliveryman.html",
                        "/loginpage_shopowner.html",
                        "/loginpage_shopworker.html",
                        "/notloggedin.html",
                        "/index.html",
                        "/css/**",
                        "/js/**",
                        "/fonts/**",
                        "/images/**",
                        "/toRegister",
                        "/toLogin",
                        "/logout");
        registry.addInterceptor(new NonShopOwnerInterceptor()).addPathPatterns(
                "/mainpage_shopowner.html",
                "/shopworker-list.html",
                "/sidebar_shopowner.html",
                "/deliveryman_list.html",
                "/chart_count.html",
                "/chart_total.html",
                "/shopworker/**",
                "/deliveryman/**",
                "/deliveryman-list.html"
                );

        registry.addInterceptor(new NonAdminInterceptor()).addPathPatterns(
                "/mainpage_admin.html",
                "/shopowner/**",
                "/customer/**",
                "/store/**"
        );
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/notloggedin.html").setViewName("notloggedin");
        registry.addViewController("/illegalvisit.html").setViewName(("illegalvisit"));
    }
}

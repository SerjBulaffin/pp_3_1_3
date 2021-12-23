package com.kata.bootfetch.kata_fetch.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) { //если вошедший с ролью admin, отправляем на страницу /admin
            httpServletResponse.sendRedirect("/admin");
        } else if (roles.contains("ROLE_USER")) { //если вошедший с ролью user, отправляем на страницу /user
            httpServletResponse.sendRedirect("/user");
        } else { //все остальные на стартовую страницу
            httpServletResponse.sendRedirect("/index");
        }

    }
}
/*
- LoginSuccessHandler - хэндлер, содержащий в себе алгоритм действий при успешной аутентификации. Например, тут мы можем
отправить пользователя с ролью админа на админку после логина, а с ролью юзер на главную страницу сайта и т.п.
 */
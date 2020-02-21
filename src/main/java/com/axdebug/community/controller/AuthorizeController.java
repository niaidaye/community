package com.axdebug.community.controller;

import com.axdebug.community.dto.AccesstokenDTO;
import com.axdebug.community.dto.GitHubUser;
import com.axdebug.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName AuthorizeController
 * @Description TODO
 * @Author aixu
 * @Date 2020/2/20 10:17 下午
 * @Version 1.0
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    String clientId;
    @Value("${github.client.secret}")
    String clientSecret;
    @Value("${github.redirect.uri}")
    String redirectUri;

    //  通过CallBack-url获取code，state
    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                            HttpServletRequest request) {

        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id(clientId);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(redirectUri);
        accesstokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accesstokenDTO);
        GitHubUser user = gitHubProvider.getUser(accessToken);
        if (user != null) {
            // 登录成功
            request.getSession().setAttribute("user", user);
        } else {
            // 登录失败
        }
        return "redirect:/";
    }
}

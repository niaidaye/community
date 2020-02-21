package com.axdebug.community.provider;

import com.alibaba.fastjson.JSON;
import com.axdebug.community.dto.AccesstokenDTO;
import com.axdebug.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @ClassName GitHubProvider
 * @Description TODO
 * @Author aixu
 * @Date 2020/2/20 11:04 下午
 * @Version 1.0
 */
@Component
public class GitHubProvider {
    public String getAccessToken(AccesstokenDTO accesstokenDTO) {
        String url = "https://github.com/login/oauth/access_token";

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accesstokenDTO));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String strings = string.split("&")[0].split("=")[1];
            System.out.println(strings);
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GitHubUser getUser(String accessToken) {
        String url = "https://api.github.com/user?access_token=" + accessToken;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

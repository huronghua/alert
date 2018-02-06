package com.banmatrip.alert.interceptor;

import com.google.gson.Gson;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.banmatrip.alert.constant.Constant.ALERT_AUTHORITY;

/**
 * @author jepson
 * @Description: 获取session请求拦截器
 * @create 2017-10-19 18:29
 * @Copyright: 2017 www.banmatrip.com All rights reserved.
 **/
public class SessionInterceptor implements HandlerInterceptor {


    @Value("${oauthserver.getResource.function.url}")
    private String oauthServer_getResource_url;
    @Value("${oauthserver.getResource.dataFunction.url}")
    private String oauthServer_getDataFunction_url;
    @Value("${oauthserver.getResource.userinfo.url}")
    private String oauthServer_getUserInfo_url;

    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    /**
     * 前置处理
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        try {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();
            String tokenValue = oAuth2AuthenticationDetails.getTokenValue();
            String userInfoUrl = oauthServer_getUserInfo_url + "?access_token=" + tokenValue;
            Gson gson = new Gson();
            Map<String, Object> userInfoMap = gson.fromJson(processHttp(userInfoUrl), Map.class);
            Map<String, Object> data = (Map<String, Object>) userInfoMap.get("data");
            Map<String, Object> memberArchitectureInfo = (Map<String, Object>) data.get("memberArchitectureInfo");
            Map<String, Object> memberInfo = (Map<String, Object>) memberArchitectureInfo.get("memberInfo");
            Integer userId = Integer.parseInt(String.valueOf(memberInfo.get("id")));
            String userName = (String) memberInfo.get("name");
            HttpSession httpSession = httpServletRequest.getSession();
            httpSession.setAttribute("userName", userName);
            httpSession.setAttribute("userId", userId);
            String functionInfoUrl = oauthServer_getResource_url + "?access_token=" + tokenValue;
            Map<String, Object> functionMap = gson.fromJson(processHttp(functionInfoUrl), Map.class);
            Map<String, Object> functionData = (Map<String, Object>) functionMap.get("data");
            Map<String, Object> alertFunction = (Map<String, Object>) functionData.get(ALERT_AUTHORITY);
            Map<String, Object> alertFunctionFinal = new HashMap<>();
            if (MapUtils.isNotEmpty(alertFunction)) {
                Set set = alertFunction.entrySet();
                for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String newKey = key.replaceAll("/", "");
                    alertFunctionFinal.put(newKey, true);
                }
            }

            httpSession.setAttribute("alertFunction", alertFunctionFinal);
            String dataFunctionUrl = oauthServer_getDataFunction_url + "?access_token=" + tokenValue;
            Map<String, Object> dataFunctionMap = gson.fromJson(processHttp(dataFunctionUrl), Map.class);
            Map<String, Object> dataFunction = (Map<String, Object>) dataFunctionMap.get("data");
            httpSession.setAttribute("dataFunction", dataFunction);
            logger.info("登录用户名：" + userName);
        } catch (Exception e) {
            logger.info("前置处理错误" + e.getMessage());
        }
        return true;
    }

    /**
     * 后置处理
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 完成后处理
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

    /**
     * 处理HTTP
     *
     * @param url
     * @throws IOException
     */
    private String processHttp(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}

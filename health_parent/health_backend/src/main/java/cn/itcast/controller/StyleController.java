package cn.itcast.controller;

import cn.itcast.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/style")
public class StyleController {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    private Cookie getCookie(Cookie[] cookies, String cookieName) {
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    @RequestMapping("/changeStyle")
    public Result changeStyle(String styleName) {
        System.out.println(styleName);
        Cookie styleCookie = new Cookie("styleName", styleName);
        styleCookie.setPath("/");
        styleCookie.setMaxAge(2 * 365 * 24 * 60 * 60);
        response.addCookie(styleCookie);
        return new Result(true, styleName);
    }

    @RequestMapping("/getStyle")
    public Result getStyle() {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = getCookie(cookies, "styleName");
        String styleName = cookie == null ? "style.css" : cookie.getValue();
        System.out.println(styleName);
        return new Result(true, styleName);
    }
}

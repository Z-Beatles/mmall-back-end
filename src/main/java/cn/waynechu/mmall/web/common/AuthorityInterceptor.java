package cn.waynechu.mmall.web.common;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.vo.UserInfoVO;
import com.alibaba.fastjson.JSON;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * @author waynechu
 * Created 2018-06-01 22:28
 */
public class AuthorityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);

        if (currentUser == null) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();

            String result;
            if (requestURI.startsWith("/v1/manager")) {
                result = JSON.toJSONString(Result.createByErrorMessage("尚未登录，请以管理员身份登录"));
            } else {
                result = JSON.toJSONString(Result.createByErrorMessage("用户未登录"));
            }
            printWriter.print(result);
            printWriter.flush();
            printWriter.close();
            return false;
        }

        if (requestURI.startsWith("/v1/manager") && !currentUser.getRole().equals(Const.Role.ROLE_ADMIN)) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter printWriter = response.getWriter();
            printWriter.print(JSON.toJSONString(Result.createByErrorMessage("无操作权限")));
            printWriter.flush();
            printWriter.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Do nothing here.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Do nothing here.
    }
}

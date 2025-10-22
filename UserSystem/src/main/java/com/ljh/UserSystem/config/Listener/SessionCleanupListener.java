package com.ljh.UserSystem.config.Listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Session清理监听器
 * 用于监听HTTP Session的销毁事件，并在Session销毁时从全局用户Session映射中移除对应的Session记录
 */
@Component
public class SessionCleanupListener implements HttpSessionListener {

    /**
     * 当HTTP Session被销毁时触发此方法
     * 该方法会从ServletContext的"userSessions"属性中移除当前被销毁的Session记录
     *
     * @param se HttpSessionEvent对象，包含被销毁的Session信息
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ServletContext context = session.getServletContext();

        // 从全局映射中移除对应的session
        @SuppressWarnings("unchecked")
        Map<String, HttpSession> userSessions =
                (Map<String, HttpSession>) context.getAttribute("userSessions");

        if (userSessions != null) {
            // 查找并移除对应的session
            userSessions.values().removeIf(httpSession -> httpSession.getId().equals(session.getId()));
            //打印Session管理器中的Session信息
            System.out.println("Session清理监听器：Session管理器中的Session信息：" + userSessions);
        }
    }
}


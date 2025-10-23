package com.ljh.UserSystem.utils;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static cn.dev33.satoken.SaManager.log;
import static com.ljh.UserSystem.constant.EmailConstant.EMAIL_CODE;
import static com.ljh.UserSystem.constant.EmailConstant.EMAIL_CODE_CREAT_TIME;

@Component
public class MailMsg {
    @Resource
    private JavaMailSenderImpl mailSender;
//    @Resource
//    private RedisTemplate<String,String> redisTemplate;

    @Value("${app.name}")
    private String appName;

    public boolean mail(String email, HttpServletRequest  request) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //生成随机验证码
        String code = CodeGeneratorUtil.generateCode(6);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //设置一个html邮件信息
        helper.setText("<h3>欢迎使用老江湖的" + appName + "应用</h3>"+"<p style='color: blue'> 您的邮箱验证码为 "+code+"（有效期一分钟）", true);
        //设置邮件主题名
        helper.setSubject("来自 老江湖 的验证码");
        //发给谁-》邮箱地址
        helper.setTo(email);
        //谁发的-》发送人邮箱
        helper.setFrom("l2292360909@163.com");
//        //将邮箱验证码以邮件地址为key存入redis,1分钟过期
//        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(1));
        //替代方案，存储在Session中
        HttpSession session = request.getSession();
        session.setAttribute(EMAIL_CODE, code);
        session.setAttribute(EMAIL_CODE_CREAT_TIME, System.currentTimeMillis());
        try {
            mailSender.send(mimeMessage);
            log.debug("发送短信验证码成功，验证码："+code);
        } catch (MailException e) {
            // 处理邮件发送失败
            log.error("邮件发送失败", e);
        }
        return true;
    }
}

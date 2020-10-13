package com.kuaidaoresume.mail.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.IToLog;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.env.EnvConstant;
import com.kuaidaoresume.mail.config.AppConfig;
import com.kuaidaoresume.mail.dto.EmailRequest;
import io.sentry.SentryClient;
import io.sentry.context.Context;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;


@Service
public class MailSendService {

    private static ILogger logger = SLoggerFactory.getLogger(MailSendService.class);

    @Autowired
    EnvConfig envConfig;

    @Autowired
    SentryClient sentryClient;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Async(AppConfig.ASYNC_EXECUTOR_NAME)
    public void sendMailAsync(EmailRequest req) {
        IToLog logContext = () -> {
            return new Object[]{
                "subject", req.getSubject(),
                "to", req.getTo(),
                "html_body", req.getHtmlBody()
            };
        };

        // In dev and uat - only send emails to @gmail.com // TODO: Aaron Liu prepend env for sanity

        try {
            MimeMessage message = getMimeMessage(req);
            javaMailSender.send(message);
            logger.info("Successfully sent email - request id : " + message, logContext);
        } catch (Exception ex) {
            Context sentryContext = sentryClient.getContext();
            sentryContext.addTag("subject", req.getSubject());
            sentryContext.addTag("to", req.getTo());
            sentryClient.sendException(ex);
            logger.error("Unable to send email ", ex, logContext);
        }
    }

    private MimeMessage getMimeMessage(EmailRequest req) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper =
            new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        org.thymeleaf.context.Context tlContext = new org.thymeleaf.context.Context();
        System.out.println(req.getModel());

        tlContext.setVariables(req.getModel());
        // TODO: Aaron Liu switch to other templates as necessary
        String html = templateEngine.process("welcome-email-template", tlContext);

        helper.setTo(req.getTo());
        helper.setText(html, true);
        helper.setSubject(req.getSubject());
        helper.setFrom(req.getFrom());

        return message;
    }
}
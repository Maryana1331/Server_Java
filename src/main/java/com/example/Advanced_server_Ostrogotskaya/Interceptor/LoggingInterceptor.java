package com.example.Advanced_server_Ostrogotskaya.Interceptor;

import com.example.Advanced_server_Ostrogotskaya.entities.LogEntity;
import com.example.Advanced_server_Ostrogotskaya.repositories.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import static com.example.Advanced_server_Ostrogotskaya.constants.Constants.ANONYMOUS;

@Getter
@Setter
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final LogRepository logRepository;

    public LoggingInterceptor(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        LogEntity logEntity = new LogEntity();
        logEntity.setMethod(request.getMethod());
        logEntity.setUri(request.getRequestURI());

        String remoteUser = request.getRemoteUser();
        logEntity.setUserName((remoteUser != null && !remoteUser.trim().isEmpty()) ? remoteUser : ANONYMOUS);

        if (ex != null) {
            logEntity.setErrorMessage(ex.getMessage() != null ? ex.getMessage() : "Нет сообщения");
            logEntity.setStatus(String.valueOf(response.getStatus()));
            logRepository.save(logEntity);
        }
    }
}

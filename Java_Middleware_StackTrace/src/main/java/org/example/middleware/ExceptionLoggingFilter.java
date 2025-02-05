package org.example.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.ExceptionLog;
import org.example.entity.RequestLog;
import org.example.helper.CustomException;
import org.example.helper.LogHelper;
import org.example.helper.ServerType;
import org.example.repository.ExceptionLogRepository;
import org.example.repository.RequestLogRepository;
import org.example.response.TransactionResult;
import org.example.response.TransactionStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class ExceptionLoggingFilter implements Filter {

    private final RequestLogRepository requestLogRepository;
    private final ExceptionLogRepository exceptionLogRepository;
    // Example: from config or hardcoded
    private final ServerType serverType = ServerType.SQL;

    public ExceptionLoggingFilter(RequestLogRepository requestLogRepository,
                                  ExceptionLogRepository exceptionLogRepository) {
        this.requestLogRepository = requestLogRepository;
        this.exceptionLogRepository = exceptionLogRepository;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        TransactionResult transactionResult = new TransactionResult();
        transactionResult.setTransactionStatus(TransactionStatus.NOT_STARTED);

        // 1) Create RequestLog and save
        RequestLog requestLog = LogHelper.createRequestLog(httpRequest, serverType);
        requestLogRepository.save(requestLog);
        int requestId = requestLog.getId(); // ID after DB save

        try {
            // 2) Proceed with filter chain
            chain.doFilter(request, response);

            // 3) Update request log status code
            requestLog.setResponseCode(httpResponse.getStatus());
            requestLogRepository.save(requestLog);

        } catch (CustomException e) {
            handleCustomException(httpRequest, httpResponse, e, requestId, transactionResult);
        } catch (Exception e) {
            handleException(httpRequest, httpResponse, e, requestId, transactionResult);
        }
    }

    private void handleCustomException(HttpServletRequest request,
                                       HttpServletResponse response,
                                       CustomException e,
                                       int requestId,
                                       TransactionResult transactionResult) throws IOException {

        // 1) Save exception log
        ExceptionLog exceptionLog = LogHelper.createExceptionLogWithCustomException(request, e, serverType, requestId);
        exceptionLogRepository.save(exceptionLog);

        // 2) Update RequestLog
        //    If your repo method is named "finalize" currently, rename it to something else,
        //    or call findById + save again:
        Optional<RequestLog> existingReq = requestLogRepository.findById(requestId);
        existingReq.ifPresent(rLog -> {
            rLog.setResponseCode(e.getErrorCodes());
            requestLogRepository.save(rLog);
        });

        // 3) Fill TransactionResult
        transactionResult.setTransactionStatus(TransactionStatus.FAILED);
        String message = (e.getMessage() != null) ? e.getMessage() : "Bilinmeyen bir hata oluştu. Hata Kodu: 000";
        transactionResult.getMessages().add(message);

        // 4) Return JSON with status 200 (to match old behavior)
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        String json = new ObjectMapper().writeValueAsString(transactionResult);
        response.getWriter().write(json);
    }

    private void handleException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Exception e,
                                 int requestId,
                                 TransactionResult transactionResult) throws IOException {

        // 1) Save exception log
        ExceptionLog exceptionLog = LogHelper.createExceptionLogWithException(request, e, serverType, requestId);
        exceptionLogRepository.save(exceptionLog);

        // 2) Update RequestLog
        Optional<RequestLog> existingReq = requestLogRepository.findById(requestId);
        existingReq.ifPresent(rLog -> {
            // In old C# code, 418 was used
            rLog.setResponseCode(418);
            requestLogRepository.save(rLog);
        });

        // 3) Fill TransactionResult
        transactionResult.setTransactionStatus(TransactionStatus.FAILED);
        String message = (e.getMessage() != null) ? e.getMessage() : "Bilinmeyen bir hata oluştu. Hata Kodu: 000";
        transactionResult.getMessages().add(message);

        // 4) Return JSON with status 200 (to match old behavior)
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        String json = new ObjectMapper().writeValueAsString(transactionResult);
        response.getWriter().write(json);
    }
}

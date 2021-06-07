package com.smarsh.pingidentity;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * This class is used to log json request message body and headers as they are sent to the Smarsh Identify Store Web Service.
 * Note that the Authentication token header is intentionally not logged.
 *
 * @author Mark Anderson
 *
 */
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Log log = LogFactory.getLog(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        log(request, body);
        ClientHttpResponse response = execution.execute(request, body);

        return response;
    }

    private void log(HttpRequest request, byte[] body) {

        HttpHeaders headers = request.getHeaders();
        for (String header : headers.keySet()) {
            if (!header.equals(SmarshIdentityStoreProvisioner.HEADER_AUTHENTICATION_TOKEN)) {
                log.debug("Request Header: " + header + " -> " + headers.get(header));
            }
        }
        log.debug("Request json Body: " + new String(body));
    }
}
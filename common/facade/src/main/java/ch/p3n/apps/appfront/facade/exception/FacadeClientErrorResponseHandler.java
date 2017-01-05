package ch.p3n.apps.appfront.facade.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Client error response handler.
 *
 * @author deluc1
 * @author zempm3
 */
public class FacadeClientErrorResponseHandler implements ResponseErrorHandler {

    private static final Pattern ERROR_PATTERN = Pattern.compile("^#ERROR#([A-Z_]+)#$");

    @Override
    public boolean hasError(final ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getRawStatusCode() != 200;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        final String error = IOUtils.toString(clientHttpResponse.getBody(), Charset.defaultCharset());
        final Matcher matcher = ERROR_PATTERN.matcher(error);
        BusinessError responseError = BusinessError.UNKNOWN;
        if (matcher.matches()) {
            responseError = BusinessError.valueOf(matcher.group(1));
        }

        throw new FacadeRuntimeException(responseError);
    }
}

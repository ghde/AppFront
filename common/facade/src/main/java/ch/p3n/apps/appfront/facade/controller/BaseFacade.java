package ch.p3n.apps.appfront.facade.controller;

import ch.p3n.apps.appfront.facade.exception.FacadeClientErrorResponseHandler;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Base facade contains base functionality for all facades.
 *
 * @author deluc1
 * @author zempm3
 */
public abstract class BaseFacade {

    private static final String BASE_URL = "https://appfront.p3n.ch/rest/%s";

    private RestTemplate template;

    public RestTemplate getRestTemplate() {
        if (template == null) {
            template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
            template.setErrorHandler(new FacadeClientErrorResponseHandler());
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }
        return template;
    }

    /**
     * @param serviceName service name.
     * @return service url used for the facade.
     */
    public String getServiceUrl(final String serviceName) {
        return String.format(BASE_URL, serviceName);
    }

}

package ch.p3n.apps.appfront.facade.controller;

import ch.p3n.apps.appfront.api.controller.LoginController;
import ch.p3n.apps.appfront.api.dto.ActivityDTO;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.exception.FacadeErrorHandler;
import ch.p3n.apps.appfront.facade.exception.FacadeRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

/**
 * Facade implementation of {@link LoginController}.
 *
 * @author deluc1
 * @author zempm3
 */
public class LoginControllerFacade extends BaseFacade implements LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginControllerFacade.class);

    @Override
    public Collection<ActivityDTO> postLogin(final AuthenticationDTO authenticationData) throws BusinessException {
        try {
            final String serviceUrl = getServiceUrl("login", true);
            LOGGER.info("Calling postLogin service on " + serviceUrl);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Post-Body: " + authenticationData);
            }
            final ActivityDTO[] response = getRestTemplate().postForObject(serviceUrl, authenticationData, ActivityDTO[].class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Response-Body: " + Arrays.toString(response));
            }
            return Arrays.asList(response);
        } catch (FacadeRuntimeException e) {
            throw FacadeErrorHandler.handle(e);
        }
    }
}

package ch.p3n.apps.appfront.facade.controller;

import ch.p3n.apps.appfront.api.controller.RegistrationController;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.exception.FacadeErrorHandler;
import ch.p3n.apps.appfront.facade.exception.FacadeRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade implementation of {@link RegistrationController}.
 *
 * @author deluc1
 * @author zempm3
 */
public class RegistrationControllerFacade extends BaseFacade implements RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationControllerFacade.class);

    @Override
    public AuthenticationDTO postRegister(final AuthenticationDTO authenticationData) throws BusinessException {
        try {
            final String serviceUrl = getServiceUrl("register", true);
            LOGGER.info("Calling postRegister service on " + serviceUrl);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Post-Body: " + authenticationData);
            }
            final AuthenticationDTO response = getRestTemplate().postForObject(serviceUrl, authenticationData, AuthenticationDTO.class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Response-Body: " + response);
            }
            return response;
        } catch (FacadeRuntimeException e) {
            throw FacadeErrorHandler.handle(e);
        }
    }

}

package ch.p3n.apps.appfront.facade.controller;

import ch.p3n.apps.appfront.api.controller.ActivationController;
import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.api.dto.InterestDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.exception.FacadeErrorHandler;
import ch.p3n.apps.appfront.facade.exception.FacadeRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade implementation of {@link ActivationController}.
 *
 * @author deluc1
 * @author zempm3
 */
public class ActivationControllerFacade extends BaseFacade implements ActivationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivationControllerFacade.class);

    @Override
    public InterestDTO postActivate(final ActivationDTO activationData) throws BusinessException {
        try {
            final String serviceUrl = getServiceUrl("activate", false);
            LOGGER.info("Calling postActivate service on " + serviceUrl);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Post-Body: " + activationData);
            }
            final InterestDTO response = getRestTemplate().postForObject(serviceUrl, activationData, InterestDTO.class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Response-Body: " + response);
            }
            return response;
        } catch (FacadeRuntimeException e) {
            throw FacadeErrorHandler.handle(e);
        }
    }

    @Override
    public void postDeactivate(final ActivationDTO activationData) throws BusinessException {
        try {
            final String serviceUrl = getServiceUrl("deactivate", false);
            LOGGER.info("Calling postDeactivate service on " + serviceUrl);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Post-Body: " + activationData);
            }
            getRestTemplate().postForObject(serviceUrl, activationData, Object.class);
        } catch (FacadeRuntimeException e) {
            throw FacadeErrorHandler.handle(e);
        }
    }

}

package ch.p3n.apps.appfront.facade.controller;

import ch.p3n.apps.appfront.api.controller.NotificationController;
import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.api.dto.MatchDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.exception.FacadeErrorHandler;
import ch.p3n.apps.appfront.facade.exception.FacadeRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

/**
 * Facade implementation of {@link NotificationController}.
 *
 * @author deluc1
 * @author zempm3
 */
public class NotificationControllerFacade extends BaseFacade implements NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationControllerFacade.class);

    @Override
    public void postNotify(final ActivationDTO activationData, final String otherClientInterestId) throws BusinessException {
        try {
            final String serviceUrl = getServiceUrl("notify/" + otherClientInterestId, false);
            LOGGER.info("Calling postNotify service on " + serviceUrl);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Post-Body: " + activationData);
            }
            getRestTemplate().postForObject(serviceUrl, activationData, Object.class);
        } catch (FacadeRuntimeException e) {
            throw FacadeErrorHandler.handle(e);
        }
    }

    @Override
    public Collection<MatchDTO> postNotifications(final ActivationDTO activationData) throws BusinessException {
        try {
            final String serviceUrl = getServiceUrl("notifications", false);
            LOGGER.info("Calling postNotifications service on " + serviceUrl);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Post-Body: " + activationData);
            }
            final MatchDTO[] response = getRestTemplate().postForObject(serviceUrl, activationData, MatchDTO[].class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Response-Body: " + Arrays.toString(response));
            }
            return Arrays.asList(response);
        } catch (FacadeRuntimeException e) {
            throw FacadeErrorHandler.handle(e);
        }
    }

}

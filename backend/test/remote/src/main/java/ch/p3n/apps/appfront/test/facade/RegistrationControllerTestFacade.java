package ch.p3n.apps.appfront.test.facade;

import ch.p3n.apps.appfront.facade.controller.RegistrationControllerFacade;
import ch.p3n.apps.appfront.test.util.TestUtil;

/**
 * Overwrite for {@link RegistrationControllerFacade} with a different url.
 *
 * @author deluc1
 * @author zempm3
 */
public class RegistrationControllerTestFacade extends RegistrationControllerFacade {

    @Override
    public String getServiceUrl(final String serviceName, final boolean useSSL) {
        return TestUtil.url(serviceName);
    }

}

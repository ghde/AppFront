package ch.p3n.apps.appfront.test.facade;

import ch.p3n.apps.appfront.facade.controller.ActivationControllerFacade;
import ch.p3n.apps.appfront.test.util.TestUtil;

/**
 * Overwrite for {@link ActivationControllerFacade} with a different url.
 *
 * @author deluc1
 * @author zempm3
 */
public class ActivationControllerTestFacade extends ActivationControllerFacade {

    @Override
    public String getServiceUrl(final String serviceName) {
        return TestUtil.url(serviceName);
    }

}

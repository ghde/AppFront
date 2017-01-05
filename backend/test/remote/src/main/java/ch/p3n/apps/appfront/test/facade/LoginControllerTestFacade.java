package ch.p3n.apps.appfront.test.facade;

import ch.p3n.apps.appfront.facade.controller.LoginControllerFacade;
import ch.p3n.apps.appfront.test.util.TestUtil;

/**
 * Overwrite for {@link LoginControllerFacade} with a different url.
 *
 * @author deluc1
 * @author zempm3
 */
public class LoginControllerTestFacade extends LoginControllerFacade {

    @Override
    public String getServiceUrl(final String serviceName, final boolean useSSL) {
        return TestUtil.url(serviceName);
    }

}

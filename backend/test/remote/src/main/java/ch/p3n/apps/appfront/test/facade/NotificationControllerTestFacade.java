package ch.p3n.apps.appfront.test.facade;

import ch.p3n.apps.appfront.facade.controller.NotificationControllerFacade;
import ch.p3n.apps.appfront.test.util.TestUtil;

/**
 * Overwrite for {@link NotificationControllerFacade} with a different url.
 *
 * @author deluc1
 * @author zempm3
 */
public class NotificationControllerTestFacade extends NotificationControllerFacade {

    @Override
    public String getServiceUrl(final String serviceName) {
        return TestUtil.url(serviceName);
    }

}

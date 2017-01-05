package ch.p3n.apps.appfront.business.security;

import ch.p3n.apps.appfront.facade.security.HashUtil;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.UUID;

/**
 * Abstract class of {@link SecurityBusiness}.
 *
 * @author zempm3
 * @author deluc1
 */
public abstract class AbstractSecurityBusiness implements SecurityBusiness {

    private static final String HASH = "#";

    private String serverRandom;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // TODO: find a better solution for (persistent) server random generation.
        serverRandom = UUID.randomUUID().toString();
    }

    @Override
    public String createHash(String clientId, String interestId, String clientRandom) {
        final StringBuilder sb = new StringBuilder();
        sb.append(clientId).append(HASH);
        sb.append(interestId).append(HASH);
        sb.append(clientRandom).append(HASH);
        sb.append(serverRandom);
        return HashUtil.createHash(sb.toString());
    }

}

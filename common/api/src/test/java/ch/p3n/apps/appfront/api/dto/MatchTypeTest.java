package ch.p3n.apps.appfront.api.dto;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for {@link MatchType}.
 *
 * @author zempm3
 * @author deluc1
 */
public class MatchTypeTest {

    @Test
    public void testGetByTypeId_bluetooth() {
        Assert.assertEquals(MatchType.BLUETOOTH, MatchType.getByTypeId(MatchType.BLUETOOTH.getTypeId()));
    }

    @Test
    public void testGetByTypeId_map() {
        Assert.assertEquals(MatchType.MAP, MatchType.getByTypeId(MatchType.MAP.getTypeId()));
    }

    @Test
    public void testGetByTypeId_invalid() {
        Assert.assertNull(MatchType.getByTypeId(-2));
        Assert.assertNull(MatchType.getByTypeId(555));
    }

}
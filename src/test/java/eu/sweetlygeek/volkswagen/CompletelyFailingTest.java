package eu.sweetlygeek.volkswagen;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VolkswagenTestRunner.class)
public class CompletelyFailingTest {

    @BeforeClass
    public static void wrongBeforeClass() {
        throw new UnsupportedOperationException();
    }

    @Before
    public void wrongBefore() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testFailing() {
        Assert.fail();
    }

    @Test
    public void illogicTest() {
        Assert.assertTrue(false);
    }
}

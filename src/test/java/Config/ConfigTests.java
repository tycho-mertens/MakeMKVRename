package Config;

import tycho.core.config.Config;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ConfigTests {
    File configFile = new File("/home/user/testConfiguration.json");
    Config config;

    @Before
    public void Setup() throws IOException {
        config = Config.getTestInstance();
    }

    @After
    public void Cleanup(){
        FileUtils.deleteQuietly(configFile);
    }

    @Test
    public void saveTest() throws IOException {
        config.addProperty("SaveTest", true);
        config.save();

        Config c = Config.getTestInstance();
        assertTrue(c.asBoolean("SaveTest"));

        config.addProperty("SaveTest", false);
        config.save();

        c = Config.getTestInstance();
        assertFalse(c.asBoolean("SaveTest"));
    }

    @Test
    public void asStringTest(){
        config.addProperty("Test1", "TestValue1");
        config.addProperty("Test2", "TestValue2");

        assertEquals("TestValue1", config.asString("Test1"));
        assertEquals("TestValue2", config.asString("Test2"));
    }

    @Test
    public void asIntegerTest(){
        config.addProperty("Test3", 3524);
        config.addProperty("Test4", 9875);

        assertEquals(3524, config.asInteger("Test3"));
        assertEquals(9875, config.asInteger("Test4"));
    }

    @Test
    public void asDoubleTest(){
        config.addProperty("Test1", 2.35);
        config.addProperty("Test2", 9.99);

        assertEquals(2.35d, config.asDouble("Test1"),0);
        assertEquals(9.99d, config.asDouble("Test2"),0);

    }

    @Test
    public void asFloatTest(){
        config.addProperty("Test1", 2.3554687);
        config.addProperty("Test2", 0.3528428975);

        assertEquals(2.3554687f, config.asFloat("Test1"),0);
        assertEquals(0.3528428975f, config.asFloat("Test2"), 0);
    }

    @Test
    public void asBooleanTest(){
        config.addProperty("Test1", false);
        config.addProperty("Test2", true);

        assertFalse(config.asBoolean("Test1"));
        assertTrue(config.asBoolean("Test2"));
    }
}

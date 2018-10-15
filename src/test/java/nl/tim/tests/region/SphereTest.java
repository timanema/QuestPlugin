package nl.tim.tests.region;

import nl.tim.questplugin.area.Sphere;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class SphereTest
{
    private Object testObject;
    private Sphere regularSphere;
    private Sphere testSphere;
    private Location locationInSphere;
    private Location locationNotInSphere;
    private Location locationInSphereIgnoreHeight;
    private Location locationNotInSphereIgnoreHeight;
    private Location locationInOtherWorld;

    @Before
    public void setup()
    {
        // Mock worlds
        World world1 = PowerMockito.mock(World.class);
        World world2 = PowerMockito.mock(World.class);

        when(world1.getName()).thenReturn("world1");
        when(world2.getName()).thenReturn("world2");

        // Create test object
        testObject = new Object();

        // Creating locations
        Location center = new Location(world1, 15, 15, 15);
        double radius = 5.5;

        locationInSphere = new Location(world1, 15, 15, 15 + 4);
        locationNotInSphere = new Location(world1, 15, 15, 15 + 7);
        locationInSphereIgnoreHeight = new Location(world1, 15 - 1, 15 + 2, 15 - 10);
        locationNotInSphereIgnoreHeight = new Location(world1, 15 - 10, 15 - 2, 15 - 20);
        locationInOtherWorld = new Location(world2, 15, 15 , 15);

        // Creating objects
        UUID uuid = UUID.randomUUID();
        regularSphere = new Sphere(uuid, center, radius);
        testSphere = new Sphere(uuid, center, radius);
    }

    @Test
    public void cube_equals_test_equal()
    {
        assertEquals("These objects are equal", regularSphere, testSphere);
    }

    @Test
    public void cube_equals_test_not_instance_of()
    {
        assertFalse("Sphere should check instance", regularSphere.equals(testObject));
    }

    @Test
    public void cube_equals_test_equal_same_instance()
    {
        assertEquals("These objects are equal", regularSphere, regularSphere);
    }

    @Test
    public void cube_equals_test_not_equal()
    {
        assertFalse("These objects are not equal", regularSphere.equals(new Sphere(UUID.randomUUID(), new Location(null, 0 ,0 ,0), 1)));
    }


    @Test
    public void cube_in_region_valid()
    {
        assertTrue("Should be in Sphere", regularSphere.inRegion(locationInSphere, false));
    }

    @Test
    public void cube_in_region_invalid_height()
    {
        assertFalse("Should not be in Sphere (z-axis)", regularSphere.inRegion(locationNotInSphere, false));
    }

    @Test
    public void cube_in_region_valid_ignore_height()
    {
        assertTrue("Should be in Sphere", regularSphere.inRegion(locationInSphereIgnoreHeight, true));
    }

    @Test
    public void cube_in_region_invalid_x_ignore_height()
    {
        assertFalse("Should not be in Sphere (x-axis)", regularSphere.inRegion(locationNotInSphereIgnoreHeight, true));
    }

    @Test
    public void cube_in_region_invalid_other_world()
    {
        assertFalse("Should not be in Sphere (other world)", regularSphere.inRegion(locationInOtherWorld, false));
    }
}

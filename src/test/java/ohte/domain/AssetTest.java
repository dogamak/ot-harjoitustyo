package ohte.domain;

import org.junit.*;
import static org.junit.Assert.*;

import ohte.domain.Asset;

public class AssetTest {
    @Test
    public void assetsEqualsBasedOnId() {
        Asset a1 = new Asset();
        a1.setId(1);
        a1.setHostname("Hostname 1");
        a1.setModel("Model 1");
        a1.setManufacturer("Manufacturer 1");
        a1.setSerialNumber("SN-1");
        a1.getIpAddressesProperty().add("127.0.0.1");

        Asset a2 = new Asset();
        a2.setId(1);
        a2.setHostname("Hostname 2");
        a2.setModel("Model 2");
        a2.setManufacturer("Manufacturer 2");
        a2.setSerialNumber("SN-2");
        a1.getIpAddressesProperty().add("127.0.0.2");

        assertTrue(a1.equals(a2));
    }

    @Test
    public void assetsEqualsBasedOnIdNeqtiveCase() {
        Asset a1 = new Asset();
        a1.setId(1);
        a1.setHostname("Hostname 1");
        a1.setModel("Model 1");
        a1.setManufacturer("Manufacturer 1");
        a1.setSerialNumber("SN-1");
        a1.getIpAddressesProperty().add("127.0.0.1");

        Asset a2 = new Asset();
        a2.setId(2);
        a2.setHostname("Hostname 1");
        a2.setModel("Model 1");
        a2.setManufacturer("Manufacturer 1");
        a2.setSerialNumber("SN-1");
        a2.getIpAddressesProperty().add("127.0.0.2");

        assertFalse(a1.equals(a2));
    }
}

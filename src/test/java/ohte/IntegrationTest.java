package ohte;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;

import ohte.domain.Application;
import ohte.domain.Asset;
import ohte.domain.Credentials;

public class IntegrationTest {
    File file;
    Application app;
    Credentials rootCredentials = new Credentials("root", "root");

    @Before
    public void generateSQLiteFile() throws IOException, SQLException {
        file = File.createTempFile("test", ".inv");
        initializeApplication();
    }

    void initializeApplication() throws SQLException {
        app = new Application();
    }

    void createInventory() throws SQLException {
        initializeApplication();
        app.createInventory(file.getAbsolutePath(), rootCredentials);
    }

    void openInventory() throws SQLException {
        initializeApplication();
        app.openInventory(file.getAbsolutePath(), rootCredentials);
    }

    @Test
    public void canLoginUsingCreatedSuperuser() throws SQLException {
        createInventory();
        openInventory();
        assertTrue(app.isAuthenticated());
        assertEquals("root", app.getAccount().getUsername());
    }

    @Test
    public void accountRemovalPersists() throws SQLException {
        createInventory();
        app.createAccount(new Credentials("normal", "normal"));
        openInventory();
        assertEquals(2, app.getStorage().getAccounts().size());
        app.removeAccount(app.getStorage().getAccounts().get(0));
        assertEquals(1, app.getStorage().getAccounts().size());
        openInventory();
        assertEquals(1, app.getStorage().getAccounts().size());
    }

    @Test
    public void cannotLoginWithIncorrectCredentials() throws SQLException {
        createInventory();
        initializeApplication();
        app.openInventory(file.getAbsolutePath(), new Credentials("root", "toor"));
        assertFalse(app.isAuthenticated());
        assertNull(app.getAccount());
    }

    @Test
    public void createdAssetsPersist() throws SQLException {
        createInventory();
        app.createAsset();
        openInventory();
        assertEquals(1, app.getStorage().getAssets().size());
    }

    @Test
    public void changesToAssetsPersist() throws SQLException {
        createInventory();
        app.createAsset();

        Asset asset = app.getStorage().getAssets().get(0);
        asset.setModel("Test Model");
        asset.setManufacturer("Test Manufacturer");
        asset.setHostname("Test Hostname");

        openInventory();

        List<Asset> assets = app.getStorage().getAssets();
        asset = assets.get(0);
        assertEquals(1, assets.size());

        assertEquals("Test Model", asset.getModel());
        assertEquals("Test Manufacturer", asset.getManufacturer());
        assertEquals("Test Hostname", asset.getHostname());
    }

    @Test
    public void assetDeletionPersists() throws SQLException {
        createInventory();
        app.createAsset();
        openInventory();
        assertEquals(1, app.getStorage().getAssets().size());
        Asset asset = app.getStorage().getAssets().get(0);
        app.removeAsset(asset);
        assertEquals(0, app.getStorage().getAssets().size());
        openInventory();
        assertEquals(0, app.getStorage().getAssets().size());
    }
}

package ohte.storage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import org.junit.*;
import static org.junit.Assert.*;

import ohte.domain.Asset;

public class AssetSqlitePersisterTest {
  ObservableSet<Asset> set;
  Connection conn;
  AssetSqlitePersister persister;

  @Before
  public void initializePersister() throws SQLException {
    conn = DriverManager.getConnection("jdbc:sqlite::memory:");
    persister = new AssetSqlitePersister(conn);
    set = FXCollections.observableSet();
    persister.synchronize(set);
  }

  @Test
  public void addingAnAssetToTheSetCausesAnRowToBeInserted() throws SQLException {
    Asset asset = new Asset();
    set.add(asset);

    int rowCount = conn.createStatement()
      .executeQuery("SELECT COUNT(*) FROM assets")
      .getInt(1);

    assertEquals(1, rowCount);
  }

  @Test
  public void removingAnAssetFromTheSetCausesARowToBeDeleted() throws SQLException {
    Asset asset = new Asset();
    set.add(asset);

    int rowCount = conn.createStatement()
      .executeQuery("SELECT COUNT(*) FROM assets")
      .getInt(1);

    assertEquals(1, rowCount);

    set.remove(asset);

    rowCount = conn.createStatement()
      .executeQuery("SELECT COUNT(*) FROM assets")
      .getInt(1);

    assertEquals(0, rowCount);
  }

  @Test
  public void existingAssetsAreLoadedFromTheDatabase() throws IOException, SQLException {
    File tmpFile = File.createTempFile("test", "sqlite");
    String connectionString = "jdbc:sqlite:" + tmpFile.getAbsolutePath();

    try (Connection conn = DriverManager.getConnection(connectionString)) {
      persister = new AssetSqlitePersister(conn);
      set = FXCollections.observableSet();
      persister.synchronize(set);

      Asset asset = new Asset();
      asset.setModel("Model");
      asset.setSerialNumber("SN-123");
      asset.setHostname("host.name");
      set.add(asset);
    }

    try (Connection conn = DriverManager.getConnection(connectionString)) {
      persister = new AssetSqlitePersister(conn);
      ObservableSet<Asset> newSet = FXCollections.observableSet();
      persister.synchronize(newSet);
      assertEquals(1, newSet.size());
      Asset asset = newSet.stream().findAny().orElse(null);
      assertEquals("Model", asset.getModel());
      assertEquals("SN-123", asset.getSerialNumber());
      assertEquals("host.name", asset.getHostname());
    }
  }

  @Test
  public void updatingAssetPropertyPersists() throws IOException, SQLException {
    File tmpFile = File.createTempFile("test", "sqlite");
    String connectionString = "jdbc:sqlite:" + tmpFile.getAbsolutePath();

    try (Connection conn = DriverManager.getConnection(connectionString)) {
      persister = new AssetSqlitePersister(conn);
      set = FXCollections.observableSet();
      persister.synchronize(set);

      Asset asset = new Asset();
      asset.setModel("Model (Initial)");
      asset.setSerialNumber("SN-123 (Initial)");
      asset.setHostname("host.name (Initial)");
      set.add(asset);
    }

    try (Connection conn = DriverManager.getConnection(connectionString)) {
      persister = new AssetSqlitePersister(conn);
      ObservableSet<Asset> newSet = FXCollections.observableSet();
      persister.synchronize(newSet);
      assertEquals(1, newSet.size());
      Asset asset = newSet.stream().findAny().orElse(null);

      asset.setModel("Model");
      asset.setSerialNumber("SN-123");
      asset.setHostname("host.name");
    }

    try (Connection conn = DriverManager.getConnection(connectionString)) {
      persister = new AssetSqlitePersister(conn);
      ObservableSet<Asset> newSet = FXCollections.observableSet();
      persister.synchronize(newSet);
      assertEquals(1, newSet.size());
      Asset asset = newSet.stream().findAny().orElse(null);
      assertEquals("Model", asset.getModel());
      assertEquals("SN-123", asset.getSerialNumber());
      assertEquals("host.name", asset.getHostname());
    }
  }
}

package ohte.storage;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

import javafx.beans.property.Property;

import ohte.domain.Asset;

public class AssetSqlitePersister extends SqlitePersister<Asset> {
    public AssetSqlitePersister(Connection conn) {
        super(conn);
    }

    @Override
    void createTables() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS assets (" + 
                "  id INTEGER PRIMARY KEY NOT NULL, " +
                "  model TEXT NOT NULL, " +
                "  manufacturer TEXT NOT NULL, " +
                "  serialNumber TEXT NOT NULL, " +
                "  hostname TEXT NOT NULL" +
                ")"
            );
        }
    }

    @Override
    List<Asset> loadInitialEntries() throws SQLException {
        ArrayList<Asset> assets = new ArrayList<>();

        ResultSet results = conn.createStatement()
            .executeQuery("SELECT * FROM assets");

        while (results.next()) {
            Asset asset = new Asset();

            asset.setId(results.getInt("id"));
            asset.setManufacturer(results.getString("manufacturer"));
            asset.setModel(results.getString("model"));
            asset.setSerialNumber(results.getString("serialNumber"));
            asset.setHostname(results.getString("hostname"));

            assets.add(asset);
        }

        return assets;
    }

    @Override
    void registerEntryListeners(Asset asset) {
        registerFieldUpdater("manufacturer", asset.getId(), asset.getManufacturerProperty());
        registerFieldUpdater("model", asset.getId(), asset.getModelProperty());
        registerFieldUpdater("serialNumber", asset.getId(), asset.getSerialNumberProperty());
        registerFieldUpdater("hostname", asset.getId(), asset.getHostnameProperty());
    }

    @Override
    void insertEntry(Asset asset) throws SQLException {
        PreparedStatement stmt = conn
            .prepareStatement("INSERT INTO assets (manufacturer, model, serialNumber, hostname) VALUES (?,?,?,?)");

        stmt.setString(1, asset.getManufacturer());
        stmt.setString(2, asset.getModel());
        stmt.setString(3, asset.getSerialNumber());
        stmt.setString(4, asset.getHostname());

        stmt.executeUpdate();

        asset.setId(stmt.getGeneratedKeys().getInt(1));
    }

    private void registerFieldUpdater(String column, int id, Property<String> property) {
        property.addListener((prop, oldValue, newValue) -> {
            this.updateField(column, id, newValue);
        });
    }

    private void updateField(String column, int id, String value) {
        String sql = "UPDATE assets SET " + column + " = ? WHERE id = ?";

        System.out.println(sql + " " + id + " " + value);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, value);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    void deleteEntry(Asset asset) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM assets WHERE id = ?");

        stmt.setInt(1, asset.getId());

        stmt.executeUpdate();
    }
}

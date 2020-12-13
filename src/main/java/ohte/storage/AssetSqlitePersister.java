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

/**
 * A {@link Persister} implementation for {@link Asset Assets} using SQLite as it's backend.
 */
public class AssetSqlitePersister extends SqlitePersister<Asset> {
    /** {@inheritDoc} */
    public AssetSqlitePersister(Connection conn) {
        super(conn);
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    void registerEntryListeners(Asset asset) {
        registerFieldUpdater("manufacturer", asset.getId(), asset.getManufacturerProperty());
        registerFieldUpdater("model", asset.getId(), asset.getModelProperty());
        registerFieldUpdater("serialNumber", asset.getId(), asset.getSerialNumberProperty());
        registerFieldUpdater("hostname", asset.getId(), asset.getHostnameProperty());
    }

    /** {@inheritDoc} */
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

    /**
     * Registers a listener for a property, which calls {@link #updateField} on changes.
     *
     * @param column Name of the database column into which changes are synchronized.
     * @param id Identifier of the {@link Asset} to whose this property belongs.
     * @param property The property whose value should be updated into the database on changes.
     */
    private void registerFieldUpdater(String column, int id, Property<String> property) {
        property.addListener((prop, oldValue, newValue) -> {
            this.updateField(column, id, newValue);
        });
    }

    /**
     * Executes an {@link UPDATE} statement.
     * 
     * @param column Name of the column to be updated.
     * @param id Identifier of the {@link Asset} whose column is to be updated.
     * @param value The new value for the column.
     */
    private void updateField(String column, int id, String value) {
        String sql = "UPDATE assets SET " + column + " = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, value);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    void deleteEntry(Asset asset) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM assets WHERE id = ?");

        stmt.setInt(1, asset.getId());

        stmt.executeUpdate();
    }
}

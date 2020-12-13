package ohte.ui;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableSet;

import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import ohte.domain.Asset;
import ohte.domain.Application;

/**
 * FXML Controller for the Assets-tab in the main view.
 */
public class AssetTabController {
    /**
     * Reference to the main Asset table.
     * Populated by FXML.
     */
    @FXML
    private TableView<Asset> assetTable;

    /**
     * Reference to the Asset creation button in the bottom-left corner.
     * Populated by FXML.
     */
    @FXML
    private Button createAssetButton;

    /**
     * Reference to the Asset removal button in the bottom-left corner.
     * Populated by FXML.
     */
    @FXML
    private Button removeAssetButton;

    /**
     * Initialize the Asset table.
     * Called by FXML after this instance has been populated.
     */
    @FXML
    @SuppressWarnings("checkstyle:methodlength")
    private void initialize() {
        // Set icons for the bottom-left buttons
        createAssetButton.setGraphic(FontIcon.of(FontAwesome.PLUS));
        removeAssetButton.setGraphic(FontIcon.of(FontAwesome.MINUS));

        // Configure the Hostname column
        TableColumn<Asset, String> hostnameColumn = new TableColumn<>("Hostname");
        hostnameColumn.setCellValueFactory(data -> data.getValue().getHostnameProperty());

        // Configure the IP Address column
        TableColumn<Asset, String> ipAddressColumn = new TableColumn<>("IP Address");
        ipAddressColumn.setCellValueFactory(data -> {
            String joined = data.getValue().getIpAddresses()
                .stream()
                .filter(ip -> ip != null)
                .map(ip -> ip.toString())
                .collect(Collectors.joining(", "));

            return new ReadOnlyStringWrapper(joined);
        });

        // Configure the Manufactur column
        TableColumn<Asset, String> manufacturerColumn = new TableColumn<>("Manufacturer");
        manufacturerColumn.setCellValueFactory(data -> data.getValue().getManufacturerProperty());

        // Configure the Model column
        TableColumn<Asset, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(data -> data.getValue().getModelProperty());

        // Configure the Serial Number column
        TableColumn<Asset, String> serialNumberColumn = new TableColumn<>("Serial Number");
        serialNumberColumn.setCellValueFactory(data -> data.getValue().getSerialNumberProperty());

        assetTable.getColumns()
            .addAll(
                hostnameColumn,
                ipAddressColumn,
                manufacturerColumn,
                modelColumn,
                serialNumberColumn
            );

        // Bind the table to the global list of Assets in a way that 
        // allows modifications to propagate in both directions.
        ObservableSet<Asset> assets = Application.getSingleton()
            .getStorage()
            .getAssetsObservable();

        assetTable.setItems(new ObservableSetToListAdapter<>(assets));

        // Change the global focused object whenever an Asset is selected in the table.
        assetTable
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((prop, oldValue, newValue) ->
                    Application.getSingleton().getFocused().set(newValue));
    }

    /**
     * Called when the user clicks the Asset creation button.
     */
    @FXML
    private void handleCreateAsset(ActionEvent event) {
        Application.getSingleton().createAsset();
    }

    /**
     * Called when the user clicks the Asset removal button.
     */
    @FXML
    private void handleRemoveAsset(ActionEvent event) {
      Asset selected = assetTable
        .getSelectionModel()
        .selectedItemProperty()
        .get();

      Application
        .getSingleton()
        .removeAsset(selected);
    }
}

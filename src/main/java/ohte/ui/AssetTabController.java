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

public class AssetTabController {
    @FXML
    private TableView<Asset> assetTable;

    @FXML
    private Button createAssetButton;

    @FXML
    private Button removeAssetButton;

    @FXML
    @SuppressWarnings("checkstyle:methodlength")
    private void initialize() {
        createAssetButton.setGraphic(FontIcon.of(FontAwesome.PLUS));
        removeAssetButton.setGraphic(FontIcon.of(FontAwesome.MINUS));

        TableColumn<Asset, String> hostnameColumn = new TableColumn<>("Hostname");
        hostnameColumn.setCellValueFactory(data -> data.getValue().getHostnameProperty());

        TableColumn<Asset, String> ipAddressColumn = new TableColumn<>("IP Address");
        ipAddressColumn.setCellValueFactory(data -> {
            String joined = data.getValue().getIpAddresses()
                .stream()
                .filter(ip -> ip != null)
                .map(ip -> ip.toString())
                .collect(Collectors.joining(", "));

            return new ReadOnlyStringWrapper(joined);
        });

        TableColumn<Asset, String> manufacturerColumn = new TableColumn<>("Manufacturer");
        manufacturerColumn.setCellValueFactory(data -> data.getValue().getManufacturerProperty());

        TableColumn<Asset, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(data -> data.getValue().getModelProperty());

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

        assetTable.setEditable(true);

        ObservableSet<Asset> assets = Application.getSingleton()
            .getStorage()
            .getAssetsObservable();

        assetTable.setItems(new ObservableSetToListAdapter<>(assets));

        assetTable
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((prop, oldValue, newValue) ->
                    Application.getSingleton().getFocused().set(newValue));
    }

    @FXML
    private void handleCreateAsset(ActionEvent event) {
        Application.getSingleton().createAsset();
    }

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

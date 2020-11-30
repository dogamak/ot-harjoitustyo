package ohte.ui;

import javafx.beans.property.Property;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

import ohte.domain.Asset;

public class AssetPane extends VBox {
  GridPane propertyGrid = new GridPane();
  int rowCounter = 0;

  AssetPane(Asset asset) {
    addPropertyRow("Model", asset.getModelProperty());
    addPropertyRow("Manufacturer", asset.getManufacturerProperty());
    addPropertyRow("Hostname", asset.getHostnameProperty());
    addPropertyRow("Serial number", asset.getSerialNumberProperty());

    getChildren().addAll(propertyGrid);
  }

  private void addPropertyRow(String name, Property<String> property) {
    Label label = new Label(name + ": ");

    EditableProperty<String> editable = new EditableProperty<>(
        property,
        EditableProperty.stringEditorFactory()
    );

    propertyGrid.getChildren().addAll(label, editable);
    GridPane.setConstraints(label, 0, rowCounter);
    GridPane.setConstraints(editable, 1, rowCounter);

    rowCounter++;
  }
}

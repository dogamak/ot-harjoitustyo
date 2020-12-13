package ohte.ui;

import javafx.beans.property.Property;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

import ohte.domain.Asset;

/**
 * Component for displaying contents for the right-hand-side
 * Asset information pane in the main view.
 */
public class AssetPane extends VBox {
    /**
     * Reference to the grid containing all property name-value pairs.
     */
    GridPane propertyGrid = new GridPane();

    /**
     * Running counter of grid rows used when constructing the grid.
     */
    int rowCounter = 0;

    /**
     * Create a pane and populate it with data from an {@link Asset}.
     */
    AssetPane(Asset asset) {
        addPropertyRow("Model", asset.getModelProperty());
        addPropertyRow("Manufacturer", asset.getManufacturerProperty());
        addPropertyRow("Hostname", asset.getHostnameProperty());
        addPropertyRow("Serial number", asset.getSerialNumberProperty());

        getChildren().addAll(propertyGrid);
    }

    /**
     * Add an row to the property grid with an label for the property name
     * and an editable field for the value.
     *
     * @param name Human-readable and user-facing property name
     * @param property Corresponding wrtiable property
     */
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

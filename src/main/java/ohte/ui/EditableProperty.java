package ohte.ui;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.Property;
import javafx.geometry.Pos;
import java.util.function.Consumer;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Component for displaying a property value with an edit-button and inline editor.
 */
public class EditableProperty<T> extends HBox {
    /**
     * Reference to the property.
     */
    Property<T> value;

    /**
     * Possibly un-saved value gotten from the editor.
     */
    T editedValue;

    /**
     * Reference to the edit-button displayed when not in edit mode.
     */
    Button editButton = new Button();

    /**
     * Reference to the cancel-button displayed when in edit mode.
     */
    Button cancelButton = new Button();

    /**
     * Reference to the apply-button displayed when in edit mode.
     */
    Button applyButton = new Button();

    /**
     * Factory for creating a JavaFX component for displaying the property's value
     * in non-edit mode.
     */
    ValueDisplayFactory<T> valueLabelFactory = EditableProperty::defaultValueLabelFactory;

    /**
     * Factory for creating an editor for the property.
     */
    ValueEditorFactory<T> editorFactory;

    /**
     * The current mode.
     */
    private boolean editing = false;

    /**
     * Interface for creating an editor component for a property.
     */
    public static interface ValueEditorFactory<T> {
        /**
         * Create an editor component for a property.
         *
         * @param value Original value of the property.
         * @param callback Callback which is to be called with the modified value
         *      every time the user makes a modification.
         *
         * @return The JavaFX root node of the editor.
         */
        Node create(T value, Consumer<T> callback);
    }

    /**
     * Interface for creating a JavaFX component for displaying a property's value. 
     */
    public static interface ValueDisplayFactory<T> {
        /**
         * Create a JavaFX component from a property value.
         *
         * @param value Current value of the property.
         * @return JavaFX component
         */
        Node create(T value);
    }

    /**
     * Create a component displaying the value of a property and providing editing functionality.
     *
     * @param value The property whose value to display and edit
     * @param editorFactory Factory for creating the editor component
     */
    public EditableProperty(Property<T> value, ValueEditorFactory<T> editorFactory) {
        this.value = value;
        this.editorFactory = editorFactory;

        refreshValue();
        value.addListener(this::onValueChange);

        editButton.setStyle("-fx-background-color: transparent");
        cancelButton.setStyle("-fx-background-color: transparent");
        applyButton.setStyle("-fx-background-color: transparent");

        editButton.setGraphic(FontIcon.of(FontAwesomeSolid.EDIT));
        cancelButton.setGraphic(FontIcon.of(FontAwesomeSolid.TIMES_CIRCLE));
        applyButton.setGraphic(FontIcon.of(FontAwesomeSolid.CHECK_CIRCLE));

        editButton.setOnAction(this::onEdit);
        cancelButton.setOnAction(this::onCancel);
        applyButton.setOnAction(this::onApply);

        setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Called when the upstream property is changed.
     *
     * @param observable Reference to the property
     * @param oldValue Value of the property before the change
     * @param newValue Value of the property after the change
     */
    private void onValueChange(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        handleValueChange(newValue);
    }

    /**
     * Updates the UI to display the current value.
     */
    private void refreshValue() {
        handleValueChange(value.getValue());
    }

    /**
     * Called whenever the property's value has been changed.
     * Updates the UI if not in edit mode.
     *
     * @param newValue The new value of the property.
     */
    private void handleValueChange(T newValue) {
        if (!editing) {
            Node label = valueLabelFactory.create(newValue);
            getChildren().setAll(label, editButton);
        }
    }

    /**
     * Default factory for creating labels for property values
     * using their {@link Object#toString}-method.
     *
     * @param newValue New value for the property.
     * @return Label with the value's string representation.
     */
    private static <T> Node defaultValueLabelFactory(T newValue) {
        return new Label(newValue.toString());
    }

    /**
     * Called when the user clicks the edit button.
     */
    private void onEdit(ActionEvent event) {
        editing = true;

        Node editor = editorFactory.create(
                value.getValue(),
                (value) -> this.editedValue = value
                );

        getChildren()
            .setAll(editor, applyButton, cancelButton);
    }

    /**
     * Called when the user clicks the cancel button.
     */
    private void onCancel(ActionEvent event) {
        editing = false;
        refreshValue();
    }

    /**
     * Called when the user clicks the apply button.
     */
    private void onApply(ActionEvent event) {
        editing = false;

        if (editedValue != null) {
            value.setValue(editedValue);
            editedValue = null;
        } else {
            refreshValue();
        }
    }

    /**
     * Factory for a simple editor based around a {@link TextField}.
     *
     * @return A factory for creating an editor for string values.
     */
    public static ValueEditorFactory<String> stringEditorFactory() {
        return (username, callback) -> {
            TextField field = new TextField();

            field.setText(username);

            field.textProperty()
                .addListener((prop, oldValue, newValue) -> {
                    callback.accept(newValue);
                });

            return field;
        };
    };
}

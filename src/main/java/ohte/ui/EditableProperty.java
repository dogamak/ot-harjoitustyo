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

public class EditableProperty<T> extends HBox {
  Property<T> value;
  T editedValue;
  Button editButton = new Button();
  Button cancelButton = new Button();
  Button applyButton = new Button();
  ValueDisplayFactory<T> valueLabelFactory = EditableProperty::defaultValueLabelFactory;
  ValueEditorFactory<T> editorFactory;
  private boolean editing = false;

  public static interface ValueEditorFactory<T> {
    Node create(T value, Consumer<T> callback);
  }

  public static interface ValueDisplayFactory<T> {
    Node create(T value);
  }

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

  private void onValueChange(ObservableValue<? extends T> observable, T oldValue, T newValue) {
    handleValueChange(newValue);
  }

  private void refreshValue() {
    handleValueChange(value.getValue());
  }

  private void handleValueChange(T newValue) {
    if (!editing) {
      Node label = valueLabelFactory.create(newValue);
      getChildren().setAll(label, editButton);
    }
  }

  private static <T> Node defaultValueLabelFactory(T newValue) {
    return new Label(newValue.toString());
  }

  private void onEdit(ActionEvent event) {
    editing = true;

    Node editor = editorFactory.create(
      value.getValue(),
      (value) -> this.editedValue = value
    );

    getChildren()
      .setAll(editor, applyButton, cancelButton);
  }

  private void onCancel(ActionEvent event) {
    editing = false;
    refreshValue();
  }

  private void onApply(ActionEvent event) {
    editing = false;

    if (editedValue != null) {
      value.setValue(editedValue);
      editedValue = null;
    } else {
      refreshValue();
    }
  }

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

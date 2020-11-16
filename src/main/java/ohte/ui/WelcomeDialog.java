package ohte.ui;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import ohte.domain.Credentials;

/**
 * Dialog displayed as the first thing during startup.
 * Prompts the user to choose between creating a new inventory and opening an existing one.
 */
public class WelcomeDialog extends Dialog<InventoryAction> {
  /**
   * Button displaying "Create Inventory File".
   *
   * This static value can be compared against the ButtonType
   * object returned by the Dialog to determine the user's choiche.
   */
  private static ButtonType CREATE_BUTTON = new ButtonType("Create Inventory File");

  /**
   * Button dsplaying "Open Inventory File".
   *
   * This static value can be compared against the ButtonType
   * object returned by the Dialog to determine the user's choiche.
   */
  private static ButtonType OPEN_BUTTON = new ButtonType("Open Inventory File");

  public WelcomeDialog() {
    DialogPane pane = getDialogPane();

    TextFlow textFlow = new TextFlow();

    Text header = new Text("Welcome to IT Asset Manager!");
    header.setStyle("-fx-font-weight: bold");

    Text body = new Text("\nClick below to create a new empty inventory file or to open an existing one.");
    textFlow.getChildren().addAll(header, body);
    pane.setContent(textFlow);

    setResultConverter(button -> convertResult(this, button));

    pane.getButtonTypes().addAll(CREATE_BUTTON, OPEN_BUTTON);

  }

  /**
   * Returns the dialog's result value based on the {@link ButtonType} clicked by the user.
   *
   * @param dialog - Reference to the WelcomeDialog instance.
   *    Needed for having access to the current Window for displaying a file chooser.
   * @param buttonType - ButtonType chosen by the user.
   */
  static InventoryAction convertResult(WelcomeDialog dialog, ButtonType buttonType) {
    FileChooser chooser = new FileChooser();
    Window window = dialog.getDialogPane().getScene().getWindow();

    File file = null;
    InventoryAction.Type type = null;

    if (buttonType == CREATE_BUTTON) {
      file = chooser.showSaveDialog(window);
      type = InventoryAction.Type.CREATE;
    } else if (buttonType == OPEN_BUTTON) {
      file = chooser.showOpenDialog(window);
      type = InventoryAction.Type.OPEN;
    }

    InventoryDialog invDialog = new InventoryDialog(type, file);
    Credentials credentials = invDialog.showAndWait().orElse(null);

    return new InventoryAction(type, file, credentials);
  }
}

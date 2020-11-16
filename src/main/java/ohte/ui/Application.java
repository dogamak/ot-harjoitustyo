package ohte.ui;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;

import ohte.domain.Credentials;

/**
 * Manages the startup UI flow and launches the main UI.
 */
public class Application extends javafx.application.Application {
  @Override
  public void start(Stage stage) {
    startupFlow();
    showMainView(stage);
  }

  public static void main(String[] args) {
    launch();
  }

  /**
   * Start the startup UI flow, which prompts for inventory file and credentials.
   *
   * The startup flow consists of two dialogs: {@link WelcomeDialog}
   * and {@link InventoryDialog}.
   */
  private void startupFlow() {
    WelcomeDialog dialog = new WelcomeDialog();

    InventoryAction result = dialog.showAndWait().orElse(null);

    if(result == null)
      return;

    if (result.getType() == InventoryAction.Type.CREATE) {
      ohte.domain.Application.getSingleton()
        .createInventory(result.getFile().getAbsolutePath(), result.getCredentials());
    } else if (result.getType() == InventoryAction.Type.OPEN) {
      Credentials credentials = result.getCredentials();
      boolean success;

      do {
        success = ohte.domain.Application.getSingleton()
          .openInventory(result.getFile().getAbsolutePath(), result.getCredentials());

        if (!success) {
          InventoryDialog retryDialog = new InventoryDialog(InventoryAction.Type.OPEN, result.getFile());
          credentials = retryDialog.showAndWait().orElse(null);
        }
      } while (!success);
    }
  }

  /**
   * Creates and displays {@link MainViewController the main view}.
   */
  private void showMainView(Stage stage) {
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
        .getResource("ui/MainView.fxml"));

    try {
      Parent root = loader.load();
      Scene scene = new Scene(root);
      stage.setScene(scene);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    stage.setTitle("IT Asset Manager");
    stage.show();
  }
}

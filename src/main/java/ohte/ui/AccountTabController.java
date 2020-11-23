package ohte.ui;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import static javafx.collections.FXCollections.observableList;

import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import ohte.domain.Application;
import ohte.domain.Credentials;
import ohte.storage.Account;

/**
 * Controller for the Accounts tab.
 */
public class AccountTabController {
  /**
   * Reference to the {@link Button} for creating a new account.
   */
  @FXML
  private Button createAccountButton;

  /**
   * Reference to the {@link Button} for removing an account.
   */
  @FXML
  private Button removeAccountButton;

  /**
   * Reference to the {@link TableView} for displaying a list of all {@link Account Accounts}.
   */
  @FXML
  private TableView accountTable;

  /**
   * Sets up the UI after the child components have been initialized.
   * 
   * Called automatically by {@link FXMLLoader}.
   */
  @FXML
  private void initialize() {
    createAccountButton.setGraphic(FontIcon.of(FontAwesome.USER_PLUS));
    removeAccountButton.setGraphic(FontIcon.of(FontAwesomeSolid.USER_MINUS));

    TableColumn<Account, String> usernameColumn = new TableColumn("Username");
    usernameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getUsername()));

    TableColumn<Account, String> roleColumn = new TableColumn("Role");
    roleColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getRole().toString()));

    accountTable.getColumns().addAll(usernameColumn, roleColumn);

    List<Account> accounts;

    try {
      accounts = Application.getSingleton().getStorage().getAccounts();
    } catch (SQLException sqle) {
      accounts = new ArrayList<>();
    }

    accountTable.setItems(observableList(accounts));

    // Change the globally focused object whenever user focuses
    // a new row in the table.
    accountTable
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((prop, oldValue, newValue) ->
        Application.getSingleton().getFocused().set(newValue));
  }

  /**
   * Event handler which is called from the FXML whenever the user
   * clicks the {@link Button} for creating a new account.
   */
  @FXML
  private void handleCreateAccount(ActionEvent event) {
    CredentialsDialog dialog = new CredentialsDialog("Create normal account");

    Label message = new Label("Input credentials for a new account:");
    dialog.getMessagePane().getChildren().add(message);

    Credentials credentials = dialog.showAndWait()
      .map(r -> r.getCredentials())
      .orElse(null);

    Application.getSingleton().createAccount(credentials);
  }

  /**
   * Event handler which is called from the FXML whenever the user
   * clicks the {@link Button} for removing an account.
   */
  @FXML
  private void handleRemoveAccount(ActionEvent event) {}
}

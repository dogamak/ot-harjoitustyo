package ohte.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import ohte.domain.Account;

public class AccountPaneController {
  @FXML
  private Label usernameLabel;

  @FXML
  private Label roleLabel;

  public void setAccount(Account account) {
    usernameLabel.setText(account.getUsername());
    roleLabel.setText(account.getRole().toString());
  }
}

package ohte.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import ohte.domain.Account;

/**
 * FXML Controller for the right-hand-side Account information pane in the main view.
 */
public class AccountPaneController {
    /**
     * Reference to the username label.
     * Populated by FXML.
     */
    @FXML
    private Label usernameLabel;

    /**
     * Reference to the role label.
     * Populated by FXML.
     */
    @FXML
    private Label roleLabel;

    /**
     * Update pane information.
     *
     * @param account Account whose information to display on the pane
     */
    public void setAccount(Account account) {
        usernameLabel.setText(account.getUsername());
        roleLabel.setText(account.getRole().toString());
    }
}

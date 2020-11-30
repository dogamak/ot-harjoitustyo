package ohte.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import static javafx.collections.FXCollections.observableList;

import ohte.domain.Application;
import ohte.domain.Account;
import ohte.domain.Asset;

/**
 * Controller for managing the main UI.
 *
 * Manages the tabs in the main tab pane, the bottom-most status bar and
 * keeps the right-side panel up to date.
 */
public class MainViewController implements ChangeListener<Object> {
    /**
     * Reference to a {@link Label} displaying the bottom-left status message.
     */
    @FXML
    private Label statusMessageLabel;

    /**
     * Reference to a {@link Label} displaying the current authenticate user.
     */
    @FXML
    private Label accountLabel;

    /**
     * Reference to the pane which contains UI for the resizeable right-side pane.
     */
    @FXML
    private Pane sidePane;

    /**
     * Reference to the pane which contains UI for the left-side pane.
     */
    @FXML
    private TabPane mainTabPane;

    /**
     * Sets up the UI after the child components have been initialized.
     * 
     * Called automatically by {@link FXMLLoader}.
     */
    @FXML
    private void initialize() {
        updateAccountLabel();
        addTabView("Accounts", "ui/AccountTab.fxml");
        addTabView("Assets", "ui/AssetTab.fxml");

        Application.getSingleton().getFocused().addListener(this);
    }

    /**
     * Change listener which gets triggered whenever the global focused object changes.
     *
     * Populates the right-side pane with the focused object's information.
     */
    @Override
    public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
        System.out.println(newValue);

        Parent paneContent = null;

        if (newValue instanceof Account) {
            paneContent = createAccountPane((Account) newValue);
        }

        if (newValue instanceof Asset) {
            paneContent = new AssetPane((Asset) newValue);
        }

        if (paneContent != null) {
            sidePane.getChildren().setAll(paneContent);
        }
    }

    private Parent createAccountPane(Account account) {
        Parent pane = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/AccountPane.fxml"));

            pane = loader.load();
            ((AccountPaneController) loader.getController()).setAccount(account);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return pane;
    }

    /**
     * Adds a new tab to the main tab pane.
     *
     * @param name     Name for the tab which is diaplayed in the UI.
     * @param fxmlPath Resource path to the FXML file containing the UI description.
     */
    private void addTabView(String name, String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));

        Tab tab = new Tab();
        tab.setText(name);

        try {
            Parent root = loader.load();
            tab.setContent(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        mainTabPane.getTabs().add(tab);
    }

    /**
     * Updates the authenticated account's name in the UI.
     */
    private void updateAccountLabel() {
        Account account = Application.getSingleton().getAccount();

        if (account == null) {
            accountLabel.setText("File Locked");
        } else {
            accountLabel.setText("Account: " + account.getUsername());
        }
    }

    /**
     * Event handler, which is called from the FXML when user clicks
     * the "Change Account" button.
     */
    @FXML
    private void handleChangeAccount(ActionEvent event) {}
}

package ohte.ui;

import java.io.File;

import ohte.domain.Credentials;

/**
 * Represents the action chosen by user at startup.
 *
 * There are two different possible actions:
 *  - {@link #CREATE}: Create a new inventory file.
 *  - {@link #OPEN}: Open an existing inventory file.
 */
public enum InventoryAction {
    CREATE,
    OPEN;
}

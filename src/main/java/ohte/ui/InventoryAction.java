package ohte.ui;

import java.io.File;

import ohte.domain.Credentials;

/**
 * Represents the action chosen by user at startup.
 *
 * There are two different possible actions, determined by the {@link #type} property accessible via {@link #getType}:
 *  - {@link Type.CREATE}: Create a new inventory file.
 *  - {@link Type.OPEN}: Open an existing inventory file.
 */
public enum InventoryAction {
    CREATE,
    OPEN;
}

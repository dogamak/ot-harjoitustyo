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
class InventoryAction {
  /** Represents the type of an action. */
  static enum Type {
    CREATE,
    OPEN;
  }

  /** Type of the action. */
  Type type;

  /** File to be created or to be opened. */
  File file;

  InventoryAction(Type type, File file) {
    this.type = type;
    this.file = file;
  }

  /**
   * Returns the type of action chosen by the user.
   */
  Type getType() {
    return type;
  }

  /**
   * Returns the file associated with the action.
   * The file may or may not exist on the filesystem.
   */
  File getFile() {
    return file;
  }
}

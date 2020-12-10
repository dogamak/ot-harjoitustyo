package ohte;

import ohte.ui.Application;

public class Launcher {
  public static void main(String[] args) {
    for (String arg : args)
      System.out.println(arg);
    javafx.application.Application.launch(Application.class, args);
  }
}

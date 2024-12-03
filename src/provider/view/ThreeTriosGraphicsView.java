package provider.view;

import javax.swing.*;

import provider.model.ReadOnlyThreeTriosModel;

/**
 * This is an implementation of the ThreeTriosGUIView interface
 * that uses Java Swing to draw the results of the game state.
 */
public abstract class ThreeTriosGraphicsView extends JFrame implements ThreeTriosGUIView {

  // protected so that RedPlayer and BluePlayer views can see them
  protected HandPanel leftPanel;
  protected HandPanel rightPanel;
  protected GridPanel gridPanel;


  /**
   * Constructs a ThreeTriosGraphicsView object.
   * @param model the ReadOnlyThreeTriosModel to get information about the game state from
   */
  public ThreeTriosGraphicsView(ReadOnlyThreeTriosModel model) {
    // sets up the view based on which player is being displayed -> delegates to individual players
  }

  /**
   * Refresh the view to reflect any changes in the game state.
   */
  @Override
  public void refresh() {
    this.repaint();
    // resets the view so that is displays any updated changes in the game state
  }

  /**
   * Make the view visible to start the game session.
   */
  @Override
  public void makeVisible(boolean bool) {
    this.setVisible(bool);
  }

  public HandPanel getLeftPanel() {
    return this.leftPanel;
  }


  /**
   * Updates the view to reflect the new changes updated.
   */
  public abstract void reflectNewChanges();
}

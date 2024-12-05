package adapters;

import provider.view.BluePlayerView;
import view.ThreeTriosViewInterface;
import controller.Features;

/**
 * View Adapter class that makes the provider views compatible with our controller.
 */
public class ViewAdapter implements ThreeTriosViewInterface {
  private final BluePlayerView providerView;

  /**
   * Constructor for the ViewAdapter class.
   *
   * @param providerView provider view
   */
  public ViewAdapter(BluePlayerView providerView) {
    this.providerView = providerView;
  }

  @Override
  public void addFeatures(Features features) {
    if (features instanceof provider.controller.ThreeTriosController) {
      providerView.addClickListener((provider.controller.ThreeTriosController) features);
    } else {
      throw new IllegalArgumentException("Features must implement ThreeTriosController");
    }
  }


  @Override
  public void updateView() {
    providerView.reflectNewChanges();
  }

  @Override
  public void showErrorMessage(String message) {
    providerView.showErrorMessage(message);
  }

  @Override
  public void showGameOverMessage(String message) {
    // TODO: Adapt the message to the expected format for the provider's view
    providerView.displayGameOver(null);
  }

  @Override
  public void setVisible(boolean visible) {
    providerView.makeVisible(visible);
  }
}

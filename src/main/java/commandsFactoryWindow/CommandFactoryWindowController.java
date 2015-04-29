/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandsFactoryWindow;

/**
 *
 * @author Godzio
 */
public class CommandFactoryWindowController {

    boolean isPanelDetached = false;

    /**
     * Default constructor.
     */
    public CommandFactoryWindowController() {
        isPanelDetached = false;
    }

    /**
     * Check if draw panel is visible.
     *
     * @return is draw panel visible.
     */
    public boolean isVisible() {
        return CommandFactoryWindow.getCommandFactoryWindow().isVisible();
    }

    /**
     * Set draw panel visibility.
     *
     * @param visible visibility.
     */
    public void setVisible( boolean visible ) {
        if ( !isPanelDetached ) {
            CommandFactoryWindow.getCommandFactoryWindow().setVisible( visible );
        }
    }
}

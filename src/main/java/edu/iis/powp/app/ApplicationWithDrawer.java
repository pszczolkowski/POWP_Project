package edu.iis.powp.app;

import edu.iis.client.plottermagic.ClientPlotter;
import edu.iis.client.plottermagic.IPlotter;
import edu.iis.powp.gui.event.predefine.SelectChangeVisibleOptionListener;
import edu.iis.powp.gui.event.predefine.SelectClearPanelOptionListener;
import edu.kis.powp.drawer.panel.DrawPanelController;

/**
 * Application.
 */
public class ApplicationWithDrawer {

    private static boolean isAppCreated = false;
	
    /**
     * Startup configuration.
     */
	public synchronized static void configureApplication()
	{
	    if (!isAppCreated)
	    {
	        isAppCreated = true; 

	        
	        Application.addComponent(DriverManager.class);
	        Application.addComponent(Context.class);
	        
	        Context context = Application.getComponent(Context.class);
	        IPlotter clientPlotter = new ClientPlotter();
	        context.addDriver("Client Plotter", clientPlotter);
	        
	        setupDrawerPlugin( context);
	        
	        context.setVisibility(true);
	    }
	}

	/**
	 * Setup Drawer Plugin and add to context.
	 * 
	 * @param context Application context.
	 */
	private static void setupDrawerPlugin(Context context) {    
        SelectClearPanelOptionListener selectClearPanelOptionListener = new SelectClearPanelOptionListener();
        SelectChangeVisibleOptionListener selectChangeVisibleOptionListener =
	            new SelectChangeVisibleOptionListener();
	
        
		Application.addComponent(DrawPanelController.class);
		context.addComponentMenu(DrawPanelController.class, "Draw Panel", 0);
		context.addComponentMenuElement(DrawPanelController.class, "Visibility", selectChangeVisibleOptionListener, true);
		Application.getComponent(DrawPanelController.class).setVisible(true);
		context.addComponentMenuElement(DrawPanelController.class, "Clear Panel", selectClearPanelOptionListener, false);
	}

	

}

package fitintegration;

public abstract class PluginInformation {
	private static String PLUGIN_ID;
	private static FactoryInterface factoryForPlugin;

	public static String getPLUGIN_ID() {
		return PLUGIN_ID;
	}

	public static void setPLUGIN_ID(String plugin_id) {
		if (PLUGIN_ID == null)
			PLUGIN_ID = plugin_id;
	}

	public static FactoryInterface getFactoryForPlugin() {
		return factoryForPlugin;
	}

	public static void setFactoryForPlugin(FactoryInterface factoryForPlugin) {
		if (PluginInformation.factoryForPlugin == null)
			PluginInformation.factoryForPlugin = factoryForPlugin;
	}
	
	
	
}

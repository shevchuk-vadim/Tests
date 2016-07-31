package ua.shevchuk.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * The class for selecting commands that handle the HTTP requests.  
 */
public class CommandFactory {

	private final static CommandFactory INSTANCE = new CommandFactory();
	
	private Map<String, ActionCommand> commands = new HashMap<>();

    private CommandFactory() {}

	/**
	 * Selects a command that handle the HTTP request.
	 * @param path a String specifying the HTTP request URI.
	 * @return an ActionCommand object that handle the HTTP request. 
	 */
    public static ActionCommand getCommand(String path) {
		Map<String, ActionCommand> commands = INSTANCE.commands; 
		ActionCommand command = commands.get(path);
		if (command == null) {
			int i = path.lastIndexOf('/') + 1;
			String name = "ua.shevchuk.controller.commands." + path.substring(i) + "Command";
	        try {
				command = (ActionCommand) Class.forName(name).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
	        commands.put(path, command);
		}
		return command;
	}

}

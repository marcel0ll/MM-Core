package me.otho.metamods.core.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.google.gson.JsonObject;

public class RegisterHandler {

	private static HashMap<String, IRegister> registerRegistry = new HashMap<String, IRegister>();
	
	public static void addRegisterType ( String type, IRegister register ) {
		if ( registerRegistry.containsKey( type ) ) {
			throw new Error ("Tried to register registerType: " + type + " more than once" );
		}
		
		registerRegistry.put( type, register );
	}
	
	public static void callRegisters ( ArrayList<JsonObject> jsonData ) {
		Collections.sort(jsonData, new Comparator<JsonObject>() {
	        @Override
	        public int compare(JsonObject obj2, JsonObject obj1)
	        {
	        	Integer priority1 = RegisterHandler.get( obj1.get("type").getAsString() ).getPriority();
	        	Integer priority2 = RegisterHandler.get( obj2.get("type").getAsString() ).getPriority();
	        	

	            return priority1.compareTo(priority2);
	        }
	    });
		
		for( JsonObject obj : jsonData ) {
			String id = obj.get("id").getAsString();
			String type = obj.get("type").getAsString();
			if ( !registerRegistry.containsKey( type ) )  {
				throw new Error("Object with id: " + id + " tried to register with type: " + type + ", but type was not found." );
			}
			
			IRegister register = registerRegistry.get( type );
			
			System.out.println("Register object: " + obj.get("id").getAsString() + " as type:" + type );
			register.register(obj);
		}
	}
	
	public static IRegister get( String id ) {
		return registerRegistry.get(id);
	}
}

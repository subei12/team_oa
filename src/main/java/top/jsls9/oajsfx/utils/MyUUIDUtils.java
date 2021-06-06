package top.jsls9.oajsfx.utils;

import java.util.UUID;

public class MyUUIDUtils {
	
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-","");
	}
}

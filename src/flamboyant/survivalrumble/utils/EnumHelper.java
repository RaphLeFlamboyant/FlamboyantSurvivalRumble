package flamboyant.survivalrumble.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class EnumHelper {
	private static final Random random = new Random();
	
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
    
    public static <T extends Enum<?>> ArrayList<String> listValuesAsString(Class<T> clazz)
    {
    	T[] values = clazz.getEnumConstants();
    	ArrayList<String> res = new ArrayList<String>();
    	for (T v : values)
    	{
    		res.add(v.toString());
    	}
    	
    	return res;
    }
}

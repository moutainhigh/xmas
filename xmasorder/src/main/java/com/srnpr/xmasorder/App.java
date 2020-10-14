package com.srnpr.xmasorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	List<String> a = new ArrayList<String>();
    	a.add("2");
    	a.add("3");
    	a.add("4");
    	a.add("5");
    	for (int i = 0; i < a.size(); i++) {
			if("5".equals(a.get(i))){
				a.add("6");
			}
			System.out.println( a.get(i));
		}
    	System.out.println( a );
    }
}

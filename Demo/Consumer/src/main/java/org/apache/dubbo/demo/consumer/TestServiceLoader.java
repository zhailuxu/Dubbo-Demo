package org.apache.dubbo.demo.consumer;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;
    
public class TestServiceLoader {  
   
	public static void main(String[] args) {
		ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
		// (2)   
		Iterator<Driver> iterator = loader.iterator();
		while (iterator.hasNext()) {
			Driver driver = (Driver) iterator.next();
			System.out.println("driver:" + driver.getClass() + ",loader:" + driver.getClass().getClassLoader());
		}
		// (3)
		System.out.println("current thread contextloader:" + Thread.currentThread().getContextClassLoader());
		// (4)
		System.out.println("ServiceLoader loader:" + ServiceLoader.class.getClassLoader());
	}

}

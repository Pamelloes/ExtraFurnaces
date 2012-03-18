package org.dyndns.pamelloes.SpoutFurnaces.client;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.commons.io.IOUtils;

public class DualClassLoader extends ClassLoader {
	
	private DualClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	public static DualClassLoader getDualClassLoader(ClassLoader cl1, ClassLoader cl2, String ...impose) {
		DualClassLoaderPart c1 = new DualClassLoaderPart(cl1, impose);
		DualClassLoaderPart c2 = new DualClassLoaderPart(cl2,c1, impose);
		c1.setOther(c2);
		return c1;
	}
	
	private static class DualClassLoaderPart extends DualClassLoader {
		private ClassLoader other;
		private Method load;
		private String[] impose;
		
		public DualClassLoaderPart(ClassLoader parent, String ...impose) {
			super(parent);
			this.impose = impose;
		}
		
		public DualClassLoaderPart(ClassLoader parent, ClassLoader other, String ...impose) {
			super(parent);
			setOther(other);
			this.impose = impose;
		}

		@Override
		protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			Class<?> c = findLoadedClass(name);
			if(c != null) return c;
			try {
				c = super.loadClass(name, resolve);
			} catch(Exception e) {}
			if(c != null) return redefine(c);
			try {
				c = (Class<?>) load.invoke(other, name, resolve);
			} catch(Exception e) {}
			if(c != null) return redefine(c);
			throw new ClassNotFoundException();
		}
		
		private Class<?> redefine(Class<?> clazz) {
			boolean success = false;
			for(String start : impose) success = success || clazz.getName().startsWith(start);
			if(!success) return clazz;
			try {
				byte[] bytes = getClassBytes(clazz);
				if(bytes == null) return clazz;
				return defineClass(clazz.getName(), bytes, 0, bytes.length);
			} catch(IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static byte[] getClassBytes(Class<?> clazz) throws IOException {
			String name = clazz.getName().replace('.', '/') + ".class";
			if( clazz.getClassLoader() == null) return null;
			InputStream input =  clazz.getClassLoader().getResourceAsStream(name);
			try {
				return IOUtils.toByteArray(input);
			} finally {
				input.close();
			}
		}
		
		public void setOther(ClassLoader cl) {
			other = cl;
			try {
				Method m = other.getClass().getDeclaredMethod("loadClass", String.class, boolean.class);
				m.setAccessible(true);
				load = m;
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}

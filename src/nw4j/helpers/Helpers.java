/*
 * Copyright (c) 2024, Miracle-Masterpiсe <mrmiraclemasterpiece@gmail.com or https://t.me/MiracleMasterpiece>. All rights reserved.
 * Use is subject to license terms.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */
package nw4j.helpers;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.Linker.Option;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import javax.management.RuntimeErrorException;

import nw4j.wrapper.c.pointers.VoidPointer;

/**
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final class Helpers {
	
	public static final boolean ENABLE_CHECKS;

	static {
		//-DnwDisableNativeWrapperChecks.
		String disableChecks = System.getProperty("nwDisableNativeWrapperChecks");
		if (disableChecks == null) {
			ENABLE_CHECKS = true;
		}else {
			if (disableChecks.equals("true")) {
				ENABLE_CHECKS = false;
			}else {
				ENABLE_CHECKS = true;
			}
		}
	}
	
	private Helpers() {
		throw new RuntimeErrorException(new IllegalAccessError("Class not instanceable!"));
	}

	//Loads the library based on the platform.
	public static void loadlib(String libName) {
		Platform platform = Platform.getPlatform();
		String libPath = System.getProperty("java.library.path");
		String extension = platform.getNativeLibraryFileExtension();
		System.load(libPath + "/" + libName + extension);
	}

	/**
	 * Checks the address for 0, and if it is equal to 0, throws an exception.
	 * @return The transmitted address.
	 * */
	public static long addressNonNull(long address) {
		if (address == VoidPointer.nullptr) {
			throw new NullAddressException();
		}
		return address;
	}

	private static class NullAddressException extends NullPointerException{
		private static final long serialVersionUID = 2372738562365L;
	}

	public static MethodHandle getCriticalMethodHandle(String name, FunctionDescriptor desc) {
		Linker linker = Linker.nativeLinker();
		SymbolLookup lookup = SymbolLookup.loaderLookup();
		Option critical = Option.critical(false);

		MethodHandle result = null;

		try{
			result = linker.downcallHandle(lookup.find(name).orElseThrow(), desc, critical);
		}catch(Throwable t) {
			t.printStackTrace();
		}
		return result;
	}
}

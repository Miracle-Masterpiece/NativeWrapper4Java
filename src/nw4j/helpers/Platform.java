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

/**
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final class Platform {

	public static final byte LINUX 			= 0;
	public static final byte UNIX 			= 1;
	public static final byte SOLARIS 		= 2;
	public static final byte WINDOWS		= 3;
	public static final byte MAC_OS 		= 4;
	public static final byte UNKNOW 		= 5;
	private static final Platform CURRENT_PLATFORM = definePlatform();

	private final byte platform;

	private final byte[] platformName;

	private final byte[] simpleName;

	private final byte[] nativeLibraryExtension;

	private Platform(int platform, String platformName, String simplePlatformName, String nativeLibraryExtension) {
		this.platform 				= (byte)platform;
		this.platformName 			= platformName.getBytes();
		this.simpleName 			= simplePlatformName.getBytes();
		this.nativeLibraryExtension = nativeLibraryExtension.getBytes();
	}

	private static Platform definePlatform() {
		final String linux 		= new String(new char[]{'l','i','n','u','x'});
		final String unix  		= new String(new char[]{'u','n','i','x'});
		final String nix  		= new String(new char[]{'n','i','x'});
		final String solaris 	= new String(new char[]{'s','o','l','a','r','i','s'});
		final String sol		= new String(new char[]{'s','o','l'});
		final String win 		= new String(new char[]{'w','i','n'});
		final String windows 	= new String(new char[]{'w','i','n','d','o','w','s'});
		final String macos 		= new String(new char[]{'m','a','c','o','s'});
		final String osx 		= new String(new char[]{'o','s','x'});

		final String soLib		= new String(new char[] {'.','s','o'});
		final String dllLib		= new String(new char[] {'.','d','l','l'});
		final String dylibLib	= new String(new char[] {'.','d','y','l','i','b'});
		final String unknowLib	= new String(new char[] {'.','?'});

		final int platformid;
		final String osname 	= System.getProperty(new String(new char[] {'o','s','.','n','a','m','e'})).toLowerCase();
		final String libExtension;
		final String simpleName;


		if (osname.contains(linux)) {
			platformid = LINUX;
			libExtension = soLib;
			simpleName = linux;
		}else if (osname.contains(unix) || osname.contains(nix)) {
			platformid = UNIX;
			libExtension = soLib;
			simpleName = unix;
		}else if (osname.contains(solaris) || osname.contains(sol)) {
			platformid = SOLARIS;
			libExtension = soLib;
			simpleName = solaris;
		}else if (osname.contains(windows) || osname.contains(win)) {
			platformid = WINDOWS;
			libExtension = dllLib;
			simpleName = windows;
		}else if (osname.contains(macos) || osname.contains(osx)) {
			platformid = MAC_OS;
			libExtension = dylibLib;
			simpleName = macos;
		}else {
			platformid = UNKNOW;
			libExtension = unknowLib;
			simpleName = new String(new char[] {'u','n','k','n','o','w'});
		}
		return new Platform(platformid, osname, simpleName, libExtension);
	}

	public static Platform getPlatform() {
		return CURRENT_PLATFORM;
	}

	public String getPlatformName() {
		return new String(platformName);
	}

	public String getSimplePlatformName() {
		return new String(simpleName);
	}

	public String getNativeLibraryFileExtension() {
		return new String(nativeLibraryExtension);
	}

	public byte getPlatformID() {
		return platform;
	}

	@Override
	public boolean equals(Object obj) {
		return true;
	}

	@Override
	public int hashCode() {
		return platform;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String header 					= new String(new char[] {'=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','=','='});
		String platformid 				= new String(new char[] {'P', 'l', 'a', 't', 'f', 'o', 'r', 'm', ' ', 'i', 'd', ' ', '=', ' '});
		String platformName 			= new String(new char[] {'P', 'l', 'a', 't', 'f', 'o', 'r', 'm', ' ', 'n', 'a', 'm', 'e', ' ', '=', ' '});
		String platformSimpleName 		= new String(new char[] {'P', 'l', 'a', 't', 'f', 'o', 'r', 'm', ' ', 's', 'i', 'm', 'p', 'l', 'e', ' ', 'n', 'a', 'm', 'e', ' ', '=', ' '});
		String platformLibraryExtension = new String(new char[] {'P', 'l', 'a', 't', 'f', 'o', 'r', 'm', ' ', 'n', 'a', 't', 'i', 'v', 'e', ' ', 'l', 'i', 'b', ' ', 'e', 'x', 't', 'e', 'n', 's', 'i', 'o', 'n', ' ', '=', ' '});

		sb.append(header).append('\n').append(platformid).append(this.platform).append('\n').append(platformName).append(new String(this.platformName)).append('\n').append(platformSimpleName).append(new String(this.simpleName)).append('\n').append(platformLibraryExtension).append(new String(nativeLibraryExtension)).append('\n').append(header);

		return sb.toString();
	}
}

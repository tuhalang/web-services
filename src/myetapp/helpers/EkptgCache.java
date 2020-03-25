package myetapp.helpers;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class EkptgCache {

	public static CacheManager cacheManager;
	public static Cache myCache = null;

	static {
		cacheManager = CacheManager.create();
		myCache = cacheManager.getCache("myCache");
	}

	public EkptgCache() {

	}

}

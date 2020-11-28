package net.minestom.server.utils.debug;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

public class TimingUtils {

	private static final Object2LongOpenHashMap<Object> timingsMap = new Object2LongOpenHashMap<>();

	public static Object startTiming() {
		final Object o = new Object();
		synchronized (timingsMap) {
			timingsMap.put(o, -System.nanoTime());
		}
		return o;
	}

	public static long endTiming(Object o) {
		long nanoTime = System.nanoTime();
		synchronized (timingsMap) {
			nanoTime += timingsMap.getLong(o);
		}
		return nanoTime;
	}

}

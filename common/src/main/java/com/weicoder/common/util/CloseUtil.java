package com.weicoder.common.util;

import java.io.OutputStream;

import com.weicoder.common.interfaces.Close;
import com.weicoder.common.log.Logs;

/**
 * 关闭各种资源方法
 * @author WD  
 */
public final class CloseUtil {
	/**
	 * 关闭Closeable流数据源接口
	 * @param c 流数据源
	 */
	public static void close(Close... cs) {
		// 循环关闭资源
		for (Close c : cs) {
			// 判断不为空
			if (!EmptyUtil.isEmpty(c)) {
				c.close();
			}
		}
	}

	/**
	 * 关闭Close流数据源接口
	 * @param cs 流数据源
	 */
	public static void close(AutoCloseable... cs) {
		// 循环关闭资源
		for (AutoCloseable c : cs) {
			try {
				// 判断不为空
				if (!EmptyUtil.isEmpty(c)) {
					// 是输出流
					if (c instanceof OutputStream) {
						((OutputStream) c).flush();
					}
					// 关闭
					c.close();
				}
			} catch (Exception e) {
				Logs.warn(e);
			}
		}
	}

	private CloseUtil() {}
}

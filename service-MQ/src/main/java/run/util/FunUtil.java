package run.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FunUtil {
	private String centerNum = "13800220500";
	private String centerIP = "";
	private String centerPort = "";
	private final static Logger log = LoggerFactory.getLogger(FunUtil.class);
	public static String xmlPath() {
		String str = "";
		try {
			str = FunUtil.class.getResource("/conf.xml").getPath();
		} catch (NullPointerException e) {

		}
		return str;
	}

	// 读取xml文档
	public static String readXml(String str1, String str2) {
		return "";
	}

	public String HexString(int src) {
		String str = "";
		str = Integer.toHexString(src);
		if (str.length() < 2) {
			str = "0" + str;
		}
		return str;
	}

	public static int StringToInt(String str) {
		int value = -1;
		try {
			value = Integer.parseInt(str.trim());
		} catch (NumberFormatException e) {
			// TODO: handle exception
			log.info("数字字符串解析失败");
			log.error(e.getMessage(), e);
		} catch (NullPointerException e) {
			// TODO: handle exception
			log.info("数字字符串为空");
			log.error(e.getMessage(), e);
		}
		return value;
	}
}
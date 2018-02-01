package top.ashman.wx.infrastructure.util.aes;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * XmlParse class
 *
 * <ol>
 * 	<li>提取出 xml 数据包中的加密消息</li>
 * 	<li>生成 xml 消息</li>
 * </ol>
 *
 * @author singoasher
 * @date 2018/2/1
 */
class XmlParse {

	/**
	 * 提取出 xml 数据包中的加密消息
	 *
	 * @param xmlString 待提取的xml字符串
	 * @return 提取出的加密消息字符串
	 * @throws DocumentException
	 */
	public static Object[] extract(String xmlString) throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(xmlString);

		Element root = document.getRootElement();
		String encrypt = root.element("Encrypt").getStringValue();
		String toUserName = root.element("ToUserName").getStringValue();

		Object[] result = new Object[3];
		result[0] = 0;
		result[1] = encrypt;
		result[2] = toUserName;

		return result;
	}

	/**
	 * 生成 xml 消息
	 *
	 * @param encrypt 加密后的消息密文
	 * @param signature 安全签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 生成的xml字符串
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {
		String format = "<xml>\n" +
				"<Encrypt><![CDATA[%1$s]]></Encrypt>\n" +
				"<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n" +
				"<TimeStamp>%3$s</TimeStamp>\n" +
				"<Nonce><![CDATA[%4$s]]></Nonce>\n" +
				"</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);

	}
}

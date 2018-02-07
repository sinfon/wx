/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

/**
 * 针对org.apache.commons.codec.binary.Base64，
 * 需要导入架包commons-codec-1.9（或commons-codec-1.8等其他版本）
 * 官方下载地址：http://commons.apache.org/proper/commons-codec/download_codec.cgi
 */
package top.ashman.wx.infrastructure.util.aes;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.DocumentException;
import top.ashman.wx.infrastructure.util.WxConstants;
import top.ashman.wx.infrastructure.util.security.*;

/**
 * 提供接收和推送给公众平台消息的加解密接口(UTF8编码的字符串).
 *
 * <ol>
 * 	<li>第三方回复加密消息给公众平台</li>
 * 	<li>第三方收到公众平台发送的消息，验证消息的安全性，并对消息进行解密。</li>
 * </ol>
 *
 * @author singoasher
 * @date 2018/2/1
 */
public class WxBizMsgCrypt {
	private static Charset CHARSET = Charset.forName("utf-8");
	private byte[] aesKey;

	private String appId;
	private String token;
	private byte[] encodingAESKey;

	/**
	 * 构造函数
	 * @param token 公众平台上，开发者设置的token
	 * @param encodingAESKey 公众平台上，开发者设置的encodingAESKey
	 * @param appId 公众平台appid
	 * 
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public WxBizMsgCrypt(String appId, String token, String encodingAESKey) throws AesException {
		if (encodingAESKey.length() != 43) {
			throw new AesException(AesException.ILLEGAL_AES_KEY);
		}

		this.token = token;
		this.appId = appId;
		aesKey = Base64.decodeBase64(encodingAESKey);
	}

	/**
	 * 构造函数
	 * @param appId 公众平台开发者 Id
	 * @param token 服务器令牌，微信公众平台开发者选项中设置
	 * @param encodingAESKey 服务器消息加解密密钥
	 * @param encodingAESKeyLength 密钥长度
	 */
	public WxBizMsgCrypt(String appId, String token, String encodingAESKey, Integer encodingAESKeyLength)
			throws AesException {
		if (encodingAESKey.length() != encodingAESKeyLength) {
			throw new AesException(AesException.ILLEGAL_AES_KEY);
		}

		this.appId = appId;
		this.token = token;
		this.encodingAESKey = Base64.decodeBase64(encodingAESKey);
	}

	/**
	 * 随机生成十六位字符串
	 * @return 随机生成十六位字符串
	 */
	private String getRandomStr() {
		Random random = new Random();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < WxConstants.RANDOM_LENGTH; i++) {
			int number = random.nextInt(WxConstants.RANDOM_BASE.length());
			stringBuilder.append(WxConstants.RANDOM_BASE.charAt(number));
		}
		return stringBuilder.toString();
	}

	/**
	 * 将公众平台回复用户的消息加密打包.
	 * <ol>
	 * 	<li>对要发送的消息进行AES-CBC加密</li>
	 * 	<li>生成安全签名</li>
	 * 	<li>将消息密文和安全签名打包成xml格式</li>
	 * </ol>
	 * 
	 * @param replyMsg 公众平台待回复用户的消息，xml格式的字符串
	 * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
	 * @param nonce 随机串，可以自己生成，也可以用URL参数的nonce
	 * 
	 * @return 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce, encrypt的xml格式的字符串
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public String encryptMsg(String replyMsg, String timeStamp, String nonce) throws AesException {
		// 加密
		String encrypt = AESUtil.encrypt(getRandomStr(), replyMsg, appId, encodingAESKey);

		// 生成安全签名
		if (timeStamp == "") {
			timeStamp = Long.toString(System.currentTimeMillis());
		}

		String signature = SHA1Util.getSignature(token, timeStamp, nonce, encrypt);

		// System.out.println("发送给平台的签名是: " + signature[1].toString());
		// 生成发送的xml
		String result = XmlParse.generate(encrypt, signature, timeStamp, nonce);
		return result;
	}

	/**
	 * 检验消息的真实性，并且获取解密后的明文.
	 * <ol>
	 * 	<li>利用收到的密文生成安全签名，进行签名验证</li>
	 * 	<li>若验证通过，则提取xml中的加密消息</li>
	 * 	<li>对消息进行解密</li>
	 * </ol>
	 * 
	 * @param msgSignature 签名串，对应URL参数的msg_signature
	 * @param timeStamp 时间戳，对应URL参数的timestamp
	 * @param nonce 随机串，对应URL参数的nonce
	 * @param postData 密文，对应POST请求的数据
	 * 
	 * @return 解密后的原文
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public String decryptMsg(String msgSignature, String timeStamp, String nonce, String postData)
			throws AesException, DocumentException {

		// 密钥，公众账号的app secret
		// 提取密文
		Object[] encrypt = XmlParse.extract(postData);

		// 验证安全签名
		String signature = SHA1Util.getSignature(token, timeStamp, nonce, encrypt[1].toString());

		// 和URL中的签名比较是否相等
		if (!signature.equals(msgSignature)) {
			throw new AesException(AesException.VALIDATE_SIGNATURE_ERROR);
		}

		// 解密
		return AESUtil.decrypt(encrypt[1].toString(), encodingAESKey, appId);
	}


}
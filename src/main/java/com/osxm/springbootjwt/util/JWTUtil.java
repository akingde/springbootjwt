/**
 * @Title: JWTUtil.java
 * @Package com.edu.badou.core.util
 * @Description: TODO
 * @author oscarchen
 * @date 2019年10月27日
 * @version V1.0
 */
package com.osxm.springbootjwt.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @ClassName: JWTUtil
 * @Description: TODO
 * @author oscarchen
 */
public class JWTUtil {
	// 过期时间8小时
	private static final long EXPIRE_TIME = 8 * 60 * 60 * 1000;

	/**
	 * 校验token是否正确
	 * 
	 * @param token  密钥
	 * @param secret 用户的密码
	 * @return 是否正确
	 */
	public static boolean verify(String token, String userId, String secret) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withClaim("userId", userId).build();
			verifier.verify(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 * 
	 * @return token中包含的用户名
	 */
	public static String getUserId(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("userId").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 生成签名,指定时间后过期,一经生成不可修改，令牌在指定时间内一直有效
	 * 
	 * @param userId 用户ID
	 * @param secret 用户的密码
	 * @return 加密的token
	 */
	public static String sign(String userId, String secret) {
		try {
			Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
			Algorithm algorithm = Algorithm.HMAC256(secret);
			// 附带userId信息
			return JWT.create().withClaim("userId", userId).withExpiresAt(date).sign(algorithm);
		} catch (Exception e) {
			return null;
		}
	}
}
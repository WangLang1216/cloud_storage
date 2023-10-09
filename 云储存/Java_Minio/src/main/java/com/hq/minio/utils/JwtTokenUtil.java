package com.hq.minio.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hq.minio.entity.UserInfo;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtTokenUtil {
    final static String SECRET = "jwtToken";//私钥
    final static Gson gson = new Gson();
    final static long TOKEN_EXP = 60 * 60;

    /**
     * 创建token
     * @param userInfo
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String createJwt(UserInfo userInfo) throws UnsupportedEncodingException {
        Algorithm al = Algorithm.HMAC256(SECRET);
        Instant instant = LocalDateTime.now().plusSeconds(TOKEN_EXP).atZone(ZoneId.systemDefault()).toInstant();
        Date expire = Date.from(instant);
        Gson gson = new Gson();
        String s = gson.toJson(userInfo);
        String token = JWT.create()
                .withSubject("userInfo")
                .withClaim("user", s)
                .withExpiresAt(expire)
                .sign(al);
        return token;
    }

    /**
     * token校验
     * @param token
     * @return boolean
     * @throws UnsupportedEncodingException
     */
    public static boolean verify(String token) throws UnsupportedEncodingException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt.getExpiresAt().before(new Date())) {
                System.out.println("token已过期");
                return false;
            }
        } catch (Exception e) {
            System.out.println("token已过期");
            return false;
        }
        return true;
    }

    /**
     * 获取用户信息
     * @param request
     * @return UserInfo
     * @throws UnsupportedEncodingException
     */
    public static UserInfo getUserIdByToken(String token) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        Claim claim = jwt.getClaim("user");
        String json = claim.asString();
        UserInfo userInfo = gson.fromJson(json, UserInfo.class);
        return userInfo;
    }

}

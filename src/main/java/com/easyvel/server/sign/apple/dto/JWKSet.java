package com.easyvel.server.sign.apple.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * refer to below
 * https://developer.apple.com/documentation/sign_in_with_apple/jwkset
 */
@Getter
@Setter
@ToString
public class JWKSet {

    private List<Keys> keys;

    @Getter
    @Setter
    @ToString
    public static class Keys {
        String alg;
        String e;
        String kid;
        String kty;
        String n;
        String use;
    }
}

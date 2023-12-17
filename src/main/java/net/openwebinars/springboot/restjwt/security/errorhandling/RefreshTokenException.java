package net.openwebinars.springboot.restjwt.security.errorhandling;

public class RefreshTokenException extends JwtTokenException{
    public RefreshTokenException(String msg) {
        super(msg);
    }
}

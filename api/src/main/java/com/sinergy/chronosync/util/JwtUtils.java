package com.sinergy.chronosync.util;

import com.sinergy.chronosync.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for managing JSON Web Tokens (JWT).
 *
 * <p> Provides methods for generating, validating, and extracting information
 * from JWTs. It handles the signing and claims management of tokens, including
 * expiration handling and user information extraction.</p>
 */
@Service
public class JwtUtils {

	@Value("${security.jwt.secret:default}")
	private String SECRET_KEY;

	@Value("${security.jwt.expiration:1}")
	private Long JWT_EXPIRATION;

	/**
	 * Extracts all claims from the given JWT jwtString.
	 *
	 * @param jwtString {@link String} JWT from which to extract claims
	 * @return {@link Claims} all claims from the jwtString
	 */
	private Claims extractAllClaims(String jwtString) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(jwtString).getPayload();
	}

	/**
	 * Retrieves the signing key used for validating the JWT.
	 *
	 * @return {@link SecretKey} used for signing the JWT
	 */
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Extracts a specific claim from the JWT using the provided claims' resolver.
	 *
	 * @param jwtString      {@link String} JWT from which to extract the claim
	 * @param claimsResolver a function that defines how to extract the desired claim
	 * @param <T>            the type of the claim to be extracted
	 * @return {@link T} the extracted claim of type {@link T}
	 */
	private <T> T extractClaim(String jwtString, Function<Claims, T> claimsResolver) {
		return claimsResolver.apply(extractAllClaims(jwtString));
	}

	/**
	 * Builds a JWT string with the specified additional claims and user details.
	 *
	 * @param additionalClaims a {@link Map} of additional claims to include in the token
	 * @param userDetails      the {@link UserDetails} object containing user information
	 * @return {@link String} representing the generated JWT string
	 */
	private String buildJWTString(Map<String, Object> additionalClaims, UserDetails userDetails) {
		return Jwts
			.builder()
			.claims(additionalClaims)
			.subject(userDetails.getUsername())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + 1000 * 3600 * JWT_EXPIRATION))
			.signWith(getSigningKey(), Jwts.SIG.HS256)
			.compact();
	}

	/**
	 * Checks if the given JWT has expired.
	 *
	 * @param jwtString {@link String} JWT to check for expiration
	 * @return {@code true} if the token is expired, {@code false} otherwise
	 */
	private boolean isTokenExpired(String jwtString) {
		return extractExpiration(jwtString).before(new Date());
	}

	/**
	 * Extracts the expiration date from the given JWT.
	 *
	 * @param jwtString {@link String} JWT from which to extract the expiration date
	 * @return {@link Date} representing the token's expiration date
	 */
	private Date extractExpiration(String jwtString) {
		return extractClaim(jwtString, Claims::getExpiration);
	}

	/**
	 * Extracts the username (subject) from the given JWT.
	 *
	 * @param jwtString {@link String} JWT from which to extract the username
	 * @return {@link String} representing the username extracted from the JWT
	 */
	public String extractUsername(String jwtString) {
		return extractClaim(jwtString, Claims::getSubject);
	}

	/**
	 * Generates a new JWT string for the specified user details.
	 *
	 * @param userDetails the {@link UserDetails} object containing user information
	 * @return {@link String} representing the generated JWT string
	 */
	public String generateJWTString(UserDetails userDetails) {
		return buildJWTString(new HashMap<>(), userDetails);
	}

	/**
	 * Validates the given JWT against the provided user details.
	 *
	 * @param jwtString   {@link String} JWT to validate
	 * @param userDetails the {@link UserDetails} object to compare against
	 * @return {@code true} if the token is valid for the user details, {@code false} otherwise
	 */
	public Boolean isTokenValid(String jwtString, UserDetails userDetails) {
		return !isTokenExpired(jwtString) && extractUsername(jwtString).equals(userDetails.getUsername());
	}

	/**
	 * Extracts JWT from {@link HttpServletRequest} request header.
	 *
	 * @param request {@link HttpServletRequest} http request
	 * @return {@link String} token from the header
	 */
	public static String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new TokenException("Invalid JWT token");
		}

		return authHeader.substring(7);
	}
}

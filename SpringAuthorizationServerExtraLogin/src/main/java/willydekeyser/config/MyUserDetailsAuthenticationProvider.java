package willydekeyser.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import willydekeyser.user.User;

@Component
public class MyUserDetailsAuthenticationProvider implements AuthenticationProvider {

	private final MyUserDetailsService myUserDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	public MyUserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder, MyUserDetailsService myUserDetailsService) {
		this.myUserDetailsService = myUserDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        String test = ((MyUsernamePasswordAuthenticationToken) authentication).getTest();
        User user = (User) myUserDetailsService.loadUserByUsername(username);
        
        if (!user.enabled()) {
        	throw new BadCredentialsException("User not found!");
		}
        if(!passwordEncoder.matches(password, user.password())) {
        	throw new BadCredentialsException("User not found!");
        }
        if (!test.equals(user.test())) {
        	throw new BadCredentialsException("User not found!");
		}
		return new MyUsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), ((MyUsernamePasswordAuthenticationToken) authentication).getTest(), user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(MyUsernamePasswordAuthenticationToken.class);
	}

	
}

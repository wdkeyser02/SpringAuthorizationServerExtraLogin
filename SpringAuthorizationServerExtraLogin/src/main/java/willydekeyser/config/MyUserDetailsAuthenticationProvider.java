package willydekeyser.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
        User user = (User) myUserDetailsService.loadUserByUsername(username);
        
        if(!passwordEncoder.matches(password, user.getPassword())) {
        	throw new BadCredentialsException("User not found!");
        }
        if (!((MyUsernamePasswordAuthenticationToken) authentication).getTest().equals("TEST")) {
        	throw new BadCredentialsException("User not found!");
		}
		return new MyUsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), ((MyUsernamePasswordAuthenticationToken) authentication).getTest(), user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(MyUsernamePasswordAuthenticationToken.class);
	}

	
}

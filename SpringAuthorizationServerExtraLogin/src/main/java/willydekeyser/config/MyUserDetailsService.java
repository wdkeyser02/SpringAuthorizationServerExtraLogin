package willydekeyser.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	
	List<User> users = new ArrayList<>();
	
	public MyUserDetailsService() {
		users.add((User) User.withUsername("user").password("{noop}password").roles("USER").build());
		users.add((User) User.withUsername("admin").password("{noop}password").roles("USER", "ADMIN").build());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return users.stream()
				.filter(user -> user.getUsername().equals(username))
				.findAny()
				.orElseThrow(() -> new BadCredentialsException("User not found!"));
	}

}

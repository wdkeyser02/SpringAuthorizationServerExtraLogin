package willydekeyser.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import willydekeyser.user.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

	
	private final JdbcTemplate jdbcTemplate;
	
	public MyUserDetailsService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		List<GrantedAuthority> authorities = new ArrayList<>();
		String sql = """
				SELECT user.username, user.password, user.enabled, authorities.authority, users_custom_info.test
				FROM users_custom_info users_custom_info, users user 
				LEFT JOIN authorities on user.username = authorities.username 
				WHERE user.username = users_custom_info.username AND user.username = ?;
				""";
		return jdbcTemplate.query(sql, rs -> {
			String username = "";
			String password = "";
			boolean enabled = false;
			String test = "";
			boolean first = true;
			while (rs.next()) {
				if (first) {
					first = false;
					username = rs.getString("username");
					password = rs.getString("password");
					enabled = rs.getBoolean("enabled");
					test = rs.getString("test");
				}
				authorities.add(new SimpleGrantedAuthority(rs.getString("authority")));
			}
			if (userName.equals(username)) {
				return new User(username, password, enabled, authorities, test);
			}
			throw new UsernameNotFoundException("User not found!");
		}, userName);

	}

}

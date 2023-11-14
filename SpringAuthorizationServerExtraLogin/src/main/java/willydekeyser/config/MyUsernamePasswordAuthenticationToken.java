package willydekeyser.config;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class MyUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	private String test;
	
	public MyUsernamePasswordAuthenticationToken(Object principal, Object credentials, String test) {
        super(principal, credentials);
        this.test = test;
        super.setAuthenticated(false);
    }

    public MyUsernamePasswordAuthenticationToken(Object principal, Object credentials, String test, 
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.test = test;
    }

    public String getTest() {
        return this.test;
    }
}

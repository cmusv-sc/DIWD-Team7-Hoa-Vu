package neo4j.services;

import java.util.Iterator;
import java.util.Map;

import neo4j.repositories.PaperRepository;
import neo4j.repositories.UserRepository;
import neo4j.security.CurrentUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

	@Autowired UserRepository userRepository;
	
    public User getUser(String username) {
        Iterator<Map<String, Object>> result = userRepository.findUser(username).iterator();
        
        String name;
        String pass;
        if (result.hasNext()) {
            Map<String, Object> row = result.next();
            name = row.get("name").toString();
            pass = row.get("pass").toString();
            User u = new CurrentUser(name,pass);
            return u;
        }
        return null;
    }
}
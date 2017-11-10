package in.techbeat.palapa.repository;

import in.techbeat.palapa.model.db.User;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface UserRepository extends GraphRepository<User> {
    User findByUsername(String username);
    User findByUsernameAndPasswordHash(String username, String passwordHash);
    User findByEmail(String email);
}

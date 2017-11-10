package in.techbeat.palapa.repository;

import in.techbeat.palapa.model.db.Role;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface RoleRepository extends GraphRepository<Role> {
    Role findByName(String name);
}

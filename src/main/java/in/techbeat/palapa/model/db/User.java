package in.techbeat.palapa.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @GraphId
    private Long id;
    private String username;
    private String passwordHash;
    private String email;

    @Relationship(type = "HAS_ROLE")
    private Set<Role> roles;

    @Relationship(type = "FOLLOWS")
    private Set<User> followedUsers;

    // Bi-directional friendship
    @Relationship(type = "IS_FRIEND_OF", direction = Relationship.UNDIRECTED)
    private Set<User> friends;

    public void hasRole(Role role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }
}

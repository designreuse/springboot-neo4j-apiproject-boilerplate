package in.techbeat.palapa.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @GraphId
    private Long id;
    private String name;
}

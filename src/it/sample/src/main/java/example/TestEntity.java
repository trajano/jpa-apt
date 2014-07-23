package example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(name = "TestEntity.getAllReverseOrder", query = "select p from PersistedBean p order by p.someTimestamp desc, p.id desc"),
        @NamedQuery(name = "TestEntity.getAllByMessage", query = "select p from PersistedBean p where p.message = :message") })
public class TestEntity {
    /**
     * ID.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Message.
     */
    private String message;

}
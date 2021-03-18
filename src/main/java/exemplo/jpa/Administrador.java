package exemplo.jpa;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "TB_ADMINISTRADOR")
@DiscriminatorValue(value = "A")
@PrimaryKeyJoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Administrador.porNome",
                    query = "SELECT a FROM Administrador a WHERE a.nickname LIKE :nickname ORDER BY a.id"
            )
        }
)
public class Administrador extends Usuario implements Serializable {

    @Override
    public String toString() {
        return "ToStringAdmin[ id=" + id + " ]";
    }

}

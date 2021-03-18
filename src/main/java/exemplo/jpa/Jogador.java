package exemplo.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "TB_JOGADOR")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Jogador.porNome",
                    query = "SELECT a FROM Jogador a WHERE a.nickname LIKE :nickname ORDER BY a.id"
            )
        }
)
@DiscriminatorValue(value = "J")
@PrimaryKeyJoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
public class Jogador extends Usuario implements Serializable {

    @Column(name = "NUM_PONTOS")
    private int pontos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_JOGADORES_CONQUISTAS",
            joinColumns = {
                @JoinColumn(name = "ID_JOGADOR")},
            inverseJoinColumns = {
                @JoinColumn(name = "ID_CONQUISTA")})
    private List<Conquista> conquistas;

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("toStringJogador - [");
        sb.append(super.toString());
        sb.append("]");
        return sb.toString();
    }
}

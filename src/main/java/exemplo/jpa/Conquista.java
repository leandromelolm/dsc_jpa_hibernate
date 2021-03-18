package exemplo.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "TB_CONQUISTA")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Conquista.porNome",
                    query = "SELECT c FROM Conquista c WHERE c.nome LIKE :nome ORDER BY c.id"
            )
        }
)
public class Conquista implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9\\s]{3,20}", message = "{exemplo.jpa.Conquista.nome}")
    @Column(name = "TXT_NOME")
    private String nome;

    @NotBlank
    @Pattern(regexp = ".{5,100}", message = "{exemplo.jpa.Conquista.descricao}")
    @Column(name = "TXT_DESCRICAO")
    private String descricao;

    @Min(0)
    @Max(10000)
    @Column(name = "NUM_PONTOS")
    private int pontos;
    
    //A qual jogo pertence    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_JOGO", referencedColumnName = "ID")
    private Jogo jogo;
    
    //O autor da conquista
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ADMINISTRADOR", referencedColumnName = "ID")
    private Administrador administrador;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    
}

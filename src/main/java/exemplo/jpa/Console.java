package exemplo.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "TB_CONSOLE")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Console.porNome",
                    query = "SELECT c FROM Console c WHERE c.nome LIKE :nome ORDER BY c.id"
            )
        }
)
public class Console implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank
    @Size(max = 15)
    @Column(name = "TXT_NOME", length = 15, nullable = false)
    private String nome;

    @NotNull
    @Pattern(regexp = "[1-2]{1}[0-999]{3}", 
            message = "{exemplo.jpa.Console.ano}")
    @Column(name = "NUM_ANO", nullable = false)
    private String ano;
    
    @NotBlank
    @Size(max = 15)
    @Column(name = "TXT_FABRICANTE", length = 15, nullable = false)
    private String fabricante;

    @OneToMany(mappedBy = "console", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Jogo> jogos;

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

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos;
    }

}

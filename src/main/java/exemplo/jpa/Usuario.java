package exemplo.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "TB_USUARIO")
@Inheritance(strategy = InheritanceType.JOINED) 
@DiscriminatorColumn(name = "DISC_USUARIO", 
        discriminatorType = DiscriminatorType.STRING, length = 1)
public abstract class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    
    @NotBlank
    @Size (min = 5, max = 20)
    @Pattern(regexp = "\\p{Lower}+", message = "{exemplo.jpa.Usuario.nome}")
    @Column(name = "TXT_NICKNAME")
    protected String nickname;
    
    @NotBlank
    @Email
    @Column(name = "TXT_EMAIL", length = 30, nullable = false)
    protected String email;
    
    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "((?=.*\\p{Digit})(?=.*\\p{Lower})(?=.*\\p{Upper})(?=.*\\p{Punct}).{8,20})", 
            message = "{exemplo.jpa.Usuario.senha}")
    @Column(name = "TXT_SENHA")
    protected String senha;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CRIACAO", nullable = true)
    protected Date dataCriacao;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_ULTIMO_LOGIN", nullable = true)    
    protected Date dataUltimoLogin;
    
//   @PrePersist
//    public void setDataCriacao() {
//        this.setDataCriacao(new Date());
//    }
//       @PrePersist
//    public void setDataUltimoLogin() {
//        this.setDataUltimoLogin(new Date());
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;

        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.append(", ");
        sb.append(this.nickname);

        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataUltimoLogin() {
        return dataUltimoLogin;
    }

    public void setDataUltimoLogin(Date dataUltimoLogin) {
        this.dataUltimoLogin = dataUltimoLogin;
    }


}

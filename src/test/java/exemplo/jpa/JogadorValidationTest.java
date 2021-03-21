package exemplo.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class JogadorValidationTest extends Teste {   
    
    @Test(expected = ConstraintViolationException.class)
    public void persistirJogadorInvalido(){
        Jogador jogador = null;
        try {
            jogador = new Jogador();
            jogador.setNickname("Alfonso");//Nome inválido (precisa de no minimo 5 caracteres e letras minúsculas)
            jogador.setEmail("email_invalido@");//email inválido
            jogador.setSenha("testesenha!invalido");//senha inválida por dois motivos. 1- caracteres acima do permitido 2- falta caracteres (Numero e Maiúsculo)
            jogador.setDataUltimoLogin(new Date());
            jogador.setDataCriacao(new Date());
           
            em.persist(jogador);
            em.flush();  
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> contraintViolations = ex.getConstraintViolations();
            
            contraintViolations.forEach(violation -> {
                assertThat(violation.getRootBeanClass() + "." + violation.getPropertyPath() + ": " + violation.getMessage(),
                        CoreMatchers.anyOf(
                                startsWith("class exemplo.jpa.Jogador.nickname: Deve possuir letras minúsculas e ao menos 5 caracteres"),
                                startsWith("class exemplo.jpa.Jogador.email: Deve ser um endereço de e-mail com formato válido"),
                                startsWith("class exemplo.jpa.Jogador.senha: A senha deve possuir entre 8 e 12 caracteres e pelo menos um caractere de: pontuação, maiúscula, minúscula e número")
                                
                        )    
                );
            });
            /*
            assertEquals com com o valor esperado de 4. 
            nome invalido
            email invalido
            senha Quantidade de caracteres invalido
            senha obrigatoriedade de caracteres inválidos
            */
            assertEquals(4,contraintViolations.size()); 
            throw ex;
        }
    }
    
        @Test(expected = ConstraintViolationException.class)
    public void atualizarJogadorInvalido() {
        TypedQuery<Jogador> query = em.createQuery("SELECT j FROM Jogador j WHERE j.nickname like :nickname", Jogador.class);
        query.setParameter("nickname", "zxasddd");
        Jogador jogador = query.getSingleResult();
        jogador.setSenha("12345678");

        try {
            em.flush();
        } catch (ConstraintViolationException ex) {    
            ConstraintViolation violation = ex.getConstraintViolations().iterator().next();
            assertEquals("A senha deve possuir entre 8 e 12 caracteres e pelo menos um caractere de: pontuação, maiúscula, minúscula e número", violation.getMessage());
            assertEquals(1, ex.getConstraintViolations().size());
            throw ex;
        }
    }
}

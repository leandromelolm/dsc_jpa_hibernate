package exemplo.jpa;

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

public class JogoValidationTest extends Teste {   
    
    @Test(expected = ConstraintViolationException.class)
    public void persistirJogoInvalido(){
        Jogo jogo = null;
        try {
            jogo = new Jogo();
            jogo.setNome("the legend of zelda"); //Nome Inválido
            //E não possui um console atrelado a ele! @NotNull
            em.persist(jogo);
            em.flush();  
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> contraintViolations = ex.getConstraintViolations();
            
            contraintViolations.forEach(violation -> {
                assertThat(violation.getRootBeanClass() + "." + violation.getPropertyPath() + ": " + violation.getMessage(),
                        CoreMatchers.anyOf(
                                startsWith("class exemplo.jpa.Jogo.nome: O nome do jogo possui um padrão inválido."),
                                startsWith("class exemplo.jpa.Jogo.console: não deve ser nulo")        
                        )    
                );
            });
            assertEquals(2, contraintViolations.size());
            assertNull(jogo.getId()); 
            throw ex;
        }
    }
    
     @Test(expected = ConstraintViolationException.class)
    public void atualizarJogoInvalido() {
        TypedQuery<Jogo> query = em.createQuery("SELECT x FROM Jogo x  WHERE x.nome  like :nome", Jogo.class);
        query.setParameter("nome", "Earthbound");
        Jogo jogo = query.getSingleResult();
        System.out.println("exemplo.jpa.JogoValidationTest.atualizarJogoInvalido() + " + jogo.getNome());
        jogo.setNome("earthBOUND"); //Nome inválido

        try {
            em.flush();
        } catch (ConstraintViolationException ex) {    
            ConstraintViolation violation = ex.getConstraintViolations().iterator().next();
            assertEquals("O nome do jogo possui um padrão inválido.", violation.getMessage());
            assertEquals(1, ex.getConstraintViolations().size());
            throw ex;
        }
    }       
}
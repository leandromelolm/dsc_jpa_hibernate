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
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ConquistaValidationTest extends Teste {   
    
    @Test(expected = ConstraintViolationException.class)
    public void persistirConquistaInvalida(){
        Conquista conquista = null;
        try {
            conquista = new Conquista();
            conquista.setDescricao("abcd"); //Menos de 5 caracteres
            conquista.setNome("!@#$%"); //Caracteres especiais
            conquista.setPontos(10); 
            
            em.persist(conquista);
            em.flush();  
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> contraintViolations = ex.getConstraintViolations();
            
            contraintViolations.forEach(violation -> {
                assertThat(violation.getRootBeanClass() + "." + violation.getPropertyPath() + ": " + violation.getMessage(),
                        CoreMatchers.anyOf(
                                startsWith("class exemplo.jpa.Conquista.nome: O nome da conquista não pode possuir caracteres especiais e possuir entre 3 e 20 caracteres."),
                                startsWith("class exemplo.jpa.Conquista.descricao: A descrição da conquista deve conter entre 5 e 100 caracteres.")
                        )    
                );
            });
            assertEquals(2,contraintViolations.size());
            throw ex;
        }
    }
    
    @Test(expected = ConstraintViolationException.class)
    public void atualizarVendedorInvalido() {
        TypedQuery<Conquista> query = em.createQuery("SELECT x FROM Conquista x WHERE x.nome like :nome", Conquista.class);
        query.setParameter("nome", "Perfect");
        Conquista conquista = query.getSingleResult();
        
        conquista.setDescricao("xy");
        try {
            em.flush();
        } catch (ConstraintViolationException ex) {    
            ConstraintViolation violation = ex.getConstraintViolations().iterator().next();
            assertEquals("A descrição da conquista deve conter entre 5 e 100 caracteres.", violation.getMessage());
            assertEquals(1, ex.getConstraintViolations().size());
            throw ex;
        }
    }
}

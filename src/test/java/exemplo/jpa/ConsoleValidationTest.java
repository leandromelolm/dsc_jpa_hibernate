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

public class ConsoleValidationTest extends Teste {   
    
    @Test(expected = ConstraintViolationException.class)
    public void persistirConsoleInvalido(){
        Console console = null;
        try {
            console = new Console();
            console.setNome("Nintendo Wii");
            console.setAno("06");//Ano inválido
            console.setFabricante("");//Fabricante inválido
            em.persist(console);
            em.flush();  
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> contraintViolations = ex.getConstraintViolations();
            
            contraintViolations.forEach(violation -> {
                assertThat(violation.getRootBeanClass() + "." + violation.getPropertyPath() + ": " + violation.getMessage(),
                        CoreMatchers.anyOf(
//                                startsWith("class exemplo.jpa.Console.nome: não deve estar em branco"),
                                startsWith("class exemplo.jpa.Console.ano: deve ser um ano com 4 dígitos(AAAA)"),
                                startsWith("class exemplo.jpa.Console.fabricante: não deve estar em branco")                                
                        )    
                );
            });
            assertEquals(2,contraintViolations.size());
            assertNull(console.getId()); 
            throw ex;
        }
    }
    
     @Test(expected = ConstraintViolationException.class)
    public void atualizarConsoleInvalido() {
        TypedQuery<Console> query = em.createQuery("SELECT c FROM Console c  WHERE c.nome  like :nome", Console.class);
        query.setParameter("nome", "Mega Drive");
        Console console = query.getSingleResult();
        console.setAno("89");

        try {
            em.flush();
        } catch (ConstraintViolationException ex) {    
            ConstraintViolation violation = ex.getConstraintViolations().iterator().next();
            assertEquals("deve ser um ano com 4 dígitos(AAAA).", violation.getMessage());
            assertEquals(1, ex.getConstraintViolations().size());
            throw ex;
        }
    }       
}
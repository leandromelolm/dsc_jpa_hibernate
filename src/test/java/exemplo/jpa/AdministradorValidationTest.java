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
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class AdministradorValidationTest extends Teste {   
    
    @Test(expected = ConstraintViolationException.class)
    public void persistirAdministradorInvalido(){
        Administrador administrador = null;
        try {
            administrador = new Administrador();
            administrador.setNickname("Eduard0");//Nome inválido (precisa de no minimo 5 caracteres e letras minúsculas)
            administrador.setEmail("email_invalido@");//email inválido
            administrador.setSenha("testesenhainvalida");//senha inválida
            administrador.setDataUltimoLogin(new Date());
            administrador.setDataCriacao(new Date());
            administrador.setId(15);
           
            em.persist(administrador);
            em.flush();  
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> contraintViolations = ex.getConstraintViolations();
            
            contraintViolations.forEach(violation -> {
                assertThat(violation.getRootBeanClass() + "." + violation.getPropertyPath() + ": " + violation.getMessage(),
                        CoreMatchers.anyOf(
                                startsWith("class exemplo.jpa.Administrador.nickname: Deve possuir letras minúsculas e ao menos 5 caracteres"),
                                startsWith("class exemplo.jpa.Administrador.email: Deve ser um endereço de e-mail com formato válido"),
                                startsWith("class exemplo.jpa.Administrador.senha: A senha deve possuir entre 8 e 12 caracteres e pelo menos um caractere de: pontuação, maiúscula, minúscula e número")
                                
                        )    
                );
            });
            assertEquals(4,contraintViolations.size());
            assertNotNull(administrador.getId()); 
            throw ex;
        }
    }

            @Test(expected = ConstraintViolationException.class)
    public void atualizarAdministradorInvalido() {
        TypedQuery<Administrador> query = em.createQuery("SELECT a FROM Administrador a WHERE a.nickname like :nickname", Administrador.class);
        query.setParameter("nickname", "mestresonic");
        Administrador administrador = query.getSingleResult();
        administrador.setEmail("mestresonic.com.br");

        try {
            em.flush();
        } catch (ConstraintViolationException ex) {    
            ConstraintViolation violation = ex.getConstraintViolations().iterator().next();
            assertEquals("Deve ser um endereço de e-mail com formato válido", violation.getMessage());
            assertEquals(1, ex.getConstraintViolations().size());
            throw ex;
        }
    }
}

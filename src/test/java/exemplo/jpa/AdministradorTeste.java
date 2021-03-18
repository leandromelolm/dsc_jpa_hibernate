package exemplo.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.TypedQuery;
import org.junit.Test;
import static org.junit.Assert.*;

public class AdministradorTeste extends Teste {

    @Test
    public void persistirAdministrador() {
        Administrador administrador = new Administrador();
        administrador.setNickname("adminmortal");
        administrador.setEmail("adminmortal@gmail.com");
        administrador.setSenha("M0RT4LK0MB4t$");
        administrador.setDataCriacao(new Date());
        administrador.setDataUltimoLogin(new Date());
        em.persist(administrador);
        em.flush();

        assertNotNull(administrador.getId());
    }

    @Test
    public void consultarAdministrador() {
        Administrador administrador = em.find(Administrador.class, 4);
        assertNotNull(administrador);
        assertEquals("mestresonic", administrador.getNickname());
        assertEquals("mastersonic@google.com", administrador.getEmail());
        assertEquals("Sonic_1234", administrador.getSenha());
    }

    @Test
    public void atualizarAdministrador() {
        String nicknameNovo = "domguy";
        String nicknameAntigo = "doomguy";
        
        TypedQuery<Administrador> query = em.createNamedQuery("Administrador.porNome", Administrador.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);      
        query.setParameter("nickname", nicknameAntigo);
        
        Administrador administrador = query.getSingleResult();          
        assertEquals(nicknameAntigo, administrador.getNickname());
        
        administrador.setNickname(nicknameNovo);
        em.flush();
        
        assertEquals(0, query.getResultList().size());        
        query.setParameter("nickname", nicknameNovo);
        administrador = query.getSingleResult(); 
        assertNotNull(administrador);
        assertEquals(nicknameNovo, administrador.getNickname());
    }

    
    @Test
    public void atualizarAdministradorMerge() {
        String nicknameAntigo = "laracroft";
        String senhaAntiga = "TombRaider123!";        
        String nicknameNovo = "lalacroft";
        String senhaNova = "TOmbR41der!";
        
        TypedQuery<Administrador> query = em.createNamedQuery("Administrador.porNome", Administrador.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);   
        query.setParameter("nickname", nicknameAntigo);
        Administrador administrador = query.getSingleResult();          
        assertEquals(nicknameAntigo, administrador.getNickname());
        assertEquals(senhaAntiga, administrador.getSenha());
        
        administrador.setNickname(nicknameNovo);
        administrador.setSenha(senhaNova);
        em.clear();
        em.merge(administrador);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        administrador = em.find(Administrador.class, 6, properties);
        assertEquals(nicknameNovo, administrador.getNickname());      
        assertEquals(senhaNova, administrador.getSenha());       
               
    }
   
    @Test
    public void removerAdministrador() {
        String nome = "adminmaster";
        System.out.println("AdministradorTeste - Iniciando removerAdministrador");
        TypedQuery<Administrador> query = em.createNamedQuery("Administrador.porNome", Administrador.class);
        query.setParameter("nickname", nome);
        Administrador administrador = query.getSingleResult();
        assertNotNull(administrador);
        em.remove(administrador);
        em.flush();
        assertEquals(0, query.getResultList().size());
        System.out.println("AdministradorTeste - Terminando removerAdministrador");
    }
}

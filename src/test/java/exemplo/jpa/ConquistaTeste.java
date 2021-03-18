package exemplo.jpa;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.TypedQuery;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConquistaTeste extends Teste {

    @Test
    public void persistirConquista() {
        Jogo jogo = em.find(Jogo.class, 2);      
        assertEquals("Streets of Rage", jogo.getNome());
        Conquista conquista = new Conquista();
        conquista.setNome("Good Ending");
        conquista.setDescricao("Não se alie a Mr. X, o desafie e o vença.");
        conquista.setJogo(jogo);
        conquista.setPontos(500);
        em.persist(conquista);
        em.flush();
        assertNotNull(conquista.getId());
    }

    @Test
    public void consultarConquista() {     
        Conquista conquista = em.find(Conquista.class, 1);
        assertNotNull(conquista);
        assertEquals("Black Cat", conquista.getNome());
        assertEquals("Conquiste sete vidas", conquista.getDescricao());
        assertEquals(100, conquista.getPontos());
    }
    
    
    @Test
    public void atualizarConquista() {
        String nomeConquista = "Taekwondo";
        int pontuacaoAntiga = 250;
        int pontuacaoNova = 200;
        
        TypedQuery<Conquista> query = em.createNamedQuery("Conquista.porNome", Conquista.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);      
        query.setParameter("nome", nomeConquista);
        
        Conquista conquista = query.getSingleResult();          
        assertEquals(nomeConquista, conquista.getNome());
        assertEquals(pontuacaoAntiga, conquista.getPontos());
        
        conquista.setPontos(pontuacaoNova);
        em.flush();
        
        conquista = query.getSingleResult();
        assertEquals(pontuacaoNova, conquista.getPontos());
    }

    
    @Test
    public void atualizarConquistaMerge() {
        String nomeConquista = "Perfect";
        String novaDescrição = "Vença os dois rounds sem tomar dano.";
        
        TypedQuery<Conquista> query = em.createNamedQuery("Conquista.porNome", Conquista.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);   
        query.setParameter("nome", nomeConquista);
        Conquista conquista = query.getSingleResult();          
        assertEquals(nomeConquista, conquista.getNome());
        
        conquista.setDescricao(novaDescrição);
        em.clear();
        em.merge(conquista);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        conquista = em.find(Conquista.class, 16, properties);
        assertEquals(novaDescrição, conquista.getDescricao());    
        
    }
    
    @Test
    public void removerConquista() {
        /*
    }
        System.out.println("ConquistaTeste - Iniciando removerConquista");
        TypedQuery<Conquista> query = em.createNamedQuery("Conquista.DelDependencia", Conquista.class);
        query.setParameter("id", "1");
*/
        
        TypedQuery<Conquista> query = em.createNamedQuery("Conquista.porNome", Conquista.class);
        query.setParameter("nome", "Marble Zone 3");
        Conquista conquista = query.getSingleResult();
        assertNotNull(conquista);
        em.remove(conquista);
        em.flush();
        assertEquals(0, query.getResultList().size());
        System.out.println("ConquistaTeste - Terminando removerConquista");
    }
}

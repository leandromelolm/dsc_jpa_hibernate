package exemplo.jpa;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.TypedQuery;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConsoleTeste extends Teste {

    @Test
    public void persistirConsole() {
        Console console = new Console();
        console.setNome("PlayStation 2");
        console.setFabricante("Sony");
        console.setAno("1999");
        em.persist(console);
        em.flush();
        assertNotNull(console.getId());
    }

    @Test
    public void consultarConsole() {
        Console console = em.find(Console.class, 1);
        assertNotNull(console);
        assertEquals("Mega Drive", console.getNome());
        assertEquals("Sega", console.getFabricante());
    }
    
    
    @Test
    public void atualizarConsole() {
        String nomeAntigo = "Neo-geo";
        String nomeNovo = "NEOGEO AES";
        
        TypedQuery<Console> query = em.createNamedQuery("Console.porNome", Console.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);      
        query.setParameter("nome", nomeAntigo);
        
        Console console = query.getSingleResult();          
        assertEquals(nomeAntigo, console.getNome());
        
        console.setNome(nomeNovo);
        em.flush();
        
        assertEquals(0, query.getResultList().size());
        query.setParameter("nome", nomeNovo);
        console = query.getSingleResult();
        assertNotNull(console);        
        assertEquals(nomeNovo, console.getNome());
    }
        
    @Test
    public void atualizarConsoleMerge() {
        String nomeAntigo = "Super Nintendo";
        String nomeNovo = "Super NES";
        
        TypedQuery<Console> query = em.createNamedQuery("Console.porNome", Console.class);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);   
        query.setParameter("nome", nomeAntigo);
        Console console = query.getSingleResult();          
        assertEquals(nomeAntigo, console.getNome());
        
        console.setNome(nomeNovo);
        em.clear();
        em.merge(console);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        console = em.find(Console.class, 2, properties);
        assertEquals(nomeNovo, console.getNome());    
        
    }
    
    @Test
    public void removerConsole() {
        System.out.println("ConsoleTeste - Iniciando removerConsole");
        TypedQuery<Console> query = em.createNamedQuery("Console.porNome", Console.class);
        query.setParameter("nome", "Atari");
        Console console = query.getSingleResult();
        assertNotNull(console);
        em.remove(console);
        em.flush();
        assertEquals(0, query.getResultList().size());
        System.out.println("ConsoleTeste - Terminando removerConsole");
    }
}

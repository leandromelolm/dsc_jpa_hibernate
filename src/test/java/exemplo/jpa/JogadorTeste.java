package exemplo.jpa;

import java.util.Date;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.TypedQuery;
import org.junit.Test;
import static org.junit.Assert.*;

public class JogadorTeste extends Teste {

    @Test
    public void persistirJogador() {
        Jogador jogador = new Jogador();
        jogador.setNickname("niicolas");
        jogador.setEmail("nicolas@teste.com");
        jogador.setSenha("doom666!D");
        jogador.setDataCriacao(new Date());
        jogador.setDataUltimoLogin(new Date());
        jogador.setPontos(666);
        em.persist(jogador);
        em.flush();

        assertNotNull(jogador.getId());
    }

    @Test
    public void consultarJogador() {
        Jogador jogador = em.find(Jogador.class, 1);
        assertNotNull(jogador);
       
        assertEquals("zxasddd", jogador.getNickname());
        assertEquals("zxasddd@gmail.com", jogador.getEmail());
        assertEquals("MEGA_drive", jogador.getSenha());
        assertEquals(1000, jogador.getPontos());
    }
    
       @Test
    public void removerJogador() {
        String nome = "supergamerz";
        System.out.println("JogadorTeste - Iniciando removerJogador");
        TypedQuery<Jogador> query = em.createNamedQuery("Jogador.porNome", Jogador.class);
        query.setParameter("nickname", nome);
        Jogador jogador = query.getSingleResult();
        assertNotNull(jogador);
        em.remove(jogador);
        em.flush();
        assertEquals(0, query.getResultList().size());
        System.out.println("JogadorTeste - Terminando removerJogador");
    }
    
        
}

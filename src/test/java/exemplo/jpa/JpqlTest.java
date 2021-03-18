package exemplo.jpa;

import static exemplo.jpa.Teste.logger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.startsWith;
import org.junit.Test;
import static org.junit.Assert.*;

public class JpqlTest extends Teste {

    @Test
    public void quantidadeJogadores() {
         logger.info ("Executando quantidadeJogadores()");
        TypedQuery<Long> query;
        query = em.createQuery(
                "SELECT COUNT(c) FROM Jogador c WHERE c.id IS NOT NULL",
                Long.class);
        Long resultado = query.getSingleResult();
        System.out.println("Resultado do Teste:" + resultado);
        assertEquals(new Long(3),resultado);
    }

    @Test
    public void quantidadeJogadoresADMs() {
        logger.info ("Executando quantidadeJogadoresADMs()");
        TypedQuery<Long> query;
        query = em.createQuery(
                "SELECT COUNT(c) FROM Administrador c WHERE c.id IS NOT NULL", 
                Long.class);
        Long resultado = query.getSingleResult();
        System.out.println("Resultado do Teste:" + resultado);
        assertEquals(new Long(4), resultado);  
    }
     
    @Test
    public void ordenacaoJogadoresADMs() {
        logger.info ("Executando ordenacaoJogadoresADMs()");
        TypedQuery<Jogador> query;
        query = em.createQuery(
                "SELECT c.nickname FROM Administrador c ORDER BY c.nickname ASC",
                Jogador.class);
        List<Jogador> jogadores = query.getResultList();
        System.out.println("Resultado do Teste:" + jogadores.get(0));
        System.out.println("Resultado do Teste:" + jogadores.get(1));
        System.out.println("Resultado do Teste:" + jogadores.get(2));
        System.out.println("Resultado do Teste:" + jogadores.get(3));
        assertEquals(4, jogadores.size());
        assertEquals("adminmaster", jogadores.get(0));
        assertEquals("doomguy", jogadores.get(1));
        assertEquals("laracroft", jogadores.get(2));
        assertEquals("mestresonic", jogadores.get(3));
        
    }

    @Test
    public void PrimeiroEUltimoCriado() {
        logger.info ("Executando PrimeiroEUltimoLogin()");
        Query query = em.createQuery(
                "SELECT MAX(c.dataCriacao), MIN(c.dataCriacao) FROM Jogador c");
        Object[] resultado = (Object[]) query.getSingleResult();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String maiorData = dateFormat.format((Date) resultado[0]);
        String menorData = dateFormat.format((Date) resultado[1]);
        System.out.println(maiorData);
        System.out.println(menorData);
        assertEquals("01-01-2020", maiorData);
        assertEquals("01-08-2019", menorData);
    }

    @Test
    public void NomeQtdPontosUltimoJogadorLogado() {
        logger.info ("Executando NomeQtdPontosUltimoJogadorLogado()");
        Query query = em.createQuery(
                "SELECT c.nickname, c.pontos, c.dataUltimoLogin from Jogador c where c.dataUltimoLogin IN (SELECT MAX(c.dataUltimoLogin) FROM Jogador c)");

        Object[] resultado = (Object[]) query.getSingleResult();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String nickname = (String) resultado[0];
        int pontos = (int) resultado[1];
        String dataUltimoLogin = dateFormat.format((Date) resultado[2]);
        System.out.println("Resultado:" + "Nick: " + nickname + " Pontos: " + pontos + " UltimoLogin " + dataUltimoLogin);
        assertEquals("starguardian, 250, 02-02-2021", resultado[0] +", "+ resultado[1]+", "+ dataUltimoLogin);
    }

    @Test
    public void ordenacaoConquistaNome() {
        logger.info ("Executando ordenacaoConquistaNome()");        
        TypedQuery<Object[]> query;
        query = em.createQuery(
                "SELECT c.nome, c.descricao FROM Conquista c ORDER BY c.nome DESC, c.descricao ASC",
                Object[].class);
        List<Object[]> conquistas = query.getResultList();
        assertEquals(21, conquistas.size());

        assertEquals("ULTIMATE:Zere o jogo no expert, sem tomar dano, usando apenas um botão com os olhos fechados e de costas", conquistas.get(0)[0] + ":" + conquistas.get(0)[1]);
        assertEquals("Taekwondo:Vença um round usando apenas os botões de chute", conquistas.get(1)[0] + ":" + conquistas.get(1)[1]);
        assertEquals("Super Samurai:Vença um round em até 10 segundos", conquistas.get(2)[0] + ":" + conquistas.get(2)[1]);
        assertEquals("Perfect:Vença um round de perfect", conquistas.get(3)[0] + ":" + conquistas.get(3)[1]);
    }

    @Test
    public void conquistaPorJogo() {
        logger.info ("Executando conquistaPorJogo()");
        TypedQuery<Conquista> query;
        query = em.createQuery(
                "SELECT c FROM Conquista c WHERE c.jogo.nome = :nome",
                Conquista.class);
        query.setParameter("nome", "Metal Slug");
        List<Conquista> conquistas = query.getResultList();
        assertEquals(6, conquistas.size());
    }

    @Test
    public void conquistasMaisDificeis() {
        logger.info ("Executando conquistasMaisDificeis()");
        TypedQuery<Conquista> query = em.createQuery(
                "SELECT c FROM Conquista c JOIN c.jogo j WHERE c.pontos > :pontos ORDER BY c.pontos + 0 DESC",
                Conquista.class);
        query.setParameter("pontos", 250);
        List<Conquista> conquistas = query.getResultList();
        assertNotNull(conquistas);
        assertEquals(conquistas.size(), 5);
        assertEquals(conquistas.get(0).getJogo().getNome() + "->" + conquistas.get(0).getNome() + "->" + conquistas.get(0).getPontos(), "Samurai Shodown->ULTIMATE->10000");
        assertEquals(conquistas.get(1).getJogo().getNome() + "->" + conquistas.get(1).getNome() + "->" + conquistas.get(1).getPontos(), "Sonic the Hedgehog->Marble Zone Time Attack->350");
        assertEquals(conquistas.get(2).getJogo().getNome() + "->" + conquistas.get(2).getNome() + "->" + conquistas.get(2).getPontos(), "Metal Slug->Mission 3 Complete->300");
        assertEquals(conquistas.get(3).getJogo().getNome() + "->" + conquistas.get(3).getNome() + "->" + conquistas.get(3).getPontos(), "Metal Slug->Mission 2 Complete->300");
        assertEquals(conquistas.get(4).getJogo().getNome() + "->" + conquistas.get(4).getNome() + "->" + conquistas.get(4).getPontos(), "Metal Slug->Mission 1 Complete->300");
    }

    @Test
    public void jogoComConquistaComMaisPontos() {
        logger.info ("Executando jogoComConquistaComMaisPontos()");
        TypedQuery<Object[]> query = em.createQuery(
                "SELECT j.nome, c.pontos FROM Conquista c JOIN c.jogo j WHERE c.pontos IN (SELECT MAX(c.pontos) FROM Conquista c)",
                Object[].class);
        Object[] obj = query.getSingleResult();
        assertEquals(obj[0] + "->" + obj[1], "Samurai Shodown->10000");
    }
    
    @Test
    public void ordenacaoConsoles() {
        logger.info("Executando ordenacaoConsoles()");
        TypedQuery<Console> query;
        query = em.createQuery(
                "SELECT c FROM Console c ORDER BY c.nome DESC", 
                Console.class);
        List<Console> consoles = query.getResultList();
        assertEquals(5, consoles.size());
        assertEquals("Super Nintendo", consoles.get(0).getNome());
        assertEquals("Nintendo 64", consoles.get(1).getNome());
        assertEquals("Neo-geo", consoles.get(2).getNome());
        assertEquals("Mega Drive", consoles.get(3).getNome());
        assertEquals("Atari", consoles.get(4).getNome());
    }

    @Test
    public void jogoMaximaMinimoDataLancamento(){
        logger.info ("Executando jogoMaximaMinimoDataLancamento()");
        Query query = em.createQuery("SELECT MAX(c.dataLancamento), MIN(c.dataLancamento) FROM Jogo c");
        Object[] resultado = (Object[]) query.getSingleResult();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String maiorData = dateFormat.format((Date) resultado[0]);
        String menorData = dateFormat.format((Date) resultado[1]);
        assertEquals("06-06-2000", maiorData);
        assertEquals("10-10-1991", menorData);    
    }
    
    @Test
    public void jogoPorConsole(){
       logger.info("Executando jogoPorConsole()");
       TypedQuery<Jogo> query = em.createQuery("SELECT c FROM Jogo c WHERE c.console.nome =:nome", Jogo.class);
       query.setParameter("nome", "Nintendo 64");
       List<Jogo> jogo = query.getResultList();
       assertEquals(2, jogo.size());
    }
    
    @Test
    public void quantidadedeJogos() {
        logger.info("Executando quantidadedeJogos()");
        TypedQuery<Long>query = em.createQuery("SELECT COUNT(c) FROM Jogo c WHERE c IS NOT NULL", Long.class);
        Long resultado = query.getSingleResult();
        assertEquals(new Long(9), resultado);
    
    }
    @Test
    public void consolePorFabricante() {
        logger.info("Executando consolePorFabricante()");
        TypedQuery<Console>query = em.createQuery("SELECT c FROM Console c WHERE c.fabricante LIKE :fabricante",Console.class);
        query.setParameter("fabricante","Nintendo");
        List<Console> consoles = query.getResultList();
        
        consoles.forEach(Console -> {
            assertTrue(Console.getFabricante().startsWith("Nintendo"));
        });
        assertEquals(2, consoles.size());
    }
}
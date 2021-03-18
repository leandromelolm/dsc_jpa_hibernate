package exemplo.jpa;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    exemplo.jpa.AdministradorTeste.class,
    exemplo.jpa.AdministradorValidationTest.class,
    
    exemplo.jpa.ConquistaTeste.class,
    exemplo.jpa.ConquistaValidationTest.class,
    
    exemplo.jpa.ConsoleTeste.class,
    exemplo.jpa.ConsoleValidationTest.class,    
  
    exemplo.jpa.JogoTeste.class,
    exemplo.jpa.JogoValidationTest.class,
    
    exemplo.jpa.JogadorTeste.class,
    exemplo.jpa.JogadorValidationTest.class,
    
})
public class TesteSuite {
    
}
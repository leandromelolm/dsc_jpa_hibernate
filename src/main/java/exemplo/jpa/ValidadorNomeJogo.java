package exemplo.jpa;

import exemplo.jpa.*;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidadorNomeJogo implements ConstraintValidator<ValidaNomeJogo, String> {
    private List<String> preposicoes; 
    
    @Override
    public void initialize(ValidaNomeJogo validaJogo) {
        this.preposicoes = new ArrayList<>();
        this.preposicoes.add("On");
        this.preposicoes.add("In");
        this.preposicoes.add("At");
        this.preposicoes.add("Since");
        this.preposicoes.add("For");
        this.preposicoes.add("Ago");
        this.preposicoes.add("Before");
        this.preposicoes.add("To");
        this.preposicoes.add("By");
        this.preposicoes.add("Of");
        this.preposicoes.add("The");
    }
    
    @Override
    public boolean isValid(String valor, ConstraintValidatorContext context) {
        String[] partes = valor.split(" ");
        String regexPrimeiraLetra = "[A-Z0-9]";       
                 
        if(!String.valueOf(partes[0].charAt(0)).matches(regexPrimeiraLetra)) return false;
       
        for (int index = 1; index < partes.length; index++) {
            for(String preposicao: preposicoes) {
                if(preposicao.equals(partes[index])) return false;
            }
        }
        
        for (int index = 1; index < partes.length; index++) {
            if(!String.valueOf(partes[index].charAt(0)).matches(regexPrimeiraLetra)) return false;
        }
        
        return true;
    }
}
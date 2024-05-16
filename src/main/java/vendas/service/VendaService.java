package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vendas.enums.ESituacaoVenda;
import vendas.model.Venda;
import vendas.repository.VendaRepository;

import java.util.Date;
import java.util.List;

@Service
public class VendaService {
    
    @Autowired
    private VendaRepository repository;
    
    public List<Venda> findAll(){
        return repository.findAll();
    }
    
    public Venda findById(Long id){
        return repository.findById(id).get();
    }
    
    public Venda criarRascunho(Venda venda){
        venda.setSituacao(ESituacaoVenda.RASCUNHO);
        venda.setDataDaVenda(new Date());
        return repository.save(venda);
    }
}

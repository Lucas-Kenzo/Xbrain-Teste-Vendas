package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vendas.model.Venda;
import vendas.repository.VendaRepository;

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
    
    public Venda salvar(Venda venda){
        return repository.save(venda);
    }
}

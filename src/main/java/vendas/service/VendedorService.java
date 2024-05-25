package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vendas.exception.NotFoundException;
import vendas.exception.ValidacaoException;
import vendas.model.Vendedor;
import vendas.repository.VendedorRepository;

import java.util.List;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository repository;

    public List<Vendedor> findAll(){
        return repository.findAll();
    }

    public Vendedor findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vendedor de ID: " + id + " não encontrado!"));
    }

    public Vendedor salvar(Vendedor vendedor){
        vendedor.setNome(vendedor.getNome().toUpperCase());
        verificaNome(vendedor.getNome());
        return repository.save(vendedor);
    }

    public void verificaNome(String nome){
        boolean exist = repository.findByNome(nome).isPresent();
        if (exist){
            throw  new ValidacaoException("O nome inserido já existe");
        }
    }

}

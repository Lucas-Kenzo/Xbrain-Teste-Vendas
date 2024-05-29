package vendas.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import vendas.exception.EntidadeEmUsoException;
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
        verificaCpf(vendedor.getCpf());
        return repository.save(vendedor);
    }

    public void verificaNome(String nome){
        boolean exist = repository.findByNome(nome).isPresent();
        if (exist){
            throw  new ValidacaoException("O nome inserido já está cadastrado");
        }
    }
    public void verificaCpf(String cpf){
        boolean exist = repository.findByCpf(cpf).isPresent();
        if (exist){
            throw  new ValidacaoException("O cpf inserido já está cadastrado");
        }
    }

    public Vendedor editar(Long vendedorId, Vendedor vendedor){
        var vendedorAtual = findById(vendedorId);
        BeanUtils.copyProperties(vendedor, vendedorAtual, "id");
        return salvar(vendedorAtual);
    }

    public void excluir(Long vendedorId){
        var vendedor = findById(vendedorId);
        try{
            repository.delete(vendedor);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException("Não foi possível excluir o vendedor de ID " + vendedorId);
        }
    }
}

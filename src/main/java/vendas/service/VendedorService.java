package vendas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import vendas.exception.EntidadeEmUsoException;
import vendas.exception.NotFoundException;
import vendas.exception.ValidacaoException;
import vendas.model.Vendedor;
import vendas.repository.VendedorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendedorService {

    private final VendedorRepository repository;

    public List<Vendedor> findAll() {
        return repository.findAll();
    }

    public Vendedor findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vendedor de ID: " + id + " não encontrado!"));
    }

    public Vendedor salvar(Vendedor vendedor) {
        vendedor.setNome(vendedor.getNome().toUpperCase());
        verificaNome(vendedor.getNome(), vendedor.getId());
        verificaCpf(vendedor.getCpf(), vendedor.getId());
        return repository.save(vendedor);
    }

    public void verificaNome(String nome, Long vendedorId) {
        var vendedor = repository.findByNome(nome);
        if (vendedor.isPresent() && !vendedor.get().getId().equals(vendedorId)) {
            throw new ValidacaoException("O nome inserido já está cadastrado");
        }
    }

    public void verificaCpf(String cpf, Long vendedorId) {
        var vendedor = repository.findByCpf(cpf);
        if (vendedor.isPresent() && !vendedor.get().getId().equals(vendedorId)) {
            throw new ValidacaoException("O cpf inserido já está cadastrado");
        }
    }

    public Vendedor editar(Long vendedorId, Vendedor vendedor) {
        vendedor.setNome(vendedor.getNome().toUpperCase());
        var vendedorAtual = findById(vendedorId);
        BeanUtils.copyProperties(vendedor, vendedorAtual, "id");
        verificaNome(vendedorAtual.getNome(), vendedorAtual.getId());
        verificaCpf(vendedorAtual.getCpf(), vendedorAtual.getId());
        return salvar(vendedorAtual);
    }

    public void excluir(Long vendedorId) {
        var vendedor = findById(vendedorId);
        try {
            repository.delete(vendedor);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException("Não foi possível excluir o vendedor de ID " + vendedorId);
        }
    }
}

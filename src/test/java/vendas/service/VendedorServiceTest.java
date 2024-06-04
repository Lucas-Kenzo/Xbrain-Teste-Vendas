package vendas.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vendas.exception.NotFoundException;
import vendas.exception.ValidacaoException;
import vendas.model.Vendedor;
import vendas.repository.VendedorRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static vendas.utils.TestData.*;

@ExtendWith(MockitoExtension.class)
class VendedorServiceTest {

    @InjectMocks
    private VendedorService service;

    @Mock
    private VendedorRepository repository;

    @Test
    void findById_deveLancarNotFoundException_quandoVendedorNaoEncontrado() {
        assertThrows(NotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void salvar_deveRetornarNomeUpperCase_quandoSolicitado() {
        when(repository.save(any(Vendedor.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        var vendedor = service.salvar(umVendedor());
        assertEquals(umVendedor().getNome().toUpperCase(), vendedor.getNome());
    }

    @Test
    void salvar_deveLancarValidacaoException_quandoNomeExiste() {
        var vendedor = umVendedor();
        vendedor.setId(2L);
        when(repository.findByNome(anyString())).thenReturn(Optional.ofNullable(umVendedor()));
        assertThrows(ValidacaoException.class, () -> service.salvar(vendedor));
    }

    @Test
    void salvar_deveLancarValidacaoException_quandoCPFExiste() {
        var vendedor = umVendedor();
        vendedor.setId(2L);
        when(repository.findByCpf(anyString())).thenReturn(Optional.ofNullable(umVendedor()));
        assertThrows(ValidacaoException.class, () -> service.salvar(vendedor));
    }

   @Test
    void editar_deveEditarVendedorNome_quandoSolicitado() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(umVendedor()));
        when(repository.save(any(Vendedor.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        var vendedor = umVendedor();
        vendedor.setNome("Vendedor Eaditado");
        var vendedorEditado = service.editar(1L, vendedor);
        assertEquals(vendedor.getNome().toUpperCase(), vendedorEditado.getNome());
    }

    @Test
    void editar_naoDeveEditarVendedorId_quandoSolicitado() {
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(umVendedor()));
        when(repository.save(any(Vendedor.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        var vendedor = umVendedor();
        vendedor.setId(2L);
        var vendedorEditado = service.editar(1L, vendedor);
        assertEquals(1L, vendedorEditado.getId());
    }
}
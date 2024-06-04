package vendas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vendas.dto.VendaRelatorioResponse;
import vendas.exception.ValidacaoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static vendas.utils.TestData.*;

@ExtendWith(MockitoExtension.class)
class VendaRelatorioServiceTest {

    @InjectMocks
    private VendaRelatorioService service;

    @Mock
    private VendaService vendaService;

    @Mock
    private VendedorService vendedorService;

    @Test
    void findAll_deveRetornarRelatorio_quandoSolicitado() {
        var dataInicial = LocalDate.of(2024, 1, 1);
        var dataFinal = LocalDate.of(2024, 1, 30);
        when(vendedorService.findAll()).thenReturn(List.of(umVendedor()));
        when(vendaService.getVendasFinalizadasPorPeriodo(1L, dataInicial, dataFinal)).thenReturn(30);

        assertThat(service.findAll(dataInicial, dataFinal))
                .extracting(VendaRelatorioResponse::getVendedorNome, VendaRelatorioResponse::getTotalVendas,
                        VendaRelatorioResponse::getMediaDiaria)
                .contains(tuple("Vendedor 01", 30, BigDecimal.valueOf(1.0) ));

        verify(vendedorService).findAll();
        verify(vendaService).getVendasFinalizadasPorPeriodo(1L, dataInicial, dataFinal);
    }

    @Test
    void findAll_deveLAncarValidacaoException_quandoDataFinalMenorQueDataInicial() {
        var dataInicial = LocalDate.of(2024, 1, 30);
        var dataFinal = LocalDate.of(2024, 1, 1);
        assertThrows(ValidacaoException.class, () -> service.findAll(dataInicial, dataFinal));

        assertThatThrownBy(() -> service.findAll(dataInicial, dataFinal))
                .isInstanceOf(ValidacaoException.class)
                .hasMessage("DataFinal não pode ser menor que dataInicial");
    }

}
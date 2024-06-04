package vendas.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import vendas.exception.EntidadeEmUsoException;
import vendas.exception.NotFoundException;
import vendas.exception.ValidacaoException;
import vendas.service.VendaService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vendas.utils.TestData.*;

@WebMvcTest(VendaController.class)
@MockBeans({@MockBean(RestTemplate.class)})
class VendaControllerTest {

    private static String API_BASE = "/api/vendas";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VendaService service;

    @Test
    @SneakyThrows
    void findAll_deveRetornarOk_quandoSolicitado() {
        when(service.findAll())
                .thenReturn(List.of(umaVendaPendente()));

        mvc.perform(get(API_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].valor", is(360)))
                .andExpect(jsonPath("$[0].vendedorId", is(1)))
                .andExpect(jsonPath("$[0].vendedorNome", is("VENDEDOR 01")))
                .andExpect(jsonPath("$[0].situacao", is("PENDENTE")));

        verify(service).findAll();
    }

    @Test
    @SneakyThrows
    void findById_deveRetornarOk_quandoSolicitado() {
        when(service.findById(anyLong()))
                .thenReturn(umaVendaPendente());

        mvc.perform(get(API_BASE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.valor", is(360)))
                .andExpect(jsonPath("$.vendedorId", is(1)))
                .andExpect(jsonPath("$.vendedorNome", is("VENDEDOR 01")))
                .andExpect(jsonPath("$.situacao", is("PENDENTE")));

        verify(service).findById(anyLong());
    }

    @Test
    @SneakyThrows
    void findById_deveRetornarNotFound_quandoVendaNaoEncontrada() {
        doThrow(NotFoundException.class).when(service).findById(any());
        mvc.perform(get(API_BASE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        verify(service).findById(anyLong());
    }

    @Test
    @SneakyThrows
    void salvar_deveRetornarCreated_quandoSolicitado() {

        when(service.criarRascunho(any()))
                .thenReturn(umaVendaRascunho());

        mvc.perform(post(API_BASE + "/criar-rascunho")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("vendedorId", "1")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.valor", is(0)))
                .andExpect(jsonPath("$.vendedorId", is(1)))
                .andExpect(jsonPath("$.vendedorNome", is("VENDEDOR 01")))
                .andExpect(jsonPath("$.situacao", is("RASCUNHO")));
    }

    @Test
    @SneakyThrows
    void adicionarProdutos_deveRetornarOk_quandoSolicitado() {
        var venda = umaVendaPendente();
        venda.setItens(outraListaVendaProduto());
        venda.calcularValorTotal();

        when(service.adicionaProdutosNaVenda(any(), eq(umaListaVendaProdutoRequest())))
                .thenReturn(venda);

        mvc.perform(post(API_BASE + "/adicionar-produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(umaListaVendaProdutoRequest()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.valor", is(1030)))
                .andExpect(jsonPath("$.vendedorId", is(1)))
                .andExpect(jsonPath("$.vendedorNome", is("VENDEDOR 01")))
                .andExpect(jsonPath("$.situacao", is("PENDENTE")))
                .andExpect(jsonPath("$.itens[0].id", is(1)))
                .andExpect(jsonPath("$.itens[0].vendaId", is(1)))
                .andExpect(jsonPath("$.itens[0].produtoId", is(4)));

        verify(service).adicionaProdutosNaVenda(any(), eq(umaListaVendaProdutoRequest()));
    }

    @Test
    @SneakyThrows
    void finalizar_deveRetornarCreated_quandoSolicitado() {

        when(service.finalizarVenda(any()))
                .thenReturn(umaVendaFinalizada());

        mvc.perform(put(API_BASE + "/finalizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.valor", is(876)))
                .andExpect(jsonPath("$.vendedorId", is(1)))
                .andExpect(jsonPath("$.vendedorNome", is("VENDEDOR 01")))
                .andExpect(jsonPath("$.situacao", is("FINALIZADA")));

        verify(service).finalizarVenda(any());
    }

    @Test
    @SneakyThrows
    void alterarVendedor_deveRetornarOk_quandoSolicitado() {

        when(service.alterarVendedor(any(), any()))
                .thenReturn(umaVendaPendente());

        mvc.perform(put(API_BASE + "/altera-vendedor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("vendedorId", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.valor", is(360)))
                .andExpect(jsonPath("$.vendedorId", is(1)))
                .andExpect(jsonPath("$.vendedorNome", is("VENDEDOR 01")))
                .andExpect(jsonPath("$.situacao", is("PENDENTE")));

        verify(service).alterarVendedor(any(), any());
    }

    @Test
    @SneakyThrows
    void excluir_deveRetornarNoContent_quandoSolicitado() {
        mvc.perform(delete(API_BASE + "/1")).andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    void excluir_deveRetornarBadRequest_quandoVendaNaoExcluida() {
        doThrow(EntidadeEmUsoException.class).when(service).excluir(any());
        mvc.perform(delete(API_BASE + "/1")).andExpect(status().isBadRequest());
        verify(service).excluir(any());
    }

}
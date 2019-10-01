package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private SPCService spc;

	@Mock
	private LocacaoDAO dao;
	
	@Parameter          //valor 0 para o primeiro registro do Array Parameters
	public List<Filme> filmes;
	@Parameter(value=1) //valor 1 para o segundo registro do Array Parameters
	public Double valorLocacao;
	@Parameter(value=2) //valor 2 para o terceiro registro do Array Parameters
	public String cenario;
		
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		System.out.println("iniciando 3...");
		CalculadoraTest.ordem.append("3");
	}
	
	@After
	public void tearDown() {
		System.out.println("finalizando 3...");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println(CalculadoraTest.ordem.toString());
	}
	
	@Parameters(name="Teste {index} = {2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(umFilme().agora(), umFilme().agora()), 8.00, "2 Filmes: Sem desconto"},
			{Arrays.asList(umFilme().agora(), new Filme("Filme 1", 2, 4.0), new Filme("Filme 1", 2, 4.0)), 11.00, "3 Filmes: 25%"},
			{Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora()), 13.00, "4 Filmes: 50%"},
			{Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora()), 14.00, "5 Filmes: 75%"},
			{Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora()), 14.00,  "6 Filmes: 100%"},
			{Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora()), 18.00,  "7 Filmes: Sem desconto"}
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws Exception {
		Usuario usuario = new Usuario("Leandro");

		Thread.sleep(5000);
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		//verificacao
		assertThat(locacao.getValor(), is(valorLocacao));
//		System.out.println("!");
	}
	
	@Test
	@Ignore
	public void print() {
		System.out.println(valorLocacao);
	}
}

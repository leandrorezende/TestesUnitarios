package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@InjectMocks @Spy
	private LocacaoService service;
	
	@Mock
	private SPCService spc;
	
	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private EmailService email;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
//		service = new LocacaoService();
//		dao = Mockito.mock(LocacaoDAO.class);
//		service.setLocacaoDAO(dao);
//		
//		spc = Mockito.mock(SPCService.class);
//		service.setSPCSerivice(spc);
//		
//		email = Mockito.mock(EmailService.class);
//		service.setEmailService(email);
		System.out.println("iniciando 2 ...");
		CalculadoraTest.ordem.append("2");
	}
	
	@After
	public void tearDown() {
		System.out.println("finalizando 2...");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println(CalculadoraTest.ordem.toString());
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		List<Filme> filmes = new ArrayList<Filme>(Arrays.asList(umFilme().comValor(5.90).agora(), umFilme().comValor(14.00).agora()));
		Usuario usuario = umUsuario().agora();
		
		Mockito.doReturn(DataUtils.obterData(28, 4, 2017)).when(service).obterData();
	
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		error.checkThat(locacao.getValor(), is(19.90));
//		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
//		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
//		error.checkThat(locacao.getDataLocacao(), ehHoje());
//		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
		
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		List<Filme> filmes = new ArrayList<Filme>(Arrays.asList(umFilmeSemEstoque().agora()));
		Usuario usuario = umUsuario().agora();
		
		service.alugarFilme(usuario, filmes);
	}
	
	@Test(expected = LocadoraException.class)
	public void naoDeveAlugarFilmeSemUsuario() throws Exception {
		List<Filme> filmes = new ArrayList<Filme>(Arrays.asList(umFilme().agora()));
		Usuario usuario = null;
		
		service.alugarFilme(usuario, filmes);
	}
	
	@Test(expected = LocadoraException.class)
	public void naoDeveAlugarFilmeSemFilme() throws Exception {
		List<Filme> filmes = Collections.emptyList();
		Usuario usuario = umUsuario().agora();
		
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void testLocacaoDescontoTerceiroFilme75Pc() throws Exception {
		//cenario
		List<Filme> filmes = new ArrayList<Filme>(Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora()));
		Usuario usuario = umUsuario().agora();
				
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(locacao.getValor(), is(11.00));
	}
	
	@Test
	public void testLocacaoDescontoQuartoFilme50Pc() throws Exception {
		//cenario
		List<Filme> filmes = new ArrayList<Filme>(Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora()));
		Usuario usuario = umUsuario().agora();
				
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(locacao.getValor(), is(13.00));
	}
	
	@Test
	public void testLocacaoDescontoQuintoFilme75Pc() throws Exception {
		//cenario
		List<Filme> filmes = new ArrayList<Filme>(Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora()));
		Usuario usuario = umUsuario().agora();
				
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(locacao.getValor(), is(14.00));
	}
	
	@Test
	public void testLocacaoDescontoSextoFilme100Pc() throws Exception {
		//cenario
		List<Filme> filmes = new ArrayList<Filme>(Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora()));
		Usuario usuario = umUsuario().agora();
				
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(locacao.getValor(), is(14.00));
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		//cenario
		List<Filme> filmes = new ArrayList<Filme>(Arrays.asList(umFilme().agora()));
		Usuario usuario = umUsuario().agora();

		Mockito.doReturn(DataUtils.obterData(29, 4, 2017)).when(service).obterData();
				
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
//		boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
//		Assert.assertTrue(ehSegunda);
//		assertThat(locacao.getDataRetorno(), caiEm(Calendar.WEDNESDAY));
		assertThat(locacao.getDataRetorno(), caiNumaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception{
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuário 2").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

//		exception.expect(LocadoraException.class);
//		exception.expectMessage("Usuário Negativado");
		
		try {
			service.alugarFilme(usuario, filmes);
		//Verificacao
			Assert.fail(); //garante que caso a excessao não seja lançada, um falso positivo não aconteça
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
		}
		
		verify(spc).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
		
		List<Locacao> locacoes = Arrays.asList(
				umLocacao().comUsuario(usuario).atrasado().agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().comUsuario(usuario3).atrasado().agora(),
				umLocacao().comUsuario(usuario3).atrasado().agora());
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		verify(email, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		// verifica no mock email, que serão relizadas exatamente duas execuções do método
		// notificarAtraso passando como parâmetro qualquer instância da classe Usuário
		verify(email).notificarAtraso(usuario);
		verify(email, Mockito.times(2)).notificarAtraso(usuario3);
		verify(email, never()).notificarAtraso(usuario2);
		verifyNoMoreInteractions(email);
		Mockito.verifyZeroInteractions(spc);
	}
	
	@Test
	public void deveTratarErronoSPC() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = asList(umFilme().agora());
		
		//verificação
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catrastrófica"));
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPF, tente novamente");
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		//cenário
		Locacao locacao = umLocacao().agora();
		
		//acao
		service.prorrogarLocacao(locacao, 3);
		
		//verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenário
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		//acao
		Class<LocacaoService> clazz = LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valor = (Double) metodo.invoke(service, filmes);
		
		//verificacao
		Assert.assertThat(valor, is(4.0));
	}
}

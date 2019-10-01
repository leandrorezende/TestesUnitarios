package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devoMostrarDifencaEntreMockSpy() {
		Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod();
//		Mockito.when(calcSpy.somar(1, 3)).thenReturn(8);
		Mockito.doReturn(5).when(calcSpy).somar(1, 2); 
		
		System.out.println("Mock:" + calcMock.somar(1, 2));
		System.out.println("Spy:" + calcMock.somar(1, 2));
		
		System.out.println("Mock");
		calcMock.imprime();
		System.out.println("Spy");
		calcSpy.imprime();
	}
	
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);

		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
//		Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
		Mockito.when(calc.somar(argCapt.capture(), Mockito.anyInt())).thenReturn(5);
		// irá retornar 5 conforme definido no retorno do mock acima
//		Assert.assertEquals(0, calc.somar(44, 2));
		
		// irá retornar 0 pois é o valor default do tipo int, uma vez que o mock não 
		// foi ensinado para retornar uma valor expecífico para esse cenário
		Assert.assertEquals(5, calc.somar(1, 10000));
//		System.out.println(argCapt.getValue());
		System.out.println(argCapt.getAllValues()); //return all captured values from the method call
	}
}

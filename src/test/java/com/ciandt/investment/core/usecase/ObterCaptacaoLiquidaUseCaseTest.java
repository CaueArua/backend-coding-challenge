package com.ciandt.investment.core.usecase;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.ciandt.investment.core.domain.InformeDiario;
import com.ciandt.investment.dataprovider.InformeDiarioGateway;

public class ObterCaptacaoLiquidaUseCaseTest {

    private ObterCaptacaoLiquidaUseCase obterCaptacaoLiquidaUseCase;
    private InformeDiarioBoundary informeDiarioBoundary;

    @Before
    public void setup() {
        informeDiarioBoundary = new InformeDiarioGateway();
        obterCaptacaoLiquidaUseCase = new ObterCaptacaoLiquidaUseCase(informeDiarioBoundary);
    }

    @Test
    public void deveObterOsDadosdeInformesDiario() {

        List<InformeDiario> all = informeDiarioBoundary.getAll();
        Assert.assertEquals(325850, all.size());
    }
    
    @Test
    public void deveObiterApenasDoMesSelecionado() {    	
    	
    	InformeDiarioGateway mockInfo = mock(InformeDiarioGateway.class);
    	when(mockInfo.getAll()).thenReturn(Arrays.asList(
    			new InformeDiario("00.017.024/0001-53;2019-07-01;1131539.72;27.017665200000;1130787.66;0.00;0.00;1"),
    			new InformeDiario("00.017.024/0001-53;2019-07-02;1131818.25;27.022944800000;1131008.63;0.00;0.00;1"),
    			new InformeDiario("00.017.024/0001-53;2019-07-03;1132096.85;27.028226000000;1131229.67;0.00;0.00;1"),
    			new InformeDiario("00.017.024/0001-53;2019-07-04;1132376.50;27.033509000000;1131450.78;0.00;0.00;1"),
    			new InformeDiario("00.017.024/0001-53;2019-07-05;1132361.70;27.038817200000;1131672.95;0.00;0.00;1"),
    			new InformeDiario("00.017.024/0001-53;2019-08-08;1132640.44;27.044101100000;1131894.10;0.00;0.00;1"),
    			new InformeDiario("00.017.024/0001-53;2019-08-09;1132919.25;27.049386700000;1132115.32;0.00;0.00;1"),
    			new InformeDiario("00.017.024/0001-53;2019-09-10;1128929.75;27.054673700000;1129008.05;0.00;3328.55;1"),
    			new InformeDiario("00.017.024/0001-53;2019-09-11;1129207.65;27.059954200000;1129228.41;0.00;0.00;1"),
    			new InformeDiario("00.017.024/0001-53;2019-10-12;1129486.59;27.065235900000;1129448.82;0.00;0.00;1")
    	));
    	
    	ObterCaptacaoLiquidaUseCase useCese = new ObterCaptacaoLiquidaUseCase(mockInfo);    	
    	
    	List<InformeDiario> julyList =  useCese.filterByMonth(Month.JULY, mockInfo.getAll());
    	List<InformeDiario> augustList =  useCese.filterByMonth(Month.AUGUST, mockInfo.getAll());
    	List<InformeDiario> decemberList =  useCese.filterByMonth(Month.DECEMBER, mockInfo.getAll());    	
    	
    	Assert.assertEquals(5, julyList.size());
    	Assert.assertEquals(2, augustList.size());
    	Assert.assertEquals(0, decemberList.size());    	
    }
    
    
    @Test
    public void deveAgruparPeloCnpj() {    	
    	
    	InformeDiarioGateway mockInfo = mock(InformeDiarioGateway.class);
    	when(mockInfo.getAll()).thenReturn(Arrays.asList(
			new InformeDiario("00.017.024/0001-53;2019-07-01;1131539.72;27.017665200000;1130787.66;0.00;0.00;1"),
			new InformeDiario("00.017.024/0001-53;2019-07-02;1131818.25;27.022944800000;1131008.63;0.00;0.00;1"),
			new InformeDiario("00.017.024/0001-53;2019-07-03;1132096.85;27.028226000000;1131229.67;0.00;0.00;1"),
			new InformeDiario("00.089.915/0001-15;2019-07-03;14730724.46;77.509613600000;14736858.62;473694.52;53879.21;205"),
			new InformeDiario("00.089.915/0001-15;2019-07-04;14715334.80;77.518676700000;14719476.73;219112.11;238217.17;204"),
			new InformeDiario("00.089.915/0001-15;2019-07-05;14877518.84;77.527950200000;14881416.79;304410.50;144231.30;204"),
			new InformeDiario("00.102.322/0001-41;2019-07-15;106943229.99;11.254737300000;106945206.48;0.00;0.00;1"),
			new InformeDiario("00.102.322/0001-41;2019-07-17;109978847.21;11.573947900000;109978422.19;0.00;0.00;1"),
			new InformeDiario("00.102.322/0001-41;2019-07-18;113408895.53;11.934791400000;113407242.94;0.00;0.00;1"),
			new InformeDiario("00.102.322/0001-41;2019-07-19;111347516.34;11.717733600000;111344707.60;0.00;0.00;1")    			
    	));    	
    	ObterCaptacaoLiquidaUseCase useCese = new ObterCaptacaoLiquidaUseCase(mockInfo); 
    	
    	Map<String, List<InformeDiario>> map = useCese.groupByCnpj(mockInfo.getAll());
    	    	
    	Assert.assertFalse(map.containsKey("00.211.294/0001-09"));
    	Assert.assertTrue(map.containsKey("00.017.024/0001-53"));
    	Assert.assertTrue(map.containsKey("00.102.322/0001-41"));    	
    	Assert.assertEquals(3, map.size());
     	Assert.assertEquals(3, map.get("00.017.024/0001-53").size());
     	Assert.assertEquals(4, map.get("00.102.322/0001-41").size());    	        	 
    }
    
    
    @Test
    public void deveSomarACaptacaoLiquida() {  
    	InformeDiarioGateway mockInfo = mock(InformeDiarioGateway.class);
    	when(mockInfo.getAll()).thenReturn(Arrays.asList(
			new InformeDiario("00.068.305/0001-35;2019-07-01;57719796.26;26.590131000000;57696078.76;11451.00;42676.35;7627"),
			new InformeDiario("00.068.305/0001-35;2019-07-02;57691679.26;26.594809000000;57680694.48;37662.24;63196.68;7626"),
			new InformeDiario("00.068.305/0001-35;2019-07-03;57659965.10;26.599947000000;57645591.30;32815.00;79061.74;7612"),
			new InformeDiario("00.068.305/0001-35;2019-07-04;57591376.81;26.604941000000;57573531.68;15467.00;98348.73;7611"),
			new InformeDiario("00.068.305/0001-35;2019-07-05;57529194.13;26.609177000000;57507942.01;13350.59;88106.65;7607"),
			new InformeDiario("00.068.305/0001-35;2019-07-08;57547527.95;26.614965000000;57522968.77;36741.27;34225.14;7606"),
			new InformeDiario("00.068.305/0001-35;2019-07-09;57529537.58;26.619936000000;57517607.20;78350.00;94453.65;7600"),
			new InformeDiario("00.068.305/0001-35;2019-07-10;57362141.76;26.624957000000;57349588.19;57550.31;236418.37;7594"),
			new InformeDiario("00.068.305/0001-35;2019-07-11;57287298.00;26.630061000000;57271246.05;69945.00;159280.51;7589"),
			new InformeDiario("00.068.305/0001-35;2019-07-12;57265439.29;26.634907000000;57246084.47;1025.00;36608.44;7589"),
			new InformeDiario("00.068.305/0001-35;2019-07-15;57184007.27;26.639889000000;57161297.86;29264.99;124759.35;7583"),
			new InformeDiario("00.068.305/0001-35;2019-07-16;57194900.66;26.644870000000;57184896.93;25250.00;12340.28;7578"),
			new InformeDiario("00.068.305/0001-35;2019-07-17;57237757.81;26.649638000000;57224454.62;60451.58;31126.02;7580"),
			new InformeDiario("00.068.305/0001-35;2019-07-18;57316412.11;26.655079000000;57299824.88;88459.10;24772.75;7581"),
			new InformeDiario("00.068.305/0001-35;2019-07-19;57389002.02;26.660005000000;57369703.28;125610.33;66321.44;7586"),
			new InformeDiario("00.068.305/0001-35;2019-07-22;57334134.83;26.665007000000;57311498.70;33866.89;102834.21;7592"),
			new InformeDiario("00.068.305/0001-35;2019-07-23;57779350.21;26.670175000000;57769330.78;465257.87;18533.08;7595"),
			new InformeDiario("00.068.305/0001-35;2019-07-24;57802954.68;26.674924000000;57789621.11;78844.09;68841.51;7596"),
			new InformeDiario("00.068.305/0001-35;2019-07-25;57793893.01;26.679605000000;57777137.62;25802.74;48427.66;7599"),
			new InformeDiario("00.068.305/0001-35;2019-07-26;57795398.27;26.684710000000;57775271.58;45175.36;58096.26;7598"),
			new InformeDiario("00.068.305/0001-35;2019-07-29;57877631.96;26.689724000000;57854195.88;99022.35;30953.16;7597")
    	));
    	    	
    	BigDecimal soma = obterCaptacaoLiquidaUseCase.sumCaptacaoLiquida(mockInfo.getAll());
    	Assert.assertEquals(new BigDecimal("-88019.27"), soma); 
    }
    
    @Test
    public void deveRetornarAsDezMaioresCaptacoesDoMes() {
    	
    	List<String> top = obterCaptacaoLiquidaUseCase.getTopCaptacaoLiquida(10, Month.JULY);
    	List<String> ret = Arrays.asList(
    		"14.933.427/0001-57",
    		"10.347.224/0001-28",
    		"14.508.605/0001-00",
    		"02.266.145/0001-64",
    		"01.608.573/0001-65",
    		"05.170.419/0001-05",
    		"31.341.341/0001-54",
    		"32.862.118/0001-15",
    		"30.493.995/0001-30",
    		"32.862.087/0001-00");    	    	
    	Assert.assertEquals(10, top.size());
    	Assert.assertEquals(ret, top);
    }
    
    
    
    @Test
    public void deveRetornarAsMaioresCaptacoesDoMes() {
    	    	
    	InformeDiarioGateway mockInfo = mock(InformeDiarioGateway.class);
    	when(mockInfo.getAll()).thenReturn(Arrays.asList(
    			// empresa A captacao : Julho = +10; Agosto =  +40
    	        new InformeDiario("00.068.305/0001-35;2019-07-01;0;0;0;50;60;0"),  // -10
    	        new InformeDiario("00.068.305/0001-35;2019-07-02;0;0;0;60;30;0"),  // +30
    	        new InformeDiario("00.068.305/0001-35;2019-07-03;0;0;0;90;100;0"), // -10
    	        new InformeDiario("00.068.305/0001-35;2019-08-01;0;0;0;20;0;0"),  // + 20 
    	        new InformeDiario("00.068.305/0001-35;2019-08-02;0;0;0;40;20;0"), // +20
    	        
    	        // empresa B captacao : Julho = -20; Agosto =  +20
    	        new InformeDiario("00.089.915/0001-15;2019-07-01;0;0;0;60;90;0"), // -30
    	        new InformeDiario("00.089.915/0001-15;2019-07-02;0;0;0;40;40;0"), // 0
    	        new InformeDiario("00.089.915/0001-15;2019-07-03;0;0;0;10;0;0"), // +10               
    	        new InformeDiario("00.089.915/0001-15;2019-08-04;0;0;0;100;80;0"), // +20
    	        new InformeDiario("00.089.915/0001-15;2019-08-05;0;0;0;60;50;0"), // +10
    	        
    	        // empresa C captacao : Julho = +8; Agosto =  -80
    	        new InformeDiario("00.222.725/0001-24;2019-07-01;0;0;0;8;0;0"), // +8
    	        new InformeDiario("00.222.725/0001-24;2019-07-03;0;0;0;100;100;0"), // +10                
    	        new InformeDiario("00.222.725/0001-24;2019-08-04;0;0;0;80;90;0"), // -10
    	        new InformeDiario("00.222.725/0001-24;2019-08-05;0;0;0;10;80;0"), // -70      
    	        
    	        // empresa D captacao : Julho = -20; Agosto =  -40
    	        new InformeDiario("00.306.278/0001-91;2019-07-01;0;0;0;60;90;0"), // -30
    	        new InformeDiario("00.306.278/0001-91;2019-07-03;0;0;0;10;0;0"), // +10               
    	        new InformeDiario("00.306.278/0001-91;2019-08-04;0;0;0;80;100;0"), // -20
    	        new InformeDiario("00.306.278/0001-91;2019-08-05;0;0;0;120;140;0") // -20
    	));  	
    	
    	ObterCaptacaoLiquidaUseCase useCese = new ObterCaptacaoLiquidaUseCase(mockInfo); 
    	List<String> top07 = useCese.getTopCaptacaoLiquida(2, Month.JULY);
    	List<String> top08 = useCese.getTopCaptacaoLiquida(3, Month.AUGUST);
    	      	
    	Assert.assertEquals(2, top07.size());
    	Assert.assertEquals(3, top08.size());
    	Assert.assertEquals(Arrays.asList("00.068.305/0001-35","00.222.725/0001-24"), top07);
    	Assert.assertEquals(Arrays.asList("00.068.305/0001-35","00.089.915/0001-15","00.306.278/0001-91"), top08);
    	
    }
    

}
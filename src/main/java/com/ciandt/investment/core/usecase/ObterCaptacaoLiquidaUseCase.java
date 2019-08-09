package com.ciandt.investment.core.usecase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.ciandt.investment.core.domain.InformeDiario;

public class ObterCaptacaoLiquidaUseCase {

    private final InformeDiarioBoundary informeDiarioBoundary;

    public ObterCaptacaoLiquidaUseCase(InformeDiarioBoundary informeDiarioBoundary) {
        this.informeDiarioBoundary = informeDiarioBoundary;
    }

    /***
     * Retorna A lista com o nome dos X fundos com a maior captação liquida no mes
     * @param top Nº de fundos a ser retornado
     * @param month mes a ser analizado
     * @return retorna uma lista com o Cnpj dos X fundos com maior captação liquida
     */
    public List<String> getTopCaptacaoLiquida(int top, Month month) {
    	List<InformeDiario> getAll = this.informeDiarioBoundary.getAll();
    	List<InformeDiario> list = filterByMonth(month, getAll);
    	Map<String, List<InformeDiario>> grupedMap = groupByCnpj(list);
    	
    	Map<String, BigDecimal> collect = grupedMap
			.entrySet()
			.parallelStream()
			.collect(Collectors.toMap(
				entry -> entry.getKey(),
				entry -> sumCaptacaoLiquida(entry.getValue()))
			);
    	
    	return collect
    		.entrySet()
    		.parallelStream()
	    	 .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
	    	 .limit(top)
	    	 .map(Entry::getKey)
	    	 .collect(Collectors.toList());    		
    }
    
    
    /***
     * Retorna um lista de InformeDiario contendo apenas os informes do mes especificado
     * @param month Mês do ano
     * @param Lista lista de informe diario a ser filtrada
     * @return Lista contendo apenas os informe do mes selecionado
     */
    public List<InformeDiario> filterByMonth(Month month, List<InformeDiario> list){
    	return list.parallelStream()
    		.filter(info -> 
    			info.getDataCompetencia()
    				.getMonth()
    				.equals(month)
    		)
    		.collect(Collectors.toList());
    }
        
    /***
     * Retorna um mapa Agrupado por cnpj
     * @param list Lista a ser agrupada
     * @return retorna um Map<String, List<InformeDiario>> Agrupando os InformeDiario por cnpj
     */
    public Map<String, List<InformeDiario>> groupByCnpj(List<InformeDiario> list){
    	return list
			.parallelStream()
			.collect(Collectors.groupingBy(InformeDiario::getCnpj));    	    	
	}
    
    /***
     * Soma a captação liquida da lista infornada
     * @param lista lista a ser somada
     * @return Retorna a soma da captação liquida da lista;
     */
    public BigDecimal sumCaptacaoLiquida(List<InformeDiario> lista) {
    	return lista.parallelStream()
			.map(InformeDiario::getCaptacaoLiquidaDia)
			.reduce(BigDecimal.ZERO, BigDecimal::add);    	
    }
    
    

}

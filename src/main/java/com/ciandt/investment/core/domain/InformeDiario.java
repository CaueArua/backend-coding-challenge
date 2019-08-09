package com.ciandt.investment.core.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InformeDiario {

    private static final int CNPJ_FUNDO = 0;
    private static final int DT_COMPTC = 1;
    private static final int VL_TOTAL = 2;
    private static final int VL_QUOTA = 3;
    private static final int VL_PATRIM_LIQ = 4;
    private static final int CAPTC_DIA = 5;
    private static final int RESG_DIA = 6;
    private static final int NR_COTST = 7;

    private String cnpj;
    private LocalDate dataCompetencia;
    private BigDecimal valorTotal;
    private BigDecimal valorCota;
    private BigDecimal valorPatrimonioLiquido;
    private BigDecimal captacaoDia;
    private BigDecimal resgateDia;
    private Integer numeroCotistas;

    public InformeDiario(final String csvLine) {

        final String[] line = csvLine.split(";");

        this.cnpj = line[CNPJ_FUNDO];
        this.dataCompetencia = LocalDate.parse(line[DT_COMPTC],DateTimeFormatter.ISO_DATE);

        this.valorTotal = BigDecimal.valueOf(new Double(line[VL_TOTAL]));
        this.valorCota = BigDecimal.valueOf(new Double(line[VL_QUOTA]));
        this.valorPatrimonioLiquido = BigDecimal.valueOf(new Double(line[VL_PATRIM_LIQ]));

        this.captacaoDia = BigDecimal.valueOf(new Double(line[CAPTC_DIA]));
        this.resgateDia = BigDecimal.valueOf(new Double(line[RESG_DIA]));

        this.numeroCotistas = new Integer(line[NR_COTST]);
    }

    public String getCnpj() {
        return cnpj;
    }

    public LocalDate getDataCompetencia() {
        return dataCompetencia;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public BigDecimal getValorCota() {
        return valorCota;
    }

    public BigDecimal getValorPatrimonioLiquido() {
        return valorPatrimonioLiquido;
    }

    public BigDecimal getCaptacaoDia() {
        return captacaoDia;
    }

    public BigDecimal getResgateDia() {
        return resgateDia;
    }

    public Integer getNumeroCotistas() {
        return numeroCotistas;
    }
    
    public BigDecimal getCaptacaoLiquidaDia() {
    	return captacaoDia.subtract(resgateDia);
    }
}

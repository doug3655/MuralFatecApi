package br.com.MuralFatecApi.DTO;

import lombok.Data;

@Data
public class DadosPDFVinculo {
	private Grupo grupo;
	private String periodo;
	private String curso;
	private String orientador;
}
package br.com.MuralFatecApi.DTO;
import java.util.List;
import lombok.Data;

@Data
public class Grupo {
	
	private int ID_GRUPO;
	private String NM_TEMA;
	private int ID_ORIENTADOR;
	private Curso curso;
	private Periodo periodo;
	private Status status;
	private Status statusVinculo;
	private List<Usuario> Alunos;
}
package br.com.MuralFatecApi.DTO;
import java.util.List;

import lombok.Data;

@Data
public class Grupo {
	
	private Integer id_grupo;
	private String nm_tema;
	private Integer id_orientador;
	private Integer id_tp_curso;
	private Integer id_tp_periodo;
	private Integer id_tp_status;
	private Integer id_tp_status_vinculo;
	private Boolean fl_tg1;
	private Boolean fl_tg2;
	private List<Aluno> Alunos;
}
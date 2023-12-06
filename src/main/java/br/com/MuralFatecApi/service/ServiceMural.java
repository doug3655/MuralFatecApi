package br.com.MuralFatecApi.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.google.gson.Gson;
import com.lowagie.text.DocumentException;

import br.com.MuralFatecApi.DTO.DadosPDFVinculo;
import br.com.MuralFatecApi.DTO.Grupo;
import br.com.MuralFatecApi.DTO.Notificacao;
import br.com.MuralFatecApi.DTO.Usuario;

@Service
public class ServiceMural {

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceBusca serviceBusca;
	
	public File criaPdfVinculoTg(Integer idGrupo, Boolean isPrimeiroVinculo) throws IOException, DocumentException {
		Context context = new Context();
        String html = "";
        DadosPDFVinculo dados = serviceBusca.buscaDadosGrupoPDF(idGrupo);
        LocalDate hoje = LocalDate.now(ZoneId.of("GMT-3")) ;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu") ;
        String dataHoje = hoje.format(formato) ;
        context.setVariable("dados", dados);
        context.setVariable("isPrimeiroVinculo", isPrimeiroVinculo);
        context.setVariable("dataHoje", dataHoje);
        html = loadAndFillTemplate("VinculoTG",context);
        return renderPdfVinculoTg(html);
	}
	
	private String loadAndFillTemplate(String templapeNome,Context context) {
		return templateEngine.process(templapeNome, context);
	}
	
	private File renderPdfVinculoTg(String html) throws IOException, DocumentException {
		File file = File.createTempFile("students", ".pdf");
		OutputStream outputStream = new FileOutputStream(file);
		ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
		renderer.setDocumentFromString(html, new ClassPathResource("/templates/").getURL().toExternalForm());
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
	}
	
	public Usuario realizaLogin(String email,String senha) throws Exception{
		Usuario result = new Usuario();
		try {
			String sql = "SELECT TOP 1 tu.ID_USUARIO,tu.NM_USUARIO,tu.NR_RA,tu.NM_TELEFONE,tu.NM_EMAIL,tu.ID_TP_PERFIL_USUARIO FROM muraldb.dbo.TB_USUARIO tu WHERE tu.NM_EMAIL = ? AND tu.NM_SENHA = ? AND tu.ID_TP_STATUS = 1 ORDER BY tu.ID_USUARIO DESC";
			result = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Usuario>(Usuario.class),email,senha);
		}catch(Exception e) {
			System.out.println("Erro na execução da query do realizaLogin:"+e.getMessage());
		}
		return result;
	}
	
	public boolean alterarDadosUsuario(Usuario usuario,Integer tipo) throws Exception {
		return tipo==1?alterarSenha(usuario):alterarTodosDadosUsuario(usuario);
	}
	
	private boolean alterarSenha(Usuario usuario) throws Exception{
		boolean returno = true;
		try {
			String sql = "UPDATE muraldb.dbo.TB_USUARIO SET NM_SENHA=? WHERE ID_USUARIO=?";
			jdbcTemplate.update(sql, usuario.getNm_senha(),usuario.getId_usuario());
		}catch(Exception e) {
			System.out.println("Erro na execução da query do alterarSenha:"+e.getMessage());
			returno = false;
		}
		return returno;
	}
	
	private boolean alterarTodosDadosUsuario(Usuario usuario) throws Exception{
		boolean retorno = true;
		try {
			String sql = "UPDATE muraldb.dbo.TB_USUARIO	SET NM_USUARIO=?,NR_RA=?,NM_EMAIL=?,NM_TELEFONE=?,NM_SENHA=? WHERE ID_USUARIO=?";
			jdbcTemplate.update(sql,usuario.getNm_usuario(),usuario.getNr_ra(),usuario.getNm_email(),usuario.getNm_telefone(),usuario.getNm_senha(),usuario.getId_usuario());
		}catch(Exception e) {
			System.out.println("Erro na execução da query do alterarTodosDadosUsuario:"+e.getMessage());
			retorno = false;
		}
		return retorno;
	}
	
	public boolean alterarTodosDadosGrupo(Grupo grupo) throws Exception{
		boolean retorno = true;
		try {
			String sql = "UPDATE muraldb.dbo.TB_GRUPO SET NM_TEMA=?,ID_ORIENTADOR=?,ID_TP_CURSO=?,ID_TP_PERIODO=?,FL_TG1=?,FL_TG2=? WHERE ID_GRUPO=?";
			jdbcTemplate.update(sql,grupo.getNm_tema(),grupo.getId_orientador(),grupo.getId_tp_curso(),grupo.getId_tp_periodo(),grupo.getFl_tg1(),grupo.getFl_tg2(),grupo.getId_grupo());
		}catch(Exception e) {
			System.out.println("Erro na execução da query do alterarTodosDadosGrupo:"+e.getMessage());
			retorno = false;
		}
		return retorno;
	}
	
	public Integer alunoSairGrupo(Integer idAluno,Integer idGrupo) throws Exception{
		Integer alunos = -1;
		try {
			String sql = "UPDATE muraldb.dbo.TB_GRUPO_COMPONENTE SET ID_TP_STATUS=4	WHERE ID_USUARIO=? AND ID_GRUPO=?";
			jdbcTemplate.update(sql,idAluno,idGrupo);
			String sqlDadosGrupo = "SELECT count(tgc.ID_USUARIO) FROM muraldb.dbo.TB_GRUPO_COMPONENTE tgc WHERE tgc.ID_TP_STATUS = 4 AND tgc.ID_GRUPO = ?";
			alunos =  jdbcTemplate.queryForObject(sqlDadosGrupo,Integer.class, idGrupo);
			if(alunos==0){
				String sqlEncerrarGrupo = "UPDATE muraldb.dbo.TB_GRUPO SET ID_TP_STATUS=4 WHERE ID_GRUPO=?";
				jdbcTemplate.update(sqlEncerrarGrupo,idGrupo);
			}
		}catch(Exception e) {
			alunos = -1;
			e.printStackTrace();
			System.out.println("Erro na execução da query do alterarTodosDadosGrupo:"+e.getMessage());
		}
		return alunos;
	}
	
	public boolean resolverNotificacao(Notificacao notificacao,Integer tpPerfil) {
		boolean retorno = true;
		try {
			String sql = "UPDATE muraldb.dbo.TB_NOTIFICACAO SET ID_TP_STATUS=? WHERE ID_NOTIFICACAO=?";
			jdbcTemplate.update(sql,notificacao.getId_tp_status(),notificacao.getId_notificacao());
			switch(notificacao.getId_tp_notificacao()) {
				case 1:
					String sqlUsuario = "UPDATE muraldb.dbo.TB_USUARIO SET ID_TP_STATUS=?,ID_TP_PERFIL_USUARIO=? WHERE ID_USUARIO=?";
					jdbcTemplate.update(sqlUsuario,notificacao.getId_tp_status(),tpPerfil,notificacao.getNr_entidade_alvo());
					break;
				case 2:
					String sqlGrupo = "UPDATE muraldb.dbo.TB_GRUPO SET ID_TP_STATUS=? WHERE ID_GRUPO=?";
					jdbcTemplate.update(sqlGrupo,notificacao.getId_tp_status(),notificacao.getNr_entidade_alvo());
					break;
				case 3:
					String sqlVinculo = "UPDATE muraldb.dbo.TB_GRUPO SET ID_TP_STATUS_VINCULO=? WHERE ID_GRUPO=?";
					jdbcTemplate.update(sqlVinculo,notificacao.getId_tp_status(),notificacao.getNr_entidade_alvo());
					break;
				default:
					System.out.println("Tipo de notificação não reconhecido");
					retorno = false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Erro na execução da query do resolverNotificacao:"+e.getMessage());
			retorno = false;
		}
		return retorno;
	}

	public void teste(String email,String senha) {
		//Varias linhas
		String sql = "SELECT tu.ID_USUARIO,tu.NM_USUARIO,tu.NR_RA,tu.NM_EMAIL,tu.NM_EMAIL,tu.ID_TP_PERFIL_USUARIO FROM muraldb.dbo.TB_USUARIO tu";
		List<Usuario> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Usuario>(Usuario.class));
		System.out.println(new Gson().toJson(result));
	}
}
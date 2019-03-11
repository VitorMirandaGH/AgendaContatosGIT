package br.senai.sp.agendacontatos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contato;

public class CadastroContatoActivity extends AppCompatActivity{

    private Contato contatoAtualizar;

    private CadastroContatoHelper helper;

    // No estado onCreate() decide se irá preencher a tela de cadastro com um item que foi clicado anteriormente ou não
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_contato);

        helper = new CadastroContatoHelper(this);

        Intent intent = getIntent();
        contatoAtualizar = (Contato)intent.getSerializableExtra("contato");
        // Se o contato estiver com valores quando a tela de cadastro for criada, então deve ser preenchida a tela com as infos do contato em questão
        if(contatoAtualizar != null){
            helper.preencherFormulario(contatoAtualizar);
        }

    }

    // Adiciona o menu com o botão salvar na activity de cadastro de contatos
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cadastro_contatos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Ao selecionar o botão "Salvar" executa o comando necessário
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_salvar:
                Contato contato = helper.getContato();
                ContatoDAO dao = new ContatoDAO(this);

                // Se o contato for nulo, ele não existe, e é então executado o comando de salvar, senão, ele atualiza o mesmo cadastro
                if(contatoAtualizar == null){

                    if (helper.validacao()) {
                        dao.salvar(contato);
                        dao.close();
                        finish();
                        Toast.makeText(this, contato.getNome() + " salvo com sucesso", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    int cod = contatoAtualizar.getCod();
                    contatoAtualizar = helper.getContato();
                    contatoAtualizar.setCod(cod);

                    if(helper.validacao()){
                        dao.atualizar(contatoAtualizar);
                        dao.close();
                        finish();
                        Toast.makeText(this, contatoAtualizar.getNome() + " atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            default:
                Toast.makeText(CadastroContatoActivity.this, "Nada aconteceu", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}

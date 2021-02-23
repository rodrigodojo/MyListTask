package com.dojo.mylisttask;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private EditText meuTexto;
    private ListView minhaLista;
    private Button meuBotao;

    private SQLiteDatabase bancoDados;

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<Integer> ids;
    private ArrayList<String> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meuTexto = findViewById(R.id.editText);
        meuBotao = findViewById(R.id.buttonit);
        minhaLista = findViewById(R.id.listView);

        carregaTarefa();

        minhaLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                apagarTarefas(ids.get(position));
                return false;
            }
        });

        meuBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarNovaTarefa(meuTexto.getText().toString());
            }
        });
    }

    private void carregaTarefa() {
        try {
            bancoDados = openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS minhastabelas(id INTEGER PRIMARY KEY AUTOINCREMENT, tafera VARCHAR)");

            //Cursor cursor = bancoDados.rawQuery("SELECT * FROM minhastabelas", null);
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM minhastabelas ORDER BY id DESC", null);

            int indiceColunaID = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tafera");

            itens = new ArrayList<String>();
            ids= new ArrayList<Integer>();

            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2,android.R.id.text1,itens);
            minhaLista.setAdapter(itensAdaptador);

            cursor.moveToFirst();
            while (cursor != null) {
                Log.i("LogX", "ID" + cursor.getString(indiceColunaID) + "tafera:" + cursor.getString(indiceColunaTarefa));
                itens.add(cursor.getString(indiceColunaTarefa));
                ids.add(Integer.parseInt(cursor.getString(indiceColunaID)));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionarNovaTarefa(String novaTarefa){
        try{
            if(novaTarefa.equals("")){
                Toast.makeText(MainActivity.this,"Insira uma tarefa!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"Tarefa "+novaTarefa+" inserida!",Toast.LENGTH_SHORT).show();
                meuTexto.setText("");
                bancoDados.execSQL("INSERT INTO minhastabelas(tafera) VALUES('" + novaTarefa + "')");
                carregaTarefa();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void apagarTarefas(Integer id){
        try{
            bancoDados.execSQL("DELETE FROM minhastabelas WHERE id="+id);
            Toast.makeText(MainActivity.this,"Tarefa Removida!",Toast.LENGTH_SHORT).show();
            carregaTarefa();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
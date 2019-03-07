package com.android.firebaseapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    /* Referencia para o banco de dados
    *  Ao utilizar o método getInstance estamos recuperando a instancia do firebase
    *  utilizada para salvar os dados
    *  getReference -> volta para o nó raiz do Firebase
    * */

    /*private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuario = FirebaseAuth.getInstance();*/

    private ImageView imageFoto;
    private Button buttonUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonUpload = findViewById(R.id.buttonUploadId);
        imageFoto = findViewById(R.id.imageFotoId);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //configuração para imagem ser salva em memória
                imageFoto.setDrawingCacheEnabled(true);
                //construindo imagem para a mamória
                imageFoto.buildDrawingCache();

                //recupera bitmap da imagem (imagem a ser carregada)
                Bitmap bitmap = imageFoto.getDrawingCache();

                //comprimindo bitmap para formato png/jpg
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //baos é uma representação dos dados que nós poderemos fazer o upload para o firebase
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos );


                //converte o baos para pixel bruto em uma matriz de bytes
                //(dados da imagens)
                byte[] dadosImagem = baos.toByteArray();

                //define nós para o storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imagens = storageReference.child("imagens");

                //apagar imagem
                /*StorageReference imagemRef = imagens.child( "celular.jpeg");

                imagemRef.delete().addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Erro ao deletar o arquivo" +
                                e.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Sucesso ao deletar o arquivo", Toast.LENGTH_LONG).show();
                    }
                });*/

                //nome da imagem
                //String nomeArquivo = UUID.randomUUID().toString();

                //imageRef irá criar uma pasta chamada imagens e dentro terá um arquivo chamado celular.jpeg
                //StorageReference imagemRef = imagens.child( nomeArquivo + ".jpeg");
                StorageReference imagemRef = imagens.child( "celular.jpeg");

                Glide.with(MainActivity.this)
                        .using(new FirebaseImageLoader())
                        .load( imagemRef )
                        .into( imageFoto);

                //retorna o objeto que irá controlar o upload
                /*UploadTask uploadTask = imagemRef.putBytes( dadosImagem );

                //tratando falhas
                uploadTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "Upload da imagem falhou" +
                                        e.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //taskSnapshot é o nosso parametro de retorno
                        Uri url = taskSnapshot.getDownloadUrl();
                        Toast.makeText(MainActivity.this, "Sucesso ao fazer o upload da imagem" + url.toString(),
                                Toast.LENGTH_LONG).show();

                    }
                });*/




            }
        });

        //permite que crie um filho para a referência
        //DatabaseReference usuarios = reference.child( "usuarios" );
        //DatabaseReference produtos = reference.child( "produtos" );

        //filtros
        //DatabaseReference usuarioPesquisa = usuarios.child("-L_J8n24eD0PrY6b1Vw7");
        //Query usuarioPesquisa = usuarios.orderByChild("nome").equalTo("Caroline");
        //Query usuarioPesquisa = usuarios.orderByKey().limitToLast(2);
        //Query usuarioPesquisa = usuarios.orderByKey().limitToFirst(2);

        //maior ou igual(>=) / startAt -> valor inicial que desejamos filtrar
        //Query usuarioPesquisa = usuarios.orderByChild("idade").startAt(40);

        //menor ou igual (<=) / endAt -> valor maximo a ser filtrado
        //Query usuarioPesquisa = usuarios.orderByChild("idade").endAt(30);

        //entre dois valores
        /*Query usuarioPesquisa = usuarios.orderByChild("idade")
                                .startAt(26)
                                .endAt(30);*/

        //filtrar palavras
        //"\uf8ff" esse bloco de código faz com que o firebase consiga interpretar a letra como ela realmente é
        /*Query usuarioPesquisa = usuarios.orderByChild("nome")
                .startAt("A")
                .endAt("D" + "\uf8ff");*/


        /*usuarioPesquisa.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //com getValue passamos a classe que queremos recuperar
                Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                Log.i("Dados Usuário","nome: " + dadosUsuario.getNome());

                Log.i("Dados Usuário", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        //salvando dados no firebase com identificador unico automático
        /* Usuario usuario = new Usuario();
        usuario.setNome("Jessica");
        usuario.setSobrenome("Cordasso");
        usuario.setIdade(28);
        usuarios.push().setValue( usuario );*/


        //salvando dados no firebase com identificador unico manual
        /*Usuario usuario = new Usuario();
        usuario.setNome("Alberto");
        usuario.setSobrenome("Amparo");
        usuario.setIdade(25);
        usuarios.child("002").setValue( usuario ); */

        //salvando dados no firebase
        /* Produto produto= new Produto();
        produto.setDescrição("Acer Aspire");
        produto.setMarca("Acer");
        produto.setPreco(1999.99);
        produtos.child("002").setValue( produto ); */

        //recuperar dados d0 firebase
        /*usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("FIREBASE", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        //Cadastro de usuário
        /*usuario.createUserWithEmailAndPassword("borges_cah@hotmail.com", "2345678")
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful()){
                            Log.i("CreateUser", "Sucesso ao cadastrar usuário");
                        }else{
                            Log.i("CreateUser", task.getException().getMessage());
                        }
                    }
                });*/

        //verifica usuário logado
        /*if (usuario.getCurrentUser() != null){
            Log.i("CreateUser", "Usuario logado");
        }else{
            Log.i("CreateUser", "Usuário não esta logado");
        }*/

        //deslogar usuário
        /*usuario.signOut();*/

        //logar usuario
        /* usuario.signInWithEmailAndPassword("borges_cah@hotmail.com", "2345678")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful()){
                            Log.i("signIn", "Sucesso ao logar usuário");
                        }else{
                            Log.i("signIn", "Falha ao logar usuário");
                        }
                    }
                });*/


    }
}

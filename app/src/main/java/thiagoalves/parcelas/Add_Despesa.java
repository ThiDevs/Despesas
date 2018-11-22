package thiagoalves.parcelas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Add_Despesa extends AppCompatActivity {
    Float Parcela;
    static String email;
    static String i;
    EditText descricao;
    EditText valor;
    EditText parcela;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__despesa);
        intent = new Intent(this, MainActivity.class);
        email = LoginActivity.getEmail();
        i = String.valueOf(MainActivity.getInitiMember())+";"+String.valueOf(MainActivity.getInitAno());

        descricao = findViewById(R.id.editText);
        valor = findViewById(R.id.editText2);
        parcela = findViewById(R.id.editText3);

        Button salvar = findViewById(R.id.button);
        salvar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();


                        DocumentReference docRef = db.collection(email).document(String.valueOf(i));
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    Map<String,Object> user = new ArrayMap<>();

                                    List<Integer>array = new ArrayList<>();
                                    array.add(Integer.valueOf(valor.getText().toString()));
                                    array.add(Integer.valueOf(parcela.getText().toString()));
                                    user.put(descricao.getText().toString(),array);
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    assert document != null;
                                    if (document.exists()) {
                                        db.collection(email).document(i).update(user);
                                    } else {
                                        db.collection(email).document(i).set(user);
                                    }
                                } else {
                                    Log.d("Carai", "get failed with ", task.getException());
                                }
                            }
                        });


                        startActivity(intent);


                    }
                }
        );
    }
    public void setParcela(Float Parcela){
        this.Parcela = Parcela;
    }
    public Float getParcela(){
        return Parcela;
    }
}

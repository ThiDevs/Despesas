package thiagoalves.parcelas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    int month;
    int ano;
    Button mes;
    String[] Mes = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
    CardsAdapter cardsAdapter;
    double total = 0.00;
    TextView valor_total;
    ListView lvCards;
    List<CardModel> cards;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Add) {
            Intent intent = new Intent(this, Add_Despesa.class);
            setInitMember(month);
            setInitAno(ano);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        valor_total = findViewById(R.id.textView3);
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        month = localDate.getMonthValue() -1 ;
        ano =  localDate.getYear();
        lvCards = findViewById(R.id.lv);
        cards = new ArrayList<>();
        //* CardView *//

        cardsAdapter = new CardsAdapter(this);

        GetInfoFromMonthandYear(month, ano);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                //List<CardModel> Cards =
                //  for (CardModel card : Cards){
                 //   cardsAdapter.add(cards);
                   // lvCards.setAdapter(cardsAdapter);
                //}

            }
        });





        lvCards.setAdapter(cardsAdapter);

        lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }});


        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection(LoginActivity.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                try{
                                    Map<String,Object> data = document.getData();
                                    for (Object array : data.values()){
                                        List<Integer> inte = (List<Integer>) array;

                                        total += Double.valueOf(String.valueOf(inte.get(0)).replaceAll(" ",""));
                                        Log.d("CardView", String.valueOf(total));
                                    }

                                } catch (Exception ignored){}
                            }
                        } else {
                            Log.d("CardView", "Error getting documents: ", task.getException());
                        }
                        valor_total.setText(String.valueOf(total));
                    }

                });




        //* CardView *//

        //*   Date  *//
        mes = findViewById(R.id.mes);
        Button esquerda = findViewById(R.id.esquerda);
        Button direita = findViewById(R.id.direita);

        mes.setText(Mes[month].concat(", " + String.valueOf(ano)));

        direita.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        month += 1;
                        try {
                            mes.setText(Mes[month].concat(", " + String.valueOf(ano)));
                        } catch (Exception e){
                            month = 0;
                            ano += 1;
                            mes.setText(Mes[month].concat(", " + String.valueOf(ano)));
                        }
                        GetInfoFromMonthandYear(month, ano);
                    }
                }
        );

        esquerda.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        month -= 1;
                        try {
                            mes.setText(Mes[month].concat(", " + String.valueOf(ano)));
                        } catch (Exception e){
                            month = 11;
                            ano -= 1;
                            mes.setText(Mes[month].concat(", " + String.valueOf(ano)));
                        }
                        GetInfoFromMonthandYear(month, ano);

                    }
                }
        );
    //* Fim - Date *//




    }
    public void GetInfoFromMonthandYear(int month, int year){

        cardsAdapter.clear();
        lvCards.setAdapter(cardsAdapter);

        Log.d("TAG555523","Oi");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection(LoginActivity.getEmail()).document(month+";"+year);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("acbert12314", "DocumentSnapshot data: " + document.getData());

                        Map<String,Object> data = document.getData();
                        for ( String key : data.keySet() ) {
                            List<Integer> inte = (List<Integer>) data.get(key);
                            Log.d("TAG555523", key);
                            assert inte != null;
                            String valor = String.valueOf(inte.get(0));
                            String parcelas = String.valueOf(inte.get(1));

                            String parcela = String.valueOf(Double.valueOf(valor) / Double.valueOf(parcelas));


                            cardsAdapter.add(new CardModel("teste", key, "Valor total:" + valor,parcelas+" parcelas de "+ parcela, ""));
                        }


                    } else {
                        Log.d("acbert12314", "No such document");
                    }
                } else {
                    Log.d("acbert12314", "get failed with ", task.getException());
                }
            }
        });


        lvCards.setAdapter(cardsAdapter);

    }

    static int i;
    public void setInitMember(int i){
        this.i =  i;
    }
    public static Integer getInitiMember(){
        return i;
    }
    static int j;
    public void setInitAno(int j){
        this.j =  j;
    }
    public static Integer getInitAno(){
        return j;
    }

}

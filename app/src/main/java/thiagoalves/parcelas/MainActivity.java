package thiagoalves.parcelas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.florent37.materialviewpager.MaterialViewPager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int month;
    int ano;
    Button mes;
    String[] Mes = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mes = findViewById(R.id.mes);
        Button esquerda = findViewById(R.id.esquerda);
        Button direita = findViewById(R.id.direita);
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        month = localDate.getMonthValue() -1 ;
        ano =  localDate.getYear();

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
                    }
                }
        );


    }
}

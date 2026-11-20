package com.inglesencasaya.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MainActivity extends Activity {
    private final int purple = Color.rgb(104,63,204), ink = Color.rgb(28,29,58);
    private final String[] units = {"Saludos y presentaciones", "Familia y hogar", "Comida y compras", "Viajes y direcciones", "Conversaciones cotidianas"};
    private final String[][] questions = {
        {"¿Cómo se dice 'Hola'?","¿Cómo se dice 'Gracias'?","¿Cómo se dice 'Buenos días'?","¿Cómo se dice 'Adiós'?","¿Cómo se dice 'Por favor'?"},
        {"¿Cómo se dice 'Casa'?","¿Cómo se dice 'Madre'?","¿Cómo se dice 'Padre'?","¿Cómo se dice 'Hermana'?","¿Cómo se dice 'Familia'?"},
        {"¿Cómo se dice 'Agua'?","¿Cómo se dice 'Pan'?","¿Cómo se dice 'Manzana'?","¿Cómo se dice 'Leche'?","¿Cómo se dice 'Comprar'?"},
        {"¿Cómo se dice 'Aeropuerto'?","¿Cómo se dice 'Tren'?","¿Cómo se dice 'Izquierda'?","¿Cómo se dice 'Derecha'?","¿Cómo se dice 'Boleto'?"},
        {"¿Cómo se dice '¿Cómo estás?'?","¿Cómo se dice 'Estoy bien'?","¿Cómo se dice 'Hasta mañana'?","¿Cómo se dice 'Nos vemos'?","¿Cómo se dice 'Disculpa'?"},
    };
    private final String[][] answers = {
        {"Hello","Thank you","Good morning","Goodbye","Please"},
        {"House","Mother","Father","Sister","Family"},
        {"Water","Bread","Apple","Milk","Buy"},
        {"Airport","Train","Left","Right","Ticket"},
        {"How are you?","I am fine","See you tomorrow","See you","Excuse me"},
    };
    private final String[][] distractors = {
        {"Goodbye","Sorry"},{"Night","Hello"},{"Good night","Thank you"},{"Welcome","Please"},{"Thanks","Bye"},
        {"Car","School"},{"Sister","Brother"},{"Uncle","Mother"},{"Brother","Father"},{"Friends","House"},
        {"Milk","Bread"},{"Water","Rice"},{"Orange","Potato"},{"Juice","Apple"},{"Sell","Eat"},
        {"Station","Hotel"},{"Bus","Plane"},{"Right","Straight"},{"Left","Near"},{"Map","Passport"},
        {"Where are you?","What is this?"},{"I am hungry","I am late"},{"Good night","Good morning"},{"Come here","Go away"},{"Welcome","Goodbye"}
    };
    private SharedPreferences prefs;
    private LinearLayout root;
    private int unit=0, question=0, correct=0, hearts=5;

    @Override public void onCreate(Bundle b) {super.onCreate(b);prefs=getSharedPreferences("learning_v2",MODE_PRIVATE);home();}
    private GradientDrawable shape(int color) {GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(32);return d;}
    private void screen() {
        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.setBackgroundColor(Color.rgb(248,247,255));
        root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(28,35,28,28);
        scroll.addView(root);setContentView(scroll);
    }
    private TextView label(String s,int size,boolean bold) {
        TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(ink);
        if(bold)t.setTypeface(null,Typeface.BOLD);t.setPadding(6,12,6,12);root.addView(t);return t;
    }
    private void button(String s,Runnable action,boolean secondary) {
        Button b=new Button(this);b.setAllCaps(false);b.setText(s);b.setTextSize(16);
        b.setTextColor(secondary?purple:Color.WHITE);b.setBackground(shape(secondary?Color.rgb(231,222,255):purple));
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,66);p.setMargins(0,8,0,8);root.addView(b,p);
        b.setOnClickListener(v->action.run());
    }
    private int unlocked(){return prefs.getInt("unlocked",0);}
    private void home(){
        screen();label("✦ INGLÉS EN CASA YA",18,true).setTextColor(purple);
        label("Tu camino hacia el inglés",28,true);
        label("🔥 Racha: "+prefs.getInt("streak",0)+" días    ⭐ XP: "+prefs.getInt("xp",0),18,true);
        label("RECORRIDO DE APRENDIZAJE",16,true).setTextColor(purple);
        for(int i=0;i<units.length;i++){
            final int index=i;
            boolean open=i<=unlocked();
            label("UNIDAD "+(i+1)+" · "+units[i],19,true);
            button(open?(prefs.getBoolean("done_"+i,false)?"✓ Repasar unidad":"▶ Empezar lección"):"🔒 Completa la unidad anterior",()->{if(open)start(index);},!open);
        }
        button("Mi progreso",this::profile,true);
    }
    private void profile(){
        screen();label("Mi progreso",28,true);
        label("⭐ Experiencia total: "+prefs.getInt("xp",0)+" XP",20,false);
        label("🔥 Racha actual: "+prefs.getInt("streak",0)+" días",20,false);
        int count=0;for(int i=0;i<units.length;i++)if(prefs.getBoolean("done_"+i,false))count++;
        label("🏆 Unidades completadas: "+count+" de "+units.length,20,false);
        button("Volver al recorrido",this::home,false);
    }
    private void start(int index){unit=index;question=0;correct=0;hearts=5;quiz();}
    private void quiz(){
        screen();label("UNIDAD "+(unit+1)+" · "+units[unit],16,true).setTextColor(purple);
        label("Pregunta "+(question+1)+" de "+questions[unit].length+"    ❤️ "+hearts,17,true);
        label(questions[unit][question],26,true);
        int slot=unit*5+question;
        String right=answers[unit][question];
        String[] opts={right,distractors[slot][0],distractors[slot][1]};
        int shift=(question+unit)%3;
        for(int i=0;i<3;i++){String option=opts[(i+shift)%3];button(option,()->respond(option.equals(right)),false);}
        button("Salir de la lección",this::home,true);
    }
    private void respond(boolean right){
        if(right)correct++;else hearts--;
        String answer=answers[unit][question];
        new android.app.AlertDialog.Builder(this).setTitle(right?"¡Muy bien! 🎉":"Sigue practicando")
            .setMessage(right?"Respuesta correcta.":"La respuesta correcta es: "+answer)
            .setCancelable(false).setPositiveButton("Continuar",(d,w)->{
                question++;
                if(hearts==0){screen();label("❤️ Sin corazones",26,true);label("Vuelve a intentarlo para continuar.",17,false);button("Reintentar",()->start(unit),false);}
                else if(question<questions[unit].length)quiz();else finish();
            }).show();
    }
    private void finish(){
        int xp=correct*10;
        prefs.edit().putInt("xp",prefs.getInt("xp",0)+xp).putBoolean("done_"+unit,true)
            .putInt("unlocked",Math.max(unlocked(),Math.min(unit+1,units.length-1))).apply();
        updateStreak();
        screen();label("🎉 ¡Lección completada!",28,true);
        label("⭐ +"+xp+" XP · "+correct+" de "+questions[unit].length+" respuestas correctas",20,true);
        button("Continuar aprendiendo",this::home,false);
        button("Repasar esta unidad",()->start(unit),true);
    }
    private void updateStreak(){
        LocalDate now=LocalDate.now();String previous=prefs.getString("lastDay","");
        int streak=prefs.getInt("streak",0);
        if(!previous.equals(now.toString())){
            try{long diff=ChronoUnit.DAYS.between(LocalDate.parse(previous),now);streak=diff==1?streak+1:1;}
            catch(Exception e){streak=1;}
            prefs.edit().putString("lastDay",now.toString()).putInt("streak",streak).apply();
        }
    }
    @Override public void onBackPressed(){home();}
}

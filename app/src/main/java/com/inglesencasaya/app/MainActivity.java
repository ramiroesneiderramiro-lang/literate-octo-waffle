package com.inglesencasaya.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.SharedPreferences;

public class MainActivity extends Activity {
    private final int purple = Color.rgb(104, 63, 204);
    private final int ink = Color.rgb(28, 29, 58);
    private LinearLayout root;
    private SharedPreferences prefs;
    private int question = 0;
    private int correct = 0;
    private final String[] prompts = {"¿Cómo se dice 'Hola'?", "¿Cómo se dice 'Gracias'?", "¿Cómo se dice 'Casa'?", "¿Cómo se dice 'Agua'?", "¿Cómo se dice 'Libro'?"};
    private final String[][] options = {{"Hello", "Goodbye", "Please"}, {"Sorry", "Thanks", "Night"}, {"House", "Car", "School"}, {"Bread", "Water", "Milk"}, {"Book", "Table", "Chair"}};
    private final int[] answers = {0,1,0,1,0};

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("progreso", MODE_PRIVATE);
        home();
    }
    private GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }
    private void screen() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(248, 247, 255));
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 48, 28, 28);
        scroll.addView(root);
        setContentView(scroll);
    }
    private TextView text(String value, int size, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(ink);
        if (bold) t.setTypeface(null, Typeface.BOLD);
        t.setPadding(4, 12, 4, 12);
        root.addView(t);
        return t;
    }
    private void button(String label, Runnable action) {
        Button b = new Button(this);
        b.setAllCaps(false);
        b.setText(label);
        b.setTextSize(17);
        b.setTextColor(Color.WHITE);
        b.setBackground(bg(purple, 28));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, 64);
        p.setMargins(0, 12, 0, 12);
        root.addView(b, p);
        b.setOnClickListener(v -> action.run());
    }
    private void home() {
        screen();
        TextView brand = text("✦ INGLÉS EN CASA YA", 18, true);
        brand.setTextColor(purple);
        text("Aprende inglés a tu ritmo", 30, true);
        text("Lecciones cortas, progreso real y una experiencia diseñada para ti.", 17, false);
        text("🏆 Lecciones completadas: " + prefs.getInt("lessons", 0), 19, true);
        text("Nivel 1 · Palabras esenciales", 23, true);
        button("Comenzar lección", () -> {question = 0; correct = 0; quiz();});
        text("Próximamente: más niveles, audio y recompensas.", 15, false);
    }
    private void quiz() {
        screen();
        text("LECCIÓN 1  •  " + (question + 1) + " / " + prompts.length, 16, true).setTextColor(purple);
        text(prompts[question], 27, true);
        for (int i = 0; i < options[question].length; i++) {
            final int choice = i;
            button(options[question][i], () -> answer(choice));
        }
        button("Volver al inicio", this::home);
    }
    private void answer(int selected) {
        boolean ok = selected == answers[question];
        if (ok) correct++;
        new android.app.AlertDialog.Builder(this)
            .setTitle(ok ? "¡Correcto! 🎉" : "Sigue practicando")
            .setMessage(ok ? "¡Muy bien!" : "Respuesta: " + options[question][answers[question]])
            .setPositiveButton("Continuar", (d, w) -> {
                question++;
                if (question < prompts.length) quiz(); else finishLesson();
            }).setCancelable(false).show();
    }
    private void finishLesson() {
        prefs.edit().putInt("lessons", prefs.getInt("lessons", 0) + 1).apply();
        screen();
        text("🎉 ¡Lección completada!", 29, true);
        text("Respuestas correctas: " + correct + " de " + prompts.length, 21, false);
        button("Volver al inicio", this::home);
        button("Repetir lección", () -> {question = 0; correct = 0; quiz();});
    }
    @Override public void onBackPressed() { home(); }
}

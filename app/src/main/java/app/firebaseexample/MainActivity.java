package app.firebaseexample;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Dialog modalAdd;
    Toast t;
    ArrayList<Producto> listaProductos = new ArrayList<Producto>();;
    ArrayAdapter adapter;
    ListView listView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    int indice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView= findViewById(R.id.ListView);
        listarDatos();

    }
    public void listarDatos(){
        myRef.child("Productos").orderByChild("nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            listaProductos.clear();
                for (DataSnapshot objSnap : dataSnapshot.getChildren()){
                    Producto p = objSnap.getValue(Producto.class);
                    listaProductos.add(p);
                }
                adapter = new ArrayAdapter<Producto>(MainActivity.this, R.layout.activity_listview, R.id.txtProduct, listaProductos);
                listView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void modalAdd(View view){
        modalAdd = new Dialog(MainActivity.this);
        modalAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modalAdd.setCancelable(true);
        modalAdd.setContentView(R.layout.modaladd);
        modalAdd.show();
    }

    public void btnAdd(View view) {
        TextView nombreAdd = modalAdd.findViewById(R.id.cajaNombre);
        TextView descripcionAdd = modalAdd.findViewById(R.id.cajaDescripcion);
        TextView precioAdd = modalAdd.findViewById(R.id.cajaPrecio);
        Producto p = new Producto();
        p.setId(UUID.randomUUID().toString());
        p.setNombre(nombreAdd.getText().toString());
        p.setDescripcion(descripcionAdd.getText().toString());
        p.setPrecio(precioAdd.getText().toString());
        myRef.child("Productos").child(p.getId()).setValue(p);
        t.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
        modalAdd.dismiss();
    }

    public void modalEditar(View view) {
        View item = (View) view.getParent();
        int pos = listView.getPositionForView(item);
        modalAdd = new Dialog(MainActivity.this);
        modalAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modalAdd.setCancelable(true);
        modalAdd.setContentView(R.layout.modaledit);
        modalAdd.show();
        TextView nombreEdit = modalAdd.findViewById(R.id.cajaNombre);
        TextView descripcionEdit = modalAdd.findViewById(R.id.cajaDescripcion);
        TextView precioEdit = modalAdd.findViewById(R.id.cajaPrecio);
        nombreEdit.setText(listaProductos.get(pos).getNombre());
        precioEdit.setText(listaProductos.get(pos).getPrecio());
        descripcionEdit.setText(listaProductos.get(pos).getDescripcion());
        indice = pos;
    }

    public void btnEditar(View view){
        TextView nombreEdit = modalAdd.findViewById(R.id.cajaNombre);
        TextView descripcionEdit = modalAdd.findViewById(R.id.cajaDescripcion);
        TextView precioEdit = modalAdd.findViewById(R.id.cajaPrecio);
        Producto p = new Producto();
        p.setId(listaProductos.get(indice).getId().trim());
        p.setNombre(nombreEdit.getText().toString().trim());
        p.setDescripcion(descripcionEdit.getText().toString().trim());
        p.setPrecio(precioEdit.getText().toString().trim());
        myRef.child("Productos").child(p.getId()).setValue(p);
        t.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
        modalAdd.dismiss();
    }

    public void delete(View view){
        View item = (View) view.getParent();
        int pos = listView.getPositionForView(item);
        Producto p = new Producto();
        p.setId(listaProductos.get(pos).getId().trim());
        myRef.child("Productos").child(p.getId()).removeValue();
        t.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
    }


}

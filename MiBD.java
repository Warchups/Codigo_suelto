package com.example.gnommostudios34.practica_christianllopis.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.gnommostudios34.practica_christianllopis.API.MiPeliculaOperacional;
import com.example.gnommostudios34.practica_christianllopis.DAO.PeliculaDAO;
import com.example.gnommostudios34.practica_christianllopis.R;
import com.example.gnommostudios34.practica_christianllopis.pojo.Pelicula;

import java.io.ByteArrayOutputStream;

public class MiBD extends SQLiteOpenHelper{

    private static SQLiteDatabase db;

    private Context context;

    private static final String database = "GnomoPelis";

    private static final int version = 1;

    private String sqlCreacionPeliculas = "CREATE TABLE peliculas ( id INTEGER PRIMARY KEY AUTOINCREMENT, titulo STRING, descripcion STRING, " +
            "puntuacion INTEGER, fecha STRING, imagen BLOB);";

    private static MiBD instance = null;

    private static PeliculaDAO peliculaDAO;

    public PeliculaDAO getPeliculaDAO() {
        return peliculaDAO;
    }

    public static MiBD getInstance(Context context) {
        if(instance == null) {
            instance = new MiBD(context);
            db = instance.getWritableDatabase();
            peliculaDAO = new PeliculaDAO();
        }

        return instance;
    }

    public static SQLiteDatabase getDB(){
        return db;
    }
    public static void closeDB(){db.close();}



    protected MiBD(Context context) {
        super( context, database, null, version );
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreacionPeliculas);

        insercionDatos(db);
        Log.i("SQLite", "Se crea la base de datos " + database + " version " + version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("SQLite", "Control de versiones: Old Version=" + oldVersion + " New Version= " + newVersion);
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS peliculas");

            db.execSQL(sqlCreacionPeliculas);

            insercionDatos(db);
            Log.i("SQLite", "Se actualiza versión de la base de datos, New version= " + newVersion);

        }
    }

    private void insercionDatos(SQLiteDatabase db){
        Bitmap origenBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.origen);
        ByteArrayOutputStream baosO = new ByteArrayOutputStream(20480);
        origenBitmap.compress(Bitmap.CompressFormat.PNG, 0 , baosO);
        byte[] origen = baosO.toByteArray();

        Log.i("Origen length MiBD" , "" + origen.length);

        Bitmap batmanBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.batman);
        ByteArrayOutputStream baosB = new ByteArrayOutputStream(20480);
        batmanBitmap.compress(Bitmap.CompressFormat.PNG, 0 , baosB);
        byte[] batman = baosB.toByteArray();

        Log.i("Batman length MiBD" , "" + batman.length);

        String sql1 = "INSERT INTO peliculas(titulo, descripcion, puntuacion, fecha, imagen) VALUES ('Incepcion', 'Dom Cobb (DiCaprio) es un experto en el arte de apropiarse, durante el sueño, de los secretos del subconsciente ajeno. La extraña habilidad de Cobb le ha convertido en un hombre muy cotizado en el mundo del espionaje, pero también lo ha condenado a ser un fugitivo y, por consiguiente, a renunciar a llevar una vida normal. Su única oportunidad para cambiar de vida será hacer exactamente lo contrario de lo que ha hecho siempre: la incepción, que consiste en implantar una idea en el subconsciente en lugar de sustraerla. Sin embargo, su plan se complica debido a la intervención de alguien que parece predecir cada uno de sus movimientos, alguien a quien sólo Cobb podrá descubrir.', 4, '2010', ?);";
        String sql2 = "INSERT INTO peliculas(titulo, descripcion, puntuacion, fecha, imagen) VALUES ('El caballero oscuro', 'Batman/Bruce Wayne (Christian Bale) regresa para continuar su guerra contra el crimen. Con la ayuda del teniente Jim Gordon (Gary Oldman) y del Fiscal del Distrito Harvey Dent (Aaron Eckhart), Batman se propone destruir el crimen organizado en la ciudad de Gotham. El triunvirato demuestra su eficacia, pero, de repente, aparece Joker (Heath Ledger), un nuevo criminal que desencadena el caos y tiene aterrados a los ciudadanos.', 5, '2008', ?);";

        SQLiteStatement insert1 = db.compileStatement(sql1);
        insert1.clearBindings();
        insert1.bindBlob(1, origen);
        insert1.executeInsert();

        SQLiteStatement insert2 = db.compileStatement(sql2);
        insert2.clearBindings();
        insert2.bindBlob(1, batman);
        insert2.executeInsert();



        //db.execSQL("INSERT INTO peliculas(titulo, descripcion, puntuacion, fecha, imagen) VALUES ('Incepcion', 'Dom Cobb (DiCaprio) es un experto en el arte de apropiarse, durante el sueño, de los secretos del subconsciente ajeno. La extraña habilidad de Cobb le ha convertido en un hombre muy cotizado en el mundo del espionaje, pero también lo ha condenado a ser un fugitivo y, por consiguiente, a renunciar a llevar una vida normal. Su única oportunidad para cambiar de vida será hacer exactamente lo contrario de lo que ha hecho siempre: la incepción, que consiste en implantar una idea en el subconsciente en lugar de sustraerla. Sin embargo, su plan se complica debido a la intervención de alguien que parece predecir cada uno de sus movimientos, alguien a quien sólo Cobb podrá descubrir.', 4, '2010', '" + origen + "');");
        //db.execSQL("INSERT INTO peliculas(titulo, descripcion, puntuacion, fecha, imagen) VALUES ('El caballero oscuro', 'Batman/Bruce Wayne (Christian Bale) regresa para continuar su guerra contra el crimen. Con la ayuda del teniente Jim Gordon (Gary Oldman) y del Fiscal del Distrito Harvey Dent (Aaron Eckhart), Batman se propone destruir el crimen organizado en la ciudad de Gotham. El triunvirato demuestra su eficacia, pero, de repente, aparece Joker (Heath Ledger), un nuevo criminal que desencadena el caos y tiene aterrados a los ciudadanos.', 5, '2008', '" + batman + "');");

    }

    public void insercionPelicula(Pelicula p){
        /*ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
        p.getImagen().compress(Bitmap.CompressFormat.PNG, 0 , baos);
        byte[] blob = baos.toByteArray();*/

        db.execSQL("INSERT INTO peliculas (titulo, descripcion, puntuacion, fecha, imagen) VALUES ('" +p.getTitulo()+"', '"+ p.getDescripcion() +"', "+ p.getPuntuacion()+", '"+ p.getPuntuacion()+"', "+ p.getImagen() +");");
    }

}

//db.execSQL("INSERT INTO peliculas(titulo, descripcion, puntuacion, fecha, imagen) VALUES ('Incepcion', 'desc', 4, '2010', " + R.drawable.origen + ");");

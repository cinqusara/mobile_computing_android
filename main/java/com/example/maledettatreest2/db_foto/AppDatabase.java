package com.example.maledettatreest2.db_foto;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FotoProfiloUtente.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{


    public abstract FotoProfiloUtentiDAO fotoProfiloUtentiDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, "db_fotoProfiloUtente")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}

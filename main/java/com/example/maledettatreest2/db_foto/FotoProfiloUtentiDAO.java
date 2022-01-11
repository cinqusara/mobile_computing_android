package com.example.maledettatreest2.db_foto;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.maledettatreest2.db_foto.FotoProfiloUtente;

import java.util.List;

@Dao
public interface FotoProfiloUtentiDAO {

    @Query("SELECT * FROM fotoProfiloUtente")
    List<FotoProfiloUtente> getAll();

    @Query("SELECT * FROM fotoProfiloUtente WHERE uid IN (:uid)")
    FotoProfiloUtente getFotoProfiloFromUid(String uid);

    @Insert
    void insertFotoProfiloUtente(FotoProfiloUtente... fotoProfiloUtente);

    @Delete
    void delete(FotoProfiloUtente fotoProfiloUtente);

}


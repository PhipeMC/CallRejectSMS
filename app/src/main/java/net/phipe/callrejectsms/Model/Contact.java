package net.phipe.callrejectsms.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "celphone_number")
    public String number;

    @ColumnInfo(name = "message")
    public String message;

}

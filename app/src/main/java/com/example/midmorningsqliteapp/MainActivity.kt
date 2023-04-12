package com.example.midmorningsqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var edtName:EditText
    lateinit var edtEmail:EditText
    lateinit var edtIdNumber:EditText
    lateinit var btnSave:Button
    lateinit var btnView:Button
    lateinit var btnDelete:Button
    lateinit var db:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.txtName)
        edtEmail = findViewById(R.id.txtEmail)
        edtIdNumber = findViewById(R.id.txtIDNo)
        btnSave = findViewById(R.id.btnSave)
        btnView = findViewById(R.id.mbtnView)
        btnDelete = findViewById(R.id.mbtnDelete)
        //Create a data base called eMobilis
        db = openOrCreateDatabase("eMobilisDB",Context.MODE_PRIVATE,null)
        //create a table called user in the database
        db.execSQL( "CREATE TABLE IF NOT EXISTS users(jina VARCHAR,arafa VARCHAR,kitambulisho VARCHAR )")
        // set on Click listeners to buttons
        btnSave.setOnClickListener{
        //Receive data from the user
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber = edtIdNumber.text.toString().trim()
            // check if the user is submitting empty fields
            if (name.isEmpty()|| email.isEmpty()||idNumber.isEmpty()){
                //display an error message using the defined message function
                messages("EMPTY FIELDS!!!","Please fill all input fields")

            }else{
                //Proceed to save
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+email+"','"+idNumber+"')")
                clear()
                messages("SUCCESS!!!","User saved successfully")
            }

        }
        btnView.setOnClickListener {
            // Use cursor to select all the records
            var cursor = db.rawQuery("SELECT * FROM users",null)
            // Check if there is any record found
            if (cursor.count == 0){
                messages("NO RECORDS!!!","Sorry no records found!!!")
            }else{
                //use the string buffer to append all records to the display using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n\n")
                }
                messages("USER",buffer.toString())
            }
        }
        btnDelete.setOnClickListener {
val idNumber = edtIdNumber.text.toString().trim()
            if (idNumber.isEmpty()){
                messages("Empty field" ,"please fill in 10 fields")
            }else {
                var cursor =
                    db.rawQuery("SELECT * FROM users WHERE kitambulisho='" + idNumber + "'", null)
                if (cursor.count == 0){
                    messages("NO RECORDS FOUND!!!","Sorry there no users with provided id!!!")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idNumber+"'")
                    clear()
                    messages("SUCCESS!!!","User deleted Successfully")
                }
            }
        }
    }
    fun messages( title:String,message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("cancel",null)
        alertDialog.create().show()
    }
    fun clear(){
        edtName.setText("")
        edtEmail.setText("")
        edtIdNumber.setText("")

    }
}
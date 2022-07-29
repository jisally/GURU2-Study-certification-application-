package com.example.gurufin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    val TAG: String = "Login"

    lateinit var edit_id : EditText

    lateinit var edit_pw : EditText

    lateinit var btn_login : Button

    lateinit var btn_register : Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login = findViewById<Button>(R.id.btn_login)
        btn_register = findViewById<Button>(R.id.btn_register)
        edit_id = findViewById<EditText>(R.id.edit_id)
        edit_pw = findViewById<EditText>(R.id.edit_pw)



        // 로그인
        btn_login.setOnClickListener {

            //editText로부터 입력된 값을 받아온다
            var id = edit_id.text.toString()
            var pw = edit_pw.text.toString()

            // 저장된 id, pw 가져오기
            val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
            val savedId = sharedPreference.getString("id", "")
            val savedPw = sharedPreference.getString("pw", "")

            // 입력한 id, pw값과 기존의 id, pw값 비교
            if(id == savedId && pw == savedPw){
                // 로그인 성공 다이얼로그
                dialog("success")

//                val intent = Intent(this, MainMenu::class.java)
//                startActivity(intent)
            }
            else{
                // 로그인 실패 다이얼로그
                dialog("fail")
            }
        }

        // 회원가입
        btn_register.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }

    // 로그인 시 다이얼로그
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
}
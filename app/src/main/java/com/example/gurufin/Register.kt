package com.example.gurufin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {

    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    lateinit var btn_register2 : Button
    lateinit var edit_id : EditText
    lateinit var edit_pw : EditText
    lateinit var edit_pw_re : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register2= findViewById<Button>(R.id.btn_register)
        edit_id=findViewById(R.id.edit_id)
        edit_pw=findViewById(R.id.edit_pw)
        edit_pw_re=findViewById(R.id.edit_pw_re)


        btn_register2.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val id = edit_id.text.toString()
            val pw = edit_pw.text.toString()
            val pw_re = edit_pw_re.text.toString()

            // empty경우
            if(id.isEmpty() || pw.isEmpty() || pw_re.isEmpty()){
                isExistBlank = true
            }
            else{
                if(pw == pw_re){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame){

                // 회원가입 성공 메세지
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                // 입력한 id, pw 저장
                val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("id", id)
                editor.putString("pw", pw)
                editor.apply()

                // 로그인 화면으로 이동
                val intent = Intent(this, Login::class.java)
                startActivity(intent)

            }
            else{

                // 다이얼로그
                if(isExistBlank){   // empty 가 있을 경우
                    dialog("blank")
                }
                else if(!isPWSame){ // 비밀번호가 다를 경우
                    dialog("not same")
                }
            }

        }
    }

    // 회원가입 실패 다이얼로그
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // empty 가 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        // 비밀번호가 다를 경우
        else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }

        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

}
package com.example.gurufin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gurufin.dto.Target
import com.example.gurufin.databinding.ActivityAddBinding
import java.text.SimpleDateFormat

class TargetAdd : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding

    private var target: Target?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //취소 버튼
        binding.cancelBtn.setOnClickListener {
            onBackPressed()
        }

        //목표 추가일 경우 버튼을 "등록"으로, 수정일 경우 "수정"으로 변경
        val type = intent.getStringExtra("type")

        if (type.equals("ADD")) {
            binding.registerBtn.text = "등록"
        } else {
            target = intent.getSerializableExtra("item") as Target?
            binding.editTitle.setText(target!!.title)
            binding.editContents.setText(target!!.contents)
            binding.registerBtn.text = "수정"

            binding.eraseBtn.visibility = View.VISIBLE
        }

        //삭제버튼
        binding.eraseBtn.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val content = binding.editContents.text.toString()
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val target = Target(target!!.num, title, content, currentDate, target!!.isChecked)

                val intent = Intent().apply {
                    putExtra("target", target)
                    putExtra("flag", 2) //flag=2
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        //등록 버튼
        binding.registerBtn.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val content = binding.editContents.text.toString()
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())

            if (type.equals("ADD")) {
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    val target = Target(0, title, content, currentDate, false) //Target.kt
                    val intent = Intent().apply {
                        putExtra("target", target)
                        putExtra("flag", 0)
                    }
                    setResult(RESULT_OK, intent) //setResult를 통해 넘어온 데이터는 requestActivity로
                    finish()
                }
            } else { //수정
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    val target = Target(target!!.num, title, content, currentDate, target!!.isChecked)

                    val intent = Intent().apply {
                        putExtra("target", target)
                        putExtra("flag", 1)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    //뒤로가기
    override fun onBackPressed() {
        startActivity(Intent(this, TargetMain::class.java))
        finish()
    }

}
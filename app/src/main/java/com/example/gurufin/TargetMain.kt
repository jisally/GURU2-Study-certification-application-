package com.example.gurufin

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gurufin.databinding.ActivityTargetBinding
import com.example.gurufin.dto.Target
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TargetMain : AppCompatActivity() {
    lateinit var binding: ActivityTargetBinding
    lateinit var targetAdapter: TargetAdapter

    lateinit var targetViewModel: TargetViewModel

    private var isFabOpen=false
    lateinit var addPicBtn: FloatingActionButton
    lateinit var addTargetBtn: FloatingActionButton
    lateinit var addBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTargetBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //targetViewModel
        targetViewModel=ViewModelProvider(this)[TargetViewModel::class.java]

        //targetAdapter
        targetViewModel.targetList.observe(this) {
            targetAdapter.update(it)
        }

        targetAdapter= TargetAdapter(this)
        binding.TargetList.layoutManager=LinearLayoutManager(this)
        binding.TargetList.adapter=targetAdapter

        targetAdapter.setItemCheckBoxClickListener(object: TargetAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val target = targetViewModel.getOne(itemId)
                    target.isChecked = !target.isChecked
                    targetViewModel.update(target)
                }
            }
        })

        //2
        targetAdapter.setItemClickListener(object: TargetAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val target = targetViewModel.getOne(itemId)

                    val intent = Intent(this@TargetMain, TargetAdd::class.java).apply {
                        putExtra("type", "EDIT")
                        putExtra("item", target)
                    }
                    requestActivity.launch(intent)
                }
            }
        })

        //사진 추가 버튼과 목표 추가 버튼이 나타나는 버튼
        addBtn=findViewById(R.id.addBtn)
        binding.addBtn.setOnClickListener {
            toggleFab()
        }

        //사진 추가
        addPicBtn = findViewById(R.id.addPicBtn)
        binding.addPicBtn.setOnClickListener {
            toggleFab()
            startActivity(Intent(this@TargetMain, Individualstudy::class.java))
        }

        //목표 추가
        addTargetBtn=findViewById(R.id.addTargetBtn)
        binding.addTargetBtn.setOnClickListener {
            toggleFab()
            val intent=Intent(this, TargetAdd::class.java).apply {
                putExtra("type", "ADD")
            }
            requestActivity.launch(intent)
        }


    }

    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val target = it.data?.getSerializableExtra("target") as Target

            when (it.data?.getIntExtra("flag", -1)) {
                0 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        targetViewModel.insert(target)
                    }
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        targetViewModel.update(target)
                    }
                    Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        targetViewModel.delete(target)
                    }
                    Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun toggleFab() {
        //플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션 세팅
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.addPicBtn, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.addTargetBtn, "translationY", 0f).apply { start() }
            addBtn.setImageResource(R.drawable.ic_baseline_add_24)
        } else { //플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션 세팅
            ObjectAnimator.ofFloat(binding.addPicBtn, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(binding.addTargetBtn, "translationY", -380f).apply { start() }
            addBtn.setImageResource(R.drawable.ic_baseline_close_24)
        }
        isFabOpen =!isFabOpen
    }

    //메뉴
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.targetMain ->{
                val intent =Intent(this,TargetMain::class.java)
                startActivity(intent)
                return true
            }
            R.id.translate ->{
                val intent =Intent(this,translate::class.java)
                startActivity(intent)
                return true
            }
            R.id.calendar ->{
                val intent =Intent(this,calendar::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}


package com.example.gurufin


import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class Individualstudy : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var imageText: TextView
    lateinit var goBtn: Button
    lateinit var noBtn: Button
    lateinit var cameraBtn: Button
    lateinit var galleryBtn: Button

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic)

        imageView = findViewById(R.id.imageView)
        imageText = findViewById(R.id.imageText)
        goBtn = findViewById(R.id.goBtn)
        noBtn = findViewById(R.id.noBtn)
        cameraBtn = findViewById(R.id.cameraBtn)
        galleryBtn = findViewById(R.id.galleryBtn)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val pathReference = storageReference!!.child("myImages")

        if (pathReference == null) {
            Toast.makeText(this@Individualstudy, "사진을 등록해 주세요.", Toast.LENGTH_SHORT).show()
        } else {
            val submitProfile = storageReference!!.child("myImages/image")
            submitProfile.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this@Individualstudy).load(uri)
                    .into(imageView)
            }
            imageText.visibility=View.INVISIBLE
        }


        //goBtn 누르면 사진이 db에 저장됨. 이전 화면으로 돌아가는건 뒤로가기 버튼이나 취소 버튼으로 하기
        goBtn.setOnClickListener {
            uploadImage()
            finish()
        }

        //noBtn 누르면 이전 화면으로 돌아감
        noBtn.setOnClickListener {
            onBackPressed()
        }

        //imageView를 누르면 imageView와 imageText가 invisible. 그리고 cameraBtn과 galleryBtn이 visible. 선택 가능함
        imageView.setOnClickListener {
            imageText.visibility = View.INVISIBLE
            cameraBtn.visibility = View.VISIBLE
            galleryBtn.visibility = View.VISIBLE
        }


        //카메라 버튼
        cameraBtn.setOnClickListener {
            if (checkPermission()) {
                dispatchTakePictureIntentEx()
            } else {
                requestPermission()
            }
            cameraBtn.visibility = View.INVISIBLE
            galleryBtn.visibility = View.INVISIBLE
        }


        //갤러리 버튼
        galleryBtn.setOnClickListener {
            if (checkPermission()) {
                launchGallery()
            }
            else {
                requestPermission()
            }
            cameraBtn.visibility = View.INVISIBLE
            galleryBtn.visibility = View.INVISIBLE
        }


    }

    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////


    fun createImageUri(filename:String, mimeType:String):Uri?{
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private val REQUEST_CREATE_EX = 3

    private fun dispatchTakePictureIntentEx()
    {
        val intent = Intent()
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val takePictureIntent : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri : Uri? = createImageUri("JPEG_${timeStamp}_", "image/jpg")
        filePath = uri
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePath)
        startActivityForResult(takePictureIntent, REQUEST_CREATE_EX)
    }

    fun loadBitmapFromMediaStoreBy(filepath: Uri) : Bitmap?{
        var image: Bitmap? = null
        try{
            image = if(Build.VERSION.SDK_INT > 27){
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(this.contentResolver, filepath)
                ImageDecoder.decodeBitmap(source)

            }else{
                MediaStore.Images.Media.getBitmap(this.contentResolver, filepath)
            }
        }catch(e: IOException){
            e.printStackTrace()
        }
        return image
    }

    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else if(requestCode == REQUEST_CREATE_EX) {
            if( filePath != null)
            {
                val bitmap = loadBitmapFromMediaStoreBy(filePath!!)
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("myImages/image") //폴더 이름 날짜로 저장하기
            val uploadTask = ref?.putFile(filePath!!)

        }else{
            Toast.makeText(this, "사진을 등록해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }


    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////

    //ActivityCompat Class를 사용해서 카메라 사용 권한을 요청합니다.
    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,CAMERA,
            WRITE_EXTERNAL_STORAGE),1)

    }

    //checkPermission() 함수는 권한 여부를 확인할 수 있습니다.
    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

    }

    //onRequestPermissionsResult() 함수를 override 후 권한 승인에 따른 이벤트를 확인할 수 있습니다.
    //즉 권한이 없을 경우 앱 동작을 중단하거나, 메시지를 출력할 수 있습니다.
    //카메라를 실행하기 위해서 Intent를 실행합니다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if( requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
        }
        else
        {
            Toast.makeText(this, "권한을 허용해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////

    override fun onBackPressed() {
        startActivity(Intent(this, TargetMain::class.java))
        finish()
    }



}


package com.example.sqlnote

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sqlnote.db.MyContentConst
import com.example.sqlnote.db.MyDdManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*


class EditActivity : AppCompatActivity() {

    var layout:ConstraintLayout?=null
    var addImg:FloatingActionButton?=null
    var edImg:ImageButton?=null
    var delImg:ImageButton?=null
    var myImg:ImageView?=null
    var edTitle:EditText?=null
    var edDesc:EditText?=null


    val myDbManager= MyDdManager(this)

    var id=0
    var isEdit=false
    val imageReqCode=10
    var tempImageUri="empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)
        layout=findViewById(R.id.layout_img)
        addImg=findViewById(R.id.fvImage)
        edImg=findViewById(R.id.btn_edit_img)
        delImg=findViewById(R.id.btn_del_img)
        myImg=findViewById(R.id.myImage)
        edTitle=findViewById(R.id.edTitle)
        edDesc=findViewById(R.id.edDesc)


        getMyIntent()

    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }


    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK&&requestCode==imageReqCode){
            myImg?.setImageURI(data?.data)
            tempImageUri=data?.data.toString()
            contentResolver.takePersistableUriPermission(
                data!!.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    fun onClickAddImg(view: View) {
        layout?.visibility=View.VISIBLE
        addImg?.visibility=View.GONE
    }

    fun onClickDelImg(view: View) {
        layout?.visibility=View.GONE
        addImg?.visibility=View.VISIBLE
        tempImageUri="empty"
    }

    fun onClickChooseImage(view: View) {
        val intent= Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type="image/*"
        intent.flags=Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent,imageReqCode)
    }

    fun onClickSave(view: View) {
        val myTitle=edTitle?.text.toString()
        val myDesc=edDesc?.text.toString()

        if(myTitle!=""&&myDesc!=""){
            if (isEdit){
                myDbManager.updateItem(myTitle,myDesc,tempImageUri,id,getCurrentTime())
            }
            else{
                myDbManager.insertToDb(myTitle,myDesc,tempImageUri,getCurrentTime())
            }
            finish()
        }
    }

    fun getMyIntent(){
        val i=intent
        if (i!=null){
            if (i.getStringExtra(MyContentConst.I_TITLE_KEY) != null) {
                //addImg?.visibility = View.GONE
                edTitle?.setText(i.getStringExtra(MyContentConst.I_TITLE_KEY))
                isEdit = true
                edDesc?.setText(i.getStringExtra(MyContentConst.I_DESC_KEY))
                id = i.getIntExtra(MyContentConst.I_ID_KEY, 0)
                if (i.getStringExtra(MyContentConst.I_URI_KEY) != "empty") {
                    layout?.visibility = View.VISIBLE
                    tempImageUri=i.getStringExtra(MyContentConst.I_URI_KEY)!!
                    myImg?.setImageURI(Uri.parse(tempImageUri))
                    addImg?.visibility = View.GONE
                } else {
                    layout?.visibility = View.GONE
                    //edImg?.visibility = View.GONE
                    //delImg?.visibility = View.GONE
                    addImg?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getCurrentTime(): String {
        val time=Calendar.getInstance().time
        val formatter=SimpleDateFormat("dd-MM-YY hh:mm",Locale.getDefault())
        return formatter.format(time)
    }

}
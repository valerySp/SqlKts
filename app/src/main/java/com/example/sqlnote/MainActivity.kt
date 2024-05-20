package com.example.sqlnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sqlnote.db.MyAdapter
import com.example.sqlnote.db.MyDdManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Objects

class MainActivity : AppCompatActivity() {

    val myDbManager=MyDdManager(this)
    val myAdapter=MyAdapter(ArrayList(),this)
    var rcView: RecyclerView?=null
    var empty: TextView?=null
    var searchView: SearchView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rcView=findViewById(R.id.rcView)
        empty=findViewById(R.id.emptyText)
        searchView=findViewById(R.id.searchView)

        init()
        searchView()

    }

    fun onClickNew(view: View){
        val i=Intent(this,EditActivity::class.java)
        startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter("")
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun init(){
        rcView?.layoutManager=LinearLayoutManager(this)
        val swapHelper=getSwapMg().attachToRecyclerView(rcView)
        rcView?.adapter=myAdapter
    }

    private fun fillAdapter(text: String){
        CoroutineScope(Dispatchers.Main).launch {
            val list=myDbManager.readDbData(text)
            myAdapter.updateAdapter(list)
            if (list.size>0)
                empty?.visibility=View.GONE
            else
                empty?.visibility=View.VISIBLE
        }
    }

    private fun getSwapMg():ItemTouchHelper {

        return ItemTouchHelper(object:ItemTouchHelper.
        SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               myAdapter.removeItem(viewHolder.adapterPosition,myDbManager)
            }
        })
    }

    fun searchView(){
        searchView?.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(text: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                fillAdapter(text!!)
                return true
            }

        })
    }
}
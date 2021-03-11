package com.example.roomquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    lateinit var productadapter : ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productadapter = ProductAdapter((this))

        productadapter.loadAllProducts()

        var productRV = findViewById<RecyclerView>(R.id.RV_product)
        productRV.layoutManager = LinearLayoutManager(this)
        productRV.adapter = productadapter

        var BTN_Add = findViewById<Button>(R.id.BTN_Add)
        BTN_Add.setOnClickListener {
            val addText = findViewById<EditText>(R.id.et_product).text.toString()

        findViewById<EditText>(R.id.et_amount).text.toString().toIntOrNull()?.let {

            val addProduct = ProductDB.Product(productName = addText, productPrice = 10, productAmount = it)

            launch(Dispatchers.IO){
                productadapter.productdb.db.productDao().save(addProduct)
                productadapter.loadAllProducts()
                Log.d("Room", addProduct.toString())

            }}
        }


    }
}


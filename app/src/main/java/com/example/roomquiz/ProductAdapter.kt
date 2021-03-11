package com.example.roomquiz

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProductAdapter(ctx: Context): RecyclerView.Adapter<ProductViewHolder>(), CoroutineScope by MainScope() {

    var productdb = ProductDB(ctx)
    var allProducts : List<ProductDB.Product>? = null

    fun loadAllProducts(){
        launch(Dispatchers.IO){
            allProducts = productdb.db.productDao().loadAll()
            Log.d("Room", allProducts.toString())

            launch(Dispatchers.Main){
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        var vh = ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        allProducts?.let {
            Log.d("Room", it.size.toString())
            return it.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        var currentProduct = allProducts!![position]
        var amountText = ""
        currentProduct.productAmount?.let {
            amountText = it.toString()
        }

        holder.productText.text = currentProduct.pid.toString() + " "+ currentProduct.productName + " "+ amountText + "st"

        holder.itemView.setOnClickListener {
            launch(Dispatchers.IO){
                productdb.db.productDao().delete(currentProduct)
                loadAllProducts()
            }
        }

    }


}

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var productText = view.findViewById<TextView>(R.id.TV_product_item)

}

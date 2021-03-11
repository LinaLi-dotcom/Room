package com.example.roomquiz

import android.content.Context
import android.media.AudioTrack
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class ProductDB(ctx: Context) {

   lateinit var db: ProductDatabase

    val MIGRATION_1_2 = object : Migration(1,2){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE product ADD COLUMN product_amount INTEGER  ")
        }
    }

    init {
         db = Room.databaseBuilder(
                ctx,
                ProductDatabase::class.java, "pia9product"
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Entity
    data class Product(
            @PrimaryKey(autoGenerate = true) val pid: Int = 0,
            @ColumnInfo(name = "product_name") val productName: String?,
            @ColumnInfo(name = "product_price") val productPrice: Int?,
            @ColumnInfo(name = "product_amount") val productAmount: Int?)


    @Dao
    interface ProductDao {
        @Query("SELECT * FROM product")
        fun loadAll(): List<Product>

        @Query("SELECT * FROM product WHERE product_amount = :number")
        fun loadAmount(number : Int): List<Product>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun save(product: Product)

        @Delete
        fun delete(product: Product)

        @Update
        fun update(vararg product: Product)
    }


    @Database(entities = arrayOf(Product::class), version = 2)
    abstract class ProductDatabase : RoomDatabase() {
        abstract fun productDao(): ProductDao

    }
}
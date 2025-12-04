package com.sinif.kutuphane

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private var books: List<Book>,
    private val dbHelper: DatabaseHelper,
    private val onBookClick: (Book) -> Unit,
    private val onBookLongClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView = view.findViewById(R.id.textTitle)
        val textAuthor: TextView = view.findViewById(R.id.textAuthor)
        val textBorrower: TextView = view.findViewById(R.id.textBorrower)
        val imageStatus: ImageView = view.findViewById(R.id.imageStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.textTitle.text = book.title
        holder.textAuthor.text = book.author

        if (book.status == 0) {
            // Available
            holder.imageStatus.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.green))
            holder.textBorrower.visibility = View.GONE
        } else {
            // Borrowed
            holder.imageStatus.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.textBorrower.visibility = View.VISIBLE
            
            // Fetch student name (this is not efficient for large lists, but fine for 40 books)
            // Ideally should join tables in query
            val studentName = book.studentId?.let { dbHelper.getStudentName(it) } ?: "Bilinmiyor"
            holder.textBorrower.text = "Alan: $studentName"
        }

        holder.itemView.setOnClickListener { onBookClick(book) }
        holder.itemView.setOnLongClickListener { 
            onBookLongClick(book)
            true
        }
    }

    override fun getItemCount() = books.size

    fun updateList(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}

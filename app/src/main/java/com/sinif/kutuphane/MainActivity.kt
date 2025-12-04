package com.sinif.kutuphane

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: BookAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewBooks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = BookAdapter(dbHelper.getAllBooks(), dbHelper, { book ->
            showActionDialog(book)
        }, { book ->
            showDeleteDialog(book)
        })
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddBook).setOnClickListener {
            showAddBookDialog()
        }
        
        refreshList()
    }

    private fun refreshList() {
        adapter.updateList(dbHelper.getAllBooks())
        val emptyView = findViewById<android.view.View>(R.id.emptyView)
        if (adapter.itemCount == 0) {
            emptyView.visibility = android.view.View.VISIBLE
        } else {
            emptyView.visibility = android.view.View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 1, 0, getString(R.string.add_student))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) {
            showAddStudentDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddBookDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_book, null)
        val editTitle = view.findViewById<EditText>(R.id.editBookTitle)
        val editAuthor = view.findViewById<EditText>(R.id.editBookAuthor)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.add_book))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val title = editTitle.text.toString()
                val author = editAuthor.text.toString()
                if (title.isNotEmpty()) {
                    dbHelper.addBook(title, author)
                    refreshList()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showAddStudentDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)
        val editName = view.findViewById<EditText>(R.id.editStudentName)
        val editNumber = view.findViewById<EditText>(R.id.editStudentNumber)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.add_student))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val name = editName.text.toString()
                val number = editNumber.text.toString()
                if (name.isNotEmpty()) {
                    dbHelper.addStudent(name, number)
                    Toast.makeText(this, "Öğrenci eklendi", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showActionDialog(book: Book) {
        if (book.status == 0) {
            // Available -> Issue
            showIssueDialog(book)
        } else {
            // Borrowed -> Return
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.return_book))
                .setMessage("${book.title} kitabını iade almak istiyor musunuz?")
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    dbHelper.returnBook(book.id)
                    refreshList()
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }
    }

    private fun showIssueDialog(book: Book) {
        val students = dbHelper.getAllStudents()
        if (students.isEmpty()) {
            Toast.makeText(this, "Önce öğrenci eklemelisiniz!", Toast.LENGTH_LONG).show()
            return
        }

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_issue_book, null)
        val spinner = view.findViewById<Spinner>(R.id.spinnerStudents)
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, students)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.issue_book))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val selectedStudent = spinner.selectedItem as Student
                dbHelper.issueBook(book.id, selectedStudent.id)
                refreshList()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    
    private fun showDeleteDialog(book: Book) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.confirm_delete))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                dbHelper.deleteBook(book.id)
                refreshList()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
}

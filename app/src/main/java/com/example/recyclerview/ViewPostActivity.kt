package com.example.recyclerview

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.ApiClient
import com.example.recyclerview.ApiInterface
import com.example.recyclerview.Post
import com.example.recyclerview.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Viewpostactivity : AppCompatActivity() {
    var postId = 0
    lateinit var tvPostTitle: TextView
    lateinit var tvPostBody: TextView
    lateinit var rvComments: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_viewpostactivity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent.extras != null) {
            postId = intent.extras!!.getInt("POST_ID")
            Toast.makeText(this, "$postId", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        tvPostTitle = findViewById(R.id.tvPostTitle)
        tvPostBody = findViewById(R.id.tvPostBody)
        rvComments = findViewById(R.id.rvComments)
        fetchPostById()
        fetchComments()
    }

    fun displayPost(post: Post) {
        tvPostTitle.text = post.title
        tvPostBody.text = post.body
    }

    fun fetchPostById() {
        val retroFit = ApiClient.buildApiClient(ApiInterface::class.java)
        val request = retroFit.getPostById(postId)
        request.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    response.body()?.let { displayPost(it) }
                } else {
                    Toast.makeText(baseContext, response.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun fetchComments() {
    }
}

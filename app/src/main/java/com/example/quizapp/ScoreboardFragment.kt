package com.example.quizapp


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.adapters.ScoreAdapter
import com.example.quizapp.models.User
import com.google.firebase.firestore.*

class ScoreboardFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var userArrayList: ArrayList<User>
    private lateinit var scoreAdapter: ScoreAdapter
    lateinit var db: FirebaseFirestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_scoreboard, container, false)

        recyclerView = view.findViewById(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()
        scoreAdapter = ScoreAdapter(userArrayList)

        recyclerView.adapter = scoreAdapter

        eventChangeListener()


        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun eventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("users")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userArrayList.add(dc.document.toObject(User::class.java))
                            userArrayList.sortByDescending { it.Score }
                        }
                    }
                    scoreAdapter.notifyDataSetChanged()
                }
            })
    }
}
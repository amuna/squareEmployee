package com.example.sqauretakehome.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqauretakehome.R
import com.example.sqauretakehome.domain.util.EmployeeListState
import com.example.sqauretakehome.domain.util.EmployeeListState.*
import com.example.sqauretakehome.di.Injector
import org.kodein.di.instance

class EmployeeFragment : Fragment() {

    private val TAG = "EmployeeFragment"

    companion object {
        fun newInstance() = EmployeeFragment()
        const val EMPTY_VIEW_TEXT = "Nothing to show."
    }

    private val viewModel by Injector.di.instance<EmployeeViewModel>()
    private lateinit var adapter : EmployeeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var emptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.employeeRecyclerView)
        progressBar = view.findViewById(R.id.progresBar)
        emptyView = view.findViewById(R.id.emptyListView)
        updateUi(LOADING)

        adapter = EmployeeAdapter(refreshListListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        subscribe()
    }


    private fun subscribe() {
        viewModel.employeeList.observe(viewLifecycleOwner, {
            adapter.items = it
            updateUi(VALID_LIST)
        })

        viewModel.emptyEmployeeList.observe(viewLifecycleOwner, {
            updateUi(EMPTY_LIST)
        })

        viewModel.error.observe(viewLifecycleOwner, {
            updateUi(ERROR, it)
        })
    }

    private fun updateUi(state: EmployeeListState, errorMsg: String? = null) {
        when(state) {
            LOADING -> {
                recyclerView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            }

            EMPTY_LIST -> {
                emptyView.text = EMPTY_VIEW_TEXT
                emptyView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                progressBar.visibility = View.GONE
            }

            ERROR -> {
                emptyView.text = errorMsg
                emptyView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                progressBar.visibility = View.GONE
            }

            VALID_LIST -> {
                recyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                emptyView.visibility = View.GONE
            }
        }
    }

    private val refreshListListener = {
        Log.d(TAG, "refresh button pressed")
        updateUi(LOADING)
        viewModel.getEmployees()
    }
}
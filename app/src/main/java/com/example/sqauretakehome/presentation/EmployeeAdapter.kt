package com.example.sqauretakehome.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sqauretakehome.R
import com.example.sqauretakehome.domain.Employee

class EmployeeAdapter(
    private val refreshListListener: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<Employee> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view :View
        return when (viewType) {
            REFRESH_HEADER -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.refresh_header_item, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.employee_list_item, parent, false)
                EmployeeViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            REFRESH_HEADER -> {
                (holder as HeaderViewHolder).bindHeader()
            }
            else -> {
                (holder as EmployeeViewHolder).bindEmployee(items[position - 1])
            }
        }

    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val headerItem: ConstraintLayout = view.findViewById(R.id.headerListItem)

        fun bindHeader() {
            headerItem.setOnClickListener {
                refreshListListener()
            }
        }
    }

    class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profilePicture: ImageView = view.findViewById(R.id.employeeImage)
        private val name: TextView = view.findViewById(R.id.employeeName)
        private val team: TextView = view.findViewById(R.id.employeeTeam)

        fun bindEmployee(employee: Employee) {
            name.text = employee.name
            team.text = employee.team
            Glide.with(itemView.context)
                .apply { RequestOptions() }
                .load(employee.photoSmall).into(profilePicture);
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1 //+1 for the extra header unit
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            REFRESH_HEADER
        } else {
            LIST_ITEM
        }
    }

    companion object {
        const val REFRESH_HEADER = 1
        const val LIST_ITEM = 2
    }
}
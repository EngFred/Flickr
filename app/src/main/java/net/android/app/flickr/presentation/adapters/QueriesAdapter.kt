package net.android.app.flickr.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.android.app.flickr.databinding.QueryItemBinding
import net.android.app.flickr.domain.modal.SearchQuery

class QueriesAdapter : Adapter<QueriesAdapter.QueryViewHolder>() {

    private var queryClickedListener: QueryClickedListener? = null
    fun initializeQueryClickListener(queryClickedListener: QueryClickedListener ) {
        this.queryClickedListener = queryClickedListener
    }

    inner class QueryViewHolder( private val binding: QueryItemBinding ) : ViewHolder( binding.root ) {
        fun bind( searchQuery: SearchQuery ) {
            binding.apply {
                queryTv.text = searchQuery.queryText
            }
            binding.root.setOnClickListener {
                try {
                    queryClickedListener?.onQueryClicked( searchQuery.queryText.trim() )
                } catch (ex: Exception ) {
                    Log.d("KOTLIN", "$ex")
                }
            }
            binding.deleteQueryIv.setOnClickListener {
                try {
                    queryClickedListener?.onQueryDeleted( searchQuery )
                } catch (ex: Exception ) {
                    Log.d("KOTLIN", "$ex")
                }
            }
        }
    }

    private val differCallback = object  : DiffUtil.ItemCallback<SearchQuery>() {
        override fun areItemsTheSame(oldItem: SearchQuery, newItem: SearchQuery): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchQuery, newItem: SearchQuery): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, differCallback )

    fun saveData( dataResponse: List<SearchQuery>){
        asyncListDiffer.submitList(dataResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        val binding = QueryItemBinding.inflate( LayoutInflater.from(parent.context), parent, false )
        return QueryViewHolder( binding )
    }

    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        val query = asyncListDiffer.currentList[position]
        holder.bind( query )
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    interface QueryClickedListener {
        fun onQueryClicked( queryText: String )
        fun onQueryDeleted( searchQuery: SearchQuery )
    }
}
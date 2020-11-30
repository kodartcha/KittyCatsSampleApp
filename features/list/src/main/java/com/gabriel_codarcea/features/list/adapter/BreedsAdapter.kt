package com.gabriel_codarcea.features.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.gabriel_codarcea.core.data.model.Breed
import com.gabriel_codarcea.features.list.R

class BreedsAdapter : RecyclerView.Adapter<BreedsAdapter.ListViewHolder?>(), Filterable {

    private var breedsList: MutableList<Breed> = mutableListOf()
    private var filteredBreedsList: MutableList<Breed> = mutableListOf()

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.breed_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val singleBreed: Breed = filteredBreedsList[position]
        singleBreed.image?.let {
            Glide.with(holder.image)
                .load(it)
                .transition(withCrossFade())
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(holder.image)
        } ?: holder.image.setImageResource(R.drawable.placeholder)

        holder.name.text = singleBreed.name
        holder.description.text = singleBreed.description
    }

    override fun getItemCount(): Int {
        return filteredBreedsList.size
    }

    fun setItems(listItems: MutableList<Breed>) {
        this.breedsList = listItems
        this.filteredBreedsList = listItems
        notifyDataSetChanged()
    }

    fun clearItems() {
        breedsList.clear()
        filteredBreedsList.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var image: ImageView
        var name: TextView
        var description: TextView
        override fun onClick(v: View) {
            onItemClickListener?.onItemClick(filteredBreedsList[adapterPosition].dbID)
        }

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.breed_image)
            name = itemView.findViewById(R.id.name)
            description = itemView.findViewById(R.id.description)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(breedID: Int)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredBreedsList = if (charSearch.isEmpty() || charSearch == "All") {
                    breedsList
                } else {
                    val resultList = ArrayList<Breed>()
                    for (breed in breedsList) {
                        if (breed.origin == charSearch) {
                            resultList.add(breed)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredBreedsList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.values?.let {
                    filteredBreedsList = it as MutableList<Breed>
                    notifyDataSetChanged()
                }
            }
        }
    }
}

package com.catganisation.ui.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.catganisation.data.models.Breed
import com.catganisation.ui.listeners.OnBreedImageLoad
import com.catganisation.ui.listeners.OnBreedItemSelect
import com.catganisation.ui.views.BreedItemView

class BreedAdapter(
    private val context: Context,
    private val onBreedItemSelect: OnBreedItemSelect,
    private val onBreedImageLoad: OnBreedImageLoad
) : RecyclerView.Adapter<BreedAdapter.BreedViewHolder>() {

    private val breeds: ArrayList<Breed> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val itemView: BreedItemView = BreedItemView(context)
        val layoutParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        itemView.layoutParams = layoutParams
        return BreedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return breeds.size
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.view.setContent(breeds[position])
        if(breeds[position].imageUrl == null) {
            onBreedImageLoad.loadImage(breeds[position])
        }
        holder.view.setOnClickListener {
            onBreedItemSelect.selectBreed(breeds[holder.adapterPosition].id)
        }
    }

    fun updateBreed(breed: Breed) {
        for((i, b) in breeds.withIndex()) {
            if(b.id == breed.id) {
                notifyItemChanged(i)
            }
        }
    }

    fun setBreeds(breeds: List<Breed>) {
        this.breeds.clear()
        this.breeds.addAll(breeds)
        notifyDataSetChanged()
    }

    class BreedViewHolder(val view: BreedItemView) : RecyclerView.ViewHolder(view)
}
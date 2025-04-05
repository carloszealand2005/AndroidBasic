package com.example.myapplication.api.frontend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.api.backend.apidatabase.SuperHeroItemResponse
import com.example.myapplication.databinding.ItemSuperheroBinding
import com.squareup.picasso.Picasso

class SuperHeroAdapter(var superHeroList : List<SuperHeroItemResponse> = emptyList(),
                       private val onItemSelected: (String) -> Unit) : RecyclerView.Adapter<SuperHeroViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_superhero, parent, false)
        return SuperHeroViewHolder(view)
    }

    override fun getItemCount() = superHeroList.size

    override fun onBindViewHolder(holder: SuperHeroViewHolder, position: Int) {
        val currentSuperHero = superHeroList[position]
        holder.render(currentSuperHero, onItemSelected)
    }

    fun updateList(newList : List<SuperHeroItemResponse>){
        superHeroList = newList
        notifyDataSetChanged()
    }

}



class SuperHeroViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemSuperheroBinding.bind(view)
    private val context = binding.root.context

    fun render(superHero: SuperHeroItemResponse, onItemSelected: (String) -> Unit){
        binding.tvSuperHeroName.text = superHero.name
        Picasso.get().load(superHero.image.url).into(binding.imageViewSuperHero);

        binding.root.setOnClickListener { onItemSelected(superHero.id) }
    }
}

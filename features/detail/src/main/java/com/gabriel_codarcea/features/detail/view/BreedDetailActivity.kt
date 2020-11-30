package com.gabriel_codarcea.features.detail.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.gabriel_codarcea.features.detail.R
import com.gabriel_codarcea.features.detail.databinding.ActivityBreedDetailBinding
import com.gabriel_codarcea.features.detail.viewmodel.BreedDetailViewModel
import kotlinx.android.synthetic.main.activity_breed_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BreedDetailActivity : AppCompatActivity() {

    private val viewModel by viewModel<BreedDetailViewModel>()

    private var wikiURL: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val breedID = getBreedId()

        initBindings()
        setObservables(breedID)
        initClickListener()
    }

    private fun initBindings() {
        val binding: ActivityBreedDetailBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_breed_detail
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setObservables(breedID: Int) {
        viewModel.breed.observe(this, {
            supportActionBar?.title = it.name
            wikiURL = it.wikipedia_url
        })
        fetchBreed(breedID)
    }

    private fun fetchBreed(breedID: Int) = viewModel.getBreedDetails(breedID)

    private fun getBreedId(): Int {
        intent?.data?.getQueryParameter("breedID")?.toIntOrNull()?.let {
            return it
        } ?: throw IllegalStateException("Incorrect breed ID in breed detail view!")
    }

    private fun initClickListener() {
        wikiButton.setOnClickListener {
            wikiURL?.let {
                val wikiURL = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(wikiURL)
            } ?: throw IllegalStateException("Incorrect wiki URL for breed!")
        }
    }
}

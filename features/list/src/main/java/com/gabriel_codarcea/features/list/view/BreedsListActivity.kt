package com.gabriel_codarcea.features.list.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabriel_codarcea.core.network.manager.BreedsManager
import com.gabriel_codarcea.features.list.R
import com.gabriel_codarcea.features.list.adapter.BreedsAdapter
import com.gabriel_codarcea.features.list.databinding.ActivityBreedsListBinding
import com.gabriel_codarcea.features.list.viewmodel.BreedsListViewModel
import kotlinx.android.synthetic.main.activity_breeds_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BreedsListActivity : AppCompatActivity(), BreedsAdapter.OnItemClickListener {

    private val viewModel by viewModel<BreedsListViewModel>()
    private val breedsManager: BreedsManager by inject()

    private lateinit var breedsAdapter: BreedsAdapter

    private var countries: MutableList<String?> = mutableListOf()
    private lateinit var originAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityBreedsListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_breeds_list)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initList()
        setObservables()
        initSwipeRefreshLayout()
        initCountrySpinner()
    }

    private fun initList() {
        breedsAdapter = BreedsAdapter()
        breedsAdapter.setOnItemClickListener(this)

        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = breedsAdapter
    }

    private fun setObservables() {
        // Observe breeds list data from viewModel
        viewModel.breedsList.observe(this, { breeds ->
            // Fill breeds list
            swipeToRefresh.isRefreshing = false
            breedsAdapter.clearItems()
            breedsAdapter.setItems(breeds.toMutableList())

            // Fill origin spinner with unique countries
            countries.clear()
            countries.add("All")
            countries.addAll(breeds.map { it.origin }.distinct().toMutableList())
            originAdapter.notifyDataSetChanged()
            originSpinner.setSelection(0)
        })

        // Observe downloading breeds live status from breeds manager
        breedsManager.downloadLiveStatus.observe(this, {
            liveStatus.text = it
        })

        viewModel.setBreedsManagerCallback()

        viewModel.checkBreedsDownloaded()
    }

    private fun initCountrySpinner() {
        originAdapter = ArrayAdapter(this, R.layout.origin_dropdown_item, countries)
        originAdapter.setDropDownViewResource(R.layout.origin_dropdown_item)
        originSpinner.adapter = originAdapter
        originSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                breedsAdapter.filter.filter(countries[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    override fun onItemClick(breedID: Int) {
        navigateToBreedDetails(breedID)
    }

    private fun navigateToBreedDetails(breedID: Int) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("app://breeds/breedDetails?breedID=$breedID")
        )
        startActivity(intent)
    }

    private fun initSwipeRefreshLayout() {
        swipeToRefresh.setOnRefreshListener {
            viewModel.refreshBreedsList()
        }
    }

    override fun onBackPressed() {
        breedsManager.cancelDownload()
        finish()
    }
}

package com.hour24.with.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.hour24.tb.adapter.GenericRecyclerViewAdapter
import com.hour24.with.R
import com.hour24.with.databinding.MainActivityBinding
import com.hour24.with.databinding.MainItemBinding
import com.hour24.with.model.MarvelModel
import com.hour24.with.view.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    private val TAG: String = MainActivity::class.java.name

    // DataBinding
    private lateinit var mBinding: MainActivityBinding
    private lateinit var mViewModel: MainViewModel

    // List
    private lateinit var mAdapter: GenericRecyclerViewAdapter<MarvelModel, MainItemBinding>
    private val mList = ArrayList<MarvelModel>()

    // Manager
    private val mLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        initDataBinding()
        initLayout()
        getData()

    }

    /**
     * Init DataBinding
     */
    private fun initDataBinding() {

        mViewModel = MainViewModel(this@MainActivity)
        mBinding.viewModel = mViewModel

    }

    /**
     * Init Layout
     */
    private fun initLayout() {

        // Adapter
        mAdapter = object : GenericRecyclerViewAdapter<MarvelModel, MainItemBinding>(this@MainActivity, R.layout.main_item, mList) {

            override fun onBindData(position: Int, model: MarvelModel, dataBinding: MainItemBinding) {

                try {

                    val viewModel = MainViewModel(this@MainActivity)
                    viewModel.mModel = model

                    dataBinding.viewModel = viewModel

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        mBinding.rvMain.adapter = mAdapter
        mBinding.rvMain.layoutManager = mLayoutManager

        // Adapter 주입
        mViewModel.mAdapter = mAdapter

    }

    private fun getData() {

        mViewModel.getData()

    }
}

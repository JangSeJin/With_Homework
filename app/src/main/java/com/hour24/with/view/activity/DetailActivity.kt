package com.hour24.with.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hour24.with.R
import com.hour24.with.databinding.DetailActivityBinding
import com.hour24.with.model.MarvelModel


class DetailActivity : AppCompatActivity() {

    private val TAG: String = DetailActivity::class.java.name

    // DataBinding
    private lateinit var mBinding: DetailActivityBinding
    private val mViewModel = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.detail_activity)

        initDataBinding()
        initLayout()
        getData()

    }

    /**
     * Init DataBinding
     */
    private fun initDataBinding() {

        mBinding.viewModel = mViewModel

    }

    /**
     * Init Layout
     */
    private fun initLayout() {

    }

    private fun getData() {

        mViewModel.getData()

    }


    inner class ViewModel {

        var mModel: MarvelModel? = null

        /**
         * onClick
         */
        fun onClick(v: View) {
            when (v.id) {
//                R.id.ll_content -> {
//
//                }
            }
        }

        /**
         * 마블 데이터 가져옴
         */
        fun getData() {

        }
    }
}

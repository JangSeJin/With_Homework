package com.hour24.with.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
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
        initIntent()

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

    /**
     * Intent 데이터
     */
    private fun initIntent() {

        try {

            val intent = intent
            val model: MarvelModel = intent.getSerializableExtra(MarvelModel::class.java.name) as MarvelModel
            mViewModel.mModel = model
            mViewModel.setScale()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class ViewModel {

        var mModel: MarvelModel? = null

        // 이미지 확대
        fun setScale() {
            Handler().postDelayed({
                try {
                    mBinding.pvDetail.setScale(mBinding.pvDetail.maximumScale, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 1000)
        }

    }
}

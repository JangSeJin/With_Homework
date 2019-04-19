package com.hour24.with.view.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hour24.tb.adapter.GenericRecyclerViewAdapter
import com.hour24.with.R
import com.hour24.with.databinding.MainActivityBinding
import com.hour24.with.databinding.MainItemBinding
import com.hour24.with.model.MarvelModel
import com.hour24.with.utils.ObjectUtils
import com.hour24.with.view.custom.ProgressDialog
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class MainActivity : AppCompatActivity() {

    private val TAG: String = MainActivity::class.java.name

    // DataBinding
    private lateinit var mBinding: MainActivityBinding
    private val mViewModel = ViewModel()

    // List
    private lateinit var mAdapter: GenericRecyclerViewAdapter<MarvelModel, MainItemBinding>
    private val mList = ArrayList<MarvelModel>()
    private var mElementList: List<Element> = ArrayList<Element>()

    // Manager
    private val mLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    // paging 처리
    private var mPageNo: Int = 1 // 페이지 넘버
    private val mPageSize: Int = 25 // 가져올 아이템 갯수
    private var mIsLoading: Boolean = false
    private var mIsLast: Boolean = false // 마지막 페이지 여부
    private var mLastItemVisibleFlag: Boolean = false

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

                    val viewModel = ViewModel()
                    viewModel.mModel = model

                    dataBinding.viewModel = viewModel

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        mBinding.rvMain.adapter = mAdapter
        mBinding.rvMain.layoutManager = mLayoutManager

    }

    private fun getData() {

        mViewModel.getData()

    }


    inner class ViewModel {

        var mModel: MarvelModel? = null

        /**
         * onClick
         */
        fun onClick(v: View, model: MarvelModel) {
            when (v.id) {
                R.id.ll_content -> {
                    // 상세로 이동
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra(MarvelModel::class.java.name, model)
                    startActivity(intent)
                }
            }
        }

        /**
         * 마블 데이터 가져옴
         */
        fun getData() {
            MarvelCrawling().execute()
        }

        /**
         * RecyclerView.OnScrollListener
         */
        var mOnScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastItemVisibleFlag) {
                    if (!mIsLast) {
                        mViewModel.getData()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = mLayoutManager.childCount
                val totalItemCount = mLayoutManager.itemCount
                val firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()

                if (!mIsLoading) {
                    mLastItemVisibleFlag = if (dy > 0) {
                        //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                        totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
                    } else {
                        // 전체 카운트랑 화면에 보여지는 카운트랑 같을때
                        visibleItemCount == totalItemCount
                    }
                }
            }
        }
    }

    /**
     * 웹사이트 크롤링
     */
    inner class MarvelCrawling : AsyncTask<Void, Void, ArrayList<MarvelModel>>() {

        private val mProgress = ProgressDialog(this@MainActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            mProgress.show()
        }

        override fun doInBackground(vararg void: Void): ArrayList<MarvelModel>? {

            val list = ArrayList<MarvelModel>()

            try {

                // 한번 불러온 데이터는 다시 불러오지 않는다.
                if (ObjectUtils.isEmpty(mElementList)) {

                    val doc = Jsoup.connect("https://www.thewrap.com/marvel-movies-ranked-worst-best-avengers-infinity-war-ant-man-venom-stan-lee-spider-man-into-the-spider-verse/").get()

                    // articleContents 영역 가져옴
                    val articleContents: Elements = doc
                            .select("div[class=gallery-wrap photos]")
                            .select("div[class=item-wrap]")

                    // 역순으로 변환
                    mElementList = articleContents.reversed()

                }

                // 페이지 계산
                val start = (mPageNo * mPageSize) - mPageSize // Start Index
                var end = (mPageNo * mPageSize) - 1 // End Index

                // 마지막 인덱스 처리
                val size = mElementList.size
                if (end > size) {
                    end = (size - 1)
                    mIsLast = true
                }

                for (i in start..end) {

                    val element = mElementList[i]

                    // 이미지 영역
                    val imageElement = element.select("div[class=image-wrap]")
                    val imageUrl = imageElement.select("a img").attr("data-src")

                    // 캡션 영역
                    val cationElement = element.select("div[class=caption]")

                    // 제목 처리
                    val cation = cationElement.text()
                    var title = cationElement.select("strong").text()

                    // 디스크립션 추출
                    val description = cation.replace(title, "").trim()

                    val index = title.indexOf(".")
                    if (index != -1) {
                        val rank = title.substring(0, index).toInt()
                        title = title.substring(index + 1, title.length).trim()

                        list.add(MarvelModel(rank, title, description, imageUrl))
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return list
        }

        override fun onPostExecute(result: ArrayList<MarvelModel>) {
            super.onPostExecute(result)

            // 리스트에 추가
            mList.addAll(result)
            mAdapter.notifyDataSetChanged()

            mProgress.dismiss()

            // 페이지 증가
            mPageNo++
        }
    }
}

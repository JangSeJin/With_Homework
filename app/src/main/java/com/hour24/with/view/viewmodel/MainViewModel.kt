package com.hour24.with.view.viewmodel

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hour24.tb.adapter.GenericRecyclerViewAdapter
import com.hour24.with.R
import com.hour24.with.databinding.MainItemBinding
import com.hour24.with.model.MarvelModel
import com.hour24.with.utils.ObjectUtils
import com.hour24.with.view.activity.DetailActivity
import com.hour24.with.view.custom.ProgressDialog
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class MainViewModel constructor(private val mContext: Context) {

    var mModel: MarvelModel? = null

    lateinit var mAdapter: GenericRecyclerViewAdapter<MarvelModel, MainItemBinding>

    // 마블페이지 Element
    private var mElementList: List<Element> = ArrayList<Element>()

    // 페이징 관련
    private var mPageNo: Int = 1 // 페이지 넘버
    private val mPageSize: Int = 25 // 가져올 아이템 갯수
    private var mIsLoading: Boolean = false
    private var mIsLast: Boolean = false // 마지막 페이지 여부
    private var mLastItemVisibleFlag: Boolean = false

    /**
     * onClick
     */
    fun onClick(v: View, model: MarvelModel) {
        when (v.id) {
            R.id.ll_content -> {
                // 상세로 이동
                val intent = Intent(mContext, DetailActivity::class.java)
                intent.putExtra(MarvelModel::class.java.name, model)
                mContext.startActivity(intent)
            }
        }
    }

    /**
     * 마블 데이터 가져옴
     */
    fun getData() {

        if (mIsLoading) {
            return
        }

        MarvelCrawling().execute()
    }

    /**
     * 웹사이트 크롤링
     */
    inner class MarvelCrawling : AsyncTask<Void, Void, ArrayList<MarvelModel>>() {

        private val mProgress = ProgressDialog(mContext)

        override fun onPreExecute() {
            super.onPreExecute()
            mIsLoading = true
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
            mAdapter.addAll(result)

            mProgress.dismiss()
            mPageNo++ // 페이지 증가
            mIsLoading = false

        }
    }

    /**
     * RecyclerView.OnScrollListener
     */
    var mOnScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastItemVisibleFlag) {
                if (!mIsLast) {
                    getData()
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

            val visibleItemCount = manager.childCount
            val totalItemCount = manager.itemCount
            val firstVisibleItem = manager.findFirstVisibleItemPosition()

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
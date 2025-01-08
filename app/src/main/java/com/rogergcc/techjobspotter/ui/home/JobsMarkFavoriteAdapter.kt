package com.rogergcc.techjobspotter.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.core.BaseViewHolder
import com.rogergcc.techjobspotter.databinding.ItemJobMarkedBinding
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class JobsMarkFavoriteAdapter(
//    private val itemClickListener: OnMovieClickListener,
    val jobsPositionDetailsAction: (jobPositionDomain: JobPositionUi) -> Unit,
    val jobMarkClickAction: (jobPositionDomain: JobPositionUi) -> Unit
) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    //    private var mItemsMovieResponse = emptyList<MovieResponse>()

    var mItems = listOf<JobPositionUi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            ItemJobMarkedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = RemoteJobsSpottViewHolder(itemBinding, parent.context)

//        itemBinding.root.setOnClickListener {
//            val position =
//                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
//                    ?: return@setOnClickListener
//            movieDetailsAction(mItems[position])
//        }


        itemBinding.mark.setOnClickListener {
            val position =
                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener
            jobMarkClickAction(mItems[position])
        }


        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is RemoteJobsSpottViewHolder -> holder.bind(mItems[position])
        }
    }

    override fun getItemCount(): Int = mItems.size

    private inner class RemoteJobsSpottViewHolder(
        val binding: ItemJobMarkedBinding,
        val context: Context,
    ) : BaseViewHolder<JobPositionUi>(binding.root) {

        private val coroutineScope = CoroutineScope(Dispatchers.Main)
        override fun bind(item: JobPositionUi) {

            binding.apply {
                tvTitle.text = item.title
                tvCompany.text = item.companyName

                coroutineScope.launch {
                    photoPreview.load(item.companyLogo) {

                        crossfade(true)
                        crossfade(1000)
                        delay(1500)
                        error(R.drawable.ic_round_business_center_24)
                            .target(
                                onStart = { placeholder ->
                                    progress.startShimmer()
                                    progress.visibility = View.VISIBLE
                                    photoPreview.setImageDrawable(placeholder)

                                }, onError = { error ->
                                    progress.stopShimmer()
                                    progress.visibility = View.GONE
                                    photoPreview.setImageDrawable(error)
                                }, onSuccess = { result ->
                                    progress.stopShimmer()
                                    progress.visibility = View.GONE
                                    photoPreview.setImageDrawable(result)
                                })
                    }
                }


            }
        }
    }
}
package com.rogergcc.techjobspotter.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.core.BaseViewHolder
import com.rogergcc.techjobspotter.databinding.ItemJobBinding
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class JobsOkAdapter(
//    private val itemClickListener: OnMovieClickListener,
    val jobsPositionDetailsAction: (jobPositionDomain: JobPositionUi) -> Unit,
    val jobMarkClickAction: (jobPositionDomain: JobPositionUi) -> Unit,
) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    //    private var mItemsMovieResponse = emptyList<MovieResponse>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = RemoteJobsSpottViewHolder(itemBinding, parent.context)

//        itemBinding.root.setOnClickListener {
//            val position =
//                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
//                    ?: return@setOnClickListener
//            movieDetailsAction(mItems[position])
//        }
        itemBinding.mLinearLayoutContainer.setOnClickListener {
            val position =
                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener
            jobsPositionDetailsAction(mItems[position])
        }

        itemBinding.mark.setOnClickListener {
            val position =
                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener
            val job = mItems[position]
            val isMarked = !job.isMarked
            jobMarkClickAction(job.copy(isMarked = isMarked))

        }


        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is RemoteJobsSpottViewHolder -> holder.bind(mItems[position])
        }
    }

    override fun getItemCount(): Int = mItems.size
    var mItems = listOf<JobPositionUi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    fun updateMarkIcon(job: JobPositionUi, isMarked: Boolean) {
        val position = mItems.indexOfFirst { it.id == job.id }
        if (position != -1) {
            mItems[position].isMarked = isMarked // Update the isMarked property
            notifyItemChanged(position)
        }
    }

    private inner class RemoteJobsSpottViewHolder(
        val binding: ItemJobBinding,
        val context: Context,
    ) : BaseViewHolder<JobPositionUi>(binding.root) {

        private val coroutineScope = CoroutineScope(Dispatchers.Main)
        override fun bind(item: JobPositionUi) {

            binding.apply {
                tvTitle.text = item.title
                tvCompany.text = item.companyName
                tvLocation.text = item.candidateRequiredLocation
                tvType.text = item.jobType
                tvDate.text = item.publicationDate

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


                mark.setColorFilter(
                    if (item.isMarked) ContextCompat.getColor(context, R.color.green_100)
                    else ContextCompat.getColor(context, R.color.gray_200)
                )
                mark.setImageResource(
                    if (item.isMarked) R.drawable.ic_marked
                    else R.drawable.ic_mark
                )
//                mark.setOnClickListener() {
//                                        Toast.makeText(context, "Marked", Toast.LENGTH_SHORT).show()
//                }

            }
        }
    }
}
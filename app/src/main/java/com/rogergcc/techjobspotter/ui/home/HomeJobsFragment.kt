package com.rogergcc.techjobspotter.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.core.Resource
import com.rogergcc.techjobspotter.data.cache.JobsAssetsRepository
import com.rogergcc.techjobspotter.data.mappers.JobMapper
import com.rogergcc.techjobspotter.databinding.FragmentHomeJobsBinding
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.model.JobPosition
import com.rogergcc.techjobspotter.domain.usecase.JobsPositionUseCase
import com.rogergcc.techjobspotter.ui.presentation.GetJobsViewModel
import com.rogergcc.techjobspotter.ui.presentation.JobViewModelFactory
import com.rogergcc.techjobspotter.ui.utils.provider.ContextProviderImpl


class HomeJobsFragment : Fragment(R.layout.fragment_home_jobs) {
    private var _binding: FragmentHomeJobsBinding? = null

    private val binding get() = _binding!!

//    private val contextProvider = object : ContextProvider {
//        override fun getContext(): Context = requireContext()
//    }
    private val contextProvider by lazy { ContextProviderImpl(requireContext()) }
    private val jobsMapperProvider = object : JobsMapperProvider {
        override fun getJobsMapper(): JobMapper = JobMapper()
    }

    //    private val viewModel: MainViewModel by viewModels()
    private val jobsApiRepository by lazy { JobsAssetsRepository(
        contextProvider,
        jobsMapperProvider
    ) }

    private val jobsUseCase by lazy { JobsPositionUseCase(jobsApiRepository) }

    private val viewModel by viewModels<GetJobsViewModel> {
        JobViewModelFactory(
            jobsUseCase
        )
    }
    private val mAdapterRecommendJobs by lazy {
        JobsOkAdapter(
            jobsPositionDetailsAction = { job ->
                goToPositionDetailsView(job)
            },
            jobMarkClickAction = { job ->
                onFavoriteAction(job)
            },

        )
    }


    private val mAdapterMarkedJobs by lazy {
        JobsMarkFavoriteAdapter(
            jobsPositionDetailsAction = { job ->
                goToPositionDetailsView(job)
            },
            jobMarkClickAction = { job ->
                onFavoriteAction(job)
            }
        )
    }

    private fun goToPositionDetailsView(jobPositionModel: JobPosition) {
        Log.d(TAG, "prevention $jobPositionModel")
        jobPositionModel.title?.let {

            Snackbar.make(
                binding.root,
                "Go to details ${jobPositionModel.title}",
                Snackbar.LENGTH_SHORT
            ).show()

        }
//        onMark(1, jobPositionModel.title ?: "y")


    }
    private fun onMark(mark: Int, title: String) {

        Snackbar.make(
            binding.root,
            if (mark == 0) "\uD83D\uDE13 Unmark $title" else "\uD83D\uDE0D Marked $title",
            Snackbar.LENGTH_SHORT
        ).show()


    }


    private fun onFavoriteAction(job: JobPosition) {
//        job.title?.let { onMark(1, it) }
        val isMarked = mAdapterMarkedJobs.mItems.contains(job)

        if (isMarked) {
            mAdapterMarkedJobs.mItems = mAdapterMarkedJobs.mItems.toMutableList().apply {
                remove(job)
            }
        } else {
            mAdapterMarkedJobs.mItems = mAdapterMarkedJobs.mItems.toMutableList().apply {
                add(job)
            }
        }
        mAdapterRecommendJobs.updateMarkIcon(job, !isMarked)
        onMark(if (isMarked) 0 else 1, job.title ?: "y")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeJobsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRecommendedJobs.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapterRecommendJobs
        }

        binding.rvMarkedJobs.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false  )
            adapter = mAdapterMarkedJobs

        }


//        binding.toolbar.visibility = View.GONE
        observeRecommendPositions()
        viewModel.fetchJobs()
//        binding.recyclerView.scheduleLayoutAnimation()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchJobs()


        }
        if (binding.rvMarkedJobs.adapter==null || binding.rvMarkedJobs.adapter?.itemCount == 0) {
            binding.shimmerMarkedLayout.visibility = View.GONE
        }

    }







    private fun showLoadingState() {
        binding.swipeRefresh.isRefreshing = true
//        binding.emptyView.visibility = View.GONE
        binding.errorStateView.root.visibility = View.GONE
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.shimmerFrameLayout.startShimmer()
        binding.contentLayout.visibility = View.GONE
    }
    private fun showErrorState(exception: Exception) {
        hideLoadingState()
//        binding.textEmptyErr.text = resources.getString(R.string.error_message)
        binding.errorStateView.tvErrorStateMessage.text = resources.getString(R.string.error_message)
//        binding.emptyView.visibility = View.VISIBLE
        binding.errorStateView.root.visibility = View.VISIBLE
        Log.e(TAG, "Error: $exception")
    }
    private fun updateRecommendedJobsWithMarkedStatus(recommendedJobs: List<JobPosition>, markedJobs: List<JobPosition>): List<JobPosition> {
        val markedJobTitles = markedJobs.map { it.title }
        return recommendedJobs.map { job ->
            job.copy(isMarked = markedJobTitles.contains(job.title))
        }
    }
    private fun observeRecommendPositions() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it.asString(requireContext() ), Toast.LENGTH_SHORT).show()
        }

        viewModel.resourceJobs.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                   showLoadingState()
                }

                is Resource.Success -> {
                    hideLoadingState()
                    val list = result.data
                    Log.i("HomeJob", "observeRecommendPositions ${list.count()}")
                    //                    binding.rvMovies.adapter = concatAdapter
//                    mAdapterRecommendJobs.mItems = list

                    // Update recommended jobs with marked status
                    val updatedList = updateRecommendedJobsWithMarkedStatus(list, mAdapterMarkedJobs.mItems)
                    mAdapterRecommendJobs.mItems = updatedList

                    if (list.isEmpty()) { //<-- status result is FALSE
//                        binding.emptyView.visibility = View.VISIBLE
//                        binding.textEmptyErr.text = resources.getString(R.string.error_message_no_data)

                        binding.errorStateView.root.visibility = View.VISIBLE
                        binding.errorStateView.tvErrorStateMessage.text = resources.getString(R.string.error_message_no_data)

                    } else {
//                        binding.emptyView.visibility = View.GONE
                        binding.errorStateView.root.visibility = View.GONE
                    }
                }

                is Resource.Failure -> {
                    showErrorState(result.exception)

                }
            }
        }
    }


    private fun hideLoadingState() {
        binding.swipeRefresh.isRefreshing = false
//        binding.emptyView.visibility = View.GONE
        binding.errorStateView.root.visibility = View.GONE
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.shimmerFrameLayout.stopShimmer()
        binding.contentLayout.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = "HomeJobsFragment"
    }


}
package com.rogergcc.techjobspotter.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.core.Resource
import com.rogergcc.techjobspotter.data.cache.JobsPositionCache
import com.rogergcc.techjobspotter.data.cache.database.AppDatabase
import com.rogergcc.techjobspotter.data.cloud.JobsRemoteRepository
import com.rogergcc.techjobspotter.data.cloud.api.JobsApiInstance
import com.rogergcc.techjobspotter.data.mappers.JobMapper
import com.rogergcc.techjobspotter.databinding.FragmentHomeJobsBinding
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.usecase.JobsPositionUseCase
import com.rogergcc.techjobspotter.ui.presentation.GetJobsViewModel
import com.rogergcc.techjobspotter.ui.presentation.JobViewModelFactory
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import com.rogergcc.techjobspotter.ui.utils.extensions.hideView
import com.rogergcc.techjobspotter.ui.utils.extensions.showView
import com.rogergcc.techjobspotter.ui.utils.provider.ContextProviderImpl


class HomeJobsFragment : Fragment(R.layout.fragment_home_jobs) {
    private lateinit var jobsPositionCache: List<JobPositionUi>

    private var _binding: FragmentHomeJobsBinding? = null

    private val binding get() = _binding!!

    //    private val contextProvider = object : ContextProvider {
//        override fun getContext(): Context = requireContext()
//    }
    private val contextProvider by lazy { ContextProviderImpl(requireContext()) }
    private val jobsMapperProvider = object : JobsMapperProvider {
        override fun getJobsMapper(): JobMapper = JobMapper()
    }

//    private val jobsApiRepository by lazy { JobsAssetsRepository(
//        contextProvider,
//        jobsMapperProvider
//    ) }

    private val jobsApiRepository by lazy {
        JobsRemoteRepository(
            JobsApiInstance.retrofitService,
            jobsMapperProvider
        )
    }
    private val jobsDaoCache by lazy {
        AppDatabase.getDatabase(contextProvider.getContext()).jobDao()
    }
    private val jobsCacheRepository by lazy {
        JobsPositionCache(
            jobsDaoCache,
            jobsMapperProvider
        )
    }

    private val jobsUseCase by lazy {
        JobsPositionUseCase(
            jobsApiRepository,
            jobsCacheRepository
        )
    }

    private val viewModel by viewModels<GetJobsViewModel> {
        JobViewModelFactory(
            jobsUseCase,
            jobsMapperProvider
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

    private fun goToPositionDetailsView(jobPositio: JobPositionUi) {
        Log.d(TAG, "prevention $jobPositio")
        Snackbar.make(
            binding.root, "Go to details ${jobPositio.title}",
            Snackbar.LENGTH_SHORT
        ).show()

//        val bundle = Bundle().apply {
//            putParcelable("jobPosition", jobPositionModel)
//        }

//        val action = HomeJobsFragmentDirections.actionHomeJobsFragmentToDetailsFragment(jobPositio)
        val bundle = bundleOf("selectedJobPosition" to jobPositio)
//        findNavController().navigate(action)
        findNavController().navigate(R.id.action_homeJobsFragment_to_detailsFragment, bundle)
//        onMark(1, jobPositionModel.title ?: "y")


    }

    private fun showMessage(isMarked: Boolean, title: String? = "") {
        if (!isMarked) {
            Snackbar.make(
                binding.root, "\uD83D\uDE13 Unmark $title",
                Snackbar.LENGTH_SHORT
            ).show()
        } else {
            Snackbar.make(
                binding.root, "\uD83D\uDE0D Marked $title",
                Snackbar.LENGTH_SHORT
            ).show()
        }


    }

    private fun observeMarkedJobSelected() {
        viewModel.markedJobPosition.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoadingState()
                }

                is Resource.Success -> {
                    hideLoadingState()
                    val job = result.data
                    mAdapterMarkedJobs.updateMarkIcon(job, job.isMarked)
                    Log.i(TAG, "observeMarkedJobs Jobs Found: ${job.title}")
                    if (job.isMarked) {
                        mAdapterMarkedJobs.mItems =
                            mAdapterMarkedJobs.mItems.toMutableList().apply {
                                add(job)
                            }
                    } else {
                        mAdapterMarkedJobs.mItems =
                            mAdapterMarkedJobs.mItems.toMutableList().apply {
                                remove(job)

                            }
                    }
//                    mAdapterMarkedJobs.mItems = jobsPositionCache
                    mAdapterRecommendJobs.updateMarkIcon(job, job.isMarked)
                    showMessage(job.isMarked, job.title)

                }

                is Resource.Failure -> {
                    hideLoadingState()
                    showErrorState(
                        result.exception,
                        resources.getString(R.string.error_message)
                    )
                }

            }
        }
    }

    private fun onFavoriteAction(job: JobPositionUi) {
        val isMarked = jobsPositionCache.contains(job)


        Log.d(TAG, "onFavoriteAction: mark favorite ${job.title} $isMarked")
        viewModel.markFavoriteJobPosition(job)

//        if (isMarked) {
//            mAdapterMarkedJobs.mItems = mAdapterMarkedJobs.mItems.toMutableList().apply {
//                remove(job)
//            }
//        } else {
//            mAdapterMarkedJobs.mItems = mAdapterMarkedJobs.mItems.toMutableList().apply {
//                add(job)
//            }
//        }
//
//        mAdapterRecommendJobs.updateMarkIcon(job, !isMarked)
//        onMark(if (isMarked) 0 else 1, job.title ?: "y")

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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapterMarkedJobs

        }


//        binding.toolbar.visibility = View.GONE
        observeLocalJobs()
        observeRecommendPositions()
        observeMarkedJobSelected()

        viewModel.fetchLocalJobsPositions()
        if (viewModel.remoteJobsPosition.value == null)
            viewModel.fetchRemoteJobsPositions()


//        binding.recyclerView.scheduleLayoutAnimation()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchLocalJobsPositions()
            viewModel.fetchRemoteJobsPositions()
//            showLoadingState()

        }
//        if (binding.rvMarkedJobs.adapter == null ||
//            binding.rvMarkedJobs.adapter?.itemCount == 0) {
////            binding.shimmerMarkedLayout.visibility = View.GONE
//            binding.shimmerMarkedLayout.hideView()
//        }

    }


    private fun hideLoadingState() {
        binding.swipeRefresh.isRefreshing = false
        //binding.errorStateView.root.hideView()

        binding.rvMarkedJobs.showView()
        binding.rvRecommendedJobs.showView()

        binding.shimmerRecommendedLayout.hideView()
        binding.shimmerRecommendedLayout.stopShimmer()

        binding.shimmerMarkedLayout.hideView()
        binding.shimmerMarkedLayout.stopShimmer()
    }

    private fun showLoadingState() {
        binding.swipeRefresh.isRefreshing = true
        binding.errorStateView.root.hideView()

        binding.rvMarkedJobs.hideView()
        binding.rvRecommendedJobs.hideView()

        binding.shimmerRecommendedLayout.showView()
        binding.shimmerRecommendedLayout.startShimmer()

        binding.shimmerMarkedLayout.showView()
        binding.shimmerMarkedLayout.startShimmer()
    }


    private fun showErrorState(exception: Exception, messageError: String) {

        binding.errorStateView.tvErrorStateMessage.text = messageError
        binding.errorStateView.root.showView()
        Log.e(TAG, "Error: $exception")
    }

    private fun updateMarkedStatus(
        recommendedJobs: List<JobPositionUi>,
        markedJobs: List<JobPositionUi>,
    ): List<JobPositionUi> {
        val markedJobTitles = markedJobs.map { it.title }
        return recommendedJobs.map { job ->
            job.copy(isMarked = markedJobTitles.contains(job.title))
        }
    }

    private fun observeLocalJobs() {
        viewModel.localJobsPosition.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoadingState()
                }

                is Resource.Success -> {
                    hideLoadingState()
                    jobsPositionCache = result.data
                    Log.i(TAG, "observeLocalJobs Jobs Found: ${jobsPositionCache.count()}")
                    mAdapterMarkedJobs.mItems = jobsPositionCache

                }

                is Resource.Failure -> {
                    hideLoadingState()
                    showErrorState(
                        result.exception,
                        resources.getString(R.string.error_message)
                    )
                }

            }
        }
    }


    private fun observeRecommendPositions() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it.asString(requireContext()), Toast.LENGTH_SHORT).show()
        }

        viewModel.remoteJobsPosition.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoadingState()
                }

                is Resource.Success -> {
                    hideLoadingState()
                    val jobsOnfly = result.data
                    Log.i(TAG, "observeRecommendPositions Jobs Found: ${jobsOnfly.count()}")
                    if (jobsOnfly.isEmpty()) {
                        showErrorState(
                            Exception("No data found"),
                            resources.getString(R.string.error_message_no_data)
                        )
                        return@observe
                    }

                    //binding.rvMovies.adapter = concatAdapter

                    // Update recommended jobs with marked status
                    val jobsUpdateShowMarkedList = updateMarkedStatus(jobsOnfly, mAdapterMarkedJobs.mItems)
                    mAdapterRecommendJobs.mItems = jobsUpdateShowMarkedList


                }

                is Resource.Failure -> {
                    hideLoadingState()
                    showErrorState(
                        result.exception,
                        resources.getString(R.string.error_message)
                    )

                }

            }
        }
    }


    companion object {
        private const val TAG = "HomeJobsFragment"
    }


}
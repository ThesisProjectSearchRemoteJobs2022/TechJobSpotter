package com.rogergcc.techjobspotter.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.data.cache.JobsPositionCache
import com.rogergcc.techjobspotter.data.cache.database.AppDatabase
import com.rogergcc.techjobspotter.data.cloud.JobsRemoteRepository
import com.rogergcc.techjobspotter.data.cloud.api.JobsApiInstance
import com.rogergcc.techjobspotter.data.mappers.JobMapper
import com.rogergcc.techjobspotter.databinding.FragmentHomeJobsBinding
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.usecase.JobsPositionUseCase
import com.rogergcc.techjobspotter.ui.details.JobsMarkFavoriteAdapter
import com.rogergcc.techjobspotter.ui.presentation.GetJobsViewModel
import com.rogergcc.techjobspotter.ui.presentation.JobsPositionViewModelFactory
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import com.rogergcc.techjobspotter.ui.provider.ContextProviderImpl
import com.rogergcc.techjobspotter.ui.utils.extensions.hideView
import com.rogergcc.techjobspotter.ui.utils.extensions.showSnackbarShort
import com.rogergcc.techjobspotter.ui.utils.extensions.showView
import kotlinx.coroutines.launch


class HomeJobsFragment : Fragment(R.layout.fragment_home_jobs) {
    private lateinit var jobsPositionCache: List<JobPositionUi>
    private var _binding: FragmentHomeJobsBinding? = null

    private val binding get() = _binding!!

    //    private val contextProvider = object : ContextProvider {
//        override fun getContext(): Context = requireContext()
//    }
    private val contextProvider by lazy { ContextProviderImpl(requireContext()) }
    private val jobsMapperProvider = object : JobsMapperProvider {
        override fun provider(): JobMapper = JobMapper()
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
        JobsPositionViewModelFactory(
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
        Log.d(TAG, "prevention job marked? ${jobPositio.isMarked}")
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
            requireContext().showSnackbarShort(binding.root, "\uD83D\uDE13 Unmark $title")

        } else {

            requireContext().showSnackbarShort(binding.root, "\uD83D\uDE0D Marked $title")
        }
    }
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
                .collect{state->
                    when (state) {
                        is GetJobsViewModel.UiState.Loading -> {
                            showLoadingState()
                        }
                        is GetJobsViewModel.UiState.Success -> {
                            hideLoadingState()
                            state.remoteJobsPosition?.let { jobsOnfly ->
                                Log.i(TAG, "observeRecommendPositions Jobs Found: ${jobsOnfly.count()}")
                                if (jobsOnfly.isEmpty()) {
                                    showErrorState(
                                        Exception("No data found"),
                                        resources.getString(R.string.error_message_no_data)
                                    )
                                    return@collect
                                }
                                val jobsUpdateShowMarkedList = updateMarkedStatus(jobsOnfly, mAdapterMarkedJobs.mItems)
                                mAdapterRecommendJobs.mItems = jobsUpdateShowMarkedList
                            }
                            state.localJobsPosition?.let { jobsPositionCache ->
                                Log.i(TAG, "observeLocalJobs Jobs Found: ${jobsPositionCache.count()}")
                                mAdapterMarkedJobs.mItems = jobsPositionCache
                            }
                            state.markedJobPosition?.let { job ->
                                mAdapterMarkedJobs.updateMarkIcon(job, job.isMarked)
                                if (job.isMarked) {
                                    mAdapterMarkedJobs.mItems = mAdapterMarkedJobs.mItems.toMutableList().apply {
                                        add(job)
                                    }
                                } else {
                                    mAdapterMarkedJobs.mItems = mAdapterMarkedJobs.mItems.toMutableList().apply {
                                        remove(job)
                                    }
                                }
                                mAdapterRecommendJobs.updateMarkIcon(job, job.isMarked)
                                showMessage(job.isMarked, job.title)
                            }
                        }
                        is GetJobsViewModel.UiState.Failure -> {
                            hideLoadingState()
                            showErrorState(state.errorMessage.asException(requireContext()), state.errorMessage.asString(requireContext()))
                        }
                    }

                }
        }


    }


    private fun onFavoriteAction(job: JobPositionUi) {
//        val isMarked = jobsPositionCache.contains(job)
        Log.d(TAG, "onFavoriteAction: mark[ ${job.isMarked} ] favorite ${job.title} ")
        viewModel.markFavoriteJobPosition(job)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
//        viewModel.fetchRemoteJobsPositions()
    // desventaja si cambio favorite desde details
    //  y regresa este no actualiza el icono marcado en lista remota jobs
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
        viewModel.fetchLocalJobsPositions()
//        if (viewModel.remoteJobsPosition.value == null)
//            viewModel.fetchRemoteJobsPositions()

        viewModel.fetchRemoteJobsPositions()

//        binding.recyclerView.scheduleLayoutAnimation()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchLocalJobsPositions()
            viewModel.fetchRemoteJobsPositions()
//            showLoadingState()
        }
        observeUiState()
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

    companion object {
        private const val TAG = "HomeJobsFragment"
    }


}
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
import com.rogergcc.techjobspotter.data.cloud.RemoteIJobDataSourceImpl
import com.rogergcc.techjobspotter.data.cloud.model.RemoteJobsResponse
import com.rogergcc.techjobspotter.databinding.FragmentHomeJobsBinding
import com.rogergcc.techjobspotter.domain.GetJobsUseCase
import com.rogergcc.techjobspotter.domain.Job
import com.rogergcc.techjobspotter.domain.JobsMapper
import com.rogergcc.techjobspotter.ui.presentation.GetJobsViewModel
import com.rogergcc.techjobspotter.ui.presentation.JobViewModelFactory
import com.rogergcc.techjobspotter.ui.utils.loadJSONFromAsset


class HomeJobsFragment : Fragment(R.layout.fragment_home_jobs) {
    private var jobsMocks: List<Job>? = null
    private var _binding: FragmentHomeJobsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //    private val viewModel: MainViewModel by viewModels()
    private val viewModel by viewModels<GetJobsViewModel> {
        JobViewModelFactory(
            GetJobsUseCase(
                JobsMapper(),
                RemoteIJobDataSourceImpl()
            )
        )
    }
    private val mAdapterRecommendJobs by lazy {
        JobsOkAdapter() { movie ->
            goToMovieDetailsView(movie)
        }
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
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapterRecommendJobs
        }
//        binding.toolbar.visibility = View.GONE
        jobsMocks = getJobsFromAssets()
        observePopularMoviesList()
        viewModel.fetchJobs()
//        binding.recyclerView.scheduleLayoutAnimation()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchJobs()
        }

    }


    private fun onMark(mark: Int, title: String) {
        Snackbar.make(
            binding.root,
            if (mark == 0) "\uD83D\uDE13 Unmark $title" else "\uD83D\uDE0D Marked $title",
            Snackbar.LENGTH_SHORT
        ).show()
    }



    private fun goToMovieDetailsView(jobModel: Job) {
        Log.d(TAG, "prevention $jobModel")
        //showToast(requireContext(), "prevention $jobModel")
        jobModel.title?.let { onMark(0, it) }
//        onMark(0, jobModel.title ?: "x")
        onMark(1, jobModel.title ?: "y")


    }

    private fun observePopularMoviesList() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it.asString(requireContext() ), Toast.LENGTH_SHORT).show()
        }

        viewModel.resourceJobs.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.emptyView.visibility = View.GONE
                    binding.shimmer.visibility = View.VISIBLE
                    binding.shimmer.startShimmer()
                    binding.contentLayout.visibility = View.GONE
                }

                is Resource.Success -> {
                    hideSearch()
                    val list = result.data
                    Log.i("HomeJob", "observePopularMoviesList ${list.count()}")
                    //                    binding.rvMovies.adapter = concatAdapter
                    mAdapterRecommendJobs.mItems = jobsMocks ?: emptyList()

                    if (list.isEmpty()) { //<-- status result is FALSE
                        binding.textEmptyErr.text = resources.getString(R.string.error_message_no_data)
                        binding.emptyView.visibility = View.VISIBLE
                    } else {
                        binding.emptyView.visibility = View.GONE
                    }
                }

                is Resource.Failure -> {
                    hideSearch()

                    binding.textEmptyErr.text = resources.getString(R.string.error_message)
                    binding.emptyView.visibility = View.VISIBLE
                    Log.e(TAG, "Error: ${result.exception} ")

                }
            }
        }
    }

    private fun getJobsFromAssets(): List<Job> {
        val jobsDomainList: MutableList<Job> = mutableListOf()
        try {
            // Carga el JSON desde assets y lo convierte en un objeto RemoteJobsResponse
            val remoteJobsResponse: RemoteJobsResponse? =
                context?.loadJSONFromAsset("mock_response.json")
//            Log.d(TAG, "getJobsFromAssets: $remoteJobsResponse" )
            // acceder a los datos del objeto RemoteJobsResponse
            val jobsDto = remoteJobsResponse?.jobDtos
//            Log.d(TAG, "getJobsFromAssets:$jobsDto ")
            jobsDto?.forEach { jobDto ->
                val jobDomain = jobDto?.let { JobsMapper().dtoToDomain(it) }
                jobsDomainList.add(jobDomain!!)
            }
            return jobsDomainList.toList()
        } catch (e: Exception) {
            Log.e(TAG, "getJobsFromAssets: ${e.message}")

            return jobsDomainList.toList()
        }

    }

    private fun hideSearch() {
        binding.swipeRefresh.isRefreshing = false
        binding.emptyView.visibility = View.GONE
        binding.shimmer.visibility = View.GONE
        binding.shimmer.stopShimmer()
        binding.contentLayout.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = "HomeJobsFragment"
    }


}
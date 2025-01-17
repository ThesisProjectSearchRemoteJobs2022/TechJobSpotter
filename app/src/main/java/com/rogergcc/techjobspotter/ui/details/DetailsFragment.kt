package com.rogergcc.techjobspotter.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.core.Resource
import com.rogergcc.techjobspotter.data.cache.JobsPositionCache
import com.rogergcc.techjobspotter.data.cache.database.AppDatabase
import com.rogergcc.techjobspotter.data.mappers.JobMapper
import com.rogergcc.techjobspotter.databinding.FragmentDetailsBinding
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.usecase.JobsPositionCacheUseCase
import com.rogergcc.techjobspotter.ui.presentation.JobPositionViewModel
import com.rogergcc.techjobspotter.ui.presentation.JobPositionViewModelFactory
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import com.rogergcc.techjobspotter.ui.utils.UiText
import com.rogergcc.techjobspotter.ui.utils.provider.ContextProviderImpl
import com.rogergcc.techjobspotter.ui.utils.setTextHtml

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val contextProvider by lazy { ContextProviderImpl(requireContext()) }
    private val jobsMapperProvider = object : JobsMapperProvider {
        override fun provider(): JobMapper = JobMapper()
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
        JobsPositionCacheUseCase(
            jobsCacheRepository
        )
    }

    private val viewModel by viewModels<JobPositionViewModel> {
        JobPositionViewModelFactory(
            jobsUseCase,
            jobsMapperProvider
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val restaurantDisplayItem = arguments
//            ?.getParcelable("jobPosition", JobPositionUi::class.java)

//        val jobPositionUi = arguments?.getBundle("selectedJobPosition") as JobPositionUi
        val jobPositionUi = arguments?.getParcelable<JobPositionUi>("selectedJobPosition")
        Log.d(TAG, "------------------------")
        Log.d(TAG, "jobPositionUi: logo ${jobPositionUi?.companyLogo}")
//        Log.d(TAG, "jobPositionUi: logo url ${jobPositionUi?.companyLogoUrl}")

//        val person = arguments?.getParcelable<Person>("person")
//        var person = intent?.extras?.getParcellable<JobPositionUi>("jobPosition")

        viewModel.checkJobMarked(jobPositionUi!!)
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it.asString(requireContext()), Toast.LENGTH_SHORT).show()
        }
        viewModel.markedJobPosition.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Failure -> {
                    Log.d("DetailsFragment", "Resource.Failure")
                    Log.d("DetailsFragment", "resource: ${resource.exception}")
                }
                is Resource.Loading -> {
                    Log.d("DetailsFragment", "Resource.Loading")

                }
                is Resource.Success -> {
                    Log.d("DetailsFragment", "Resource.Success")
                    Log.d("DetailsFragment", "resource: ${resource.data}")
                    setUpPhoto(resource.data.companyLogo)
                    setUpMarkedColor(resource.data.isMarked)
                    setUpDetails(resource.data)
                }
            }
        }

        Log.d(TAG, "onViewCreated: jobPositionUi logo: ${jobPositionUi?.companyLogo}")
//        Log.d(TAG, "onViewCreated: jobPositionUi logoUrl: ${jobPositionUi?.companyLogoUrl}")



    }

    private fun setUpDetails(jobPosition: JobPositionUi) {
//        UiText.DynamicString(jobPositionUi?.description ?: "").let {
//            binding.tvTitle.setTextHtml(it)
//        }
        val descriptionUiResult = UiText.DynamicString(jobPosition.description ?: "")
        binding.description.setTextHtml(descriptionUiResult)

        binding.tvTitle.text = jobPosition.title
//        binding.tvDescription.text = jobPosition?.description
        binding.tvLocationData.text = jobPosition.candidateRequiredLocation
//        binding.tvSalary.text = jobPosition?.salary
        binding.tvCompany.text = jobPosition.companyName
        binding.tvCompanyData.text = jobPosition.companyName
        binding.tvWebsiteUrl.text = jobPosition.url
        binding.tvType.text = jobPosition.jobType
//        binding.tvPublicationDate.text = jobPositionUi?.publicationDate

    }

    private fun setUpPhoto(companyLogo: String?) {
        binding.photoPreview.load(companyLogo) {
            crossfade(true)
            crossfade(1000)
            placeholder(R.drawable.ic_round_business_center_24)
            error(R.drawable.ic_twotone_info_24)
                .target(
                    onError = {
                        binding.progress.stopShimmer()
                        binding.progress.visibility = View.GONE
                        binding.photoPreview.setImageDrawable(it)
                        Log.d("DetailsFragment", "error loading image")
                    },
                    onStart = {
                        Log.d("DetailsFragment", "start loading image")
                        binding.progress.startShimmer()
                        binding.progress.visibility = View.VISIBLE
                        binding.photoPreview.setImageDrawable(it)
                    },
                    onSuccess = {
                        Log.d("DetailsFragment", "success loading image")
                        binding.progress.stopShimmer()
                        binding.progress.visibility = View.GONE
                        binding.photoPreview.setImageDrawable(it)
                    }
                )
        }
    }

    private fun setUpMarkedColor(isMarked: Boolean){
        binding.btnMark.setImageResource(
            if (isMarked) R.drawable.ic_marked
            else R.drawable.ic_mark
        )
    }
    companion object {
        private const val TAG = "DetailsFragment"

    }
}
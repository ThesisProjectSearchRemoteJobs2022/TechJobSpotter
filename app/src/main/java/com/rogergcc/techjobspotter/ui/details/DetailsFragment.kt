package com.rogergcc.techjobspotter.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.data.cache.JobsPositionCache
import com.rogergcc.techjobspotter.data.cache.database.AppDatabase
import com.rogergcc.techjobspotter.data.mappers.JobMapper
import com.rogergcc.techjobspotter.databinding.FragmentDetailsBinding
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.usecase.JobsPositionCacheUseCase
import com.rogergcc.techjobspotter.ui.presentation.JobDetailPositionViewModel
import com.rogergcc.techjobspotter.ui.presentation.JobPositionViewModelFactory
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import com.rogergcc.techjobspotter.ui.provider.ContextProviderImpl
import com.rogergcc.techjobspotter.ui.utils.UiText
import com.rogergcc.techjobspotter.ui.utils.extensions.showCustomSnackbar
import com.rogergcc.techjobspotter.ui.utils.extensions.toFormattedDateUi
import com.rogergcc.techjobspotter.ui.utils.setTextHtml
import kotlinx.coroutines.launch

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

    private val viewModel by viewModels<JobDetailPositionViewModel> {
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

        if (jobPositionUi == null) {
            Log.e(TAG, "jobPositionUi is null")
            Toast.makeText(context, "Job position data is missing", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "------------------------")
//        Log.d(TAG, "jobPositionUi: logo url ${jobPositionUi?.companyLogoUrl}")

//        val person = arguments?.getParcelable<Person>("person")
//        var person = intent?.extras?.getParcellable<JobPositionUi>("jobPosition")

        viewModel.checkJobMarked(jobPositionUi)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiPositionState
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect{ state->
                    when (state) {
                        is JobDetailPositionViewModel.DetailUiState.Loading -> {
                            Log.d(TAG, "DetailUiState.Loading")
                        }
                        is JobDetailPositionViewModel.DetailUiState.Success -> {
                            Log.d(TAG, "DetailUiState.Success")

                            if (state.jobPositionDetailUi != null) {
                                setUpDetails(state.jobPositionDetailUi)
                                setUpPhoto(state.jobPositionDetailUi.companyLogo)
                                setUpMarkedColor(state.jobPositionDetailUi.isMarked)
                            }
                            if (state.jobPositionFavoriteUi != null) {
                                showMessageFavorite(state.jobPositionFavoriteUi.isMarked, state.jobPositionFavoriteUi.title)
                            }
                        }
                        is JobDetailPositionViewModel.DetailUiState.Failure -> {
                            Log.d(TAG, "DetailUiState.Failure")
                        }
                    }
                }
        }


        binding.btnMark.setOnClickListener {
            viewModel.markFavoriteJobPosition(jobPositionUi)
        }
        binding.btnHowToApply.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.confirmation)
                .setMessage(R.string.redirect_message)
                .setPositiveButton(R.string.yes) { _, _ ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(jobPositionUi.url)
                    startActivity(intent)
                }
                .setNegativeButton(R.string.no, null)
                .show()

//            AlertDialog.Builder(requireContext())
//                .setTitle(R.string.confirmation)
//                .setMessage(R.string.redirect_message)
//                .setPositiveButton(R.string.yes) { _, _ ->
//                    val intent = Intent(Intent.ACTION_VIEW)
//                    intent.data = Uri.parse(jobPositionUi.url)
//                    startActivity(intent)
//                }
//                .setNegativeButton(R.string.no, null)
//                .show()


        }

    }

    private fun setUpDetails(jobPosition: JobPositionUi) {
//        UiText.DynamicString(jobPositionUi?.description ?: "").let {
//            binding.tvTitle.setTextHtml(it)
//        }
        val descriptionUiResult = UiText.DynamicString(jobPosition.description ?: "")
        binding.description.setTextHtml(descriptionUiResult)

        binding.tvTitle.text = jobPosition.title
        binding.tvPublicationDate.text = resources.getString(
            R.string.posted_on,
            jobPosition.publicationDate?.toFormattedDateUi()
        )

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
            error(R.drawable.ic_info)
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

    private fun showMessageFavorite(isMarked: Boolean, title: String? = "") {
        setUpMarkedColor(isMarked)
        if (!isMarked) {
            binding.bottomSheet.showCustomSnackbar("\uD83D\uDE13 Unmark $title" )
        } else {
            binding.bottomSheet.showCustomSnackbar("\uD83D\uDE0D Marked $title" )
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
package com.rogergcc.techjobspotter.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.databinding.FragmentDetailsBinding
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import com.rogergcc.techjobspotter.ui.utils.UiText
import com.rogergcc.techjobspotter.ui.utils.setTextHtml

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

//    private val args: DetailsFragmentArgs by navArgs()

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

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_details, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val restaurantDisplayItem = arguments
//            ?.getParcelable("jobPosition", JobPositionUi::class.java)

//        val jobPositionUi = arguments?.getBundle("selectedJobPosition") as JobPositionUi
        val jobPositionUi = arguments?.getParcelable<JobPositionUi>("selectedJobPosition")
        Log.d("DetailsFragment", "------------------------")
        Log.d("DetailsFragment", "jobPositionUi: $jobPositionUi")

//        val person = arguments?.getParcelable<Person>("person")
//        var person = intent?.extras?.getParcellable<JobPositionUi>("jobPosition")

        binding.photoPreview.load(jobPositionUi?.companyLogoUrl) {
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

//        UiText.DynamicString(jobPositionUi?.description ?: "").let {
//            binding.tvTitle.setTextHtml(it)
//        }
        val descriptionUiResult = UiText.DynamicString(jobPositionUi?.description ?: "")
        binding.description.setTextHtml(descriptionUiResult)

        binding.tvTitle.text = jobPositionUi?.title
//        binding.tvDescription.text = jobPositionUi?.description
        binding.tvLocationData.text = jobPositionUi?.candidateRequiredLocation
//        binding.tvSalary.text = jobPositionUi?.salary
        binding.tvCompany.text = jobPositionUi?.companyName
        binding.tvCompanyData.text = jobPositionUi?.companyName
        binding.tvWebsiteUrl.text = jobPositionUi?.url
        binding.tvType.text = jobPositionUi?.jobType
//        binding.tvPublicationDate.text = jobPositionUi?.publicationDate


    }

    companion object {

    }
}
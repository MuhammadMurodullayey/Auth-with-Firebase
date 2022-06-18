package uz.gita.onlineshop.presentation.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.onlineshop.R
import uz.gita.onlineshop.data.UserModel
import uz.gita.onlineshop.databinding.ScreenVerifyBinding
import uz.gita.onlineshop.presentation.viewmodel.VerifyViewModel
import uz.gita.onlineshop.presentation.viewmodel.impl.VerifyViewModelImpl

@AndroidEntryPoint
class VerifyScreen : Fragment(R.layout.screen_verify) {
    private val viewModel  : VerifyViewModel by viewModels<VerifyViewModelImpl>()
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private var id = UserModel("","","")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id =  requireArguments().getSerializable("USER") as UserModel
        viewModel.verifyUser(id,requireActivity())

        binding.btnVerify.setOnClickListener {
           viewModel.checkUser(id, requireActivity(), binding.code.text.toString().trim())
        }


        viewModel.timeLiveData.observe(viewLifecycleOwner,timeObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner,errorObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner,progressLiveData)
        viewModel.goToNextScreenLiveData.observe(viewLifecycleOwner,goToScreenObserver)

    }
    @SuppressLint("SetTextI18n")
    private val timeObserver = Observer<String>{
        var tim = ""
        tim = it
        binding.time.text = "00:$tim"
    }
    private val errorObserver = Observer<String>{
        Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
    }
    private val progressLiveData = Observer<Boolean> {
        if(it){
            binding.progressCircular.visibility = View.VISIBLE
        }else{
            binding.progressCircular.visibility = View.GONE
        }
    }
    private val goToScreenObserver = Observer<Unit>{
        findNavController().navigate(R.id.action_verifyScreen2_to_homeScreen)
    }

}
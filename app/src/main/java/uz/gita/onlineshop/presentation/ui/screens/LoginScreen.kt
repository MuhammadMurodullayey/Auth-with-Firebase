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
import uz.gita.onlineshop.databinding.ScreenLoginBinding
import uz.gita.onlineshop.presentation.viewmodel.LoginViewModel
import uz.gita.onlineshop.presentation.viewmodel.impl.LoginViewModelImpl

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel : LoginViewModel by viewModels<LoginViewModelImpl>()
    private var name  = ""
    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        name = requireArguments().getString("NAME").toString()

        binding.btnLogin.setOnClickListener {
            val email = binding.number.text.toString().trim()
            val password = binding.passET.text.toString().trim()
            viewModel.loginUser(email,password)
        }
        binding.registerText.setOnClickListener {
            viewModel.goToRegisterScreen()
        }


        viewModel.errorLiveData.observe(viewLifecycleOwner,errorObserver)
        viewModel.goToNextScreenLiveData.observe(this@LoginScreen,goToNextScreenObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner,progressObserver)
    }
    private val errorObserver = Observer<String>{
        Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
    }
    private val goToNextScreenObserver = Observer<Int>{
        if (it == 0){
           findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
        }else{
            findNavController().navigate(R.id.action_loginScreen_to_homeScreen)
        }
    }
    private val progressObserver = Observer<Boolean>{
        if (it){
            binding.progressCircular.visibility = View.VISIBLE
        }else{
            binding.progressCircular.visibility = View.GONE
        }
    }
}
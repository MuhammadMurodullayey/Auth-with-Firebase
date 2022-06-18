package uz.gita.onlineshop.presentation.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.onlineshop.R
import uz.gita.onlineshop.data.UserModel
import uz.gita.onlineshop.databinding.ScreenRegisterBinding
import uz.gita.onlineshop.presentation.viewmodel.RegisterViewModel
import uz.gita.onlineshop.presentation.viewmodel.impl.RegisterViewModelImpl
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {
    private val binding by viewBinding(ScreenRegisterBinding::bind)
    private val viewModel: RegisterViewModel by viewModels<RegisterViewModelImpl>()
    private var name = ""
    private var storedVerificationId =""
    private var resendToken : PhoneAuthProvider.ForceResendingToken? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passET.text.toString().trim()
            name = binding.name.text.toString().trim()
            viewModel.registerUser(name , email, password)
        }
        binding.loginText.setOnClickListener {
           viewModel.goToLoginScreen()
        }


        viewModel.errorLiveData.observe(viewLifecycleOwner,errorObserver)
        viewModel.goToNextScreenLiveData.observe(viewLifecycleOwner,goToNextScreenObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner,progressObserver)
        viewModel.registerWithPhoneNumberLiveData.observe(viewLifecycleOwner,registerObserver)
        viewModel.goToLoginScreenLiveData.observe(viewLifecycleOwner,goToLoginScreenObserver)
    }
    private val goToLoginScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }
    private val errorObserver = Observer<String>{
        Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
    }
    private val goToNextScreenObserver = Observer<Unit>{
        findNavController().navigate(R.id.action_registerScreen_to_verifyScreen2)
    }
    private val progressObserver = Observer<Boolean>{
        if (it){
            binding.progressCircular.visibility =  View.VISIBLE
        }else{
            binding.progressCircular.visibility =  View.GONE
        }
    }

    private val registerObserver = Observer<UserModel>{
        val bundle = Bundle()
        bundle.putSerializable("USER",it)
        findNavController().navigate(R.id.action_registerScreen_to_verifyScreen2,bundle)
    }

}
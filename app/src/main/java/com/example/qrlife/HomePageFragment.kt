import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.qrlife.R
import com.example.qrlife.databinding.FragmentHomePageBinding
import com.example.qrlife.databinding.FragmentUserPageBinding
import com.google.firebase.auth.FirebaseAuth

class HomePageFragment : Fragment(), AnimatedFragment {

    private lateinit var binding: FragmentHomePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root

        // Récupérer l'utilisateur actuellement connecté
        val user = FirebaseAuth.getInstance().currentUser

        // Vérifier si l'utilisateur est connecté
        if (user != null) {
            // L'utilisateur est connecté, récupérer son ID
            val userEmail = user.email

            // Faites quelque chose avec l'ID de l'utilisateur, par exemple, l'afficher dans la console
            println("ID de l'utilisateur dans HomePageFragment : $userEmail")
        } else {
            // L'utilisateur n'est pas connecté, gérer en conséquence
            println("Aucun utilisateur connecté dans HomePageFragment")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationUtils.startAnimationLoad(requireActivity(), binding.greenBackgroundImageAnim)

    }

    override fun startFragmentEnterAnimation() {
        AnimationUtils.startAnimationLeave(requireActivity(), binding.greenBackgroundImageAnim)
    }

    override fun onPause() {
        super.onPause()
    }


}

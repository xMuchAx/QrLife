import AnimationUtils.startAnimationLeave
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.qrlife.databinding.FragmentUserPageBinding

class UserPageFragment : Fragment(), AnimatedFragment {

    private lateinit var binding: FragmentUserPageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentUserPageBinding.inflate(inflater, container, false)
        return binding.root

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

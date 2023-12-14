import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qrlife.R
import com.example.qrlife.databinding.FragmentListQrPageBinding
import com.example.qrlife.model.QrCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.fragment.app.Fragment
import android.view.View



class ListQrPageFragment : Fragment(), AnimatedFragment {

    private lateinit var binding: FragmentListQrPageBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var qrCodeAdapter: QrCodeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListQrPageBinding.inflate(inflater, container, false)
        val view = binding.root
        AnimationUtils.startAnimationLoad(requireActivity(), binding.greenBackgroundImageAnim)


        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Assurez-vous d'initialiser qrCodeAdapter avant de l'utiliser
        qrCodeAdapter = QrCodeAdapter(emptyList()) // Utilisez la liste appropriée
        recyclerView.adapter = qrCodeAdapter


        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val userId = user.uid
            displayQrCodes(userId)
        } else {
            // Gérer le cas où l'utilisateur n'est pas connecté
            // Par exemple, rediriger vers l'écran de connexion
        }

        binding.btnCreateQr.setOnClickListener { loadFragment(CreateQrPageFragment()) }

        // Mise en place du gestionnaire d'événements pour le clic sur un élément
        qrCodeAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(qrCodeData: String, nameQrCode: String) {
                val detailsFragment = DetailsQrCodeFragment()
                val bundle = Bundle()
                bundle.putString("qrCodeData", qrCodeData)
                bundle.putString("nameQrCode", nameQrCode)  // Ajoutez le nom du QrCode au bundle
                detailsFragment.arguments = bundle
                println(qrCodeData)
                // Remplacez le fragment actuel par DetailsQrCodeFragment
                loadFragment(detailsFragment)
            }
        })

        return view
    }

    override fun startFragmentEnterAnimation() {
        AnimationUtils.startAnimationLeave(requireActivity(), binding.greenBackgroundImageAnim)
    }

    private fun displayQrCodes(userId: String) {
        db.collection("QrCode")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val qrCodes = mutableListOf<QrCode>()

                for (document in documents) {
                    val dataQrCodeArray = document.get("dataQrCode") as? List<String>
                    val nameQrCode = document.getString("nameQr")

                    if (dataQrCodeArray != null && dataQrCodeArray.isNotEmpty() && nameQrCode != null) {
                        val concatenatedData = dataQrCodeArray.joinToString(",")
                        val qrCode = QrCode(concatenatedData, nameQrCode)
                        qrCode.qrCodeBitmap = QrCode.generateQRCode(concatenatedData)
                        qrCodes.add(qrCode)
                    }
                }

                // Mettez à jour les données de l'adaptateur ici
                qrCodeAdapter.updateData(qrCodes)
            }
            .addOnFailureListener { exception ->
                println("Error retrieving QrCodes: $exception")
            }
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainer, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    override fun onPause() {
        super.onPause()
    }
}

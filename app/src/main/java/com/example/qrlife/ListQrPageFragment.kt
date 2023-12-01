import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qrlife.R
import com.example.qrlife.databinding.FragmentListQrPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.qrlife.model.QrCode


class ListQrPageFragment : Fragment() {

    private lateinit var binding: FragmentListQrPageBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var qrCodeAdapter: QrCodeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gonfler la mise en page pour ce fragment
        binding = FragmentListQrPageBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            // L'utilisateur est connecté, récupérer son ID
            val userId = user.uid

            // Afficher les QRCodes pour cet utilisateur
            displayQrCodes(userId)
        } else {
            // L'utilisateur n'est pas connecté, gérer en conséquence
            // Par exemple, rediriger vers l'écran de connexion
        }

        // Définir le gestionnaire de clic pour btnCreateQr
        binding.btnCreateQr.setOnClickListener { loadFragment(CreateQrPageFragment()) }

        return view
    }

    private fun displayQrCodes(userId: String) {
        // Effectuer une requête Firestore pour récupérer les QRCodes pour l'utilisateur donné
        db.collection("QrCode")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val qrCodes = mutableListOf<QrCode>()

                for (document in documents) {
                    val dataQrCodeArray = document.get("dataQrCode") as? List<String>
                    if (dataQrCodeArray != null && dataQrCodeArray.isNotEmpty()) {
                        // Concaténer les éléments du tableau en une seule chaîne
                        val concatenatedData = dataQrCodeArray.joinToString("")

                        // Générer un QR Code pour la chaîne concaténée
                        val qrCode = QrCode(concatenatedData)
                        qrCode.qrCodeBitmap = QrCode.generateQRCode(concatenatedData)
                        qrCodes.add(qrCode)
                    }
                }

                // Afficher les QRCodes dans le RecyclerView en utilisant un adaptateur
                qrCodeAdapter = QrCodeAdapter(qrCodes)
                recyclerView.adapter = qrCodeAdapter
            }
            .addOnFailureListener { exception ->
                // Gérer les erreurs lors de la récupération des données Firestore
                // Vous pouvez afficher un message d'erreur ou effectuer d'autres actions nécessaires
            }
    }






    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainer, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}

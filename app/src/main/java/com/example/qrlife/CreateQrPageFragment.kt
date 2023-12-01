import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.qrlife.LoginActivity
import com.example.qrlife.R
import com.example.qrlife.databinding.FragmentCreateQrPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class CreateQrPageFragment : Fragment() {

    private lateinit var binding: FragmentCreateQrPageBinding
    private val db = FirebaseFirestore.getInstance()
    private var editTextCount = 0
    private val dataQrCode = mutableListOf<String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateQrPageBinding.inflate(inflater, container, false)
        val view = binding.root
        // Récupérer l'utilisateur actuellement connecté
        val user = FirebaseAuth.getInstance().currentUser

        // Vérifier si l'utilisateur est connecté

        if (user != null) {

            addNewEditText()
            // L'utilisateur est connecté, récupérer son ID
            val userId = user.uid

            // Faites quelque chose avec l'ID de l'utilisateur, par exemple, l'afficher dans la console
            println("ID de l'utilisateur dans ListQrPageFragment : $userId")

            // Ajouter un écouteur de clic au bouton "Submit"
            binding.buttonSubmit.setOnClickListener { addQr(userId) }
            binding.buttonSubmitAdd.setOnClickListener { addNewEditText() }

        } else {
            // L'utilisateur n'est pas connecté, gérer en conséquence
            println("Aucun utilisateur connecté dans ListQrPageFragment")

            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        return view
    }

    private fun addQr(userId: String) {

        val nameQr = binding.textNameQr.text.toString()


        for (i in 0 until binding.editTextContainer.childCount) {
            val editText = binding.editTextContainer.getChildAt(i) as? EditText
            val editTextValue = editText?.text?.toString() ?: ""
            dataQrCode.add(editTextValue)
        }

        // Maintenant, editTextValues contient toutes les valeurs des EditText
        // Vous pouvez faire ce que vous voulez avec cette liste
        println("Valeurs des EditText : $dataQrCode")


        // Vérifier si dataQrCode n'est pas vide
        if (dataQrCode.isNotEmpty()) {
            // Créer une instance de la collection "QrCode" dans Firestore
            val qrCodeRef = db.collection("QrCode")

            // Créer une carte (Map) avec les données à ajouter
            val qrCodeData = HashMap<String, Any>()
            qrCodeData["dataQrCode"] = dataQrCode
            qrCodeData["userId"] = userId
            qrCodeData["nameQr"] = nameQr

            // Ajouter les données dans Firestore
            qrCodeRef.add(qrCodeData)
                .addOnSuccessListener { documentReference ->
                    println("Document ajouté avec l'ID : ${documentReference.id}")
                    // Ajout réussi, vous pouvez effectuer des actions supplémentaires si nécessaire
                }
                .addOnFailureListener { e ->
                    println("Erreur lors de l'ajout du document : $e")
                    // Gérer les erreurs d'ajout ici
                }
        } else {
            // dataQrCode est vide, gérer en conséquence
            println("dataQrCode est vide")
        }

    }

    private fun addNewEditText() {
        // Créer un nouvel EditText
        val newEditText = EditText(requireContext())

        // Définir des paramètres de mise en page (vous pouvez ajuster cela en fonction de vos besoins)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Affecter des paramètres au nouvel EditText
        newEditText.id = View.generateViewId()
        println("ID de l'EditText : ${newEditText.id}")
        newEditText.layoutParams = layoutParams
        newEditText.hint = "EditText ${++editTextCount}"

        // Ajouter le nouvel EditText au LinearLayout
        binding.editTextContainer.addView(newEditText)
    }

}

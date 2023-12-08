import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
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
            binding.imageBack.setOnClickListener{requireActivity().onBackPressed()}

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
        dataQrCode.clear()

        val nameQr = binding.textNameQr.text.toString()

        if(nameQr.isEmpty()){

            Toast.makeText(
                requireContext(),
                "Veuillez donnez un nom au QrCode",
                Toast.LENGTH_SHORT
            ).show()

        }else {
            var editTextEmpty = false
            for (i in 0 until binding.editTextContainer.childCount) {
                val cardView = binding.editTextContainer.getChildAt(i) as? CardView
                val editText = cardView?.getChildAt(0) as? EditText
                val editTextValue = editText?.text?.toString() ?: ""
                if (editTextValue.isEmpty()) {
                    editTextEmpty = true
                    break
                }
                dataQrCode.add(editTextValue)
                println("edit text test : $editTextValue")
            }

            // Maintenant, editTextValues contient toutes les valeurs des EditText
            // Vous pouvez faire ce que vous voulez avec cette liste
            println("Valeurs des EditText : $dataQrCode")

            if (editTextEmpty) {
                // Afficher votre alerte ici (dialog, toast, etc.)
                // Exemple avec un toast
                Toast.makeText(
                    requireContext(),
                    "Veuillez remplir tous les champs",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
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
        }

    }

    private fun addNewEditText() {

        // Créer un nouvel EditText en utilisant le style EditTextStyle
        val newEditText = EditText(requireContext())
        newEditText.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.edit_text_height),

        )
        newEditText.setBackgroundResource(android.R.color.transparent)

        // Définir un ID pour l'EditText
        newEditText.id = View.generateViewId()

        val cardView = CardView(requireContext())

        val cardViewLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
            topMargin = resources.getDimensionPixelSize(R.dimen.card_margin)
        }
        cardView.radius = resources.getDimension(R.dimen.card_corner_radius)
        cardView.layoutParams = cardViewLayoutParams

        // Ajouter l'EditText au CardView
        cardView.addView(newEditText)

        // Ajouter le CardView au LinearLayout
        binding.editTextContainer.addView(cardView)
        newEditText.requestFocus()

    }


}


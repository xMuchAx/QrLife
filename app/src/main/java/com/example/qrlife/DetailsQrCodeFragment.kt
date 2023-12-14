// DetailsQrCodeFragment.kt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.qrlife.R
import com.example.qrlife.databinding.FragmentDetailsQrCodeBinding
import com.example.qrlife.model.QrCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailsQrCodeFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    private lateinit var binding: FragmentDetailsQrCodeBinding
    private val NewDataQrCode = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsQrCodeBinding.inflate(inflater, container, false)
        val view = binding.root
        val user = FirebaseAuth.getInstance().currentUser


        if (user != null) {
            val userId = user.uid
            val qrCodeData = arguments?.getString("qrCodeData")
            val nameQrCode = arguments?.getString("nameQrCode")

            // Vérifiez si le QrCode n'est pas nul
            if (!qrCodeData.isNullOrBlank() && !nameQrCode.isNullOrBlank()) {
                // Accédez aux vues dans le layout
                val qrCodeParts = qrCodeData.split(",")
                binding.textNameQr.setText(nameQrCode)
                // Maintenant, qrCodeParts est une liste des parties divisées
                for (part in qrCodeParts) {
                    showDetailQrCode(part)
                }

                binding.buttonSubmit.setOnClickListener { updateQr(userId, nameQrCode, qrCodeParts) }
                binding.imageBack.setOnClickListener{requireActivity().onBackPressed()}


            }

        } else {
            // Gérer le cas où l'utilisateur n'est pas connecté
            // Par exemple, rediriger vers l'écran de connexion
        }


        return view
    }
    private fun updateQr(userId: String, nameQrCode: String?, dataQrCode: List<String>?) {
        if (nameQrCode.isNullOrBlank() || dataQrCode.isNullOrEmpty()) {
            // Gérer le cas où le nom ou les données du QrCode sont manquants
            Toast.makeText(
                requireContext(),
                "Le nom ou les données du QrCode sont manquants",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val NewNameQr = binding.textNameQr.text.toString()


        // Créer une référence au document Firestore à mettre à jour
        val qrCodeRef = db.collection("QrCode")
            .whereEqualTo("userId", userId)
            .whereEqualTo("nameQr", nameQrCode)

        // Mettre à jour le document existant
        qrCodeRef.get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Gérer le cas où le document n'est pas trouvé
                    Toast.makeText(
                        requireContext(),
                        "QrCode non trouvé",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Mettre à jour le premier document trouvé (vous pouvez ajuster si besoin)
                    val documentSnapshot = documents.documents[0]
                    val documentId = documentSnapshot.id
                    println("Nombre de documents trouvés : ${documents.size()}")
                    println("Données à mettre à jour : $dataQrCode")

                    if (NewNameQr.isEmpty()) {

                        Toast.makeText(
                            requireContext(),
                            "Veuillez donnez un nom au QrCode",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.textNameQr.requestFocus()

                    } else {
                        var editTextEmpty = false
                        for (i in 0 until binding.editTextContainer.childCount) {
                            val cardView = binding.editTextContainer.getChildAt(i) as? CardView
                            val editText = cardView?.getChildAt(0) as? EditText
                            val editTextValue = editText?.text?.toString() ?: ""
                            if (editTextValue.isEmpty()) {
                                editTextEmpty = true
                                break
                            }
                            NewDataQrCode.add(editTextValue)
                            println("edit text test : $editTextValue")
                        }

                        if (editTextEmpty) {
                            // Afficher votre alerte ici (dialog, toast, etc.)
                            // Exemple avec un toast
                            Toast.makeText(
                                requireContext(),
                                "Veuillez remplir tous les champs",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {


                            // Créer une carte (Map) avec les données à mettre à jour
                            val updatedData = HashMap<String, Any>()
                            updatedData["dataQrCode"] = NewDataQrCode
                            updatedData["nameQr"] = NewNameQr
                            // Ajouter d'autres champs à mettre à jour au besoin

                            // Mettre à jour le document
                            db.collection("QrCode").document(documentId)
                                .update(updatedData)
                                .addOnSuccessListener {
                                    println("Document mis à jour avec succès : $documentId")
                                    Toast.makeText(
                                        requireContext(),
                                        "QrCode mis à jour",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    requireActivity().onBackPressed()
                                }
                                .addOnFailureListener { e ->
                                    println("Erreur lors de la mise à jour du document : $e")
                                    // Gérer les erreurs de mise à jour ici
                                }
                        }
                    }
                }
            }
    }




    private fun showDetailQrCode(part: String) {

        // Créer un nouvel EditText en utilisant le style EditTextStyle
        val newEditText = EditText(requireContext())
        newEditText.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
            )
        newEditText.setBackgroundResource(android.R.color.transparent)

        // Définir un ID pour l'EditText
        newEditText.id = View.generateViewId()
        newEditText.hint = "index : text..."
        val leftPadding = resources.getDimensionPixelSize(R.dimen.left_padding)
        newEditText.setPadding(leftPadding, 0, 0, 0)

        val cardView = CardView(requireContext())

        val cardViewLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.edit_text_height),
        ).apply {
            topMargin = resources.getDimensionPixelSize(R.dimen.edit_margin)
            bottomMargin = resources.getDimensionPixelSize(R.dimen.edit_margin)

        }
        cardView.radius = resources.getDimension(R.dimen.card_corner_radius)
        cardView.setPadding(leftPadding, 0, 0, 0)
        cardView.layoutParams = cardViewLayoutParams

        // Ajouter l'EditText au CardView
        cardView.addView(newEditText)

        // Ajouter le CardView au LinearLayout
        binding.editTextContainer.addView(cardView)
        newEditText.requestFocus()

        newEditText.setText(part)

    }
}

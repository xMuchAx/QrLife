package com.example.qrlife

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import com.example.qrlife.databinding.FragmentScannedQrBinding
import com.google.firebase.auth.FirebaseAuth

class ScannedQrFragment : Fragment() {
    // Propriété binding
    private var binding: FragmentScannedQrBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScannedQrBinding.inflate(inflater, container, false)
        val view = binding!!.root
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val userId = user.uid
            val qrCodeData = arguments?.getString("qrCodeData")
            val nameQrCode = arguments?.getString("nameQrCode")

            // Vérifiez si le QrCode n'est pas nul
            if (!qrCodeData.isNullOrBlank() && !nameQrCode.isNullOrBlank()) {
                // Accédez aux vues dans le layout
                val qrCodeParts = qrCodeData.split(",")
                binding!!.textNameQr.setText(nameQrCode)
                // Maintenant, qrCodeParts est une liste des parties divisées
                for (part in qrCodeParts) {
                    showDetailQrCode(part)
                }

                binding!!.imageBack.setOnClickListener{requireActivity().onBackPressed()}


            }

        } else {
            // Gérer le cas où l'utilisateur n'est pas connecté
            // Par exemple, rediriger vers l'écran de connexion
        }


        return view
    }

    private fun showDetailQrCode(part: String) {

        // Créer un nouvel TextView
        val newTextView = TextView(requireContext())
        newTextView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        newTextView.setBackgroundResource(android.R.color.transparent)
        newTextView.gravity = Gravity.CENTER_VERTICAL
        val paddingLeft = resources.getDimensionPixelSize(R.dimen.left_padding)
        newTextView.setPadding(paddingLeft, 0, 0, 0)


        // Définir un ID pour le TextView
        newTextView.id = View.generateViewId()
        newTextView.text = part
        newTextView.textSize = 16f // Changer la taille du texte selon vos besoins

        val cardView = CardView(requireContext())

        val cardViewLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.edit_text_height)
        ).apply {
            topMargin = resources.getDimensionPixelSize(R.dimen.edit_margin)
            bottomMargin = resources.getDimensionPixelSize(R.dimen.edit_margin)
        }
        cardView.radius = resources.getDimension(R.dimen.card_corner_radius)
        cardView.layoutParams = cardViewLayoutParams

        // Ajouter l'EditText au CardView
        cardView.addView(newTextView)

        // Ajouter le CardView au LinearLayout
        binding!!.editTextContainer.addView(cardView)
    }



}

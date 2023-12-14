import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.qrlife.R
import com.example.qrlife.ScannedQrFragment
import com.example.qrlife.databinding.FragmentScanPageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class ScanPageFragment : Fragment(), AnimatedFragment {

    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var binding: FragmentScanPageBinding
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentScanPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationUtils.startAnimationLoad(requireActivity(), binding.greenBackgroundImageAnim)


        // Initialise la vue de scan
        barcodeView = binding.barcodeScannerView

        startCameraPreview()

    }

    private var isScanEnabled = true

    private fun startCameraPreview() {
        // Configure le callback pour le scan de QR code
        val barcodeCallback = object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                if (isScanEnabled) {
                    isScanEnabled = false // Désactive la détection pour ignorer les résultats supplémentaires

                    // Traite le résultat du scan ici (par exemple, affiche les données du QR code)
                    val qrCodeData = result.text.split(",")

                    db.collection("QrCode")
                        .whereEqualTo("dataQrCode", qrCodeData)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents != null && !documents.isEmpty) {
                                val document = documents.documents[0]
                                val nameQr = document.getString("nameQr")
                                if (nameQr != null) {
                                    println("data = $qrCodeData et name = $nameQr")

                                    val scannedQrFragment = ScannedQrFragment()
                                    val bundle = Bundle()

                                    bundle.putString("qrCodeData", qrCodeData.joinToString())
                                    bundle.putString("nameQrCode", nameQr.toString())  // Ajoutez le nom du QrCode au bundle
                                    scannedQrFragment.arguments = bundle

                                    // Remplacez le fragment actuel par DetailsQrCodeFragment
                                    loadFragment(scannedQrFragment)
                                } else {
                                    println("Aucun nom associé au QR Code $qrCodeData trouvé dans la base de données.")
                                }
                            } else {
                                println("Aucune correspondance trouvée dans la base de données pour $qrCodeData")
                            }

                            // Réactive la détection pour les scans futurs
                            isScanEnabled = true
                        }
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {
                // Gère les points possibles du résultat (optionnel)
            }
        }

        // Démarre le scan
        barcodeView.decodeContinuous(barcodeCallback)
        barcodeView.resume()
    }

    override fun startFragmentEnterAnimation() {
        AnimationUtils.startAnimationLeave(requireActivity(), binding.greenBackgroundImageAnim)
    }


    override fun onPause() {
        super.onPause()
        barcodeView.pause()

    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainer, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}

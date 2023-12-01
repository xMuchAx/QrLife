import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.qrlife.databinding.FragmentScanPageBinding
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class ScanPageFragment : Fragment() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var binding: FragmentScanPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialise la vue de scan
        barcodeView = binding.barcodeScannerView

        startCameraPreview()

    }


    private fun startCameraPreview() {
        // Configure le callback pour le scan de QR code
        val barcodeCallback = object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                // Traite le résultat du scan ici (par exemple, affiche les données du QR code)
                val qrCodeData = result.text
                // ... (effectue d'autres actions en fonction des données du QR code)
            }

            override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {
                // Gère les points possibles du résultat (optionnel)
            }
        }

        // Démarre le scan
        barcodeView.decodeContinuous(barcodeCallback)
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}

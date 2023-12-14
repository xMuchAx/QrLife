import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qrlife.R
import com.example.qrlife.model.QrCode

interface OnItemClickListener {
    fun onItemClick(qrCodeData: String, nameQrCode: String)
}



class QrCodeAdapter(private var qrCodes: List<QrCode>) :
    RecyclerView.Adapter<QrCodeAdapter.QrCodeViewHolder>() {

    fun updateData(newQrCodes: List<QrCode>) {
        qrCodes = newQrCodes
        notifyDataSetChanged()
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }



    class QrCodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameQrCodeTextView: TextView = itemView.findViewById(R.id.nameQrCodeTextView)

        val qrCodeImageView: ImageView = itemView.findViewById(R.id.qrCodeImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QrCodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_qr_code, parent, false)
        return QrCodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: QrCodeViewHolder, position: Int) {
        val currentQrCode = qrCodes[position]

        // Afficher les données du QrCode
        holder.nameQrCodeTextView.text = currentQrCode.nameQrCode

        // Afficher le QR Code dans l'ImageView
        holder.qrCodeImageView.setImageBitmap(currentQrCode.qrCodeBitmap)

        // Gérer le clic sur l'élément
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(currentQrCode.dataQrCode, currentQrCode.nameQrCode)
        }
    }

    override fun getItemCount(): Int {
        return qrCodes.size
    }


}

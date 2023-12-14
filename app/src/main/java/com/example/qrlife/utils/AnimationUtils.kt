// AnimationUtils.kt

import android.animation.AnimatorInflater
import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.example.qrlife.R

object AnimationUtils {

    fun startAnimationLeave(context: Context, target: ImageView) {
        val slideDownAnimator = AnimatorInflater.loadAnimator(context, R.animator.slide_down)
        slideDownAnimator?.let {
            it.setTarget(target)
            it.start()
        }
    }

    fun startAnimationLoad(context: FragmentActivity, target: ImageView) {
        val slideUpAnimator = AnimatorInflater.loadAnimator(context, R.animator.slide_up)
        slideUpAnimator?.let {
            it.setTarget(target)
            it.start()
        }
    }

    // Ajoutez d'autres fonctions d'animation réutilisables au besoin
}

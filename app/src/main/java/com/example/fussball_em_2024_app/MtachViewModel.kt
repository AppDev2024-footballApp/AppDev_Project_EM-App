import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.fussball_em_2024_app.ExchangeService
import kotlinx.coroutines.Dispatchers

import retrofit2.HttpException

class MatchViewModel(private val service: ExchangeService) : ViewModel() {

    val latestMatch = liveData(Dispatchers.IO) {
        try {
            val matchResponse = service.getLatestMatch()
            emit(matchResponse)
        } catch (e: HttpException) {
            Log.d("Message", e.message().toString())
            emit(null) // oder eine geeignete Fehlerbehandlung durchführen
        } catch (e: Exception) {
            Log.d("Message",e.message.toString())// Fehler, die keine HTTP-Fehler sind (z.B. Netzwerk-/Parsing-/Allgemeine Fehler)
            emit(null) // oder eine geeignete Fehlerbehandlung durchführen
        }
    }
}

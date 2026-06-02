package org.albaspazio.psysuite.tests.rt

import android.util.Log
import org.albaspazio.psysuite.tests.TrialBasic
import org.albaspazio.psysuite.adaptive.ado.ADOWrapper
import kotlin.math.abs

/*
    stim_value: duration of the stimulus
    correct_answer: time to press (in ms)
    user_answer:  is the error = (time pressed by the user - correct_answer)
    succ: if abs(curr error) < abs(prev error), then success is true
 */

class TrialRT (id:Int=-1, type:Int, label:String,
               isTraining: Boolean=false): TrialBasic(id, type, label, adoWrapper = null, isTraining = isTraining) {

    companion object {
        @JvmStatic val LOG_HEADER = "id\tlabel\tresponse\terror\n"
    }

    init {
        initTrial(magnitude)
    }

    // result: user's button press duration == elapsed
    // success is always true, but when receives a result == -1 (user did not press the button)
    override fun setResponse(result:Int, elapsedms:Long, prev_tr: TrialBasic?, extra_text:String) {
        super.setResponse(result, elapsedms, prev_tr, extra_text)
        success  = result != -1
    }

    // data exported to log file
    override fun Log():String{
        return "$id\t$label\t$user_answer\t${!success}\n"
    }
}
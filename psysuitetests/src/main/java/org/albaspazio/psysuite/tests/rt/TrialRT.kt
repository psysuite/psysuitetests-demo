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

    private var error:Int = 0

    init {
        initTrial(magnitude)
    }

    override fun initTrial(newvalue:Float):Long {
        magnitude       = newvalue
        correct_answer  = 0
        return stim_value
    }

    // result: user's button press duration == elapsed
    // success is true if the present error is smaller than the previous one
    // if first trial, success is always true
    override fun setResponse(result:Int, elapsedms:Long, prev_tr: TrialBasic?, extra_text:String) {
        user_answer         = result
        prev_trial          = prev_tr
        error               =   if(result == -1)    1
                                else                0
        user_answer_extra   = extra_text
    }

    // data exported to log file
    override fun Log():String{
        return "$id\t$label\t$user_answer\t$error\n"
    }
}
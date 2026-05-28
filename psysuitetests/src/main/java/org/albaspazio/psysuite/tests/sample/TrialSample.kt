package org.albaspazio.psysuite.tests.sample

import org.albaspazio.psysuite.tests.TrialBasic


class TrialSample(id:Int=-1, type:Int, label:String, val source:Int, val extraTrial:Any?): TrialBasic(
    id,
    type,
    label
){

    companion object {
        @JvmStatic val LOG_HEADER           = "id\tlabel\tlat\tconflict\tres\tcor_ans\tuser_ans\telapsed\trep\n"
        @JvmStatic val LAST_STIMULUS_DELAY  = 1000
    }

    // data exported to log file
    override fun Log():String{
        return id.toString() +  "\t" + label + "\t" + "\n"
    }
}

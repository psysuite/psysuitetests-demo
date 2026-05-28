package org.albaspazio.psysuite.tests.bis

import org.albaspazio.psysuite.tests.TrialBasic
import org.albaspazio.psysuite.adaptive.ado.ADOWrapper

// trial adopting the pattern where magnitude === stim_value

/**
 * Represents a single trial within the Bisection (BIS) test.
 *
 * This class extends [TrialBasic] and defines the specific parameters and logic
 * for a BIS trial. It manages the stimulus timing based on a given magnitude and
 * whether the stimulus is presented before or after a midpoint latency.
 *
 * @property magnitude The temporal distance (in milliseconds) from the first stimulus, that defines the stimulus presentation time.
 * @property conflict_type A string indicating the type of conflict in the trial (e.g., "none", "visual_leading").
 * @property duration The duration of the primary stimulus in milliseconds.
 * @param duration2 The duration of a secondary stimulus in milliseconds, if applicable. Defaults to 0L.
 * @property mid_latency The reference midpoint latency (in milliseconds) around which stimuli are presented. Defaults to 500L.
 * @property conflict_magn The magnitude of the conflict, if any. Defaults to 0F.
 * @param adoWrapper reference to a ADOWrapper instance, if the trial is part of an adaptive procedure is not null. Defaults to `null`.
 * @param id The unique identifier for the trial. Defaults to -1.
 * @param type The type identifier for the trial.
 * @param label A descriptive label for the trial.
 */
open class TrialBIS(id:Int=-1, type:Int, label:String, override var magnitude:Float, val conflict_type:String, val duration:Long, private val duration2:Long=0L, val mid_latency:Long = 500L, val conflict_magn:Float=0F, adoWrapper: ADOWrapper?=null, isTraining:Boolean=false):
    TrialBasic(id, type, label, adoWrapper = adoWrapper, isTraining = isTraining){

    companion object {
        /**
         * Defines the header string for logging trial data.
         * This header is used to structure the output in log files, ensuring consistency
         * in data recording for BIS trials.
         *
         * The fields are:
         * - id: Trial ID
         * - label: Trial label
         * - lat: Stimulus latency (stim_value)
         * - confl: Conflict type
         * - res: Result/Success (0 for incorrect, 1 for correct)
         * - cor_ans: Correct answer (0 or 1)
         * - user_ans: User's answer (0 or 1)
         * - elapsed: Time taken for the trial
         * - rep: Number of repetitions
         * - confl_magn: Conflict magnitude
         */
        @JvmStatic val LOG_HEADER = "id\tlabel\tlat\tuser_ans\tref\tres\tcor_ans\telapsed\trep\tconfl_magn\tconfl\n"
    }

    init {
        initTrial(magnitude)
    }

    /**
     * Sets up the trial parameters based on a new stimulus value (magnitude).
     * This method recomputes the `correct_answer` based on the `isBefore` property
     * and updates the internal `magnitude`.
     *
     * @param newvalue The new magnitude (temporal distance from `mid_latency`) for the trial.
     * @return The calculated `stim_value` (actual stimulus presentation time in ms).
     */
    override fun initTrial(newvalue: Float):Long{
        magnitude       = newvalue
        correct_answer  =   if(magnitude < mid_latency)     0
                            else                            1
        return stim_value
    }

    // Returns a list of parameters to pass to the ADO wrapper's update method
    override fun getAdoUpdatingParams(): List<Any> {
        return listOf(user_answer, stim_value)
    }
    /**
     * Generates a string formatted for logging the trial data.
     * This string adheres to the structure defined by [LOG_HEADER].
     *
     * @return A tab-separated string containing detailed information about the trial outcome and parameters.
     */
    override fun Log():String{
        return "$id\t$label\t$stim_value\t$user_answer\t$mid_latency\t$success\t$correct_answer\t$elapsed\t$repetitions\t$conflict_magn\t$conflict_type\n"
    }

    /**
     * Provides a string with debugging information for the trial.
     * Extends the debug information from [TrialBasic] with BIS-specific details
     * like stimulus position (`stim_value`) and conflict type.
     *
     * @return A string containing detailed debug information.
     */
    override fun debugInfo():String{
        return "${super.debugInfo()}, pos=$stim_value, conf_type=$conflict_type"
    }
}

package org.albaspazio.psysuite.tests.sample

import org.albaspazio.psysuite.core.models.Populations
import org.albaspazio.psysuite.tests.SettingsBasic
import org.albaspazio.psysuite.core.stimuli.DelaysAligner
import org.albaspazio.psysuite.tests.TestBasic
import kotlinx.parcelize.Parcelize
import org.albaspazio.core.accessory.Device
import org.albaspazio.psysuite.core.R


/*
This class manage simple subjects that participate in tests with only one condition.
in subclasses, user must resolve the condition code according to internal variables
 */

// base class for all tests
@Parcelize
open class SettingsSample(

    override var testclass: String = "org.albaspazio.psysuite.tests.sample.TestSample",
    override var settingsclass: String = "",
    override var answerclass: String = "",
    override var label: String = "",
    override var age: Int = -1,
    override var gender: Int = -1,
    override var population: Int = Populations.POPULATION_TD,
    override var type: Int = -1,
    override var project: String = "",

    override var block: Int = -1,
    override var isDebug: Boolean = false,
    override var device: Device? = null,
    override var vercode: Int = -1,
    override var stimuliDelays: DelaysAligner = DelaysAligner(),

    override var nextTrailModality: Int = TestBasic.TEST_NEXTTRIAL_AUTO,
    override var whitenoise: Int = TestBasic.TEST_SWITCH_CHOOSE_ON,
    override var trman_type: Int = TestBasic.TEST_TRMAN_FIXED,
    override var showResult: Int = TestBasic.TEST_SWITCH_DISABLED,
    override var canRepeat:Int = TestBasic.TEST_SWITCH_DISABLED,
    override var doTraining: Int = TestBasic.TEST_SWITCH_DISABLED,

    override var showTrialID: Int = TestBasic.TEST_SHOWTRIALS_NEVER,
    override var abortMode: Int = TestBasic.TEST_ABORT_TRIALEND,

    var stim_sources:Int = 0,       // according to modalities selection

    var audioDuration:Long = 0,     //
    var audioVolume:Int = 100,   //
    var audioResource:String = "",  // resource name (string)

    var visualDuration:Long     = 0,    //
    var visualDrawableOff:Int   = 0,    //
    var visualDrawableOn:Int    = 0,    //

    var tactileAmplitudes:String = "-1",    // array of amplitudes of vibration. default to "-1" ( a.k.a.  MAX_AMPLITUDE (-1))
    var tactileTimings:String = "",         // array of periods (Long)

    var shiftedParams:List<Long> = listOf(0L,0L,0L),
    var pairDistance:Long = 0,

    var repetitions:Int = 1,
    var iti:Long = 1000,

    override var session_spsel: Int = TestBasic.Companion.TEST_NO_LONGITUDINAL,
    override var session_spdatares: Int = R.array.sessions_array,
    override var date: String = "",
    override var exp_uid: String = ""
) : SettingsBasic(testclass, settingsclass, answerclass, label, age, gender, population, type, project, block, isDebug, device, vercode, stimuliDelays, nextTrailModality, whitenoise, trman_type, showResult, canRepeat, doTraining, showTrialID, abortMode, session_spsel, session_spdatares, date, exp_uid)







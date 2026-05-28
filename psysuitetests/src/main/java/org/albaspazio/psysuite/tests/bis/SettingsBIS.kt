package org.albaspazio.psysuite.tests.bis

import org.albaspazio.psysuite.core.R
import org.albaspazio.psysuite.core.models.Populations
import org.albaspazio.psysuite.tests.SettingsBasic
import org.albaspazio.psysuite.core.stimuli.DelaysAligner
import org.albaspazio.psysuite.tests.TestBasic
import kotlinx.parcelize.Parcelize
import org.albaspazio.core.accessory.Device

/**
 * Parcelable data class representing the configuration for a Bisection (BIS) test session.
 *
 * This class extends [SettingsBasic] and holds all the necessary parameters
 * to define a specific BIS test instance, including subject details, test conditions,
 * and hardware/software configurations.
 *
 * @property classes List of fully qualified names of the test classes to be used. For BIS, this typically defaults to ["org.albaspazio.psysuite.tests.bis.TestBIS"].
 * @property label A descriptive label for the test session (e.g., subject ID, specific condition).
 * @property age The age of the participant.
 * @property gender The gender of the participant (coded as an Int).
 * @property population The population group the participant belongs to (e.g., typically developing, specific clinical group). See [Populations].
 * @property type The specific type or variant of the BIS test to be run (e.g., auditory, visual, bimodal).
 * @property project The name of the project this subject belongs to. Defaults to an empty string.
 * @property block The current block number if the test is divided into multiple blocks.
 * @property isDebug If `true`, the test will run in debug mode, potentially with more logging or specific debug functionalities.
 * @property device Information about the device running the test. See [Device].
 * @property vercode The version code of the application.
 * @property stimuliDelays Configuration for aligning stimuli delays. See [DelaysAligner].
 * @property nextTrailModality Defines how the next trial is initiated (e.g., after user answer, timed). See [TestBasic.TEST_NEXTTRIAL_ANSWER].
 * @property whitenoise Configuration for using white noise during the test (e.g., on, off, user-configurable). See [TestBasic.TEST_SWITCH_CHOOSE_ON].
 * @property trman_type The type of trials manager to use (e.g., fixed, adaptive). See [TestBasic.TEST_TRMAN_CHOOSE_FIXED].
 * @property showResult Defines whether to show the result of each trial to the participant. See [TestBasic.TEST_SWITCH_CHOOSE_OFF].
 * @property canRepeat Defines whether the participant can request a repetition of a trial. See [TestBasic.TEST_SWITCH_CHOOSE_OFF].
 * @property doTraining Defines whether a training phase should be run before the main test. See [TestBasic.TEST_SWITCH_CHOOSE_OFF].
 */
@Parcelize
class SettingsBIS(

    override var testclass: String = "org.albaspazio.psysuite.tests.bis.TestBIS",
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

    override var nextTrailModality: Int = TestBasic.TEST_NEXTTRIAL_ANSWER,
    override var whitenoise: Int        = TestBasic.TEST_SWITCH_CHOOSE_OFF,
    override var trman_type: Int        = TestBasic.TEST_TRMAN_CHOOSE_ADAPTIVE,
    override var showResult: Int        = TestBasic.TEST_SWITCH_CHOOSE_OFF,
    override var canRepeat:Int          = TestBasic.TEST_SWITCH_CHOOSE_OFF,
    override var doTraining: Int        = TestBasic.TEST_SWITCH_CHOOSE_ON,

    override var showTrialID: Int       = TestBasic.TEST_SHOWTRIALS_NEVER,
    override var abortMode: Int         = TestBasic.TEST_ABORT_TRIALEND,

    override var session_spsel: Int = TestBasic.Companion.TEST_LONGITUDINAL_TOBESELECTED,
    override var session_spdatares: Int = R.array.sessions_array,
    override var date: String = "",
    override var exp_uid: String = ""
) : SettingsBasic(testclass, settingsclass, answerclass, label, age, gender, population, type, project, block, isDebug, device, vercode, stimuliDelays, nextTrailModality, whitenoise, trman_type, showResult, canRepeat, doTraining, showTrialID, abortMode, session_spsel, session_spdatares, date, exp_uid)







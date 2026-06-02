package org.albaspazio.psysuite.tests.rt

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.albaspazio.psysuite.core.R as CoreR
import org.albaspazio.psysuite.core.databinding.FragmentTestBinding
import org.albaspazio.psysuite.core.models.Populations
import org.albaspazio.psysuite.tests.SettingsBasic
import org.albaspazio.psysuite.core.stimuli.AudioManager
import org.albaspazio.psysuite.core.stimuli.StimuliManager
import org.albaspazio.psysuite.core.stimuli.TactileManager
import org.albaspazio.psysuite.core.stimuli.VisualManager
import org.albaspazio.psysuite.tests.TestBasic
import org.albaspazio.psysuite.tests.TrialBasic
import org.albaspazio.psysuite.core.ui.fragments.TestFragment
import org.albaspazio.psysuite.core.utils.ConditionData
import org.albaspazio.core.accessory.VibrationManager
import org.albaspazio.core.speech.SpeechManager
import org.albaspazio.psysuite.core.trials.FixedTrialsManager
import java.lang.Math.random
import java.util.Date

class TestRT(
    ctx: Context,
    activity: Activity,
    hostfragment: Fragment,
    subject: SettingsBasic,
    vibrator: VibrationManager?,
    mImageView: ImageView?,
    speechManager: SpeechManager?,
    mainView: View?
) : TestBasic(ctx, activity, hostfragment, subject, vibrator, mImageView, speechManager, mainView){

    override var LOG_TAG:String = TestRT::class.java.simpleName

    companion object {

        @JvmStatic val TEST_BASIC_LABEL                 = "RT"   // will be written in result files

        @JvmStatic var NUM_TRIALS                       = 32
        @JvmStatic val STIMULUS_DURATION_VISUAL:Long    = 50
        @JvmStatic val STIMULUS_DURATION_TACTILE:Long   = 50
        @JvmStatic val STIMULUS_DURATION_AUDIO:Long     = 50

        @JvmStatic val STIMULUS_TYPE_AUDIO              = "AUDIO"
        @JvmStatic val STIMULUS_TYPE_TACTILE            = "TACTILE"
        @JvmStatic val STIMULUS_TYPE_VISUAL             = "VISUAL"
        @JvmStatic val STIMULUS_TYPE_AUDIO_LOG          = "A"
        @JvmStatic val STIMULUS_TYPE_TACTILE_LOG        = "T"
        @JvmStatic val STIMULUS_TYPE_VISUAL_LOG         = "V"

        fun getConditionsInfo(ctx: Context): List<ConditionData>{
            return if(VibrationManager.sysHasVibrator(ctx))
                mutableListOf(
                ConditionData(TEST_BASIC_LABEL + "_" + STIMULUS_TYPE_AUDIO           , TEST_RT_AUDIO           , "${TEST_BASIC_LABEL}${STIMULUS_TYPE_AUDIO_LOG}"           , Populations.hearing_populations),
                ConditionData(TEST_BASIC_LABEL + "_" + STIMULUS_TYPE_TACTILE         , TEST_RT_TACTILE         , "${TEST_BASIC_LABEL}${STIMULUS_TYPE_TACTILE_LOG}"         , Populations.all_populations),
                ConditionData(TEST_BASIC_LABEL + "_" + STIMULUS_TYPE_VISUAL          , TEST_RT_VISUAL          , "${TEST_BASIC_LABEL}${STIMULUS_TYPE_VISUAL_LOG}"          , Populations.sighted_populations),
            )
            else
                mutableListOf(
                    ConditionData(TEST_BASIC_LABEL + "_" + STIMULUS_TYPE_AUDIO       , TEST_RT_AUDIO           , "${TEST_BASIC_LABEL}${STIMULUS_TYPE_AUDIO_LOG}"           , Populations.hearing_populations),
                    ConditionData(TEST_BASIC_LABEL + "_" + STIMULUS_TYPE_VISUAL      , TEST_RT_VISUAL          , "${TEST_BASIC_LABEL}${STIMULUS_TYPE_VISUAL_LOG}"          , Populations.sighted_populations),
                )
        }

        fun getNextTrialModes(ctx: Context):List<List<Int>>{
            return if(VibrationManager.sysHasVibrator(ctx))
                listOf( listOf(TEST_NEXTTRIAL_AUTO), listOf(TEST_NEXTTRIAL_AUTO), listOf(TEST_NEXTTRIAL_AUTO))
            else
                listOf( listOf(TEST_NEXTTRIAL_AUTO), listOf(TEST_NEXTTRIAL_AUTO))
        }
    }

    override var STIM_A  = StimuliManager.STIM_TYPE_A4
    override var STIM_V  = StimuliManager.STIM_TYPE_V1
    override var STIM_T  = StimuliManager.STIM_TYPE_T1

    override var mDrawablesResource: MutableList<Int> = mutableListOf(CoreR.drawable.blue_circle)

    private lateinit var mRespButton:Button
    private val binding: FragmentTestBinding =  (hostfragment as TestFragment).binding

    private val mTrialAbortTime: Long = 2000L   // interval after stimulus onset when trial is declared failed and next one os presented
    private var mTrialStartOnset:Long = 0L

    override fun initTest() {

        mNoise = AudioManager.getAudioResource(ctx,"wnoise_20s", 0.01f)

        // define mTestLabel according to subject.type
        mTestLabel = ""
        getConditionsInfo(ctx).map {
            if (it.id == subject.type) mTestLabel = it.label
        }
        if(mTestLabel.isEmpty()) throw Exception("ERROR in TestRT.initTest: type code was not recognized")

        createResultFile(TrialBasic.LOG_HEADER)

        // define stimuli sources and parameters
        mStimuliManager = when(subject.type){
            TEST_RT_AUDIO -> StimuliManager(
                AudioManager(STIM_A, audioResources[STIMULUS_DURATION_AUDIO] ?: "t1000hz_50ms.wav", duration = STIMULUS_DURATION_AUDIO, handler = mStimuliHandler, ctx = ctx), null, null,delaysAligner=subject.stimuliDelays, ctx, mStimuliHandler)
            TEST_RT_TACTILE -> StimuliManager(
                null, TactileManager(vibrator!!, duration = STIMULUS_DURATION_TACTILE, handler = mStimuliHandler, type = STIM_T), null,delaysAligner=subject.stimuliDelays, ctx, mStimuliHandler)
            else -> StimuliManager(
                null, null, VisualManager(STIM_V, mImageView!!, mDrawablesResource[0], duration = STIMULUS_DURATION_VISUAL, handler = mStimuliHandler),delaysAligner=subject.stimuliDelays, ctx, mStimuliHandler)
        }
        mTrialsManager = FixedTrialsManager(createTrials() as MutableList<TrialBasic>)

        testEvent.accept(Triple(EVENT_TEST_SETUP_COMPLETED, null, listOf()))
    }

    override fun onStimuliEnd() {

        val elapsed = Date().time - mTrialStartOnset

        if(elapsed > mTrialAbortTime - 10L)
            setResponse(-1, elapsed)
        else
            setResponse(elapsed.toInt(), elapsed)

        mStimuliHandler.removeCallbacksAndMessages(null)
        binding.root.removeView(mRespButton)

        super.onStimuliEnd()
    }


    override fun initSummary() {}   // here is useless

    private fun createTrials():List<TrialBasic>{
        val trials:MutableList<TrialBasic> = mutableListOf()

        for(i in 0 until NUM_TRIALS)
            trials.add(TrialRT(i, subject.type, mTestLabel))

        return trials
    }

    // =============================================================================================================================
    // DELIVER STIMULI
    // =============================================================================================================================
    override fun show(trial: TrialBasic, isRepeat: Boolean) {

        val stimulus_onset = (500L + random()*1000).toLong()
        mNoise?.start()

        mStimuliHandler.postDelayed({
            deliverStimulus(trial)
            testEvent.accept(Triple(EVENT_STIMULI_START, null, listOf()))
        }, stimulus_onset)
        mStimuliHandler.postDelayed({ onStimuliEnd() }, stimulus_onset + mTrialAbortTime)     // when max time is reached => force trial end

        createResponseButton("press", binding.root, ::onStimuliEnd)
    }

    private fun createResponseButton(txt:String, parent_layout: ConstraintLayout, onPress:() -> Unit): Button {

        if(this::mRespButton.isInitialized)
            parent_layout.removeView(mRespButton)

        mRespButton = AppCompatButton(ctx).apply {
            id              = View.generateViewId()
            text            = txt
            textAlignment   = TextView.TEXT_ALIGNMENT_CENTER
            gravity         = Gravity.CENTER
            visibility      = View.VISIBLE

            parent_layout.addView(this)

            x = (parent_layout.width*0.1).toFloat()
            y = (parent_layout.height*0.8).toFloat()

            layoutParams.width = (parent_layout.width*0.8).toInt()
            layoutParams.height = (parent_layout.height*0.15).toInt()

            setBackgroundColor(context.resources.getColor(CoreR.color.colorPrimary))
//            setTextAppearance(R.style.TextAppearance_AppCompat_Widget_Button_Colored)
            setLinkTextColor(context.resources.getColor(CoreR.color.colorPrimary))
        }
        mRespButton.setOnClickListener {
            onPress()
        }
        return mRespButton
    }

    private fun deliverStimulus(trial: TrialBasic){

        when(trial.type) {
            TEST_RT_AUDIO    ->  mStimuliManager.deliverAStimulus()
            TEST_RT_TACTILE  ->  mStimuliManager.deliverTStimulus()
            TEST_RT_VISUAL   ->  mStimuliManager.deliverVStimulus()
        }
        mTrialStartOnset = Date().time
    }
}
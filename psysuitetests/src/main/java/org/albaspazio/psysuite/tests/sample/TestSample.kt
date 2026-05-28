package org.albaspazio.psysuite.tests.sample

//import android.app.Fragment
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import org.albaspazio.psysuite.core.R as CoreR
import org.albaspazio.psysuite.core.models.Populations
import org.albaspazio.psysuite.tests.TestBasic
import org.albaspazio.psysuite.tests.TrialBasic
import org.albaspazio.psysuite.core.trials.FixedTrialsManager
import org.albaspazio.psysuite.core.utils.ConditionData
import org.albaspazio.core.accessory.VibrationManager
import org.albaspazio.core.speech.SpeechManager
import org.albaspazio.core.ui.showToast
import org.albaspazio.psysuite.core.stimuli.AudioManager
import org.albaspazio.psysuite.core.stimuli.AudioResourceException
import org.albaspazio.psysuite.core.stimuli.ImageViewDefinedException
import org.albaspazio.psysuite.core.stimuli.StimuliManager
import org.albaspazio.psysuite.core.stimuli.TactileManager
import org.albaspazio.psysuite.core.stimuli.VisualManager


/*

unimodal precision: (Audio-Vibration-Visual)

    stimulus onset
    stimulus duration
    temporal distance between two stimuli from 10 -> 500 ms  [10,15,20,25,30,35,40,50,65,70,80,90,100]
    triple stimulus (like in bisection)


*/

class TestSample(ctx: Context, activity: Activity, hostfragment: Fragment, subject: SettingsSample, vibrator: VibrationManager?, mImageView: ImageView?, speechManager: SpeechManager?, mainView: View?)
    : TestBasic(ctx, activity, hostfragment, subject, vibrator, mImageView, speechManager, mainView)
{
    private var curStimDuration: Long = 0L
    override var LOG_TAG:String = TestSample::class.java.simpleName

    companion object {
        // Overrides
        @JvmStatic val TEST_BASIC_LABEL     = "SAMPLE"

        fun getConditionsInfo(ctx: Context): List<ConditionData> {
            return mutableListOf(
                ConditionData("$TEST_BASIC_LABEL ${ctx.resources.getString(CoreR.string.aligned)}"  , TEST_SAMPLE_ALIGNED, "${TEST_BASIC_LABEL}_${ctx.resources.getString(CoreR.string.aligned)}", Populations.sighted_hearing_populations),
                ConditionData("$TEST_BASIC_LABEL ${ctx.resources.getString(CoreR.string.shifted)}"    , TEST_SAMPLE_SHIFTED, "${TEST_BASIC_LABEL}_${ctx.resources.getString(CoreR.string.shifted)}", Populations.sighted_hearing_populations),
                ConditionData("$TEST_BASIC_LABEL ${ctx.resources.getString(CoreR.string.pair)}"     , TEST_SAMPLE_PAIR   , "${TEST_BASIC_LABEL}_${ctx.resources.getString(CoreR.string.pair)}", Populations.sighted_hearing_populations)
            )
        }
        
        fun getNextTrialModes(ctx:Context):List<List<Int>>{
            return listOf(listOf(TEST_NEXTTRIAL_BUTTON, TEST_NEXTTRIAL_AUTO))
        }
    }

    override var mDrawablesResource:MutableList<Int> = mutableListOf(CoreR.drawable.white_circle, CoreR.drawable.black_circle, CoreR.drawable.blue_circle, CoreR.drawable.red_circle)

    // =============================================================================================================================
    // INIT
    // =============================================================================================================================
    override fun initTest(){
        val subj = subject as SettingsSample

        if(mImageView == null) throw ImageViewDefinedException("IMAGE_VIEW_NOT_DEFINED")

        // vibrator == null    -> throw VibratorNotDefinedException("VIBRATOR_NOT_DEFINED")

        mImageView!!.visibility   = View.INVISIBLE
        curStimDuration         = 1000L
        validAnswers            = mutableListOf()

        ITI                     = subj.iti
        mTestLabel              = ""
        getConditionsInfo(ctx).map {
            if (it.id == subj.type) mTestLabel = it.label
        }
        if(mTestLabel.isEmpty()) showToast("Should not happen. given test code was not recognized", ctx)

        if (subj.whitenoise > TEST_SWITCH_CHOOSE_OFF)    mNoise = AudioManager.getAudioResource(ctx, "wnoise_20s", 0.01f)

        createResultFile(LOG_HEADER)
        val trials = createTrials()
        mTrialsManager = FixedTrialsManager(trials as MutableList<TrialBasic>)

        setStimuliManager()
    }

    private fun setStimuliManager(){
        val subj = subject as SettingsSample

        val audioManager = when {
            subj.stim_sources and StimuliManager.STIM_TYPE_A1 > 0 ->
                AudioManager(
                    StimuliManager.STIM_TYPE_A1, -1,
                    amplitude = (subj.audioVolume * 1.0F) / 100,
                    duration = subj.audioDuration,
                    ctx = ctx, handler = mStimuliHandler
                )

            subj.stim_sources and StimuliManager.STIM_TYPE_A2 > 0 ->
                try{
                    if(subj.audioResource.isEmpty()) subj.audioResource = currAudioResourceName
                    AudioManager(
                        StimuliManager.STIM_TYPE_A2,
                        subj.audioResource,
                        (subj.audioVolume * 1.0F) / 100,
                        duration = subj.audioDuration,
                        ctx = ctx, handler = mStimuliHandler
                    )

                } catch(e:Exception){
                    throw Exception("GENERIC ERROR: $e")
                } catch(e: AudioResourceException){
                    throw AudioResourceException("AUDIO_RESOURCE_ERROR: resource name = $e")
                }

            subj.stim_sources and StimuliManager.STIM_TYPE_A3 > 0 ->
                try{
                    if(subj.audioResource.isEmpty()) subj.audioResource = currAudioResourceName
                    AudioManager(
                        StimuliManager.STIM_TYPE_A3,
                        subj.audioResource,
                        (subj.audioVolume * 1.0F) / 100,
                        duration = subj.audioDuration,
                        ctx = ctx, handler = mStimuliHandler
                    )

                } catch(e:Exception){
                    throw Exception("GENERIC ERROR: $e")
                } catch(e: AudioResourceException){
                    throw AudioResourceException("AUDIO_RESOURCE_ERROR: resource name = $e")
                }
            subj.stim_sources and StimuliManager.STIM_TYPE_A4 > 0 ->
                try{
                    if(subj.audioResource.isEmpty()) subj.audioResource = currAudioResourceName
                    AudioManager(
                        StimuliManager.STIM_TYPE_A4,
                        subj.audioResource,
                        (subj.audioVolume * 1.0F) / 100,
                        duration = subj.audioDuration,
                        ctx = ctx, handler = mStimuliHandler
                    )

                } catch(e:Exception){
                    throw Exception("GENERIC ERROR: $e")
                } catch(e: AudioResourceException){
                    throw AudioResourceException("AUDIO_RESOURCE_ERROR: resource name = $e")
                }

            else -> null
        }

        val tact_amplitudes = TactileManager.validateAmplitudes(subj.tactileAmplitudes)
        val tact_timings    = TactileManager.validateTimings(subj.tactileTimings)

//        val tactileManager  =   if(subject.stim_sources and StimuliManager.STIM_TYPE_T1 > 0)
//                                    TactileManager(vibrator!!, tact_amplitudes, duration = subject.tactileTimings.toLong(), handler = mStimuliHandler)
//                                else if(subject.stim_sources and StimuliManager.STIM_TYPE_T2 > 0)
//                                    TactileManager(vibrator!!, tact_amplitudes, tact_timings, type = StimuliManager.STIM_TYPE_T2, handler = mStimuliHandler)
//                                else throw Exception()
        // TODO Check commented code, temporary solution
        val tactileManager  = TactileManager(
            vibrator!!,
            tact_amplitudes,
            duration = 200 /*subject.tactileTimings.toLong()*/,
            handler = mStimuliHandler
        )




        val visualManager = when {
            subj.stim_sources and StimuliManager.STIM_TYPE_V1 > 0 -> {
                val on =    if(subj.visualDrawableOn >= mDrawablesResource.size)   mDrawablesResource.size
                            else                                                   subj.visualDrawableOn
                VisualManager(
                    StimuliManager.STIM_TYPE_V1,
                    mImageView!!,
                    mDrawablesResource[on],
                    duration = subj.visualDuration,
                    handler = mStimuliHandler
                )
            }
            subj.stim_sources and StimuliManager.STIM_TYPE_V2 > 0 -> {
                if(mImageView == null)  return
                val on =    if(subj.visualDrawableOn >= mDrawablesResource.size)   mDrawablesResource.size-1
                            else                                                   subj.visualDrawableOn
                VisualManager(
                    StimuliManager.STIM_TYPE_V2,
                    mImageView!!,
                    mDrawablesResource[on],
                    mDrawablesResource[subj.visualDrawableOff],
                    subj.visualDuration,
                    handler = mStimuliHandler
                )
            }
            else -> null
        }

        mStimuliManager = StimuliManager(
            audioManager,
            tactileManager,
            visualManager,
            subj.stimuliDelays,
            ctx,
            mStimuliHandler
        ) { testEvent.accept(Triple(EVENT_TEST_SETUP_COMPLETED, null, listOf())) }
    }
    // =============================================================================================================================
    // CREATE TRIALS
    // =============================================================================================================================
    private fun createTrials():List<TrialBasic>{
        val subj = subject as SettingsSample

        val extraTrial:Any? = when(subj.type){
            TEST_SAMPLE_SHIFTED     -> subj.shiftedParams
            TEST_SAMPLE_PAIR        -> subj.pairDistance
            else                    -> null
        }

        var cnt = -1
        val trials:MutableList<TrialBasic> = mutableListOf()
        for(t in 0 until subj.repetitions){
            trials.add(TrialSample(++cnt, subj.type, "", subj.stim_sources, extraTrial))
        }
        return trials
    }

    override fun initSummary(){}

    // =============================================================================================================================
    // DELIVER STIMULI
    // =============================================================================================================================
    override fun show(trial: TrialBasic, isRepeat:Boolean){
        val subj = subject as SettingsSample

        Log.d(LOG_TAG, "---------------------")
        mNoise?.start()

        mStimuliHandler.postDelayed({
            when(trial.type){

                TEST_SAMPLE_ALIGNED ->  mStimuliManager.deliverAlignedStimulus((trial as TrialSample).source){onStimuliEnd()}

                TEST_SAMPLE_SHIFTED ->  {
                    val corr_delays = subj.stimuliDelays.arrangeDelays(subj.stim_sources,
                        ((trial as TrialSample).extraTrial as List<*>)[0] as Long,
                        (trial.extraTrial as List<*>)[1] as Long,
                        trial.extraTrial[2] as Long
                    )

                    mStimuliManager.deliverShiftedStimulus(trial.source, corr_delays.a, corr_delays.t, corr_delays.v){onStimuliEnd()}
                }
                TEST_SAMPLE_PAIR    ->  mStimuliManager.deliverAlignedStimuliPair((trial as TrialSample).extraTrial as Long, trial.source){onStimuliEnd()}
            }
        }, 1000)

    }
    // =============================================================================================================================
    // DEBUG
    // =============================================================================================================================
}
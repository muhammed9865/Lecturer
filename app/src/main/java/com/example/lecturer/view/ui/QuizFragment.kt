package com.example.lecturer.view.ui

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.databasework.R
import com.example.databasework.data.dao.data_classes.subject_related.Question
import com.example.databasework.databinding.FragmentQuizBinding
import com.example.lecturer.services.TimerService
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException
import java.util.*


class QuizFragment : Fragment(R.layout.fragment_quiz), View.OnClickListener {
    private lateinit var binding: FragmentQuizBinding
    private lateinit var questionsList: Array<Question>
    private var quizName = ""
    private var currentQuestion = 0
    private val TAG = "QuizFragment"
    private var quizTime: Long = 0
    private var correctAnswerIndex = -1


    private lateinit var serviceIntent: Intent


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false)

        // Getting the questions list from arguments
        if (!requireArguments().isEmpty) {
            val args = QuizFragmentArgs.fromBundle(arguments!!)
            questionsList = args.questionsList
            Log.d(TAG, "onCreateView: ${questionsList[0]}")
            quizName = args.quizName
            quizTime = args.quizTime
        }

        binding.nextQuestionBtn.setOnClickListener(this)
        binding.previousQuestionBtn.setOnClickListener(this)


        serviceIntent = Intent(requireContext().applicationContext, TimerService::class.java)
        activity?.registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))


        setupQuestion()
        startTimer()
        binding.answersGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{rg , i ->
            if (i == correctAnswerIndex){
                Toast.makeText(requireContext(),"Correct Answer", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            quizTime = intent.getLongExtra(TimerService.TIME_EXTRA, 0)
            binding.quizTimeLeftTv.text = getTimeStringFromDouble(quizTime)
            if (quizTime == 0L) {
                stopTimer()
            }
        }
    }

    private fun startTimer() {

        serviceIntent.putExtra(TimerService.TIME_EXTRA, quizTime)
        activity?.startService(serviceIntent)
    }

    private fun stopTimer() {
        activity?.stopService(serviceIntent)
        try {
            AlertDialog.Builder(context)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    dialog.cancel()
                }.setMessage("Quiz is over, fuck off")
                .setCancelable(false)
                .show()
        }catch (e: RuntimeException){
            e.printStackTrace()
        }
    }

    private fun getTimeStringFromDouble(time: Long): String {
        val resultInt = time
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)

    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String = String.format(
        Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds
    )


    private fun setupQuestion() {

        if (questionsList.isNotEmpty()) {

            val currentPosition = "${currentQuestion + 1}/${questionsList.size}"

            binding.quizToolbar.title = "$quizName - $currentPosition"
            binding.quizToolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.quizzesFragment)
                activity?.stopService(serviceIntent)
            }
            val question = questionsList[currentQuestion]
            val answers = question.answers
            correctAnswerIndex = question.correctAnswerIndex


            // Setting the Question and answers UI
            binding.apply {
                questionTv.text = question.questionName
                optionOne.text = answers[0]
                optionTwo.text = answers[1]
                optionThree.text = answers[2]
                optionFour.text = answers[3]
                currentQuestionPositionTv.text = currentPosition
                answersGroup.clearCheck()
            }


            // Setting the Next and Back controls
            if (currentQuestion == 0) {
                binding.previousQuestionBtn.visibility = View.INVISIBLE
            } else {
                binding.previousQuestionBtn.visibility = View.VISIBLE
            }

            if (currentQuestion == questionsList.size - 1) {
                binding.nextQuestionBtn.visibility = View.INVISIBLE
            } else {
                binding.nextQuestionBtn.visibility = View.VISIBLE
            }

        }
    }


    private fun onNextClicked() {
        currentQuestion++
        setupQuestion()

    }

    private fun onBackClicked() {
        currentQuestion--
        setupQuestion()

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            binding.nextQuestionBtn.id -> {
                onNextClicked()
            }
            binding.previousQuestionBtn.id -> {
                onBackClicked()
            }
        }
    }

    private fun onAnswerSelected(){
       for (i in 0 until binding.answersGroup.size){
            if (binding.answersGroup[i].isSelected){
                if (i == correctAnswerIndex){
                    Snackbar.make(this.requireView(), "Correct Answer", 1000).setBackgroundTint(Color.GREEN).show()
                }else{
                    Snackbar.make(this.requireView(), "Wrong Answer", 1000).setBackgroundTint(Color.RED).show()
                }
            }
       }
    }


}
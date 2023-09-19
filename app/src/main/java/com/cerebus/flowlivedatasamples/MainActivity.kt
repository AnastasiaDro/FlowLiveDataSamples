package com.cerebus.flowlivedatasamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cerebus.flowlivedatasamples.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        with(viewBinding) {
            livedataButton.setOnClickListener { viewModel.triggerLiveData() }
            stateflowButton.setOnClickListener { viewModel.triggerStateFlow() }
            sharedflowButton.setOnClickListener { viewModel.triggerSharedFlow() }
            flowButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.triggerFlow().collectLatest { viewBinding.flowTextView.text = it }
                }
            }
        }
        subscribeToObservables()


    }

    private fun subscribeToObservables() {
        viewModel.liveData.observe(this) {
            Log.d("LoGG", "LiveData triggered!")
            viewBinding.liveDataTextView.text = it
        }
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                Log.d("LoGG", "StateFlow triggered!")
                viewBinding.stateFlowTextView.text = it
            }
            viewModel.sharedFlow.collectLatest {
                Log.d("LoGG", "SharedFlow triggered!")
                viewBinding.stateFlowTextView.text = it
                Snackbar.make(
                    viewBinding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}
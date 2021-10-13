package com.flipperdevices.info.main

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.activityViewModels
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.view.ComposeFragment
import com.flipperdevices.info.di.InfoComponent
import com.flipperdevices.info.main.compose.ComposeInfoScreen
import com.flipperdevices.info.main.service.FlipperViewModel
import com.flipperdevices.pair.api.PairComponentApi
import com.flipperdevices.pair.api.PairScreenArgument
import javax.inject.Inject

class InfoFragment : ComposeFragment() {
    @Inject
    lateinit var pairComponentApi: PairComponentApi

    private val bleViewModel by activityViewModels<FlipperViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentHolder.component<InfoComponent>().inject(this)
    }

    @Composable
    override fun renderView() {
        val information by bleViewModel.getDeviceInformation().collectAsState()
        val echoList by bleViewModel.getEchoAnswers().collectAsState()
        val connectionState by bleViewModel.getConnectionState().collectAsState()
        ComposeInfoScreen(information, connectionState, echoList, echoListener = {
            bleViewModel.sendEcho(it)
        }, connectionToAnotherDeviceButton = {
            pairComponentApi.openPairScreen(requireContext(), PairScreenArgument.RECONNECT_DEVICE)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bleViewModel.connectAndStart(getDeviceId())
    }

    companion object {
        const val EXTRA_DEVICE_KEY = "device_id"
    }

    private fun getDeviceId(): String {
        return arguments?.get(EXTRA_DEVICE_KEY) as String
    }
}
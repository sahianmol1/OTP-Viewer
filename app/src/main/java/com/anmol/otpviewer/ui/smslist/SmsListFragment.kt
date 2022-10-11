package com.anmol.otpviewer.ui.smslist

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anmol.otpviewer.databinding.FragmentSmsListBinding

class SmsListFragment : Fragment() {
    private lateinit var binding: FragmentSmsListBinding
    private lateinit var smsAdapter: SmsListAdapter
    private val viewModel: SmsListViewModel by viewModels()
    private var cur: Cursor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmsListBinding.inflate(inflater, container, false)
        val view = binding.root

        val uriSms: Uri = Uri.parse("content://sms/inbox")
        cur = requireActivity().contentResolver.query(uriSms, null, null, null, null)

        smsAdapter = SmsListAdapter() { sms ->
            val action = SmsListFragmentDirections.actionSmsListFragmentToSmsDetailsFragment(sms)
            findNavController().navigate(action)
        }

        binding.apply {
            rvSms.layoutManager = LinearLayoutManager(requireContext())
            rvSms.adapter = smsAdapter
        }

        viewModel.getSMS(cur)

        viewModel.smsList.observe(viewLifecycleOwner) { smsList ->
            smsAdapter.submitList(smsList)
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        cur?.close()
    }

}
package com.example.bancodigital.presenter.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.bancodigital.R
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentProfileBinding
import com.example.bancodigital.databinding.LayoutBottomSheetImageBinding
import com.example.bancodigital.util.BaseFragment
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()

    private val tagPicasso = "tagPicasso"

    private var imageProfile: String? = null
    private var currentPhotoPath: String? = null

    private var user: User? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = false)
        getProfile()
        initListener()
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initListener() {
        binding.btnSave.setOnClickListener {
            if(user == null) {
                Toast.makeText(requireContext(), "Erro: Usuário não carregado", Toast.LENGTH_SHORT).show()
            }

            if(imageProfile != null) {
                saveImageProfile()
            } else {
                validateData()
            }
        }
        binding.userImage.setOnClickListener {
            showBottomSheetImage()
        }
    }




    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showBottomSheetImage(){

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val bottomSheetImageBinding: LayoutBottomSheetImageBinding = LayoutBottomSheetImageBinding.inflate(
            layoutInflater,
            null,
            false
        )

        bottomSheetImageBinding.btnCam.setOnClickListener {
            bottomSheetDialog.dismiss()
            requestPermissionCam()
        }
        bottomSheetImageBinding.btnGalery.setOnClickListener {
            bottomSheetDialog.dismiss()
            requestPermissionGalery()
        }


        bottomSheetDialog.setContentView(bottomSheetImageBinding.root)
        bottomSheetDialog.show()

    }

    private fun requestPermissionCam() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openCamera()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(requireContext(), "Permissão Negada", Toast.LENGTH_SHORT).show()
            }
        }
        showDialogPermissionDenied(
            permissionlistener = permissionlistener,
            permission = Manifest.permission.CAMERA,
            message = R.string.text_message_camera_denied_profile_fragment
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissionGalery() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openGallery()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(requireContext(), "Permissão Negada", Toast.LENGTH_SHORT).show()
            }
        }
        showDialogPermissionDenied(
            permissionlistener = permissionlistener,
            permission = Manifest.permission.READ_MEDIA_IMAGES,
            message = R.string.text_message_galery_denied_profile_fragment
        )
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(
                requireContext(),
                "Não foi possivel abrir a camera do dipositivio",
                Toast.LENGTH_SHORT
            ).show()
        }

        if(photoFile != null) {
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.example.bancodigital.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraLauncher.launch(takePictureIntent)
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val cameraLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath!!)
            binding.userImage.setImageURI(Uri.fromFile(file))
            imageProfile = file.toURI().toString()
        }
    }

    private val galleryLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageSelected = result.data?.data
            imageProfile = imageSelected.toString()

            if (imageSelected != null) {
                binding.userImage.setImageBitmap(getBitmap(imageSelected))
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale("pt","BR")).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpeg",
            storageDir
        )
        currentPhotoPath = image.absolutePath
        return image
    }

    private fun getBitmap(pathUri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT < 28) {
                getBitmap(requireActivity().contentResolver, pathUri)
            } else {
                val source = ImageDecoder.createSource(requireActivity().contentResolver, pathUri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showDialogPermissionDenied( permissionlistener: PermissionListener,  permission: String, message: Int){
        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedTitle("Permissão Negada")
            .setDeniedMessage(message)
            .setDeniedCloseButtonText("Não")
            .setGotoSettingButtonText("Sim")
            .setPermissions(permission)
            .check();
    }

    private fun validateData() {
        val nome = binding.profileUserName.text.toString().trim()
        val telefone = binding.profileUserTelefone.unMaskedText

        if (nome.isNotEmpty()) {
            if (telefone?.isNotEmpty() == true) {
                if (telefone.length == 11) {
                    hideKeyboard()
                    user?.name = nome
                    user?.phone = telefone
                    saveProfile()

                } else {
                    showBottomSheet(message = getString(R.string.text_phone_invalid))
                }
            } else {
                showBottomSheet(message = getString(R.string.text_telefone_empty))
            }
        } else {
            showBottomSheet(message = getString(R.string.text_name_empty))
        }
    }

    private fun saveImageProfile() {
        imageProfile?.let { image ->
            profileViewModel.saveImage(image).observe(viewLifecycleOwner) { stateview ->
                when (stateview) {
                    is StateView.Loading -> binding.progressBar.isVisible = true
                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = stateview.message)
                    }

                    is StateView.Sucess -> {
                        saveProfile(stateview.data)
                        binding.progressBar.isVisible = false
                    }
                }
            }
        }
    }

    private fun saveProfile(urlImage : String? = null) {
        user?.let {

            if(urlImage != null) {
                user?.image = urlImage
            }
            profileViewModel.saveProfile(it).observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> binding.progressBar.isVisible = true
                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(
                            message = getString(FirebaseHelper.validError(stateView.message ?: ""))
                        )
                    }
                    is StateView.Sucess -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(requireContext(), "Salvo com sucesso", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getProfile() {
        profileViewModel.getProfile().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> binding.progressBar.isVisible = true
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(
                        message = getString(FirebaseHelper.validError(stateView.message ?: ""))
                    )
                }
                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
                    user = stateView.data
                    configData(user)
                }
            }
        }
    }


    private fun configData(user: User?) {
        if(user?.image?.isNotEmpty() == true) {
            Picasso.get()
                .load(user.image)
                .tag(tagPicasso)
                .fit()
                .centerCrop()
                .into(binding.userImage, object : Callback {
                    override fun onSuccess() {
                        binding.progressBar.isVisible = false
                        binding.userImage.isVisible = true
                    }

                    override fun onError(e: Exception?) {
                    }

                })
        } else {
            binding.progressBar.isVisible = false
            binding.userImage.isVisible = true
            binding.userImage.setImageResource(R.drawable.image_user)
        }

        binding.profileUserName.setText(user?.name)
        binding.profileUserTelefone.setText(user?.phone)
        binding.profileUserEmail.setText(user?.email)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

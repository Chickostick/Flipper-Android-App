package com.flipperdevices.filemanager.listing.impl.composable.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.flipperdevices.bridge.connection.feature.storage.api.model.FileType
import com.flipperdevices.filemanager.listing.impl.viewmodel.EditFileViewModel
import com.flipperdevices.filemanager.ui.components.name.NameDialog
import flipperapp.components.filemngr.listing.impl.generated.resources.fml_create_file_allowed_chars
import flipperapp.components.filemngr.listing.impl.generated.resources.fml_create_file_file_btn
import flipperapp.components.filemngr.listing.impl.generated.resources.fml_create_file_folder_btn
import flipperapp.components.filemngr.listing.impl.generated.resources.fml_create_file_title
import org.jetbrains.compose.resources.stringResource
import flipperapp.components.filemngr.listing.impl.generated.resources.Res as FML

private const val AVAILABLE_CHARACTERS = "“0-9”, “A-Z”, “a-z”, “!#\\\$%&'()-@^_`{}~”"

@Composable
fun CreateFileDialogComposable(
    editFileViewModel: EditFileViewModel,
) {
    val createFileState by editFileViewModel.state.collectAsState()

    when (val localCreateFileState = createFileState) {
        EditFileViewModel.State.Pending -> Unit
        is EditFileViewModel.State.Edit -> {
            NameDialog(
                value = localCreateFileState.name,
                title = stringResource(FML.string.fml_create_file_title),
                buttonText = stringResource(
                    resource = when (localCreateFileState.itemType) {
                        FileType.FILE -> FML.string.fml_create_file_file_btn
                        FileType.DIR -> FML.string.fml_create_file_folder_btn
                    }
                ),
                subtitle = stringResource(
                    FML.string.fml_create_file_allowed_chars,
                    AVAILABLE_CHARACTERS
                ),
                onFinish = { editFileViewModel.onFinish() },
                isError = !localCreateFileState.isValid,
                isEnabled = !localCreateFileState.isLoading,
                needShowOptions = localCreateFileState.needShowOptions,
                onTextChange = editFileViewModel::onNameChange,
                onDismissRequest = editFileViewModel::dismiss,
                onOptionSelect = editFileViewModel::onOptionSelected,
                options = localCreateFileState.options
            )
        }
    }
}

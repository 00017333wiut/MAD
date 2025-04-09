import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.R
import com.example.myapp.data.Instrument
import com.example.myapp.ui.InstrumentUiState
import kotlinx.coroutines.launch
import com.example.myapp.ui.InstrumentViewModel
import com.example.myapp.ui.OperationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrumentHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: InstrumentViewModel = viewModel(factory = InstrumentViewModel.Factory)
) {
    val instrumentUiState = viewModel.instrumentUiState
    val operationState by viewModel.operationState.collectAsState()
    val scope = rememberCoroutineScope()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedInstrument by remember { mutableStateOf<Instrument?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(operationState) {
        when (operationState) {
            is OperationState.Success -> {
                snackbarHostState.showSnackbar(
                    (operationState as OperationState.Success).message,
                    duration = SnackbarDuration.Short
                )
                viewModel.resetOperationState()
            }
            is OperationState.Error -> {
                snackbarHostState.showSnackbar(
                    (operationState as OperationState.Error).message,
                    duration = SnackbarDuration.Short
                )
                viewModel.resetOperationState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_instruments)) },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
            }
        }
    ) { paddingValues ->
        Box(modifier = modifier.padding(paddingValues).fillMaxSize()) {
            when (instrumentUiState) {
                is InstrumentUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is InstrumentUiState.Error -> {
                    ErrorScreen(
                        message = (instrumentUiState as InstrumentUiState.Error).message,
                        onRetry = { viewModel.getInstruments() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is InstrumentUiState.Success -> {
                    val instruments = (instrumentUiState as InstrumentUiState.Success).instruments
                    if (instruments.isEmpty()) {
                        EmptyInstrumentsList(modifier = Modifier.align(Alignment.Center))
                    } else {
                        InstrumentsList(
                            instruments = instruments,
                            onEditClick = { selectedInstrument = it },
                            onDeleteClick = {
                                selectedInstrument = it
                                showDeleteConfirmation = true
                            }
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = operationState is OperationState.Loading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

    // Add/Edit Instrument Dialog
//    if (showAddDialog || selectedInstrument != null) {
//        InstrumentDialog(
//            instrument = selectedInstrument,
//            onDismiss = {
//                showAddDialog = false
//                selectedInstrument = null
//            },
//            onSave = { request ->
//                if (selectedInstrument != null) {
//                    viewModel.updateInstrument(selectedInstrument!!.id, request)
//                } else {
//                    viewModel.addInstrument(request)
//                }
//                showAddDialog = false
//                selectedInstrument = null
//            }
//        )
//    }

    // Delete confirmation dialog
    if (showDeleteConfirmation && selectedInstrument != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
                selectedInstrument = null
            },
            title = { Text(stringResource(R.string.title_delete_confirm)) },
            text = { Text(
                text = stringResource(
                    R.string.msg_delete_confirm,  // Resource ID
                    selectedInstrument?.model ?: ""  // Value for %s placeholder
                )
            ) },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            viewModel.deleteInstrument(selectedInstrument!!.id)
                        }
                        showDeleteConfirmation = false
                        selectedInstrument = null
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        selectedInstrument = null
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun InstrumentsList(
    instruments: List<Instrument>,
    onEditClick: (Instrument) -> Unit,
    onDeleteClick: (Instrument) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(instruments) { instrument ->
            InstrumentCard(
                instrument = instrument,
                onEditClick = { onEditClick(instrument) },
                onDeleteClick = { onDeleteClick(instrument) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrumentCard(
    instrument: Instrument,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = instrument.model,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${instrument.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Type: ${instrument.type}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = instrument.brand,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Color: ${instrument.color}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Available: ${if (instrument.availability == "1") "Yes" else "No"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.add))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.add))
                }

                TextButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.delete))
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun EmptyInstrumentsList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.title_empty_list),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.msg_empty_list),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrumentDialog(
    instrument: Instrument? = null,
    onDismiss: () -> Unit,
    onSave: (Instrument) -> Unit
) {
    val isEditing = instrument != null

    var model by remember { mutableStateOf(instrument?.model ?: "") }
    var type by remember { mutableStateOf(instrument?.type.toString()) }
    var price by remember { mutableStateOf((instrument?.price ?: 0.0).toString()) }
    var brand by remember { mutableStateOf(instrument?.brand ?: "") }
    var availability by remember { mutableStateOf(instrument?.availability ?: "0") }
    var color by remember { mutableStateOf(instrument?.color ?: "") }
    var material by remember { mutableStateOf(instrument?.material?.toString() ?: "") }
    var stringCount by remember { mutableStateOf(instrument?.stringCount?.toString() ?: "") }
    var warranty by remember { mutableStateOf(instrument?.warranty?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (isEditing) stringResource(R.string.add) else stringResource(R.string.add)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Model") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Type") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Brand") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Available")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = availability  == "1",
                        onCheckedChange = { isChecked ->
                            availability = if (isChecked) "1" else "0" }
                    )
                }

                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("Color") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = material,
                    onValueChange = { material = it },
                    label = { Text("Material (numeric)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stringCount,
                    onValueChange = { stringCount = it },
                    label = { Text("String Count (numeric)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = warranty,
                    onValueChange = { warranty = it },
                    label = { Text("Warranty (months)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val request = Instrument(
                        model = model,
                        type = type.toString(),
                        price = price.toDoubleOrNull() ?: 0.0,
                        brand = brand,
                        availability = availability,
                        color = color,
                        material = material.toIntOrNull() ?: 0,
                        stringCount = stringCount.toIntOrNull() ?: 0,
                        warranty = warranty.toIntOrNull() ?: 0
                    )
                    onSave(request)
                },
                enabled = model.isNotBlank() && type.toString().isNotBlank() && price.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
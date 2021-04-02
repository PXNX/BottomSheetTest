package pxnx.BottomSheetTest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pxnx.BottomSheetTest.ui.theme.BottomSheetTestTheme

class MainActivity : ComponentActivity() {
    val TAG = "MAIN"
    val DESC = "DESCRIPTION"

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BottomSheetTestTheme {

                //requires two clicks
                /*    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                    val scope = rememberCoroutineScope()

                    val (bottomSheet, changeBottomSheet) = remember(calculation = { mutableStateOf(0) })

                    val currentSheet: MutableState<@Composable () -> Unit> =
                        mutableStateOf({ Text("this is the initial Text") })






                    ModalBottomSheetLayout(
                        sheetState = state,
                        sheetShape = RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp),
                        sheetBackgroundColor = LightGray,
                        sheetContent =
                        {
                            Log.w(TAG, "run sheet content ----- 1")
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        state.hide()
                                    }
                                },
                                Modifier.align(End)
                            ) {
                                Icon(
                                    Icons.Filled.Close,
                                    DESC,
                                    tint = androidx.compose.ui.graphics.Color.Companion.Gray
                                )
                            }




                            Log.w(TAG, "run sheet content")


                            currentSheet.value()


                            Log.w(TAG, "sheet done!!")


                        }

                    ) {
                        Column {


                            Button({
                                scope.launch {
                                    state.show()
                                }

                            },
                                Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()) {
                                Text("show sheet without changes", Modifier.padding(20.dp))
                            }



                            Button({

                                currentSheet.value = {
                                    Text(
                                        "with changed value. only show() is inside coroutine.",
                                        Modifier.padding(20.dp)
                                    )
                                }

                                scope.launch {
                                    state.show()

                                }

                            },
                                Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()) {
                                Text(
                                    "with changed value. only show() is inside coroutine.",
                                    Modifier.padding(20.dp)
                                )
                            }




                            Button({
                                scope.launch {
                                    currentSheet.value = {
                                        Text(
                                            "another one, both are inside coroutine",
                                            Modifier.padding(20.dp)
                                        )
                                    }
                                    state.show()
                                }

                            },
                                Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()) {
                                Text("another one, both are inside coroutine", Modifier.padding(20.dp))
                            }
                        }
                    }

                 */

                //requires two clicks
                //has no scrim and no swipe
                /*
                val state = rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))
                val scope = rememberCoroutineScope()

                val currentSheet: MutableState<@Composable () -> Unit> =
                    mutableStateOf({ Text("this is the initial Text") })

                BottomSheetScaffold(sheetContent = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                state.bottomSheetState.collapse()
                            }
                        },
                        Modifier.align(End)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            DESC,
                            tint = androidx.compose.ui.graphics.Color.Companion.Gray
                        )
                    }




                    currentSheet.value()


                              Text("Initial value")
                },
                scaffoldState = state) {


                    Column {


                        Button({
                            scope.launch {
                                state.bottomSheetState.expand()
                            }

                        },
                            Modifier
                                .padding(20.dp)
                                .fillMaxWidth()) {
                            Text("show sheet without changes", Modifier.padding(20.dp))
                        }



                        Button({

                            currentSheet.value = {
                                Text(
                                    "with changed value. only show() is inside coroutine.",
                                    Modifier.padding(20.dp)
                                )
                            }

                            scope.launch {
                                state.bottomSheetState.expand()

                            }

                        },
                            Modifier
                                .padding(20.dp)
                                .fillMaxWidth()) {
                            Text(
                                "with changed value. only show() is inside coroutine.",
                                Modifier.padding(20.dp)
                            )
                        }




                        Button({
                            scope.launch {
                                currentSheet.value = {
                                    Text(
                                        "another one, both are inside coroutine",
                                        Modifier.padding(20.dp)
                                    )
                                }
                                state.bottomSheetState.expand()
                            }

                        },
                            Modifier
                                .padding(20.dp)
                                .fillMaxWidth()) {
                            Text("another one, both are inside coroutine", Modifier.padding(20.dp))
                        }
                    }
                }
                 */

                /*       val state = rememberBottomSheetScaffoldState(
                           bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
                       )
                       val scope = rememberCoroutineScope()

                       val currentSheet: MutableState<@Composable () -> Unit> =
                           mutableStateOf({ Text("this is the initial Text") })



                       val expand = remember{
                           mutableStateOf(false)
                       }

                    /*   currentSheet..observe(context, Observer {

                           //Do something with the changed value -> it

                       })

                     */

                       var sheetContentOBSERVED: @Composable () -> Unit
                               by Delegates.observable({ Text("this is the initial Text") }) { property, oldValue, newValue ->
                           // do your stuff here
                           scope.launch {
                               state.bottomSheetState.expand()

                           }
                       }

                       BottomSheetScaffold(
                           sheetContent = {
                               IconButton(
                                   onClick = {
                                       scope.launch {
                                           state.bottomSheetState.collapse()
                                       }
                                   },
                                   Modifier.align(End)
                               ) {
                                   Icon(
                                       Icons.Filled.Close,
                                       DESC,
                                       tint = androidx.compose.ui.graphics.Color.Companion.Gray
                                   )
                               }

                               //                        currentSheet.value()

                               sheetContentOBSERVED()

                               Spacer(modifier = Modifier.height(56.dp))


                           /*    if(expand.value)
                                   scope.launch {
                                       state.bottomSheetState.expand()

                                   }

                            */




                           },
                           scaffoldState = state,
                           sheetShape = RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp)
                       ) {


                           Column {


                               Button(
                                   {
                                       scope.launch {
                                           state.bottomSheetState.expand()
                                       }

                                   },
                                   Modifier
                                       .padding(20.dp)
                                       .fillMaxWidth()
                               ) {
                                   Text("show sheet without changes", Modifier.padding(20.dp))
                               }



                               Button(
                                   {

                                       sheetContentOBSERVED={

                                                   Text(
                                                       "with changed value. only show() is inside coroutine.",
                                                       Modifier.padding(20.dp)
                                                   )


                                           }









                                   },
                                   Modifier
                                       .padding(20.dp)
                                       .fillMaxWidth()
                               ) {
                                   Text(
                                       "with changed value. only show() is inside coroutine.",
                                       Modifier.padding(20.dp)
                                   )
                               }




                               Button(
                                   {






                                           currentSheet.value = {
                                               Text(
                                                   "another one, both are inside coroutine",
                                                   Modifier.padding(20.dp)
                                               )

                                               scope.launch {

                                                      state.bottomSheetState.expand()

                                               }

                                           }



                                   },
                                   Modifier
                                       .padding(20.dp)
                                       .fillMaxWidth()
                               ) {
                                   Text("another one, both are inside coroutine", Modifier.padding(20.dp))
                               }
                           }
                       }*/

//works, but looks kinda ugly^^
                /*       val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                       val scope = rememberCoroutineScope()

                       val (bottomSheet, changeBottomSheet) = remember(calculation = { mutableStateOf(0) })

                       val currentSheet: MutableState<@Composable () -> Unit> =
                           mutableStateOf({ Text("this is the initial Text") })


                       val state2 = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)


                       val currentSheet2: MutableState<@Composable () -> Unit> =
                           mutableStateOf({ Text("this is the another Text") })



                       ModalBottomSheetLayout(
                           sheetState = state,
                           sheetShape = RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp),
                           sheetBackgroundColor = LightGray,
                           sheetContent =
                           {
                               Log.w(TAG, "run sheet content ----- 1")
                               IconButton(
                                   onClick = {
                                       scope.launch {
                                           state.hide()
                                       }
                                   },
                                   Modifier.align(End)
                               ) {
                                   Icon(
                                       Icons.Filled.Close,
                                       DESC,
                                       tint = androidx.compose.ui.graphics.Color.Companion.Gray
                                   )
                               }




                               Log.w(TAG, "run sheet content")


                               currentSheet.value()


                               Log.w(TAG, "sheet done!!")


                           }

                       ) {


                           ModalBottomSheetLayout(
                               sheetState = state2,
                               sheetShape = RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp),
                               sheetBackgroundColor = LightGray,
                               sheetContent =
                               {
                                   Log.w(TAG, "run sheet content ----- 1")
                                   IconButton(
                                       onClick = {
                                           scope.launch {
                                               state2.hide()
                                           }
                                       },
                                       Modifier.align(End)
                                   ) {
                                       Icon(
                                           Icons.Filled.Close,
                                           DESC,
                                           tint = androidx.compose.ui.graphics.Color.Companion.Gray
                                       )
                                   }




                                   Log.w(TAG, "run sheet content")


                                   currentSheet2.value()


                                   Log.w(TAG, "sheet done!!")


                               }

                           ) {
                               Column {


                                   Button(
                                       {
                                           scope.launch {
                                               state.show()
                                           }

                                       },
                                       Modifier
                                           .padding(20.dp)
                                           .fillMaxWidth()
                                   ) {
                                       Text("show sheet without changes", Modifier.padding(20.dp))
                                   }



                                   Button(
                                       {
                                           scope.launch {
                                               state.show()
                                           }

                                       },
                                       Modifier
                                           .padding(20.dp)
                                           .fillMaxWidth()
                                   ) {
                                       Text(
                                           "with changed value. only show() is inside coroutine.",
                                           Modifier.padding(20.dp)
                                       )
                                   }




                                   Button(
                                       {
                                           scope.launch {
                                               state2.show()
                                           }

                                       },
                                       Modifier
                                           .padding(20.dp)
                                           .fillMaxWidth()
                                   ) {
                                       Text(
                                           "another one, both are inside coroutine",
                                           Modifier.padding(20.dp)
                                       )
                                   }
                               }
                           }


                       }

                 */


                val state: SwipeableState<BottomSheetValue> =
                    rememberSwipeableState(initialValue = BottomSheetValue.HIDDEN)
                val scope = rememberCoroutineScope()

                val currentSheet: MutableState<@Composable () -> Unit> =
                    mutableStateOf({ Text("this is the initial Text") })

                Column {

                    Button(
                        {
                            scope.launch {
                                state.animateTo(BottomSheetValue.SHOWING)
                            }

                        },
                        Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Text("show sheet without changes", Modifier.padding(20.dp))
                    }



                    Button(
                        {
                            scope.launch {
                                currentSheet.value = {
                                    Text(
                                        "with changed value. only show() is inside coroutine.",
                                        Modifier.padding(20.dp)
                                    )
                                }

                                state.animateTo(BottomSheetValue.SHOWING)
                            }

                        },
                        Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            "with changed value. only show() is inside coroutine.",
                            Modifier.padding(20.dp)
                        )
                    }



                    Button(
                        {
                            scope.launch {
                                currentSheet.value = {
                                    Text(
                                        "another one, both are inside coroutine",
                                        Modifier.padding(20.dp)
                                    )
                                }

                                state.animateTo(BottomSheetValue.SHOWING)
                            }

                        },
                        Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            "another one, both are inside coroutine",
                            Modifier.padding(20.dp)
                        )
                    }

                    BoxWithConstraints {
                        BottomSheet(
                            parentHeight = constraints.maxHeight,
                            topOffset = with(LocalDensity.current) { 56.toDp() },
                            fillMaxHeight = true,
                            sheetState = state,
                            contentColor = Yellow,
                        ) {
                            currentSheet.value()
                        }
                    }

                }

//todo --- what about listening for changes of currentSheet and whenever it changed, open the sheet?

//associated anchor for inital value missing
                //  ModalBottomSheetLayoutDemo()


            }
        }
    }

/*
    @ExperimentalMaterialApi
    @Composable
    fun ModalBottomSheetLayoutDemo() {
        val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

        val modalState2 = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

        val modalState3 = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

        val sillyScope = rememberCoroutineScope()

        Column {


            ModalBottomSheetLayout(sheetState = modalState, sheetContent = {
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    text = "Title",
                    fontWeight = FontWeight.Bold,
                    style = typography.h5
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    text = "Content example right here :)",
                    style = typography.body1
                )
                Row(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)) {
                    Button(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = { sillyScope.launch { modalState.hide() } }) {
                        Text("Cancel")
                    }
                    Button(onClick = { sillyScope.launch { modalState.hide() } }) {
                        Text("Ok")
                    }
                }
            }, sheetElevation = 8.dp) { Text("Sheet1")}

            ModalBottomSheetLayout(sheetState = modalState2, sheetContent = {
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    text = "Title",
                    fontWeight = FontWeight.Bold,
                    style = typography.h5
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    text = "Content example right here :)",
                    style = typography.body1
                )
                Row(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)) {
                    Button(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = { sillyScope.launch { modalState2.hide() } }) {
                        Text("Cancel")
                    }
                    Button(onClick = { sillyScope.launch { modalState2.hide() } }) {
                        Text("Ok")
                    }
                }
            }, sheetElevation = 8.dp) { Text("Sheet2")}

            ModalBottomSheetLayout(sheetState = modalState3, sheetContent = {
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    text = "Title",
                    fontWeight = FontWeight.Bold,
                    style = typography.h5
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    text = "Content example right here :)",
                    style = typography.body1
                )
                Row(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)) {
                    Button(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = { sillyScope.launch { modalState3.hide() } }) {
                        Text("Cancel")
                    }
                    Button(onClick = { sillyScope.launch { modalState3.hide() } }) {
                        Text("Ok")
                    }
                }
            }, sheetElevation = 8.dp) { Text("Sheet3")}


            Button(modifier = Modifier.padding(16.dp), onClick = {
                sillyScope.launch {
                    modalState.show()
                    modalState2.show()
                    modalState3.show()
                }
            }) {
                Text("Show Bottom Sheet")
            }
        }

    }

 */

}
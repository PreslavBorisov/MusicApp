package eu.example.musicappui.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.primarySurface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.example.musicappui.MainViewModel
import eu.example.musicappui.R
import eu.example.musicappui.Screen
import eu.example.musicappui.screensInBottom
import eu.example.musicappui.screensInDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView(){

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope:CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel  = viewModel()
    val isSheetFullScreen by remember {
        mutableStateOf(false)
    }
    val modifier = if(isSheetFullScreen)Modifier.fillMaxSize()else Modifier.fillMaxWidth()
    // Allow us to find out which route is selected
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    val modelSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {it != ModalBottomSheetValue.HalfExpanded}
    )

    val roundedCornerRadius = if(isSheetFullScreen) 0.dp else 12.dp

    val bottomBar: @Composable ()  -> Unit = {
        if(currentScreen is Screen.DrawerScreen || currentScreen is Screen.BottomScreen.Home){
            BottomNavigation(Modifier.wrapContentSize()) {
                screensInBottom.forEach{
                    item ->
                    val isSelected = currentRoute==item.bRoute
                    val tint = if(isSelected)Color.White else Color.Black
                    BottomNavigationItem(selected = currentRoute == item.bRoute,
                        onClick = {
                                  controller.navigate(item.bRoute)
                            title.value = item.bTitle
                        }, icon = {


                            Icon(painter = painterResource(id = item.icon),
                                contentDescription = item.bTitle, tint = tint)
                        }, label = { Text(text = item.bTitle,color = tint)},
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black

                    )
                }


            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = modelSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = { MoreBottomSheet(modifier = modifier)}
    ) {
        Scaffold(
            bottomBar = bottomBar,
            topBar = {
                TopAppBar(title = { Text(text = title.value)},
                    actions = {
                              IconButton(onClick = { 
                                  scope.launch { 
                                      if(modelSheetState.isVisible) modelSheetState.hide()
                                      else modelSheetState.show()
                                  }
                              }) {
                                  Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                              }
                    },
                    navigationIcon = { IconButton(onClick = {
                        // Open the drawer
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription= "Menu")
                    }}
                )
            }, scaffoldState = scaffoldState,
            drawerContent = {
                LazyColumn(Modifier.padding(16.dp) ){
                    items(screensInDrawer){
                            item->
                        DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            if(item.dRoute == "add_account"){
                                //Open the dialog
                                dialogOpen.value = true
                            }else{
                                controller.navigate(item.dRoute)
                                title.value = item.dTitle
                            }
                        }
                    }
                }
            }


        ) {
            Navigation(navController = controller,viewModel = viewModel,pd = it)

            AccountDialog(dialogOpen = dialogOpen)

        }

    }

    }


@Composable
fun MoreBottomSheet(modifier: Modifier) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colors.primarySurface)){
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(painter = painterResource(id = R.drawable.baseline_settings_24), contentDescription ="Settings" ,modifier=Modifier.padding(end = 10.dp))
                Text(text = "Settings", fontSize = 20.sp, color = Color.White)
            }
            
            Row(modifier = Modifier.padding(16.dp)){
                Icon(painter = painterResource(id = R.drawable.baseline_share_24), contentDescription = "Share",modifier=Modifier.padding(end =10.dp) )
                Text(text = "Share", fontSize = 20.sp, color = Color.White)
            }
            Row(Modifier.padding(16.dp)) {
                Icon(painter = painterResource(id = R.drawable.baseline_help_24), contentDescription = "Help",modifier=Modifier.padding(end = 10.dp) )
                Text(text = "Help", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}


@Composable
fun DrawerItem(
     selected: Boolean,
     item:Screen.DrawerScreen,
     onDrawerItemClicked: ()->Unit
){
    val background = if(selected) Color.LightGray else Color.White
   Row (
       Modifier
           .fillMaxSize()
           .padding(horizontal = 8.dp, vertical = 16.dp)
           .background(background)
           .clickable {
               onDrawerItemClicked()
           }){
       Icon(painter = painterResource(id = item.icon), contentDescription =item.dTitle,
           Modifier.padding(end = 8.dp, top = 4.dp)
       )
       Text(text = item.dTitle,
           style = MaterialTheme.typography.h5
       )
   }
}

@Composable
fun Navigation(navController: NavController,viewModel: MainViewModel,pd:PaddingValues){
    NavHost(navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route,
        modifier = Modifier.padding(pd)){

        composable(Screen.DrawerScreen.Account.route){
            AccountView()
        }
        composable(Screen.DrawerScreen.Subscription.route){
            Subscription()
        }
        composable(Screen.BottomScreen.Home.bRoute){
            Home()
        }
        composable(Screen.BottomScreen.Browse.bRoute){
            Browse()
        }
        composable(Screen.BottomScreen.Library.bRoute){
            Library()
        }
    }

}
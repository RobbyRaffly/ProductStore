package com.adl.bookstore

import android.content.pm.ModuleInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.adl.bookstore.data.BookModel
import com.adl.bookstore.data.ProductModel
import com.adl.bookstore.repo.*
import com.adl.bookstore.ui.theme.BookStoreTheme
import com.adl.bookstore.viewmodel.BookViewModel
import com.adl.bookstore.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {
    val bookViewModel by viewModels<BookViewModel>(factoryProducer = { BookViewModelFactory(
        BookRepo()
    ) })
    val productViewModel by viewModels<ProductViewModel>(factoryProducer = { ProductViewModelFactory(
        ProductRepo()
    ) })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ProductOutlet(productViewModel = productViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BookStoreTheme {
        Greeting("Android")
    }
}

@Composable
fun BookOutlet(bookViewModel: BookViewModel){
    when(val bookList = bookViewModel.bookStateFlow.asStateFlow().collectAsState().value){
        is OnFailure ->{

        }
        is OnSuccess ->{
            val listOfBook = bookList.querySnapshot?.toObjects(BookModel::class.java)
            listOfBook?.let{
                Column {
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)){
                        items(listOfBook){
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                                shape = RoundedCornerShape(16.dp), elevation = 5.dp){
                                BookItem(it)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun BookItem(book:BookModel){
    var showBookSinopsis by remember {mutableStateOf(false)}

    Column(modifier = Modifier.clickable {
        showBookSinopsis=showBookSinopsis.not()
    }){
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
            model = book.gambar,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Fit)
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = book.judul, style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp))
                Text(text = "Rp. ${book.harga}", style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp))
            }
        }
        AnimatedVisibility(visible = showBookSinopsis) {
            Text(text = book.sinopsis,
                fontWeight = FontWeight.SemiBold, fontSize = 12.sp,
                modifier = Modifier.padding(15.dp), textAlign = TextAlign.Justify)
        }
    }
}

@Composable
fun ProductOutlet(productViewModel: ProductViewModel){
    when(val productList = productViewModel.productStateFlow.asStateFlow().collectAsState().value){
        is OnFailureProduct ->{

        }
        is OnSuccessProduct ->{
            val listOfProduct = productList.querySnapshot?.toObjects(ProductModel::class.java)
            listOfProduct?.let{
                Column {
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)){
                        items(listOfProduct){
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                                shape = RoundedCornerShape(16.dp), elevation = 5.dp){
                                ProductItem(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product:ProductModel){
    var showFullDesc by remember { mutableStateOf(false)}


    Column(){
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit)
            Column(modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(12.dp)) {
                Text(text = product.productName, style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp))
                Text(text = product.category, style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp))
                Text(text = product.description, style = TextStyle(fontWeight = FontWeight.ExtraLight, fontSize = 16.sp))

            }
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier
                    .clickable {
                        showFullDesc = showFullDesc.not()
                    }
                    .padding(5.dp, 10.dp, 5.dp, 10.dp)) {

                    if(showFullDesc==false){
                        Image(painter = painterResource(id = R.drawable.arrow_down), contentDescription = "", Modifier.size(20.dp,20.dp),
                            Alignment.TopEnd)
                    }else{
                        Image(painter = painterResource(id = R.drawable.arrow_up), contentDescription = "",Modifier.size(20.dp,20.dp),
                            Alignment.TopEnd)
                    }
                 }
                Text(text = "Rp. ${product.price}", style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp),textAlign = TextAlign.End)
            }
        }
        AnimatedVisibility(visible = showFullDesc) {
            Text(text = product.fullDescription,
                fontWeight = FontWeight.SemiBold, fontSize = 12.sp,
                modifier = Modifier.padding(15.dp), textAlign = TextAlign.Justify)
        }
    }
}


class BookViewModelFactory (val booksRepo: BookRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookViewModel::class.java)){
            return  BookViewModel(booksRepo) as T
        }
        throw IllegalStateException()
    }

}

class ProductViewModelFactory (val productRepo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProductViewModel::class.java)){
            return  ProductViewModel(productRepo) as T
        }
        throw IllegalStateException()
    }

}
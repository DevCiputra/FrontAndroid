package com.ciputramitra.navgraph

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

@Serializable
object Authentication

@Serializable
object Home



@Serializable
object ConsultationOnline

@Serializable
data class DoctorAll(
	val categoryPolyclinicID : Int,
	val nameCategoryPolyclinic: String,
)


@Serializable
data class DoctorDetailArgs(
	val doctorID: Int
)

@Serializable
object Cart

@Serializable
object Checkout

@Serializable
object GoogleMap

@Serializable
data class AddressShippingArgs(
	val cityCoordinates : String ,
	val addressCoordinates : String
)

@Serializable
data class PaymentArgs(
	val tokenPayment: String,
	val transactionID: Int
)

@Serializable
data class DetailTransactionArgs(
	val id: Int
)

@Serializable
data class RatesArgs(
	val userID: Int,
	val productID: Int,
	val variant: String,
	val size: String,
	val category: String,
	val image: String,
	val nameProduct: String
)

@Serializable
object ProfilePatient
package com.insidecoderz001.smartparkingsystem

data class BookingHistory(
                            val phoneNum :String?=null,
                           val bookId :String?=null,
                           val checkInDate :String?=null,
                           val checkInTime :String?=null,
                           val checkOutDate :String?=null,
                           val checkOutTime :String?=null,
                           val amount:String?=null
                          )

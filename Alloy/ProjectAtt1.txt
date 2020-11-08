abstract sig Location{}
sig Country{}
sig City{}
sig Street{}
sig decNum{
	intPart: one Int,
	decPart: one Int
	}{ decPart>0
	}


// mozda samo ici s lokacijom tj. s GPS koordinatima
sig Address extends Location {
	country: one Country,
	city: one City,
	street: one Street,
	number: one Int
	}

sig GPSCoordinates extends Location{
	latitude: one decNum,
	longitude: one decNum
	}	
	{
		latitude.intPart < 90 and latitude.decPart > -90
		longitude.intPart < 180 and longitude.decPart > -180
	}

sig WorkingTime{
	openHours: one Int,
	openMins: one Int,
	closeHours: one Int,
	closeMins: one Int,
}

sig Date{
	day: one Int,
	month: one Int,
	year: one Int
}{
	day > 0
	month > 0
	year >0 
}

sig ShoppingWindow{
	beginHours: one Int,
	beginMins: one Int,
	expectedTime: one Int,
	dayOfYear: one Date
	
}


sig Store{
	loc: one Address, // maybe without that
	locGPS: one GPSCoordinates,
	time: one WorkingTime,
	maxBuyers: one Int,
	currentBuyers: one Int,
	lastTicketNumber: one Int	

	}

sig Buyer{
	loc: one Address, // maybe without that
	locGPS: one GPSCoordinates,
	shop: one ShoppingWindow,
	ticketNumber: one Int
}

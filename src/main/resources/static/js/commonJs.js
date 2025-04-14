function deleteCity(id){
    $.post( "deleteCity", { id: id } );
    window.location = "cityList";
}

function shtoPromoted(id){
    $.post( "promoteCity", { id: id } );
    window.location = "cityList";
}

function addSeasonalDeals(id){
    $.post( "seasonalDeals", { id: id }, function (response) {
        console.log(response);
        location.reload();
    } );
}

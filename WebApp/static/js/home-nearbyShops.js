function shopClick(btn) {

    axios({
        method: 'get',
        url: "/shops/:"+btn.id
    })
    .then( (response) => console.log(response.data) )
    .catch( (error) => console.log(error) );

}

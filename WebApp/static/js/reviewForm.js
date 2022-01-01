function validateReview() {
    let reviewBody = document.forms["reviewPostForm"]["reviewBody"].value;
    let rating = document.forms["reviewPostForm"]["rating"].value;;
    if(reviewBody=="" || rating==""){
        alert("empty review!");
        return false;
    }
}
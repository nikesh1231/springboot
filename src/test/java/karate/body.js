

function fn() {
    var token = karate.get('jwt');

    return {
        Authorization: token
    }

}
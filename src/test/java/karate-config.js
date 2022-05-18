function fn() {

    var config = {
        baseUrl : 'http://localhost:5000',
        authUrl : 'http://localhost:5000/api/login',
        login_request : {
            username: 'sally_admin',
            password: 'password01!'}
    };
    var result = karate.callSingle('classpath:karate/auth/authentication.feature', config);
    config.authInfo = {'Authorization': 'Bearer ' + result.response.jwt };
    return config;
}

function getGithubInfo(user) {
    //1. Create an instance of XMLHttpRequest class and send a GET request using it. The function should finally return the object(it now contains the response!)
    return $.ajax({
        url: 'https://api.github.com/users/' + user,
        dataType: 'JSON'
    })
}

function showUser(user) {
    const name = user['name']
    const avatar = user['avatar_url']
    const bio = user['bio']
    const follows = user['followers']
    const url = user['html_url']
    const repos = user['public_repos']

    // Clear previous contents
    $('#profile .avatar img').remove()
    $('#profile .information li').remove()

    //2. set the contents of the h2 and the two div elements in the div '#profile' with the user content
    console.log(user)
    $('#profile h2').text(name)
    $('#profile .avatar').prepend(`<img src="${avatar}" alt="avatar"/>`)

    // Check to make sure user created a bio
    $('#profile .information ul').append(`<li><strong>Bio:</strong> ${bio ? bio : 'No bio provided'}</li>`)
    $('#profile .information ul').append(`<li><strong>Followers:</strong> ${follows}</li>`)
    $('#profile .information ul').append(`<li><strong>Repos:</strong> ${repos}</li>`)
    $('#profile .information ul').append(`<li><strong>URL:</strong> <a href=${url}>${url}</a></li>`)
    // $('#profile .information').text(bio ? bio : 'No bio provided.')
}

function noSuchUser(error) {
    //3. set the elements such that a suitable message is displayed
    $('#profile .avatar img').remove()
    $('#profile .information li').remove()
    $('#profile h2').text("No such user. Try again.")
}


$(document).ready(function(){
    $(document).on('keypress', '#username', function(e){
        //check if the enter(i.e return) key is pressed
        if (e.which === 13) {
            //get what the user enters
            const username = $(this).val();

            //reset the text typed in the input
            $(this).val("");

            //get the user's information and store the response
            const response = Promise.resolve(getGithubInfo(username));

            //if the response is successful show the user's details
            response.then(data => {
                showUser(data)
            }, error => {
                noSuchUser(error)
            })
        }
    })
});

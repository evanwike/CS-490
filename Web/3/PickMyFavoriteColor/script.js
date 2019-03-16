// An array of colors and assign it to a variable colors
const colors = [ "22ac5e", "d68236", "71b5c2", "af2655", "b34de7", "e6bd01", "104393", "ca4d94", "4a772d", "c180a7", "958112", "8d2f8d" ]
let favCount = 0

// Sets the preview color to the one entered in the input and display its color code using setPreviewColor function
const setPreviewColor = color => {
    $('.preview').css('background-color', color)
    $('.color-code').text($('.preview').css('background-color'))
}

// Adds color boxes to the favorite colors
const addBox = color => {
    // Makes sure only 16 favorite colors can be saved at one time by removing oldest element once count reaches 16
    if (favCount >= 16) {
        $('#colors div:last').remove()
        favCount--
    }

    $('#colors').prepend("<div class='item'></div>")
    $('#colors div:first').css('background-color', color)
    favCount++
}

const ready = () => {
    // 1. Add each color in the colors array to the div '#colors'
    colors.forEach(addBox)

    // Set the preview color to one of the colors in the colors array randomly
    setPreviewColor(colors[Math.floor(Math.random() * colors.length)])

    // An event handler for the key up event
    // The event should set the preview color to the color typed in the input
    // $(document).on('keydown keyup keypress', '#color', () => setPreviewColor($(this).val()))
    $(document).on('keydown keyup keypress', '#color', function() {setPreviewColor($(this).val())})

    // 2. Write an event handler to handle the click event on the add to favorite button so that the color gets added to
    // the list of favorite colors, the content of the input gets cleared and the focus gets back on the input
    $('#favorite').click(() => {
        addBox($('#color').val())
        $('#color').val('')
        $('#color').focus()
    })

    // 3. Write event handlers such that whenever any item in the favorite colors is clicked or hovered,
    // the color gets displayed in the preview div
    $('#colors div').on('click mouseover', function() {
        setPreviewColor($(this).css('background-color'))
        // console.log($(this).css('background-color'))
    })
}

$(document).ready(ready)
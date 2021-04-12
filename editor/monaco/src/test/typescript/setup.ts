require('jsdom-global')();

window.document.execCommand = function (command, ui, value) {
    const node = window.getSelection().anchorNode;
    switch (command) {
        case 'insertHTML':
            // @ts-ignore
            if (node.innerHTML) {
                // @ts-ignore
                node.innerHTML += value;
            } else { // Text node
                // @ts-ignore
                node.parentNode.innerHTML += value;
            }
            return true;
        case 'insertLineBreak':
            // @ts-ignore
            node.innerHTML += '<br>';
            return true;
    }
    return false;
}
window.document.queryCommandSupported = function (command) {
    return false;
}
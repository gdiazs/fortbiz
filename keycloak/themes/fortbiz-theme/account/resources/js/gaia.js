
var $gaia = (function(gaia) {
    var _gaia = gaia || {};
    _gaia.onLoad = function (callback){
        document.addEventListener("DOMContentLoaded", function() {
            callback();
        });
    }

    return _gaia;


})($gaia);

const createHamburgerToggleBtn = () => {
    const hamburgerIconWrapper = document.createElement('div');
    hamburgerIconWrapper.setAttribute('class', 'toggleMenu')

    const hamburgerIcon = document.createElement('i');

    hamburgerIcon.setAttribute('class', 'fa fa-bars');
    hamburgerIconWrapper.appendChild(hamburgerIcon);

    return hamburgerIconWrapper;
}

$gaia.onLoad(()=>{

    // Add meta tag for mobile
    var meta = document.createElement('meta');
    meta.name = 'viewport';
    meta.setAttribute('content', 'width=device-width,initial-scale=1');
    document.getElementsByTagName('head')[0].appendChild(meta);


    // Hide first menu level 
    const langMenu = document.querySelector('.kc-dropdown > ul'); 
    langMenu.classList.add('hide');

    //Hide menu 


    const titleWrapper = document.querySelector('.navbar-header > .container');

    const hamburGerBtn = createHamburgerToggleBtn();
    titleWrapper.appendChild(hamburGerBtn);

    hamburGerBtn.addEventListener('click', () => {
        document.querySelector('nav.navbar')
            .classList.toggle('h-auto');
    });


});





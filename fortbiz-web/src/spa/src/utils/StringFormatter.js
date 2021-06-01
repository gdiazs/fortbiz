export default {
    formatCrPhone(value){
        const x = value.replace(/\D/g, '').match(/(\d{0,4})(\d{0,4})/);
        value = !x[2]? x[1] : x[1] + ' - ' + x[2];
        return value
    },
    validateEmail(email) {
        // eslint-disable-next-line no-useless-escape
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }
}
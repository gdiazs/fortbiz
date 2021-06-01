export default {
    isEmptyOrNull(value){
        return value === undefined || value === null || value.replace(/\s+/g, "").length === 0
    },
    isNull(value){
        return value === null
    }
}
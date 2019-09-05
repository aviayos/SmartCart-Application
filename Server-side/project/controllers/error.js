/**
 * A class which represents a server error.
 * The class imitiates `express-validator` error.
 */
export default class ServerError {
    /**
     * Instantiate a new server error
     * @param { String } location The location on the http request.
     * @param { String } param    The name of the bad paramater.
     * @param { String } value    The value of the bad paramter.
     * @param { String } msg      The msg sent to client.
     */
    constructor(location, param, value, msg) {
        this.location = location;
        this.param = param;
        this.value = value;
        this.msg = msg;
    }
}
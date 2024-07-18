import Keycloak from "keycloak-js";
const keycloak = new Keycloak({
 url: "http://keycloak:8080/",
 realm: "ssds",
 clientId: "ssds-client",
});
console.log(keycloak);

export default keycloak;
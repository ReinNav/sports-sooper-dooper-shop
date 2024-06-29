import Keycloak from "keycloak-js";
const keycloak = new Keycloak({
 url: "http://localhost:8080/",
 realm: "ssds",
 clientId: "ssds-client",
});

export default keycloak;
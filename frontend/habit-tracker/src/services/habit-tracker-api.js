import axios from "axios"

const habitTrackerApi = axios.create({
    baseURL: "http://localhost:8080"
})

export default habitTrackerApi;
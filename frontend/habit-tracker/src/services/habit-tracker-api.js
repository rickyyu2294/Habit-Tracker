import axios from "axios"

const habitTrackerApi = axios.create({
    baseURL: "http://localhost:8080"
})
const authEndpoints = ["/login", "/refresh", "/register"]
let isRefreshing = false;
let refreshSubscribers = [];

export default habitTrackerApi

// Function to add subscribers to the queue
function subscribeTokenRefresh(cb) {
    refreshSubscribers.push(cb);
}

// Notify all subscribers once the token refresh is complete
function onRefreshed(newAccessToken) {
    refreshSubscribers.forEach((cb) => cb(newAccessToken));
    refreshSubscribers = [];
}

async function refreshAccessToken() {
    const refreshToken = localStorage.getItem("refreshToken");
    if (!refreshToken) {
        console.error("Refresh token is missing");
        throw new Error("Refresh token is missing");
    }

    try {
        const response = await habitTrackerApi.post("/refresh", { token: refreshToken });
        const {accessToken: newAccessToken, refreshToken: newRefreshToken} = response.data;

        // Store the new access token in localStorage
        localStorage.setItem("accessToken", newAccessToken);
        localStorage.setItem("refreshToken", newRefreshToken);
        return newAccessToken
    } catch (error) {
        console.error("Failed to refresh access tokens:", error);
        throw error;
    }
}

// axios request interceptor to add access token to headers
habitTrackerApi.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem("accessToken");
        const isAuthEndpoint = authEndpoints.some((endpoint) => {
            return config.url.includes(endpoint)
        })
        if (accessToken && !isAuthEndpoint) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// axios response interceptor to handle access token expiration
habitTrackerApi.interceptors.response.use(
    (response) => response, // Pass through successful responses
    async (error) => {
        const originalRequest = error.config;
        const isAuthEndpoint = authEndpoints.some((endpoint) => {
            return originalRequest.url.includes(endpoint)
        })
        
        // Check if the error is due to an expired access token
        if (error.response && error.response.status === 401 && !originalRequest._retry && !isAuthEndpoint) {
            originalRequest._retry = true; // Prevent infinite retry loops

            if (!isRefreshing) {
                isRefreshing = true
                try {
                    await refreshAccessToken();
                    isRefreshing = false
                    onRefreshed()
                    // Retry the original request with the new access token
                    console.log("Token refreshed successfully")
                    return habitTrackerApi(originalRequest);
                } catch (refreshError) {
                    isRefreshing = false
                    console.error("Refresh token expired or invalid:", refreshError);
                    localStorage.clear(); // Clear any stored tokens
                    window.location.href = "/login"; // Redirect to login page
                }
            }
            
            // Wait for the current refresh attempt to complete
            return new Promise((resolve, reject) => {
                subscribeTokenRefresh((newAccessToken) => {
                    originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                    resolve(habitTrackerApi(originalRequest));
                });
            });
        }

        // Reject the promise for other errors
        return Promise.reject(error);
    }
);

export async function login(email, password) {
    const response = await habitTrackerApiPost("/login", {email, password})
    localStorage.clear()
    localStorage.setItem("accessToken", response.data.accessToken)
    localStorage.setItem("refreshToken", response.data.refreshToken)
}

export function habitTrackerApiGet(url, params) {
    return habitTrackerApi.get(url, { params })
}

export function habitTrackerApiPost(url, data) {
    return habitTrackerApi.post(url, data)
}

export function habitTrackerApiDelete(url) {
    return habitTrackerApi.delete(url)
}
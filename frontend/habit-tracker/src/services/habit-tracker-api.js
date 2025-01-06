import axios from "axios";

const habitTrackerApi = axios.create({
    baseURL: "http://localhost:8080",
});
const authEndpoints = ["/login", "/refresh", "/register", "/logout"];
let isRefreshing = false;
let refreshSubscribers = [];

// Interceptors

// axios request interceptor to add access token to headers
habitTrackerApi.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem("accessToken");
        const isAuthEndpoint = authEndpoints.some((endpoint) => {
            return config.url.includes(endpoint);
        });
        if (accessToken && !isAuthEndpoint) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

// axios response interceptor to handle access token expiration
habitTrackerApi.interceptors.response.use(
    (response) => response, // Pass through successful responses
    async (error) => {
        const originalRequest = error.config;
        const isAuthEndpoint = authEndpoints.some((endpoint) => {
            return originalRequest.url.includes(endpoint);
        });

        // Check if the error is due to an expired access token
        if (
            error.response &&
            error.response.status === 401 &&
            !originalRequest._retry &&
            !isAuthEndpoint
        ) {
            originalRequest._retry = true; // Prevent infinite retry loops

            if (!isRefreshing) {
                isRefreshing = true;
                try {
                    await refreshAccessToken();
                    isRefreshing = false;
                    onRefreshed();
                    // Retry the original request with the new access token
                    console.log("Token refreshed successfully");
                    return habitTrackerApi(originalRequest);
                } catch (refreshError) {
                    isRefreshing = false;
                    console.error(
                        "Refresh token expired or invalid:",
                        refreshError,
                    );
                    localStorage.clear(); // Clear any stored tokens
                    window.location.href = "/login"; // Redirect to login page
                }
            }

            // Wait for the current refresh attempt to complete
            return new Promise((resolve) => {
                subscribeTokenRefresh((newAccessToken) => {
                    originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                    resolve(habitTrackerApi(originalRequest));
                });
            });
        }

        // Reject the promise for other errors
        return Promise.reject(error);
    },
);

// Helpers
async function login(email, password) {
    const response = await habitTrackerApi.post("/login", { email, password });
    localStorage.clear();
    localStorage.setItem("accessToken", response.data.accessToken);
    localStorage.setItem("refreshToken", response.data.refreshToken);
}

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
        const response = await habitTrackerApi.post("/refresh", {
            token: refreshToken,
        });
        const { accessToken: newAccessToken, refreshToken: newRefreshToken } =
            response.data;

        // Store the new access token in localStorage
        localStorage.setItem("accessToken", newAccessToken);
        localStorage.setItem("refreshToken", newRefreshToken);
        return newAccessToken;
    } catch (error) {
        console.error("Failed to refresh access tokens:", error);
        throw error;
    }
}

// API

const api = {
    // auth
    login: (email, password) => login(email, password),

    // habit
    getHabits: () => habitTrackerApi.get("/habits"),
    createHabit: (name, description, interval, frequency) => {
        return habitTrackerApi.post("/habits", {
            name,
            description,
            interval,
            frequency,
        });
    },
    deleteHabit: (habitId) => habitTrackerApi.delete(`/habits/${habitId}`),

    // habit completion
    getCompletions: (habitId, intervalType) => {
        const params = intervalType ? { intervalType: intervalType } : {};
        return habitTrackerApi.get(`/habits/${habitId}/completions`, {
            params,
        });
    },
    createCompletion: (habitId, date) =>
        habitTrackerApi.post(`/habits/${habitId}/completions`, { date: date }),
    createCompletionInInterval: (habitId, interval) =>
        habitTrackerApi.post(
            `/habits/${habitId}/completions/intervals/${interval}`,
        ),
    deleteCompletion: (habitId, date) =>
        habitTrackerApi.delete(`/habits/${habitId}/completions/${date}`),
    deleteLatestCompletionInInterval: (habitId, interval) =>
        habitTrackerApi.delete(
            `/habits/${habitId}/completions/intervals/${interval}/latest`,
        ),

    // habit group
};

export default api;

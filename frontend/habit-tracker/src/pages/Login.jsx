import React, { useState } from 'react'
import Button from '../components/Button'
import InputField from '../components/InputField'
import habitTrackerApi from "../services/habit-tracker-api"
import FormContainer from '../components/FormContainer'

function Login() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")

    const handleLogin = async (e) => {
        e.preventDefault()
        try {
            const response = await habitTrackerApi.post("/login", { email, password })
            localStorage.setItem("accessToken", response.data.accessToken)
            localStorage.setItem("refreshToken", response.data.refreshToken)
            window.location.href = "/dashboard"
        } catch (err) {
            setError("Invalid credentials. Please try again.")
        }
    }

    return (
        <FormContainer title='Login' error={error}>
            <InputField 
                label='Email' 
                value={email} 
                type='email' 
                onChange={(e) => setEmail(e.target.value)}
            />
            <InputField 
                label='Password' 
                value={password} 
                type='email' 
                onChange={(e) => setPassword(e.target.value)}
            />
            <Button type='submit' onClick={handleLogin}>
                Login
            </Button>
        </FormContainer>
    ) 
}

export default Login
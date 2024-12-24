import React, { useState } from 'react'
import Button from '../components/Button'
import InputField from '../components/InputField'
import FormContainer from '../components/FormContainer'
import { habitTrackerApiPost, login } from '../services/habit-tracker-api'

function Login() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")

    const handleLogin = async (e) => {
        e.preventDefault()
        try {
            await login(email, password)
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
                type='password' 
                onChange={(e) => setPassword(e.target.value)}
            />
            <Button type='submit' onClick={handleLogin}>
                Login
            </Button>
        </FormContainer>
    ) 
}

export default Login
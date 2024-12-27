import React, { useState } from 'react'
import MyButton from '../components/MyButton'
import InputField from '../components/InputField'
import FormContainer from '../components/FormContainer'
import api from '../services/habit-tracker-api'
import LoginForm from '../components/LoginForm'

function Login() {

    return (
        <LoginForm />
        // <FormContainer title='Login' error={error}>
        //     <InputField 
        //         label='Email' 
        //         value={email} 
        //         type='email' 
        //         onChange={(e) => setEmail(e.target.value)}
        //     />
        //     <InputField 
        //         label='Password' 
        //         value={password} 
        //         type='password' 
        //         onChange={(e) => setPassword(e.target.value)}
        //     />
        //     <MyButton type='submit' onClick={handleLogin}>
        //         Login
        //     </MyButton>
        // </FormContainer>
    ) 
}

export default Login
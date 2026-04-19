import { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { userContext } from '../../context/userContext';
import { AUTH_TOKEN_KEY, LOCAL_AUTH_PASSWORD_KEY } from '../../services/apiClient';
import { login } from '../../services/storeService';
import { getApiErrorMessage } from '../../api/responseUtils.js';
import toast from 'react-hot-toast';
import styles from './Login.module.css';

const USER_PROFILE_KEY = 'userProfile';
const GOOGLE_AUTH_URL = import.meta.env.VITE_GOOGLE_AUTH_URL;

function parseJwtPayload(token) {
    try {
        const payload = token.split('.')[1];
        const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
        const decoded = decodeURIComponent(
            atob(base64)
                .split('')
                .map((char) => `%${(`00${char.charCodeAt(0).toString(16)}`).slice(-2)}`)
                .join('')
        );
        return JSON.parse(decoded);
    } catch {
        return {};
    }
}

export default function LoginPage() {
    let { setLogin } = useContext(userContext);
    let navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    async function handleLogin(dataForm) {
        setIsLoading(true);
        try {
            let response = await login(dataForm);
            const token = response?.token || response?.data?.token || response?.result?.token;
            if (token) {
                localStorage.setItem(AUTH_TOKEN_KEY, token);
                const existingProfile = JSON.parse(localStorage.getItem(USER_PROFILE_KEY) || '{}');
                const jwtPayload = parseJwtPayload(token);
                localStorage.setItem(USER_PROFILE_KEY, JSON.stringify({
                    ...existingProfile,
                    email: dataForm.email || existingProfile.email || jwtPayload.email || jwtPayload.unique_name || '',
                    name: existingProfile.name || jwtPayload.name || jwtPayload.given_name || ''
                }));
                localStorage.setItem(LOCAL_AUTH_PASSWORD_KEY, dataForm.password);
                setLogin(token);
                navigate('/');
            } else {
                toast.error('Sign-in succeeded but no token was returned. Check API configuration.');
            }
        } catch (error) {
            toast.error(getApiErrorMessage(error));
        } finally {
            setIsLoading(false);
        }
    }
        
    let validationSchema = Yup.object({ email: Yup.string()
            .required('Email is required')
            .email('Invalid email format'),
        password: Yup.string()
            .required('Password is required')
            .matches(
                /^[A-Z][a-z0-9]{5,10}$/,
                'Password must start with an uppercase letter followed by 5 to 10 lowercase letters or digits'
            ),
    });

    let formik = useFormik({
        initialValues: {
            email: '',
            password: '',
        },
        validationSchema: validationSchema,
        onSubmit: handleLogin
    });

    function handleGoogleLogin() {
        if (!GOOGLE_AUTH_URL) {
            toast.error('Google login is not configured yet.');
            return;
        }
        window.location.href = GOOGLE_AUTH_URL;
    }

    return (
        <div className={styles.loginContainer}>
            <div className={styles.glowEffect}></div>
            <div className={styles.card}>
                <div className={styles.logoContainer}>
                    <i className={`fas fa-user-lock m-auto ${styles.logoIcon}`}></i>
                </div>

                <div className={styles.header}>
                    <h1 className={styles.title}>Welcome Back</h1>
                    <p className={styles.subtitle}>Sign in to continue to your account</p>
                </div>

                <form onSubmit={formik.handleSubmit}>
                    <div className={styles.formGroup}>
                        <div className={styles.inputContainer}>
                            <i className={`fas fa-envelope ${styles.icon}`}></i>
                            <input type="email" onChange={formik.handleChange} onBlur={formik.handleBlur}  className={styles.inputField} name="email" value={formik.values.email} id="email" placeholder="Email Address " required disabled={isLoading}/>
                        </div>
                        {formik.touched.email && formik.errors.email && (
                            <div className={styles.error}>
                                <i className={`fas fa-exclamation-circle ${styles.errorIcon}`}></i>
                                {formik.errors.email}
                            </div>
                        )}
                    </div>

                    <div className={styles.formGroup}>
                        <div className={styles.inputContainer}>
                            <i className={`fas fa-lock ${styles.icon}`}></i>
                            <input type={showPassword ? "text" : "password"} onChange={formik.handleChange} onBlur={formik.handleBlur} className={styles.inputField} name="password" value={formik.values.password} id="password" placeholder="Password " required disabled={isLoading}/>
                            <button type="button" onClick={() => setShowPassword(!showPassword)} className={styles.togglePassword} disabled={isLoading}>
                                <i className={`fas ${showPassword ? 'fa-eye-slash' : 'fa-eye'}`}></i>
                            </button>
                        </div>
                        {formik.touched.password && formik.errors.password && (
                            <div className={styles.error}>
                                <i className={`fas fa-exclamation-circle ${styles.errorIcon}`}></i>
                                {formik.errors.password}
                            </div>
                        )}
                    </div>

                    <div className="d-flex justify-content-between align-items-center mb-4">
                        <div className={styles.checkboxContainer}>
                            <input type="checkbox" id="remember" className={styles.checkbox} disabled={isLoading}/>
                            <label htmlFor="remember" className={styles.checkboxLabel}>
                                Remember me
                            </label>
                        </div>
                    </div>

                    <button  className={styles.submitButton}  type="submit" disabled={isLoading}>
                        {isLoading ? (
                            <>
                                <i className={`fas fa-spinner ${styles.buttonIcon} ${styles.loading}`}></i>
                                Signing In...
                            </>
                        ) : (
                            <>
                                <i className={`fas fa-sign-in-alt ${styles.buttonIcon}`}></i>
                                Sign In
                            </>
                        )}
                    </button>
                    <button type="button" className={styles.googleButton} onClick={handleGoogleLogin} disabled={isLoading}>
                        <i className={`fab fa-google ${styles.buttonIcon}`}></i>
                        Continue with Google
                    </button>
                    <div className={styles.registerLink}>
                        Don't have an account? 
                        <a href="/register">Sign up now</a>
                    </div>
                </form>
            </div>
        </div>
    );
}
import { useState, useContext } from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { useNavigate } from 'react-router-dom';
import { userContext } from '../../context/userContext';
import { AUTH_TOKEN_KEY, LOCAL_AUTH_PASSWORD_KEY } from '../../services/apiClient';
import { register } from '../../services/storeService';
import { getApiErrorMessage } from '../../api/responseUtils.js';
import toast from 'react-hot-toast';
import styles from './Register.module.css';

const USER_PROFILE_KEY = 'userProfile';

export default function RegisterPage() {
  let { setLogin } = useContext(userContext);
  let navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  async function handleRegister(dataForm) {
    setIsLoading(true);
    try {
      let response = await register(dataForm);
      const token = response?.token || response?.data?.token || response?.result?.token;
      if (token) {
        localStorage.setItem(AUTH_TOKEN_KEY, token);
        localStorage.setItem(USER_PROFILE_KEY, JSON.stringify({
          name: dataForm.name,
          email: dataForm.email
        }));
        localStorage.setItem(LOCAL_AUTH_PASSWORD_KEY, dataForm.password);
        setLogin(token);
        navigate('/login');
      } else {
        localStorage.setItem(USER_PROFILE_KEY, JSON.stringify({
          name: dataForm.name,
          email: dataForm.email
        }));
        localStorage.setItem(LOCAL_AUTH_PASSWORD_KEY, dataForm.password);
        toast.success('Account created. You can sign in now.');
        navigate('/login');
      }
    } catch (error) {
      toast.error(getApiErrorMessage(error));
    } finally {
      setIsLoading(false);
    }
  }

  let validationSchema = Yup.object({
    name: Yup.string()
      .required('Name is required')
      .min(3, 'Name must be at least 3 characters')
      .max(15, 'Name must be at most 15 characters'),
    email: Yup.string()
      .required('Email is required')
      .email('Invalid email format'),
    password: Yup.string()
      .required('Password is required')
      .matches(/^[A-Z][a-z0-9]{5,9}$/, 'Password must start with an uppercase letter followed by 5 to 9 lowercase letters or digits'),
    rePassword: Yup.string()
      .required('Confirm password is required')
      .oneOf([Yup.ref('password')], 'Passwords must match'),
    phone: Yup.string()
      .required('Phone is required')
      .matches(/^01[0125][0-9]{8}$/, 'Invalid Egyptian phone number')
  });

  let formik = useFormik({
    initialValues: {
      name: '',
      email: '',
      password: '',
      rePassword: '',
      phone: ''
    },
    validationSchema: validationSchema,
    onSubmit: handleRegister
  });

  return (
    <div className={`py-5 ${styles.registerContainer}`}>
      <div className={styles.glowEffect}></div>
      <div className={styles.card}>
        <div className={styles.logoContainer}>
          <i className={`fas fa-user-plus m-auto ${styles.logoIcon}`}></i>
        </div>

        <div className={styles.header}>
          <h1 className={styles.title}>Create Account</h1>
          <p className={styles.subtitle}>Fill the form to create your account</p>
        </div>

        <form onSubmit={formik.handleSubmit}>
          <div className={styles.formGroup}>
            <div className={styles.inputContainer}>
              <i className={`fas fa-user ${styles.icon}`}></i>
              <input type="text" onChange={formik.handleChange} onBlur={formik.handleBlur} className={styles.inputField} name="name" value={formik.values.name} id="name" placeholder="Full Name " required disabled={isLoading}/>
            </div>
            {formik.touched.name && formik.errors.name && (
              <div className={styles.error}>
                <i className={`fas fa-exclamation-circle ${styles.errorIcon}`}></i>
                {formik.errors.name}
              </div>
            )}
          </div>

          <div className={styles.formGroup}>
            <div className={styles.inputContainer}>
              <i className={`fas fa-envelope ${styles.icon}`}></i>
              <input type="email" onChange={formik.handleChange} onBlur={formik.handleBlur} className={styles.inputField} name="email" value={formik.values.email} id="email" placeholder="Email Address" required disabled={isLoading}/>
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

          <div className={styles.formGroup}>
            <div className={styles.inputContainer}>
              <i className={`fas fa-lock ${styles.icon}`}></i>
              <input type={showConfirmPassword ? "text" : "password"} onChange={formik.handleChange} onBlur={formik.handleBlur} className={styles.inputField} name="rePassword" value={formik.values.rePassword} id="rePassword" placeholder="Confirm Password " required disabled={isLoading}/>
              <button type="button" onClick={() => setShowConfirmPassword(!showConfirmPassword)} className={styles.togglePassword} disabled={isLoading}>
                <i className={`fas ${showConfirmPassword ? 'fa-eye-slash' : 'fa-eye'}`}></i>
              </button>
            </div>
            {formik.touched.rePassword && formik.errors.rePassword && (
              <div className={styles.error}>
                <i className={`fas fa-exclamation-circle ${styles.errorIcon}`}></i>
                {formik.errors.rePassword}
              </div>
            )}
          </div>

          <div className={styles.formGroup}>
            <div className={styles.inputContainer}>
              <i className={`fas fa-phone ${styles.icon}`}></i>
              <input type="tel" onChange={formik.handleChange} onBlur={formik.handleBlur} className={styles.inputField} name="phone" value={formik.values.phone} id="phone" placeholder="Phone Number " required disabled={isLoading} />
            </div>
            {formik.touched.phone && formik.errors.phone && (
              <div className={styles.error}>
                <i className={`fas fa-exclamation-circle ${styles.errorIcon}`}></i>
                {formik.errors.phone}
              </div>
            )}
          </div>
          <button className={styles.submitButton}  type="submit" disabled={isLoading} >
            {isLoading ? (
              <>
                <i className={`fas fa-spinner ${styles.buttonIcon} ${styles.loading}`}></i>
                Creating Account...
              </>
            ) : (
              <>
                <i className={`fas fa-user-plus ${styles.buttonIcon}`}></i>
                Register
              </>
            )}
          </button>

          <div className={styles.loginLink}>
            Already have an account? 
            <a href="/login">Sign in here</a>
          </div>
        </form>
      </div>
    </div>
  );
}
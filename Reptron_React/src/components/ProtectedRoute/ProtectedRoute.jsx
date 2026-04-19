import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import { userContext } from '../../context/userContext';

export default function ProtectedRoute(props) {
    const { isLogin } = useContext(userContext);

    if (isLogin) {
        return props.children;
    }
    else {
        return <Navigate to={'/login'} />
    }
}

import { createContext, useMemo, useState } from "react";
import { AUTH_TOKEN_KEY, LOCAL_AUTH_PASSWORD_KEY } from "../services/apiClient";
import { clearAllCache } from "../api/simpleCache.js";

export let userContext = createContext();

export default function UserContextProvider(props) {
    const [isLogin, setLogin] = useState(() => localStorage.getItem(AUTH_TOKEN_KEY) || null);

    const logout = () => {
        localStorage.removeItem(AUTH_TOKEN_KEY);
        localStorage.removeItem(LOCAL_AUTH_PASSWORD_KEY);
        clearAllCache();
        setLogin(null);
    };

    const value = useMemo(() => ({ isLogin, setLogin, logout }), [isLogin]);

    return <userContext.Provider value={value}>
            {props.children}
        </userContext.Provider>
    
}
import { createContext, useState } from "react";

export const purchaseContext = createContext();

export default function PurchaseContextProvider({ children }) {
    const [purchases, setPurchases] = useState([]);

    const addPurchase = (order) => {
        setPurchases(prev => [...prev, order]);
    };

    return (
        <purchaseContext.Provider value={{ purchases, addPurchase }}>
        {children}
        </purchaseContext.Provider>
    );
}

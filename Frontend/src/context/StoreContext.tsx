import { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';
import type { Store } from '../features/stores/types';

interface StoreContextType {
    currentStore: Store | null;
    setStore: (store: Store) => void;
}

const StoreContext = createContext<StoreContextType | undefined>(undefined);

export const StoreProvider = ({ children }: { children: ReactNode }) => {
    const [currentStore, setCurrentStore] = useState<Store | null>(() => {
        try {
            const stored = localStorage.getItem('delivery_app_store');
            return stored ? JSON.parse(stored) : null;
        } catch {
            return null;
        }
    });

    const handleSetStore = (store: Store) => {
        setCurrentStore(store);
        localStorage.setItem('delivery_app_store', JSON.stringify(store));
    };

    return (
        <StoreContext.Provider value={{ currentStore, setStore: handleSetStore }}>
            {children}
        </StoreContext.Provider>
    );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useStore = () => {
    const context = useContext(StoreContext);
    if (context === undefined) {
        throw new Error('useStore must be used within a StoreProvider');
    }
    return context;
};

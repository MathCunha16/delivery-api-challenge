import { useEffect, useState } from 'react';
import type { Store } from '../types';
import { getStores } from '../api/storeService';
import { useStore } from '../../../context/StoreContext';
import { useNavigate } from 'react-router-dom';
import clsx from 'clsx';
import { Store as StoreIcon, ChevronRight } from 'lucide-react';

export const StoreSelection = () => {
    const [stores, setStores] = useState<Store[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const { setStore } = useStore();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchStores = async () => {
            try {
                setLoading(true);
                const data = await getStores(0, 100);
                setStores(data.content);
            } catch (err) {
                setError('Falha ao carregar lojas. Verifique sua conexão.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchStores();
    }, []);

    const handleSelectStore = (store: Store) => {
        setStore(store);
        navigate('/dashboard');
    };

    if (loading) {
        return (
            <div className="flex flex-col items-center gap-4 animate-pulse">
                <div className="w-12 h-12 bg-zinc-800 rounded-full" />
                <div className="h-4 w-32 bg-zinc-800 rounded" />
            </div>
        );
    }

    if (error) {
        return (
            <div className="p-4 rounded-lg bg-red-500/10 border border-red-500/20 text-red-400">
                {error}
            </div>
        );
    }

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-3xl">
            {stores.map((store) => (
                <button
                    key={store.id}
                    onClick={() => handleSelectStore(store)}
                    className={clsx(
                        "group relative flex flex-col items-start p-8 h-full min-h-[220px]",
                        "bg-zinc-900 border border-zinc-800/60 rounded-3xl",
                        "hover:bg-zinc-800/80 hover:border-emerald-500/30 hover:shadow-2xl hover:shadow-emerald-500/10",
                        "active:scale-[0.98]",
                        "transition-all duration-300 ease-out text-left overflow-hidden"
                    )}
                >
                    {/* Default decorative gradient on hover */}
                    <div className="absolute inset-0 bg-gradient-to-br from-emerald-500/0 via-transparent to-transparent group-hover:from-emerald-500/5 transition-colors duration-500" />

                    {/* Icon Badge */}
                    <div className="relative mb-6 p-4 rounded-2xl bg-zinc-950 border border-zinc-800 group-hover:border-emerald-500/30 group-hover:bg-emerald-500/10 transition-colors duration-300">
                        <StoreIcon className="w-8 h-8 text-zinc-400 group-hover:text-emerald-400 transition-colors" />
                    </div>

                    {/* Content */}
                    <div className="relative w-full flex-1 flex flex-col">
                        <h3 className="text-2xl font-bold text-zinc-100 mb-2 group-hover:text-white tracking-tight">
                            {store.name}
                        </h3>

                        {/* Footer with simulated action */}
                        <div className="mt-auto pt-4 flex items-center justify-end w-full border-t border-zinc-800/50 group-hover:border-zinc-700/50 transition-colors">
                            <div className="flex items-center gap-1 text-emerald-500 opacity-0 -translate-x-2 group-hover:opacity-100 group-hover:translate-x-0 transition-all duration-300 font-medium text-sm">
                                Acessar Painel <ChevronRight className="w-4 h-4" />
                            </div>
                        </div>
                    </div>
                </button>
            ))}
        </div>
    );
};

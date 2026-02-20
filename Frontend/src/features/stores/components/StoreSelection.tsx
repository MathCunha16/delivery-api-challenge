import { useEffect, useState } from 'react';
import type { Store } from '../types';
import { getStores } from '../api/storeService';
import { useStore } from '../../../context/StoreContext';
import { useNavigate } from 'react-router-dom';
import clsx from 'clsx';
import { Store as StoreIcon, ChevronRight } from 'lucide-react';

interface StoreSelectionProps {
    mode?: 'PARTNER' | 'SIMULATOR';
    excludeStoreId?: string;
    includeOnlyStoreId?: string;
    customStyle?: 'PERFORMANCE';
}

export const StoreSelection = ({ mode = 'PARTNER', excludeStoreId, includeOnlyStoreId, customStyle }: StoreSelectionProps) => {
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

                let fetchedStores = data.content;

                if (excludeStoreId) {
                    fetchedStores = fetchedStores.filter(s => s.id !== excludeStoreId);
                }

                if (includeOnlyStoreId) {
                    fetchedStores = fetchedStores.filter(s => s.id === includeOnlyStoreId);
                }

                setStores(fetchedStores);
            } catch (err) {
                setError('Falha ao carregar lojas. Verifique sua conexão.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchStores();
    }, [excludeStoreId, includeOnlyStoreId]);

    const handleSelectStore = (store: Store) => {
        setStore(store);
        if (mode === 'PARTNER' || customStyle === 'PERFORMANCE') {
            navigate('/dashboard');
        } else {
            navigate(`/store/${store.id}/new-order`);
        }
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

    if (stores.length === 0) {
        return (
            <div className="p-4 rounded-lg bg-zinc-900 border border-zinc-800 text-zinc-400 text-center">
                Nenhuma loja encontrada.
            </div>
        );
    }

    const isPartner = mode === 'PARTNER';
    const isPerformance = customStyle === 'PERFORMANCE';

    return (
        <div className={clsx(
            "grid gap-6 w-full max-w-3xl",
            stores.length === 1 ? "grid-cols-1" : "grid-cols-1 md:grid-cols-2" // Make single item take full width
        )}>
            {stores.map((store) => (
                <button
                    key={store.id}
                    onClick={() => handleSelectStore(store)}
                    className={clsx(
                        "group relative flex flex-col items-start p-8 h-full min-h-[220px]",
                        "bg-zinc-900 border border-zinc-800/60 rounded-3xl",
                        "hover:bg-zinc-800/80 hover:shadow-2xl",
                        isPartner
                            ? "hover:border-emerald-500/30 hover:shadow-emerald-500/10"
                            : isPerformance
                                ? "hover:border-purple-500/30 hover:shadow-purple-500/10"
                                : "hover:border-orange-500/30 hover:shadow-orange-500/10",
                        "active:scale-[0.98]",
                        "transition-all duration-300 ease-out text-left overflow-hidden"
                    )}
                >
                    {/* Default decorative gradient on hover */}
                    <div className={clsx(
                        "absolute inset-0 bg-gradient-to-br transition-colors duration-500",
                        isPartner
                            ? "from-emerald-500/0 group-hover:from-emerald-500/5 via-transparent to-transparent"
                            : isPerformance
                                ? "from-purple-500/0 group-hover:from-purple-500/5 via-transparent to-transparent"
                                : "from-orange-500/0 group-hover:from-orange-500/5 via-transparent to-transparent"
                    )} />

                    {/* Icon Badge */}
                    <div className={clsx(
                        "relative mb-6 p-4 rounded-2xl bg-zinc-950 border border-zinc-800 transition-colors duration-300",
                        isPartner
                            ? "group-hover:border-emerald-500/30 group-hover:bg-emerald-500/10"
                            : isPerformance
                                ? "group-hover:border-purple-500/30 group-hover:bg-purple-500/10"
                                : "group-hover:border-orange-500/30 group-hover:bg-orange-500/10"
                    )}>
                        <StoreIcon className={clsx(
                            "w-8 h-8 text-zinc-400 transition-colors",
                            isPartner
                                ? "group-hover:text-emerald-400"
                                : isPerformance
                                    ? "group-hover:text-purple-400"
                                    : "group-hover:text-orange-400"
                        )} />
                    </div>

                    {/* Content */}
                    <div className="relative w-full flex-1 flex flex-col">
                        <h3 className="text-2xl font-bold text-zinc-100 mb-2 group-hover:text-white tracking-tight">
                            {store.name}
                        </h3>

                        {/* Footer with simulated action */}
                        <div className="mt-auto pt-4 flex items-center justify-end w-full border-t border-zinc-800/50 group-hover:border-zinc-700/50 transition-colors">
                            <div className={clsx(
                                "flex items-center gap-1 opacity-0 -translate-x-2 group-hover:opacity-100 group-hover:translate-x-0 transition-all duration-300 font-medium text-sm",
                                isPartner
                                    ? "text-emerald-500"
                                    : isPerformance
                                        ? "text-purple-500"
                                        : "text-orange-500"
                            )}>
                                {isPartner ? 'Acessar Painel' : isPerformance ? 'Conferir Resultados Teste' : 'Novo Pedido'} <ChevronRight className="w-4 h-4" />
                            </div>
                        </div>
                    </div>
                </button>
            ))}
        </div>
    );
};

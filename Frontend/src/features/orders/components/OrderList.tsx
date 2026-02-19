import { useEffect, useState } from 'react';
import { useStore } from '../../../context/StoreContext';
import { getOrdersByStore } from '../api/orderService';
import type { Order } from '../types';
import { StatusBadge } from './StatusBadge';
import { RefreshCw, Search, ChevronLeft, ChevronRight } from 'lucide-react';
import { Link } from 'react-router-dom';
import clsx from 'clsx';

export const OrderList = () => {
    const { currentStore } = useStore();
    const [orders, setOrders] = useState<Order[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Pagination State
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const pageSize = 10;

    const fetchOrders = async (pageNumber = 0) => {
        if (!currentStore) return;

        try {
            setLoading(true);
            setError(null);
            const data = await getOrdersByStore(currentStore.id, pageNumber, pageSize);
            setOrders(data.content.map(w => w.order));

            // Update pagination info
            setTotalPages(data.page.totalPages);
            setTotalElements(data.page.totalElements);
            setPage(data.page.number);
        } catch (err) {
            console.error(err);
            setError('Falha ao carregar pedidos.');
        } finally {
            setLoading(false);
        }
    };

    // Reset page when store changes
    useEffect(() => {
        setPage(0);
        fetchOrders(0);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentStore]);

    const handleNextPage = () => {
        if (page < totalPages - 1) {
            fetchOrders(page + 1);
        }
    };

    const handlePrevPage = () => {
        if (page > 0) {
            fetchOrders(page - 1);
        }
    };

    if (!currentStore) {
        return <div className="text-zinc-400">Nenhuma loja selecionada.</div>;
    }

    return (
        <div className="w-full space-y-4">
            {/* Header Actions */}
            <div className="flex items-center justify-between">
                <h2 className="text-xl font-semibold text-zinc-100">Últimos Pedidos</h2>
                <div className="flex items-center gap-4">
                    <span className="text-sm text-zinc-500">
                        Total: <span className="text-zinc-300 font-medium">{totalElements}</span>
                    </span>
                    <button
                        onClick={() => fetchOrders(page)}
                        disabled={loading}
                        className="p-2 rounded-lg hover:bg-zinc-800 text-zinc-400 hover:text-white transition-colors disabled:opacity-50"
                        title="Atualizar lista"
                    >
                        <RefreshCw className={clsx("w-5 h-5", loading && "animate-spin")} />
                    </button>
                </div>
            </div>

            {/* Loading State */}
            {loading && orders.length === 0 && (
                <div className="space-y-3">
                    {[...Array(5)].map((_, i) => (
                        <div key={i} className="h-20 w-full bg-zinc-900/50 rounded-xl animate-pulse delay-75" />
                    ))}
                </div>
            )}

            {/* Error State */}
            {error && (
                <div className="p-4 bg-red-500/10 border border-red-500/20 text-red-400 rounded-xl text-sm">
                    {error}
                </div>
            )}

            {/* Empty State */}
            {!loading && !error && orders.length === 0 && (
                <div className="flex flex-col items-center justify-center py-16 text-zinc-500 space-y-4 border border-zinc-800/50 rounded-xl bg-zinc-900/20">
                    <div className="p-4 bg-zinc-900 rounded-full">
                        <Search className="w-8 h-8 opacity-40" />
                    </div>
                    <p>Nenhum pedido encontrado para esta loja.</p>
                    <button
                        onClick={() => fetchOrders(0)}
                        className="text-emerald-500 hover:underline text-sm"
                    >
                        Tentar novamente
                    </button>
                </div>
            )}

            {/* Orders Table Container */}
            {!loading && !error && orders.length > 0 && (
                <div className="bg-zinc-900/50 border border-zinc-800/50 rounded-xl overflow-hidden shadow-lg backdrop-blur-sm">
                    <div className="overflow-x-auto">
                        <table className="w-full text-sm text-left">
                            <thead className="bg-zinc-950/80 text-zinc-500 uppercase text-xs font-semibold tracking-wider border-b border-zinc-800/80">
                                <tr>
                                    <th className="px-6 py-4">ID</th>
                                    <th className="px-6 py-4">Cliente</th>
                                    <th className="px-6 py-4">Status</th>
                                    <th className="px-6 py-4 text-right">Total</th>
                                    <th className="px-6 py-4 text-right">Data</th>
                                    <th className="px-6 py-4"></th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-zinc-800/50 text-zinc-300">
                                {orders.map((order) => (
                                    <tr
                                        key={order.order_id}
                                        className="group hover:bg-zinc-800/30 transition-all duration-200 cursor-pointer"
                                    >
                                        <td className="px-6 py-4 font-mono text-xs text-zinc-500 group-hover:text-zinc-400">
                                            #{order.order_id.slice(0, 8)}
                                        </td>
                                        <td className="px-6 py-4 font-medium text-zinc-200 group-hover:text-white">
                                            {order.customer.name}
                                        </td>
                                        <td className="px-6 py-4">
                                            <StatusBadge status={order.last_status_name} />
                                        </td>
                                        <td className="px-6 py-4 text-right font-medium text-zinc-300 group-hover:text-zinc-100">
                                            {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(order.total_price)}
                                        </td>
                                        <td className="px-6 py-4 text-right text-zinc-500 text-xs">
                                            <div className="flex flex-col items-end gap-0.5">
                                                <span>{new Date(order.created_at).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' })}</span>
                                                <span className="opacity-60">{new Date(order.created_at).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })}</span>
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 text-right">
                                            <Link
                                                to={`/order/${order.order_id}`}
                                                className="text-emerald-500 hover:text-emerald-400 font-medium text-xs opacity-0 group-hover:opacity-100 transition-all transform translate-x-2 group-hover:translate-x-0"
                                            >
                                                Detalhes
                                            </Link>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    {/* Pagination Footer */}
                    <div className="border-t border-zinc-800/80 bg-zinc-950/30 p-4 flex items-center justify-between">
                        <span className="text-xs text-zinc-500">
                            Página <span className="font-medium text-zinc-300">{page + 1}</span> de <span className="font-medium text-zinc-300">{totalPages}</span>
                        </span>

                        <div className="flex gap-2">
                            <button
                                onClick={handlePrevPage}
                                disabled={page === 0 || loading}
                                className="p-2 rounded-lg border border-zinc-800 hover:bg-zinc-800 text-zinc-400 hover:text-white disabled:opacity-30 disabled:hover:bg-transparent transition-colors"
                            >
                                <ChevronLeft className="w-4 h-4" />
                            </button>
                            <button
                                onClick={handleNextPage}
                                disabled={page >= totalPages - 1 || loading}
                                className="p-2 rounded-lg border border-zinc-800 hover:bg-zinc-800 text-zinc-400 hover:text-white disabled:opacity-30 disabled:hover:bg-transparent transition-colors"
                            >
                                <ChevronRight className="w-4 h-4" />
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

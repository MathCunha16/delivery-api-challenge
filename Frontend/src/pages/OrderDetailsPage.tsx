import { useEffect, useState, useCallback } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { getOrderById, deleteOrder } from '../features/orders/api/orderService';
import type { Order } from '../features/orders/types';
import { StatusBadge } from '../features/orders/components/StatusBadge';
import { ArrowLeft, Loader2, AlertCircle, Trash2 } from 'lucide-react';
import { OrderItemsTable } from '../features/orders/components/OrderDetails/OrderItemsTable';
import { OrderCustomerCard } from '../features/orders/components/OrderDetails/OrderCustomerCard';
import { OrderTimeline } from '../features/orders/components/OrderDetails/OrderTimeline';
import { OrderActions } from '../features/orders/components/OrderDetails/OrderActions';
import { OrderPaymentCard } from '../features/orders/components/OrderDetails/OrderPaymentCard';

const OrderDetailsPage = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const [order, setOrder] = useState<Order | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchOrder = useCallback(async () => {
        if (!id) return;
        try {
            setLoading(true);
            const data = await getOrderById(id);
            setOrder(data);
            setError(null);
        } catch (err) {
            console.error(err);
            setError('Não foi possível carregar os detalhes do pedido.');
        } finally {
            setLoading(false);
        }
    }, [id]);

    useEffect(() => {
        fetchOrder();
    }, [fetchOrder]);

    const handleDelete = async () => {
        if (!order || !window.confirm('Tem certeza que deseja excluir este pedido? Esta ação não pode ser desfeita.')) return;

        try {
            await deleteOrder(order.order_id);
            alert('Pedido excluído com sucesso!');
            navigate('/dashboard');
        } catch (err) {
            console.error(err);
            alert('Erro ao excluir pedido.');
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-zinc-950 flex items-center justify-center text-zinc-400">
                <Loader2 className="w-8 h-8 animate-spin" />
            </div>
        );
    }

    if (error || !order) {
        return (
            <div className="min-h-screen bg-zinc-950 flex flex-col items-center justify-center text-zinc-400 space-y-4">
                <AlertCircle className="w-12 h-12 text-red-500" />
                <p className="text-lg">{error || 'Pedido não encontrado.'}</p>
                <Link to="/dashboard" className="text-emerald-500 hover:text-emerald-400">
                    Voltar ao Dashboard
                </Link>
            </div>
        );
    }

    // Is Deletable Logic
    const isDeletable = ['CANCELED', 'DELIVERED'].includes(order.last_status_name);

    return (
        <div className="min-h-screen bg-zinc-950 text-zinc-100 p-4 md:p-8">
            <div className="max-w-6xl mx-auto space-y-6">

                {/* Header */}
                <header className="flex items-center gap-4 border-b border-zinc-800 pb-6">
                    <Link
                        to="/dashboard"
                        className="p-2 -ml-2 rounded-lg hover:bg-zinc-800 text-zinc-400 hover:text-white transition-colors"
                    >
                        <ArrowLeft className="w-6 h-6" />
                    </Link>
                    <div className="flex-1">
                        <div className="flex items-center gap-3">
                            <h1 className="text-2xl font-bold tracking-tight">Pedido #{order.order_id.slice(0, 8)}</h1>
                            <StatusBadge status={order.last_status_name} />
                        </div>
                        <p className="text-zinc-500 text-sm mt-1">
                            {new Date(order.created_at).toLocaleString('pt-BR', { dateStyle: 'full', timeStyle: 'short' })}
                        </p>
                    </div>

                    {/* Actions Area */}
                    <div className="flex items-center gap-2">
                        {isDeletable && (
                            <button
                                onClick={handleDelete}
                                className="p-2 bg-red-500/10 hover:bg-red-500/20 text-red-500 rounded-lg transition-colors flex items-center gap-2"
                                title="Excluir Pedido"
                            >
                                <Trash2 className="w-5 h-5" />
                                <span className="hidden sm:inline font-medium text-sm">Excluir</span>
                            </button>
                        )}
                    </div>
                </header>

                {/* Grid Layout */}
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

                    {/* Main Content (Items) - 2 Cols */}
                    <div className="lg:col-span-2 space-y-6">
                        <OrderItemsTable order={order} />
                    </div>

                    {/* Sidebar (Actions & Info) - 1 Col */}
                    <div className="space-y-6">
                        <OrderActions order={order} onUpdate={fetchOrder} />
                        <OrderCustomerCard order={order} onUpdate={fetchOrder} />
                        <OrderPaymentCard order={order} />
                        <OrderTimeline order={order} />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderDetailsPage;

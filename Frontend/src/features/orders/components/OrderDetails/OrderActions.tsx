import { useState } from 'react';
import type { Order, OrderStatus } from '../../types';
import { updateOrderStatus } from '../../api/orderService';
import { Play, Truck, CheckCheck, XCircle, Loader2 } from 'lucide-react';

interface OrderActionsProps {
    order: Order;
    onUpdate: () => void;
}

export const OrderActions = ({ order, onUpdate }: OrderActionsProps) => {
    const [loading, setLoading] = useState<string | null>(null);

    const handleUpdate = async (newStatus: OrderStatus) => {
        try {
            setLoading(newStatus);
            await updateOrderStatus(order.order_id, newStatus);
            onUpdate();
        } catch (error) {
            console.error('Failed to update status', error);
            alert('Erro ao atualizar status do pedido.');
        } finally {
            setLoading(null);
        }
    };

    const isProcessing = !!loading;

    // Define Actions based on State Machine
    // Received -> Confirmed -> Dispatched -> Delivered
    // Cancel available for all except final states

    const isFinalState = order.last_status_name === 'DELIVERED' || order.last_status_name === 'CANCELED';

    const renderMainAction = () => {
        switch (order.last_status_name) {
            case 'RECEIVED':
                return (
                    <button
                        onClick={() => handleUpdate('CONFIRMED')}
                        disabled={isProcessing}
                        className="w-full py-3 px-4 bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl font-medium transition-colors flex items-center justify-center gap-2 shadow-lg hover:shadow-emerald-500/20"
                    >
                        {loading === 'CONFIRMED' ? <Loader2 className="animate-spin w-5 h-5" /> : <Play className="w-5 h-5 fill-current" />}
                        Confirmar Pedido
                    </button>
                );
            case 'CONFIRMED':
                return (
                    <button
                        onClick={() => handleUpdate('DISPATCHED')}
                        disabled={isProcessing}
                        className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-500 text-white rounded-xl font-medium transition-colors flex items-center justify-center gap-2 shadow-lg hover:shadow-blue-500/20"
                    >
                        {loading === 'DISPATCHED' ? <Loader2 className="animate-spin w-5 h-5" /> : <Truck className="w-5 h-5" />}
                        Despachar Pedido
                    </button>
                );
            case 'DISPATCHED':
                return (
                    <button
                        onClick={() => handleUpdate('DELIVERED')}
                        disabled={isProcessing}
                        className="w-full py-3 px-4 bg-zinc-100 hover:bg-white text-zinc-900 rounded-xl font-bold transition-colors flex items-center justify-center gap-2 shadow-lg"
                    >
                        {loading === 'DELIVERED' ? <Loader2 className="animate-spin w-5 h-5" /> : <CheckCheck className="w-5 h-5" />}
                        Finalizar Entrega
                    </button>
                );
            default:
                return null;
        }
    };

    if (isFinalState && order.last_status_name !== 'CANCELED') {
        return (
            <div className="bg-emerald-500/10 border border-emerald-500/20 rounded-xl p-4 text-center">
                <p className="text-emerald-400 font-medium flex items-center justify-center gap-2">
                    <CheckCheck className="w-5 h-5" />
                    Pedido Concluído
                </p>
            </div>
        );
    }

    if (order.last_status_name === 'CANCELED') {
        return (
            <div className="bg-red-500/10 border border-red-500/20 rounded-xl p-4 text-center">
                <p className="text-red-400 font-medium flex items-center justify-center gap-2">
                    <XCircle className="w-5 h-5" />
                    Pedido Cancelado
                </p>
            </div>
        );
    }

    return (
        <div className="bg-zinc-900/50 border border-zinc-800/50 rounded-xl p-4 shadow-sm backdrop-blur-sm space-y-3">
            <h3 className="font-semibold text-zinc-100 mb-2">Ações Operacionais</h3>

            {renderMainAction()}

            <button
                onClick={() => handleUpdate('CANCELED')}
                disabled={isProcessing}
                className="w-full py-3 px-4 bg-red-500/10 hover:bg-red-500/20 border border-red-500/20 text-red-400 hover:text-red-300 rounded-xl font-medium transition-colors flex items-center justify-center gap-2"
            >
                {loading === 'CANCELED' ? <Loader2 className="animate-spin w-5 h-5" /> : <XCircle className="w-5 h-5" />}
                Cancelar Pedido
            </button>
        </div>
    );
};

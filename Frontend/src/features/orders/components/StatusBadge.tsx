import clsx from 'clsx';
import {
    CircleDashed,
    ChefHat,
    Truck,
    CheckCircle,
    XCircle
} from 'lucide-react';
import type { OrderStatus } from '../types';

interface StatusBadgeProps {
    status: OrderStatus;
}

const statusConfig: Record<OrderStatus, { color: string; icon: React.ElementType; label: string }> = {
    RECEIVED: {
        color: 'bg-blue-500/10 text-blue-400 border-blue-500/20',
        icon: CircleDashed,
        label: 'Recebido',
    },
    CONFIRMED: {
        color: 'bg-yellow-500/10 text-yellow-400 border-yellow-500/20',
        icon: ChefHat,
        label: 'Em Preparo',
    },
    DISPATCHED: {
        color: 'bg-purple-500/10 text-purple-400 border-purple-500/20',
        icon: Truck,
        label: 'Despachado',
    },
    DELIVERED: {
        color: 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20',
        icon: CheckCircle,
        label: 'Entregue',
    },
    CANCELED: {
        color: 'bg-red-500/10 text-red-400 border-red-500/20',
        icon: XCircle,
        label: 'Cancelado',
    },
};

export const StatusBadge = ({ status }: StatusBadgeProps) => {
    const config = statusConfig[status] || statusConfig.RECEIVED;
    const Icon = config.icon;

    return (
        <div className={clsx(
            "inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full border text-xs font-medium",
            config.color
        )}>
            <Icon className="w-3.5 h-3.5" />
            <span>{config.label}</span>
        </div>
    );
};

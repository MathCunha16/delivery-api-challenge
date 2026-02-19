import { useState } from 'react';
import { User, MapPin, Phone, Edit2 } from 'lucide-react';
import type { Order } from '../../types';
import { EditOrderModal } from './EditOrderModal';

interface OrderCustomerCardProps {
    order: Order;
    onUpdate?: () => void;
}

export const OrderCustomerCard = ({ order, onUpdate }: OrderCustomerCardProps) => {
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);

    const isEditable = ['RECEIVED', 'CONFIRMED'].includes(order.last_status_name);

    return (
        <div className="space-y-4">
            {/* Customer Card */}
            <div className="bg-zinc-900/50 border border-zinc-800/50 rounded-xl p-4 shadow-sm backdrop-blur-sm relative group">
                <div className="flex items-center justify-between mb-4 border-b border-zinc-800/50 pb-3">
                    <div className="flex items-center gap-3">
                        <div className="p-2 bg-blue-500/10 rounded-lg text-blue-400">
                            <User className="w-5 h-5" />
                        </div>
                        <h3 className="font-semibold text-zinc-100">Cliente</h3>
                    </div>
                    {isEditable && (
                        <button
                            onClick={() => setIsEditModalOpen(true)}
                            className="p-2 hover:bg-zinc-800 rounded-lg text-zinc-500 hover:text-emerald-400 transition-colors"
                            title="Editar dados"
                        >
                            <Edit2 className="w-4 h-4" />
                        </button>
                    )}
                </div>
                <div className="space-y-3">
                    <div>
                        <p className="text-xs text-zinc-500 uppercase tracking-wider mb-1">Nome</p>
                        <p className="font-medium text-zinc-200">{order.customer.name}</p>
                    </div>
                    {order.customer.temporary_phone && (
                        <div>
                            <p className="text-xs text-zinc-500 uppercase tracking-wider mb-1">Telefone</p>
                            <a
                                href={`tel:${order.customer.temporary_phone}`}
                                className="font-medium text-blue-400 hover:text-blue-300 transition-colors flex items-center gap-2"
                            >
                                <Phone className="w-3.5 h-3.5" />
                                {order.customer.temporary_phone}
                            </a>
                        </div>
                    )}
                </div>
            </div>

            {/* Address Card */}
            <div className="bg-zinc-900/50 border border-zinc-800/50 rounded-xl p-4 shadow-sm backdrop-blur-sm">
                <div className="flex items-center gap-3 mb-4 border-b border-zinc-800/50 pb-3">
                    <div className="p-2 bg-emerald-500/10 rounded-lg text-emerald-400">
                        <MapPin className="w-5 h-5" />
                    </div>
                    <h3 className="font-semibold text-zinc-100">Endereço de Entrega</h3>
                </div>
                <div className="space-y-1 text-sm text-zinc-300">
                    <p className="font-medium text-zinc-100">
                        {order.delivery_address.street_name}, {order.delivery_address.street_number}
                    </p>
                    {order.delivery_address.complement && (
                        <p className="text-zinc-400">Comp: {order.delivery_address.complement}</p>
                    )}
                    <p>{order.delivery_address.neighborhood}</p>
                    <p>{order.delivery_address.city} - {order.delivery_address.state}</p>
                    <p className="text-zinc-500 text-xs mt-2 pt-2 border-t border-zinc-800/50">
                        CEP: {order.delivery_address.zip_code ?? order.delivery_address.postal_code}
                    </p>
                    {order.delivery_address.reference && (
                        <div className="mt-3 bg-zinc-800/30 p-2 rounded-lg border border-zinc-800/50">
                            <p className="text-xs text-zinc-500 uppercase tracking-wider mb-1">Referência</p>
                            <p className="text-zinc-300 italic">"{order.delivery_address.reference}"</p>
                        </div>
                    )}
                </div>
            </div>

            <EditOrderModal
                order={order}
                isOpen={isEditModalOpen}
                onClose={() => setIsEditModalOpen(false)}
                onUpdate={onUpdate || (() => { })}
            />
        </div>
    );
};

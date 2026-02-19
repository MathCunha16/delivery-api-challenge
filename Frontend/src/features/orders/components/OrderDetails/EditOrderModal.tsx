import { useState, useEffect } from 'react';
import { X, Save, Key } from 'lucide-react';
import type { Order } from '../../types';
import { updateOrder } from '../../api/orderService';

interface EditOrderModalProps {
    order: Order;
    isOpen: boolean;
    onClose: () => void;
    onUpdate: () => void;
}

export const EditOrderModal = ({ order, isOpen, onClose, onUpdate }: EditOrderModalProps) => {
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState({
        customer_name: order.customer.name,
        temporary_phone: order.customer.temporary_phone || '',
        street: order.delivery_address.street_name,
        number: order.delivery_address.street_number,
        neighborhood: order.delivery_address.neighborhood,
        city: order.delivery_address.city,
        state: order.delivery_address.state,
        zip_code: order.delivery_address.zip_code || order.delivery_address.postal_code || '',
        reference: order.delivery_address.reference || ''
    });

    // Reset form when order changes
    useEffect(() => {
        if (isOpen) {
            setFormData({
                customer_name: order.customer.name,
                temporary_phone: order.customer.temporary_phone || '',
                street: order.delivery_address.street_name,
                number: order.delivery_address.street_number,
                neighborhood: order.delivery_address.neighborhood,
                city: order.delivery_address.city,
                state: order.delivery_address.state,
                zip_code: order.delivery_address.zip_code || order.delivery_address.postal_code || '',
                reference: order.delivery_address.reference || ''
            });
        }
    }, [order, isOpen]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            await updateOrder(order.order_id, {
                customer_name: formData.customer_name,
                temporary_phone: formData.temporary_phone,
                delivery_address: {
                    street_name: formData.street,
                    street_number: formData.number,
                    neighborhood: formData.neighborhood,
                    city: formData.city,
                    state: formData.state,
                    zip_code: formData.zip_code,
                    reference: formData.reference,
                    country: 'BR',
                    coordinates: order.delivery_address.coordinates // Keep existing coordinates
                }
            });
            onUpdate();
            onClose();
            alert('Pedido atualizado com sucesso!'); // Simple feedback for now
        } catch (error) {
            console.error(error);
            alert('Erro ao atualizar pedido. Verifique os dados e tente novamente.');
        } finally {
            setLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-in fade-in duration-200">
            <div className="bg-zinc-900 w-full max-w-2xl rounded-2xl border border-zinc-800 shadow-2xl overflow-hidden flex flex-col max-h-[90vh]">

                {/* Header */}
                <div className="p-6 border-b border-zinc-800 flex justify-between items-center bg-zinc-900/50">
                    <h3 className="text-xl font-bold">Editar Dados do Pedido</h3>
                    <button onClick={onClose} className="p-2 hover:bg-zinc-800 rounded-lg text-zinc-400 hover:text-white transition-colors">
                        <X className="w-5 h-5" />
                    </button>
                </div>

                {/* Form */}
                <form onSubmit={handleSubmit} className="flex-1 overflow-y-auto p-6 space-y-6">

                    {/* Customer Section */}
                    <div className="space-y-4">
                        <h4 className="text-sm font-bold text-zinc-500 uppercase tracking-wider">Cliente</h4>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Nome do Cliente</label>
                                <input
                                    type="text"
                                    required
                                    value={formData.customer_name}
                                    onChange={e => setFormData({ ...formData, customer_name: e.target.value })}
                                    className="w-full bg-zinc-950 border border-zinc-800 rounded-lg p-2.5 text-white focus:ring-2 focus:ring-emerald-500 outline-none transition-all"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Telefone</label>
                                <input
                                    type="text"
                                    required
                                    value={formData.temporary_phone}
                                    onChange={e => setFormData({ ...formData, temporary_phone: e.target.value })}
                                    className="w-full bg-zinc-950 border border-zinc-800 rounded-lg p-2.5 text-white focus:ring-2 focus:ring-emerald-500 outline-none transition-all"
                                />
                            </div>
                        </div>
                    </div>

                    <div className="h-px bg-zinc-800" />

                    {/* Address Section */}
                    <div className="space-y-4">
                        <h4 className="text-sm font-bold text-zinc-500 uppercase tracking-wider">Endereço de Entrega</h4>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">CEP</label>
                                <input
                                    type="text"
                                    required
                                    value={formData.zip_code}
                                    onChange={e => setFormData({ ...formData, zip_code: e.target.value })}
                                    className="w-full bg-zinc-950 border border-zinc-800 rounded-lg p-2.5 text-white focus:ring-2 focus:ring-emerald-500 outline-none transition-all"
                                />
                            </div>
                            <div className="md:col-span-2">
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Logradouro</label>
                                <input
                                    type="text"
                                    required
                                    value={formData.street}
                                    onChange={e => setFormData({ ...formData, street: e.target.value })}
                                    className="w-full bg-zinc-950 border border-zinc-800 rounded-lg p-2.5 text-white focus:ring-2 focus:ring-emerald-500 outline-none transition-all"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Número</label>
                                <input
                                    type="text"
                                    required
                                    value={formData.number}
                                    onChange={e => setFormData({ ...formData, number: e.target.value })}
                                    className="w-full bg-zinc-950 border border-zinc-800 rounded-lg p-2.5 text-white focus:ring-2 focus:ring-emerald-500 outline-none transition-all"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Bairro</label>
                                <input
                                    type="text"
                                    required
                                    value={formData.neighborhood}
                                    onChange={e => setFormData({ ...formData, neighborhood: e.target.value })}
                                    className="w-full bg-zinc-950 border border-zinc-800 rounded-lg p-2.5 text-white focus:ring-2 focus:ring-emerald-500 outline-none transition-all"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Cidade</label>
                                <input
                                    type="text"
                                    required
                                    value={formData.city}
                                    onChange={e => setFormData({ ...formData, city: e.target.value })}
                                    className="w-full bg-zinc-950 border border-zinc-800 rounded-lg p-2.5 text-white focus:ring-2 focus:ring-emerald-500 outline-none transition-all"
                                />
                            </div>
                            <div className="md:col-span-2">
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Referência</label>
                                <input
                                    type="text"
                                    value={formData.reference}
                                    onChange={e => setFormData({ ...formData, reference: e.target.value })}
                                    className="w-full bg-zinc-950 border border-zinc-800 rounded-lg p-2.5 text-white focus:ring-2 focus:ring-emerald-500 outline-none transition-all"
                                />
                            </div>
                        </div>
                    </div>
                </form>

                {/* Footer */}
                <div className="p-6 border-t border-zinc-800 bg-zinc-900/50 flex justify-end gap-3">
                    <button
                        type="button"
                        onClick={onClose}
                        className="px-4 py-2 rounded-lg border border-zinc-700 hover:bg-zinc-800 text-zinc-300 transition-colors font-medium"
                    >
                        Cancelar
                    </button>
                    <button
                        onClick={handleSubmit} // Submit via JS to handle external button placement
                        disabled={loading}
                        className="px-4 py-2 rounded-lg bg-emerald-500 hover:bg-emerald-600 text-black font-bold transition-colors flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        {loading ? 'Salvando...' : 'Salvar Alterações'}
                        <Save className="w-4 h-4" />
                    </button>
                </div>
            </div>
        </div>
    );
};

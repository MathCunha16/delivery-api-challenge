import type { Order } from '../../types';

interface OrderItemsTableProps {
    order: Order;
}

export const OrderItemsTable = ({ order }: OrderItemsTableProps) => {
    return (
        <div className="bg-zinc-900/50 border border-zinc-800/50 rounded-xl overflow-hidden shadow-sm backdrop-blur-sm">
            <div className="p-4 border-b border-zinc-800/50">
                <h3 className="text-lg font-semibold text-zinc-100">Itens do Pedido</h3>
            </div>
            <div className="overflow-x-auto">
                <table className="w-full text-sm text-left">
                    <thead className="bg-zinc-950/30 text-zinc-500 uppercase text-xs font-semibold tracking-wider border-b border-zinc-800/50">
                        <tr>
                            <th className="px-6 py-3">Qtd</th>
                            <th className="px-6 py-3 w-full">Item</th>
                            <th className="px-6 py-3 text-right">Total</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-zinc-800/50 text-zinc-300">
                        {order.items.map((item, index) => (
                            <tr key={index} className="hover:bg-zinc-800/20 transition-colors">
                                <td className="px-6 py-4 align-top font-medium text-zinc-400">
                                    {item.quantity}x
                                </td>
                                <td className="px-6 py-4 align-top">
                                    <div className="font-medium text-zinc-100 text-base">{item.name}</div>
                                    {item.observations && (
                                        <div className="text-zinc-500 text-xs mt-1 italic">
                                            Obs: {item.observations}
                                        </div>
                                    )}
                                    {item.condiments && item.condiments.length > 0 && (
                                        <div className="mt-2 space-y-1">
                                            {item.condiments.map((condiment, i) => (
                                                <div key={i} className="text-xs text-zinc-400 flex items-center gap-1">
                                                    <span className="w-1 h-1 bg-zinc-600 rounded-full" />
                                                    {condiment.name} (+{new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(condiment.price)})
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </td>
                                <td className="px-6 py-4 align-top text-right font-medium text-zinc-200">
                                    {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(item.total_price)}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                    <tfoot className="bg-zinc-950/20 border-t border-zinc-800/50">
                        <tr>
                            <td colSpan={2} className="px-6 py-4 text-right font-medium text-zinc-400 uppercase text-xs tracking-wider">
                                Total do Pedido
                            </td>
                            <td className="px-6 py-4 text-right font-bold text-xl text-emerald-400">
                                {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(order.total_price)}
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    );
};

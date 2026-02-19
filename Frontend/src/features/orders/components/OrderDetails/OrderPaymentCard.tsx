import { CreditCard, Banknote, Wallet } from 'lucide-react';
import type { Order, Payment } from '../../types';

interface OrderPaymentCardProps {
    order: Order;
}

const getPaymentIcon = (origin: Payment['origin']) => {
    switch (origin) {
        case 'CREDIT_CARD':
        case 'DEBIT_CARD':
        case 'VR':
        case 'VA':
            return <CreditCard className="w-5 h-5" />;
        case 'CASH':
            return <Banknote className="w-5 h-5" />;
        case 'PIX':
            return <div className="font-bold text-xs border border-current rounded px-1">PIX</div>;
        default:
            return <Wallet className="w-5 h-5" />;
    }
};

const getPaymentLabel = (origin: Payment['origin']) => {
    const map: Record<string, string> = {
        'CREDIT_CARD': 'Cartão de Crédito',
        'DEBIT_CARD': 'Cartão de Débito',
        'PIX': 'PIX',
        'CASH': 'Dinheiro',
        'VR': 'Vale Refeição',
        'VA': 'Vale Alimentação'
    };
    return map[origin] || origin;
};

export const OrderPaymentCard = ({ order }: OrderPaymentCardProps) => {
    return (
        <div className="bg-zinc-900/50 border border-zinc-800/50 rounded-xl p-4 shadow-sm backdrop-blur-sm">
            <div className="flex items-center gap-3 mb-4 border-b border-zinc-800/50 pb-3">
                <div className="p-2 bg-purple-500/10 rounded-lg text-purple-400">
                    <Wallet className="w-5 h-5" />
                </div>
                <h3 className="font-semibold text-zinc-100">Pagamento</h3>
            </div>

            <div className="space-y-3">
                {order.payments.map((payment, index) => (
                    <div key={index} className="flex items-center justify-between p-3 bg-zinc-950/30 rounded-lg border border-zinc-800/30">
                        <div className="flex items-center gap-3">
                            <div className="text-zinc-400">
                                {getPaymentIcon(payment.origin)}
                            </div>
                            <div>
                                <p className="font-medium text-zinc-200 text-sm">
                                    {getPaymentLabel(payment.origin)}
                                </p>
                                <p className="text-xs text-zinc-500">
                                    {payment.prepaid ? 'Pago Online' : 'Pagar na Entrega'}
                                </p>
                            </div>
                        </div>
                        <div className="text-right">
                            <p className="font-semibold text-zinc-100">
                                {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(payment.value)}
                            </p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

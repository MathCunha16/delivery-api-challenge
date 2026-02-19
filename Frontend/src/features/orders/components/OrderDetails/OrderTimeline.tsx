import { Circle, CheckCircle2 } from 'lucide-react';
import type { Order } from '../../types';
import clsx from 'clsx';

interface OrderTimelineProps {
    order: Order;
}

export const OrderTimeline = ({ order }: OrderTimelineProps) => {
    // Sort statuses by date descending
    const sortedStatuses = [...order.statuses].sort((a, b) => b.created_at - a.created_at);

    return (
        <div className="bg-zinc-900/50 border border-zinc-800/50 rounded-xl p-4 shadow-sm backdrop-blur-sm">
            <h3 className="font-semibold text-zinc-100 mb-4 border-b border-zinc-800/50 pb-3">Histórico de Status</h3>
            <div className="relative pl-2 space-y-6">
                {/* Connector Line */}
                <div className="absolute top-2 left-[15px] bottom-4 w-px bg-zinc-800/80" />

                {sortedStatuses.map((status, index) => {
                    const isLatest = index === 0;
                    return (
                        <div key={index} className="relative flex gap-4 items-start">
                            <div className={clsx(
                                "relative z-10 p-1 rounded-full border-4 border-zinc-950",
                                isLatest ? "bg-emerald-500/20 text-emerald-400" : "bg-zinc-800 text-zinc-500"
                            )}>
                                {isLatest ? <CheckCircle2 className="w-4 h-4" /> : <Circle className="w-4 h-4" />}
                            </div>
                            <div className="flex-1 pt-0.5">
                                <p className={clsx("font-medium", isLatest ? "text-emerald-400" : "text-zinc-300")}>
                                    {status.name}
                                </p>
                                <p className="text-xs text-zinc-500">
                                    {new Date(status.created_at).toLocaleString('pt-BR')}
                                </p>
                                <p className="text-[10px] uppercase tracking-wider text-zinc-600 mt-0.5">
                                    Origem: {status.origin}
                                </p>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

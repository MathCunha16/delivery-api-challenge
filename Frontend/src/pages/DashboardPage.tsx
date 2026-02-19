import { useStore } from '../context/StoreContext';
import { OrderList } from '../features/orders/components/OrderList';
import { LayoutDashboard, Store, ArrowLeft } from 'lucide-react';
import { Link } from 'react-router-dom';

const DashboardPage = () => {
    const { currentStore } = useStore();

    return (
        <div className="min-h-screen bg-zinc-950 text-zinc-100 p-6 md:p-8">
            <div className="max-w-7xl mx-auto space-y-8">

                {/* Header */}
                <header className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-zinc-800 pb-6">
                    <div className="space-y-1">
                        <div className="flex items-center gap-4">
                            <Link
                                to="/"
                                className="p-2 -ml-2 rounded-lg hover:bg-zinc-800/50 text-zinc-400 hover:text-white transition-colors"
                                title="Voltar para seleção de loja"
                            >
                                <ArrowLeft className="w-6 h-6" />
                            </Link>
                            <h1 className="text-3xl font-bold tracking-tight flex items-center gap-3">
                                <LayoutDashboard className="text-emerald-500 w-8 h-8" />
                                Dashboard Operacional
                            </h1>
                        </div>
                        <p className="text-zinc-400 flex items-center gap-2 text-sm pl-12">
                            <Store className="w-4 h-4" />
                            Unidade: <span className="text-zinc-200 font-medium">{currentStore?.name || 'Selecione uma loja'}</span>
                        </p>
                    </div>

                    <div className="flex items-center gap-3">
                        <Link
                            to="/"
                            className="text-sm text-zinc-500 hover:text-zinc-300 transition-colors flex items-center gap-2"
                        >
                            <span className="opacity-0 md:opacity-100 transition-opacity">Trocar Loja</span>
                        </Link>
                    </div>
                </header>

                {/* content */}
                <main>
                    <OrderList />
                </main>

            </div>
        </div>
    );
};

export default DashboardPage;

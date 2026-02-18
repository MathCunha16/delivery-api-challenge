import { StoreSelection } from '../features/stores/components/StoreSelection';
import { LayoutDashboard } from 'lucide-react';

const SelectionPage = () => {
    return (
        <div className="min-h-screen bg-zinc-950 text-zinc-100 flex flex-col items-center justify-center p-6 relative overflow-hidden">
            {/* Background Gradient Effect */}
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-full h-full max-w-7xl pointer-events-none">
                <div className="absolute top-[-10%] left-[20%] w-[500px] h-[500px] bg-emerald-500/10 rounded-full blur-[120px]" />
                <div className="absolute bottom-[-10%] right-[20%] w-[500px] h-[500px] bg-orange-500/10 rounded-full blur-[120px]" />
            </div>

            <div className="z-10 w-full max-w-5xl flex flex-col items-center">
                <div className="mb-12 text-center space-y-4">
                    <div className="inline-flex items-centerjustify-center p-3 rounded-2xl bg-zinc-900 border border-zinc-800 shadow-xl mb-6">
                        <LayoutDashboard className="w-8 h-8 text-emerald-500" />
                    </div>
                    <h1 className="text-4xl md:text-5xl font-bold tracking-tight text-transparent bg-clip-text bg-gradient-to-br from-white to-zinc-500">
                        Selecione a Unidade
                    </h1>
                    <p className="text-zinc-400 text-lg max-w-md mx-auto">
                        Escolha uma loja para gerenciar pedidos, cardápio e configurações.
                    </p>
                </div>

                <StoreSelection />
            </div>
        </div>
    );
};

export default SelectionPage;

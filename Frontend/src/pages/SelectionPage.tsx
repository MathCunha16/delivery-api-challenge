import { StoreSelection } from '../features/stores/components/StoreSelection';
import { LayoutDashboard, ShoppingCart, ChevronDown } from 'lucide-react';

const SelectionPage = () => {
    return (
        <div className="min-h-screen bg-zinc-950 text-zinc-100 relative overflow-y-auto overflow-x-hidden">
            {/* Background Gradient Effect - Fixed */}
            <div className="fixed top-0 left-1/2 -translate-x-1/2 w-full h-full max-w-7xl pointer-events-none z-0">
                <div className="absolute top-[-10%] left-[20%] w-[500px] h-[500px] bg-emerald-500/10 rounded-full blur-[120px]" />
                <div className="absolute bottom-[-10%] right-[20%] w-[500px] h-[500px] bg-orange-500/10 rounded-full blur-[120px]" />
            </div>

            {/* PARTNER AREA (Hero) */}
            <section className="min-h-screen flex flex-col items-center justify-center p-6 relative z-10">
                <div className="w-full max-w-5xl flex flex-col items-center">
                    <div className="mb-12 text-center space-y-4">
                        <div className="inline-flex items-center justify-center p-3 rounded-2xl bg-zinc-900 border border-zinc-800 shadow-xl mb-6">
                            <LayoutDashboard className="w-8 h-8 text-emerald-500" />
                        </div>
                        <h1 className="text-4xl md:text-5xl font-bold tracking-tight text-transparent bg-clip-text bg-gradient-to-br from-white to-zinc-500">
                            Área do Parceiro
                        </h1>
                        <p className="text-zinc-400 text-lg max-w-md mx-auto">
                            Escolha uma loja para gerenciar pedidos, cardápio e configurações do seu delivery.
                        </p>
                    </div>

                    <StoreSelection mode="PARTNER" />

                    {/* Scroll Indicator */}
                    <div className="absolute bottom-8 left-1/2 -translate-x-1/2 flex flex-col items-center gap-2 text-zinc-600 animate-bounce">
                        <span className="text-xs font-medium tracking-widest uppercase">Simular Pedido</span>
                        <ChevronDown className="w-6 h-6" />
                    </div>
                </div>
            </section>

            {/* SIMULATOR AREA */}
            <section className="min-h-screen flex flex-col items-center justify-center p-6 relative z-10 bg-zinc-950/80 backdrop-blur-3xl border-t border-zinc-900">
                <div className="w-full max-w-5xl flex flex-col items-center">
                    <div className="mb-12 text-center space-y-4">
                        <div className="inline-flex items-center justify-center p-3 rounded-2xl bg-zinc-900 border border-zinc-800 shadow-xl mb-6">
                            <ShoppingCart className="w-8 h-8 text-orange-500" />
                        </div>
                        <h2 className="text-4xl md:text-5xl font-bold tracking-tight text-transparent bg-clip-text bg-gradient-to-br from-white to-zinc-500">
                            Simular Novo Pedido
                        </h2>
                        <p className="text-zinc-400 text-lg max-w-md mx-auto">
                            Teste o fluxo de ponta a ponta criando um pedido manual como se fosse um cliente ou balcão.
                        </p>
                    </div>

                    <StoreSelection mode="SIMULATOR" />
                </div>
            </section>
        </div>
    );
};

export default SelectionPage;

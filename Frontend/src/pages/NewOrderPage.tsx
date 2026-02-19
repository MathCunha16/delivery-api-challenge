import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ChevronLeft, ChevronRight, Check, ShoppingBag, MapPin, User, CreditCard, Plus } from 'lucide-react';
import clsx from 'clsx';
import { MENU, type Product } from '../mocks/menu';
import api from '../lib/axios';

// Types for the form state
interface CartItem extends Product {
    quantity: number;
    observations: string;
    condiments: { name: string; price: number }[];
}

interface NewOrderForm {
    customer: {
        name: string;
        phone: string;
    };
    address: {
        zipCode: string;
        street: string;
        number: string;
        neighborhood: string;
        city: string;
        state: string;
        reference: string;
        complement: string;
    };
    cart: CartItem[];
    payment: {
        method: 'CREDIT_CARD' | 'DEBIT_CARD' | 'PIX' | 'CASH' | 'VR' | 'VA';
        prepaid: boolean;
    };
}

const STEPS = [
    { id: 1, title: 'Identificação', icon: User },
    { id: 2, title: 'Entrega', icon: MapPin },
    { id: 3, title: 'Cardápio', icon: ShoppingBag },
    { id: 4, title: 'Pagamento', icon: CreditCard },
];

const NewOrderPage = () => {
    const { storeId } = useParams<{ storeId: string }>();
    const navigate = useNavigate();
    const [currentStep, setCurrentStep] = useState(1);
    const [loading, setLoading] = useState(false);

    // State for Product Options Modal
    const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
    const [tempObservation, setTempObservation] = useState('');
    const [tempCondiments, setTempCondiments] = useState<{ name: string; price: number }[]>([]);

    const [formData, setFormData] = useState<NewOrderForm>({
        customer: { name: '', phone: '' },
        address: {
            zipCode: '', street: '', number: '', neighborhood: '',
            city: 'Goiânia', state: 'GO', reference: '', complement: ''
        },
        cart: [],
        payment: { method: 'CREDIT_CARD', prepaid: false }
    });

    const updateFormData = <K extends keyof NewOrderForm>(section: K, data: Partial<NewOrderForm[K]>) => {
        setFormData(prev => ({ ...prev, [section]: { ...prev[section], ...data } }));
    };

    const handleNext = () => {
        if (currentStep < 4) setCurrentStep(curr => curr + 1);
    };

    const handleBack = () => {
        if (currentStep > 1) setCurrentStep(curr => curr - 1);
        else navigate('/');
    };

    const openProductOptions = (product: Product) => {
        setSelectedProduct(product);
        setTempObservation('');
        setTempCondiments([]);
    };

    const toggleCondiment = (condiment: { name: string; price: number }) => {
        setTempCondiments(prev => {
            const exists = prev.find(c => c.name === condiment.name);
            if (exists) return prev.filter(c => c.name !== condiment.name);
            return [...prev, condiment];
        });
    };

    const confirmAddToCart = () => {
        if (!selectedProduct) return;

        const newItem: CartItem = {
            ...selectedProduct,
            quantity: 1,
            observations: tempObservation,
            condiments: tempCondiments
        };

        setFormData(prev => ({
            ...prev,
            cart: [...prev.cart, newItem]
        }));

        setSelectedProduct(null);
    };

    const removeFromCart = (index: number) => {
        setFormData(prev => ({
            ...prev,
            cart: prev.cart.filter((_, i) => i !== index)
        }));
    };

    const handleSubmit = async () => {
        if (!storeId) return;

        try {
            setLoading(true);

            const rawTotal = formData.cart.reduce((acc, item) => {
                const condimentsTotal = item.condiments.reduce((cAcc, c) => cAcc + c.price, 0);
                return acc + ((item.price + condimentsTotal) * item.quantity);
            }, 0);

            const total = Number(rawTotal.toFixed(2));

            const payload = {
                store_id: storeId,
                total_price: total,
                customer: {
                    name: formData.customer.name,
                    temporary_phone: formData.customer.phone
                },
                delivery_address: {
                    street_name: formData.address.street,
                    street_number: formData.address.number,
                    neighborhood: formData.address.neighborhood,
                    city: formData.address.city,
                    state: formData.address.state,
                    country: 'BR',
                    zip_code: formData.address.zipCode,
                    complement: formData.address.complement,
                    reference: formData.address.reference,
                    coordinates: {
                        latitude: -16.686891, // Mocked
                        longitude: -49.264794, // Mocked
                        coordinate_id: Math.floor(Math.random() * 1000) // Mocked
                    }
                },
                items: formData.cart.map(item => ({
                    code: item.code,
                    name: item.name,
                    quantity: item.quantity,
                    unit_price: item.price,
                    observations: item.observations,
                    condiments: item.condiments
                })),
                payments: [
                    {
                        payment_method: formData.payment.method,
                        value: total
                    }
                ]
            };

            await api.post('/orders', payload);
            alert('Pedido criado com sucesso!');
            navigate('/dashboard');

        } catch (error) {
            console.error(error);
            alert('Erro ao criar pedido.');
        } finally {
            setLoading(false);
        }
    };

    const cartTotal = formData.cart.reduce((acc, item) => {
        const condimentsTotal = item.condiments.reduce((cAcc, c) => cAcc + c.price, 0);
        return acc + ((item.price + condimentsTotal) * item.quantity);
    }, 0);

    return (
        <div className="min-h-screen bg-zinc-950 text-zinc-100 flex flex-col relative">
            {/* Header */}
            <header className="h-16 border-b border-zinc-800 flex items-center justify-between px-6 bg-zinc-900/50 backdrop-blur-md sticky top-0 z-50">
                <button onClick={handleBack} className="p-2 hover:bg-zinc-800 rounded-lg transition-colors">
                    <ChevronLeft className="w-5 h-5 text-zinc-400" />
                </button>
                <div className="text-center">
                    <h1 className="font-semibold text-lg">Novo Pedido</h1>
                    <div className="text-xs text-zinc-500">Passo {currentStep} de 4</div>
                </div>
                <div className="w-9" /> {/* Spacer */}
            </header>

            {/* Progress Bar */}
            <div className="w-full bg-zinc-900 border-b border-zinc-800">
                <div className="max-w-2xl mx-auto flex items-center justify-between p-4">
                    {STEPS.map((step) => {
                        const Icon = step.icon;
                        const isActive = step.id === currentStep;
                        const isCompleted = step.id < currentStep;

                        return (
                            <div key={step.id} className="flex flex-col items-center gap-2 relative z-10">
                                <div className={clsx(
                                    "w-10 h-10 rounded-full flex items-center justify-center transition-all duration-300 border-2",
                                    isActive ? "bg-emerald-500 border-emerald-500 text-black shadow-lg shadow-emerald-500/20 scale-110" :
                                        isCompleted ? "bg-zinc-800 border-emerald-500/50 text-emerald-500" :
                                            "bg-zinc-900 border-zinc-800 text-zinc-600"
                                )}>
                                    <Icon className="w-5 h-5" />
                                </div>
                                <span className={clsx(
                                    "text-xs font-medium transition-colors duration-300 absolute -bottom-6 w-max text-center",
                                    isActive ? "text-emerald-400" :
                                        isCompleted ? "text-zinc-400" : "text-zinc-600"
                                )}>
                                    {step.title}
                                </span>
                            </div>
                        );
                    })}
                    {/* Connection Line */}
                    <div className="absolute left-0 w-full h-0.5 bg-zinc-800 top-9 -z-0 hidden md:block" />
                </div>
                <div className="h-8" /> {/* Spacing for labels */}
            </div>

            {/* Content */}
            <main className="flex-1 w-full max-w-2xl mx-auto p-6">

                {/* STEP 1: CUSTOMER */}
                {currentStep === 1 && (
                    <div className="space-y-6 animate-in fade-in slide-in-from-right-4 duration-300">
                        <div className="flex items-center gap-3 mb-6">
                            <div className="p-3 bg-emerald-500/10 rounded-xl text-emerald-500">
                                <User className="w-6 h-6" />
                            </div>
                            <h2 className="text-2xl font-bold">Quem é você?</h2>
                        </div>

                        <div className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Seu Nome</label>
                                <input
                                    type="text"
                                    value={formData.customer.name}
                                    onChange={e => updateFormData('customer', { name: e.target.value })}
                                    className="w-full bg-zinc-900 border border-zinc-700 rounded-lg p-3 text-white focus:ring-2 focus:ring-emerald-500 focus:border-transparent outline-none transition-all"
                                    placeholder="Ex: João Silva"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Seu Telefone</label>
                                <input
                                    type="tel"
                                    value={formData.customer.phone}
                                    onChange={e => updateFormData('customer', { phone: e.target.value })}
                                    className="w-full bg-zinc-900 border border-zinc-700 rounded-lg p-3 text-white focus:ring-2 focus:ring-emerald-500 focus:border-transparent outline-none transition-all"
                                    placeholder="Ex: (62) 99999-9999"
                                />
                            </div>
                        </div>
                    </div>
                )}

                {/* STEP 2: ADDRESS */}
                {currentStep === 2 && (
                    <div className="space-y-6 animate-in fade-in slide-in-from-right-4 duration-300">
                        <div className="flex items-center gap-3 mb-6">
                            <div className="p-3 bg-orange-500/10 rounded-xl text-orange-500">
                                <MapPin className="w-6 h-6" />
                            </div>
                            <h2 className="text-2xl font-bold">Onde entregar?</h2>
                        </div>

                        <div className="grid grid-cols-2 gap-4">
                            <div className="col-span-2">
                                <label className="block text-sm font-medium text-zinc-400 mb-1">CEP</label>
                                <input
                                    type="text"
                                    value={formData.address.zipCode}
                                    onChange={e => updateFormData('address', { zipCode: e.target.value })}
                                    className="w-full bg-zinc-900 border border-zinc-700 rounded-lg p-3 text-white focus:ring-2 focus:ring-orange-500 outline-none"
                                    placeholder="00000-000"
                                />
                            </div>
                            <div className="col-span-2">
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Rua / Avenida</label>
                                <input
                                    type="text"
                                    value={formData.address.street}
                                    onChange={e => updateFormData('address', { street: e.target.value })}
                                    className="w-full bg-zinc-900 border border-zinc-700 rounded-lg p-3 text-white focus:ring-2 focus:ring-orange-500 outline-none"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Número</label>
                                <input
                                    type="text"
                                    value={formData.address.number}
                                    onChange={e => updateFormData('address', { number: e.target.value })}
                                    className="w-full bg-zinc-900 border border-zinc-700 rounded-lg p-3 text-white focus:ring-2 focus:ring-orange-500 outline-none"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Bairro</label>
                                <input
                                    type="text"
                                    value={formData.address.neighborhood}
                                    onChange={e => updateFormData('address', { neighborhood: e.target.value })}
                                    className="w-full bg-zinc-900 border border-zinc-700 rounded-lg p-3 text-white focus:ring-2 focus:ring-orange-500 outline-none"
                                />
                            </div>
                            <div className="col-span-2">
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Complemento</label>
                                <input
                                    type="text"
                                    value={formData.address.complement}
                                    onChange={e => updateFormData('address', { complement: e.target.value })}
                                    className="w-full bg-zinc-900 border border-zinc-700 rounded-lg p-3 text-white focus:ring-2 focus:ring-orange-500 outline-none"
                                    placeholder="Ex: Apto 101"
                                />
                            </div>
                            <div className="col-span-2">
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Referência</label>
                                <input
                                    type="text"
                                    value={formData.address.reference}
                                    onChange={e => updateFormData('address', { reference: e.target.value })}
                                    className="w-full bg-zinc-900 border border-zinc-700 rounded-lg p-3 text-white focus:ring-2 focus:ring-orange-500 outline-none"
                                    placeholder="Ex: Próximo à praça"
                                />
                            </div>
                        </div>
                    </div>
                )}

                {/* STEP 3: MENU */}
                {currentStep === 3 && (
                    <div className="space-y-6 animate-in fade-in slide-in-from-right-4 duration-300">
                        <div className="flex items-center gap-3 mb-6">
                            <div className="p-3 bg-purple-500/10 rounded-xl text-purple-500">
                                <ShoppingBag className="w-6 h-6" />
                            </div>
                            <h2 className="text-2xl font-bold">Cardápio</h2>
                        </div>

                        {/* Cart Summary */}
                        {formData.cart.length > 0 && (
                            <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-4 mb-6 sticky top-20 z-40 shadow-xl">
                                <div className="flex justify-between items-center mb-2">
                                    <span className="text-zinc-400 text-sm">Seu Pedido ({formData.cart.length} itens)</span>
                                    <span className="font-bold text-emerald-400">
                                        {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(cartTotal)}
                                    </span>
                                </div>
                            </div>
                        )}

                        {/* Product List */}
                        <div className="space-y-4">
                            {MENU.map(product => {
                                return (
                                    <div key={product.code} className="group flex items-center justify-between p-4 bg-zinc-900/50 border border-zinc-800/50 rounded-xl hover:bg-zinc-900 transition-all cursor-pointer hover:border-emerald-500/30" onClick={() => openProductOptions(product)}>
                                        <div className="flex-1">
                                            <h3 className="font-bold text-zinc-100">{product.name}</h3>
                                            <p className="text-sm text-zinc-500 line-clamp-2">{product.description}</p>
                                            <p className="text-emerald-400 font-medium mt-1">
                                                {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(product.price)}
                                            </p>
                                        </div>
                                        <div className="bg-zinc-800 p-2 rounded-lg group-hover:bg-emerald-500/20 transition-colors">
                                            <Plus className="w-5 h-5 text-zinc-400 group-hover:text-emerald-500 transition-colors" />
                                        </div>
                                    </div>
                                );
                            })}
                        </div>

                        {/* Selected Items List (Editable) */}
                        <div className="mt-8 space-y-3">
                            <h3 className="text-lg font-bold text-zinc-300">Itens Selecionados</h3>
                            {formData.cart.length === 0 && <p className="text-zinc-500 text-sm">Nenhum item adicionado.</p>}
                            {formData.cart.map((item, index) => (
                                <div key={index} className="p-4 bg-zinc-900 border border-zinc-800 rounded-xl flex justify-between items-start gap-4">
                                    <div className="flex-1">
                                        <div className="flex justify-between">
                                            <span className="font-bold">{item.name}</span>
                                            <span className="text-emerald-400 font-medium">
                                                {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(item.price)}
                                            </span>
                                        </div>
                                        {item.condiments.length > 0 && (
                                            <ul className="mt-2 text-sm text-zinc-400 list-disc list-inside">
                                                {item.condiments.map(c => (
                                                    <li key={c.name}>{c.name} (+{new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(c.price)})</li>
                                                ))}
                                            </ul>
                                        )}
                                        {item.observations && (
                                            <p className="mt-2 text-sm text-zinc-500 italic">"Obs: {item.observations}"</p>
                                        )}
                                    </div>
                                    <button onClick={() => removeFromCart(index)} className="text-red-400 hover:text-red-300 p-1">
                                        <span className="sr-only">Remover</span>
                                        ✕
                                    </button>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {/* STEP 4: PAYMENT */}
                {currentStep === 4 && (
                    <div className="space-y-6 animate-in fade-in slide-in-from-right-4 duration-300">
                        <div className="flex items-center gap-3 mb-6">
                            <div className="p-3 bg-blue-500/10 rounded-xl text-blue-500">
                                <CreditCard className="w-6 h-6" />
                            </div>
                            <h2 className="text-2xl font-bold">Pagamento</h2>
                        </div>

                        <div className="space-y-3">
                            {(['CREDIT_CARD', 'DEBIT_CARD', 'CASH', 'PIX', 'VR', 'VA'] as const).map(methodId => {
                                const labels: Record<string, string> = {
                                    CREDIT_CARD: 'Cartão de Crédito (Entrega)',
                                    DEBIT_CARD: 'Cartão de Débito (Entrega)',
                                    CASH: 'Dinheiro (Entrega)',
                                    PIX: 'PIX (Online)',
                                    VR: 'Vale Refeição (Entrega)',
                                    VA: 'Vale Alimentação (Entrega)'
                                };
                                return (
                                    <label
                                        key={methodId}
                                        className={clsx(
                                            "flex items-center gap-4 p-4 rounded-xl border cursor-pointer transition-all",
                                            formData.payment.method === methodId
                                                ? "bg-emerald-500/10 border-emerald-500/50 text-emerald-400"
                                                : "bg-zinc-900 border-zinc-800 text-zinc-400 hover:bg-zinc-800"
                                        )}
                                    >
                                        <input
                                            type="radio"
                                            name="payment"
                                            className="hidden"
                                            checked={formData.payment.method === methodId}
                                            onChange={() => updateFormData('payment', { method: methodId, prepaid: ['PIX', 'VR', 'VA'].includes(methodId) })}
                                        />
                                        <div className={clsx(
                                            "w-5 h-5 rounded-full border-2 flex items-center justify-center",
                                            formData.payment.method === methodId ? "border-emerald-500" : "border-zinc-600"
                                        )}>
                                            {formData.payment.method === methodId && <div className="w-2.5 h-2.5 rounded-full bg-emerald-500" />}
                                        </div>
                                        <span className="font-medium">{labels[methodId]}</span>
                                    </label>
                                )
                            })}
                        </div>

                        <div className="mt-8 p-6 bg-zinc-900 rounded-xl border border-zinc-800">
                            <div className="flex justify-between mb-2">
                                <span className="text-zinc-400">Subtotal</span>
                                <span>{new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(cartTotal)}</span>
                            </div>
                            <div className="flex justify-between mb-4">
                                <span className="text-zinc-400">Taxa de Entrega</span>
                                <span className="text-emerald-400">Grátis</span>
                            </div>
                            <div className="border-t border-zinc-800 pt-4 flex justify-between items-end">
                                <span className="text-lg font-bold">Total</span>
                                <span className="text-2xl font-bold text-emerald-400">
                                    {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(cartTotal)}
                                </span>
                            </div>
                        </div>
                    </div>
                )}
            </main>

            {/* Footer / Actions */}
            <footer className="p-6 border-t border-zinc-800 bg-zinc-900/50 backdrop-blur-md sticky bottom-0 z-40">
                <div className="max-w-2xl mx-auto flex gap-4">
                    {currentStep < 4 ? (
                        <button
                            onClick={handleNext}
                            className="w-full py-4 bg-emerald-500 hover:bg-emerald-600 text-black font-bold rounded-xl transition-colors flex items-center justify-center gap-2"
                        >
                            Próximo Passo <ChevronRight className="w-5 h-5" />
                        </button>
                    ) : (
                        <button
                            onClick={handleSubmit}
                            disabled={loading || formData.cart.length === 0}
                            className="w-full py-4 bg-emerald-500 hover:bg-emerald-600 text-black font-bold rounded-xl transition-colors flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            {loading ? 'Enviando...' : 'Finalizar Pedido'} <Check className="w-5 h-5" />
                        </button>
                    )}
                </div>
            </footer>

            {/* Product Options Modal */}
            {selectedProduct && (
                <div className="fixed inset-0 z-[60] flex items-end sm:items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-in fade-in duration-200">
                    <div className="bg-zinc-900 w-full max-w-lg rounded-2xl border border-zinc-800 shadow-2xl overflow-hidden flex flex-col max-h-[90vh]">
                        <div className="p-6 border-b border-zinc-800 flex justify-between items-start">
                            <div>
                                <h3 className="text-xl font-bold">{selectedProduct.name}</h3>
                                <p className="text-zinc-400 text-sm mt-1">{selectedProduct.description}</p>
                            </div>
                            <button onClick={() => setSelectedProduct(null)} className="p-2 hover:bg-zinc-800 rounded-lg">✕</button>
                        </div>

                        <div className="p-6 overflow-y-auto space-y-6">
                            {/* Condiments */}
                            {(selectedProduct.availableCondiments?.length ?? 0) > 0 && (
                                <div>
                                    <h4 className="font-bold mb-3 text-zinc-300">Adicionais</h4>
                                    <div className="space-y-2">
                                        {selectedProduct.availableCondiments.map(condiment => {
                                            const isSelected = tempCondiments.some(c => c.name === condiment.name);
                                            return (
                                                <label key={condiment.name} className="flex items-center justify-between p-3 rounded-xl border border-zinc-800 bg-zinc-950/50 cursor-pointer hover:bg-zinc-800 transition-colors">
                                                    <div className="flex items-center gap-3">
                                                        <div className={clsx("w-5 h-5 rounded border flex items-center justify-center", isSelected ? "bg-emerald-500 border-emerald-500" : "border-zinc-600")}>
                                                            {isSelected && <Check className="w-3.5 h-3.5 text-black" />}
                                                        </div>
                                                        <span>{condiment.name}</span>
                                                    </div>
                                                    <span className="text-emerald-400">+ {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(condiment.price)}</span>
                                                    <input
                                                        type="checkbox"
                                                        className="hidden"
                                                        checked={isSelected}
                                                        onChange={() => toggleCondiment(condiment)}
                                                    />
                                                </label>
                                            );
                                        })}
                                    </div>
                                </div>
                            )}

                            {/* Observations */}
                            <div>
                                <h4 className="font-bold mb-3 text-zinc-300">Observações</h4>
                                <textarea
                                    className="w-full bg-zinc-950 border border-zinc-700 rounded-xl p-3 text-white focus:ring-2 focus:ring-emerald-500 outline-none resize-none h-24"
                                    placeholder="Ex: Tirar cebola, maionese à parte..."
                                    value={tempObservation}
                                    onChange={(e) => setTempObservation(e.target.value)}
                                />
                            </div>
                        </div>

                        <div className="p-6 border-t border-zinc-800 bg-zinc-900">
                            <button
                                onClick={confirmAddToCart}
                                className="w-full py-4 bg-emerald-500 hover:bg-emerald-600 text-black font-bold rounded-xl transition-colors"
                            >
                                Adicionar ao Pedido • {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(
                                    selectedProduct.price + tempCondiments.reduce((acc, c) => acc + c.price, 0)
                                )}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default NewOrderPage;

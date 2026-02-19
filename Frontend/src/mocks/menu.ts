export interface Product {
    code: number;
    name: string;
    description: string;
    price: number;
    category: 'BURGER' | 'DRINK' | 'DESSERT' | 'SIDE';
    availableCondiments: { name: string; price: number }[];
    image?: string;
}

export const MENU: Product[] = [
    // Burgers
    {
        code: 101,
        name: 'X-Bacon Supremo',
        description: 'Pão brioche, 2 blends de 150g, muito bacon crocante, queijo cheddar e maionese da casa.',
        price: 34.90,
        category: 'BURGER',
        availableCondiments: [
            { name: 'Bacon Extra', price: 6.00 },
            { name: 'Queijo Cheddar Extra', price: 4.50 },
            { name: 'Maionese Extra', price: 2.00 }
        ]
    },
    {
        code: 102,
        name: 'Classic Salad',
        description: 'Pão australiano, blend 180g, alface, tomate, cebola roxa e queijo prato.',
        price: 28.50,
        category: 'BURGER',
        availableCondiments: [
            { name: 'Queijo Prato Extra', price: 4.00 },
            { name: 'Cebola Caramelizada', price: 3.00 }
        ]
    },
    {
        code: 103,
        name: 'Smash Duplo',
        description: 'Dois smashes de 90g, queijo cheddar duplo e molho especial.',
        price: 25.00,
        category: 'BURGER',
        availableCondiments: [
            { name: 'Hambúrguer Extra (90g)', price: 8.00 },
            { name: 'Queijo Extra', price: 4.00 }
        ]
    },
    // Sides
    {
        code: 201,
        name: 'Batata Rústica',
        description: 'Batatas cortadas rusticamente, com alecrim e alho.',
        price: 18.00,
        category: 'SIDE',
        availableCondiments: [
            { name: 'Cheddar e Bacon', price: 7.00 },
            { name: 'Maionese de Ervas', price: 3.00 }
        ]
    },
    {
        code: 202,
        name: 'Onion Rings',
        description: 'Anéis de cebola empanados e crocantes.',
        price: 16.00,
        category: 'SIDE',
        availableCondiments: [
            { name: 'Molho Barbecue', price: 3.00 }
        ]
    },
    // Drinks
    {
        code: 301,
        name: 'Coca-Cola Lata',
        description: '350ml',
        price: 6.00,
        category: 'DRINK',
        availableCondiments: []
    },
    {
        code: 302,
        name: 'Suco de Laranja',
        description: 'Natural 400ml',
        price: 9.00,
        category: 'DRINK',
        availableCondiments: [
            { name: 'Gelo e Limão', price: 0.00 },
            { name: 'Açúcar', price: 0.00 }
        ]
    },
    // Desserts
    {
        code: 401,
        name: 'Milkshake de Morango',
        description: 'Feito com sorvete artesanal.',
        price: 18.90,
        category: 'DESSERT',
        availableCondiments: [
            { name: 'Chantilly Extra', price: 4.00 }
        ]
    },
    {
        code: 402,
        name: 'Brownie com Sorvete',
        description: 'Brownie quente com sorvete de baunilha.',
        price: 22.00,
        category: 'DESSERT',
        availableCondiments: [
            { name: 'Bola de Sorvete Extra', price: 6.00 },
            { name: 'Calda de Chocolate Extra', price: 3.00 }
        ]
    }
];

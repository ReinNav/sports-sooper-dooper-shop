const BASE_URL = 'http://localhost:1235/cart';

const getCart = async (userId) => {
    const response = await fetch(`${BASE_URL}/${userId}`);
    if (!response.ok) {
        throw new Error('Failed to fetch cart');
    }
    return response.json();
};

const addToCart = async (userId, cartItem, quantity) => {
    const response = await fetch(`${BASE_URL}/add?userId=${userId}&quantity=${quantity}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(cartItem),
    });
    if (!response.ok) {
        throw new Error('Failed to add to cart');
    }
    return response.json();
};

const subtractFromCart = async (userId, cartItem, quantity) => {
    const response = await fetch(`${BASE_URL}/subtract?userId=${userId}&quantity=${quantity}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(cartItem),
    });
    if (!response.ok) {
        throw new Error('Failed to subtract from cart');
    }
    return response.json();
};

const removeFromCart = async (userId, productId) => {
    const response = await fetch(`${BASE_URL}/remove?userId=${userId}&productId=${productId}`, {
        method: 'POST',
    });
    if (!response.ok) {
        throw new Error('Failed to remove from cart');
    }
    return response.json();
};

const clearCart = async (userId) => {
    const response = await fetch(`${BASE_URL}/clear?userId=${userId}`, {
        method: 'POST',
    });
    if (!response.ok) {
        throw new Error('Failed to clear cart');
    }
    return response.json();
};

export { getCart, addToCart, subtractFromCart, removeFromCart, clearCart };

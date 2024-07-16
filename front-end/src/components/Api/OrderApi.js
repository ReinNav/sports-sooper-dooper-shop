const BASE_URL = 'http://localhost:1237/order';

const createOrder = async (order) => {
    const response = await fetch(`${BASE_URL}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(order),
    });

    if (!response.ok) {
        throw new Error('Fehler beim Erstellen der Bestellung');
    }

    return response.json();
};

export { createOrder };

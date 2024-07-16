import React, { useState } from 'react';
import { useAuth } from 'react-oidc-context';
import { createOrder } from './Api/OrderApi';
import { useCart } from './CartContext';
import { useNavigate } from 'react-router-dom';
import '../Checkout.css';
import { createPayment, completePayment } from './Api/PaymentApi';

const countries = [
    "Deutschland", "Österreich", "Schweiz", "Belgien", "Niederlande"
];

const Checkout = () => {
    const auth = useAuth();
    const { cart } = useCart();
    const navigate = useNavigate();

    const [step, setStep] = useState(1);
    const [shippingAddress, setShippingAddress] = useState({ country: "Deutschland" });
    const [billingAddress, setBillingAddress] = useState({});
    const [phone, setPhone] = useState(null);
    const [useShippingAsBilling, setUseShippingAsBilling] = useState(false);
    const [shipmentType, setShipmentType] = useState('STANDARD');
    const [errorMessage, setErrorMessage] = useState('');
    const [orderId, setOrderId] = useState(null);
    const [paymentRedirectUrl, setPaymentRedirectUrl] = useState(null);

    const handlePhoneChange = (e) => {
        const { value } = e.target;
        setPhone(value);
    };
    

    const validateStep = () => {
        switch (step) {
            case 1:
                return (
                    shippingAddress.firstName &&
                    shippingAddress.lastName &&
                    shippingAddress.street &&
                    shippingAddress.houseNumber &&
                    shippingAddress.city &&
                    shippingAddress.postalCode &&
                    shippingAddress.country &&
                    shipmentType
                );
            case 2:
                if (useShippingAsBilling) return true;
                return (
                    billingAddress.firstName &&
                    billingAddress.lastName &&
                    billingAddress.street &&
                    billingAddress.houseNumber &&
                    billingAddress.city &&
                    billingAddress.postalCode &&
                    billingAddress.country
                );
            case 3:
                return (
                    auth.user?.profile.email &&
                    phone
                );
            default:
                return true;
        }
    };

    const handleNextStep = () => {
        if (validateStep()) {
            setStep(step + 1);
            setErrorMessage('');
        } else {
            setErrorMessage('Bitte füllen Sie alle erforderlichen Felder aus.');
        }
    };

    const handlePreviousStep = () => {
        setStep(step - 1);
    };

    const handlePlaceOrder = async () => {
        const userId = auth.user.profile.sub;
        shippingAddress.userId = userId;
        billingAddress.userId = userId;
        const order = {
            userId,
            date: new Date().toISOString().split('T')[0],
            orderItems: cart.cartItems.map(item => ({
                productId: item.productId,
                productName: item.name,
                size: item.size,
                colour: item.colour,
                quantity: item.quantity,
                price: item.price
            })),
            totalAmount: cart.totalPrice + (shipmentType === "EXPRESS" ? 7.99 : 4.99),
            shippingAddress,
            billingAddress: useShippingAsBilling ? shippingAddress : billingAddress,
            contactDetails: {
                contactEmail: auth.user?.profile.email,
                contactPhone: phone
            },
            status: 'PENDING',
            shipmentType: shipmentType
        };

        console.log(order);

        try {
            const createdOrder = await createOrder(order);
            console.log(createdOrder);

            const paymentData = await createPayment(createdOrder.totalAmount, createdOrder.userId, createdOrder.orderId);
            console.log(paymentData);

            setStep(step + 1);
            setOrderId(createdOrder.orderId);
            setPaymentRedirectUrl(paymentData.redirectUrl);
        } catch (error) {
            console.error('Order or payment creation failed:', error);
            setErrorMessage('Fehler bei der Erstellung der Bestellung oder Zahlung.');
        }
    };
    const handleChange = (e, setState) => {
        const { name, value } = e.target;
        setState(prevState => ({ ...prevState, [name]: value }));
    };

    const renderStep = () => {
        switch (step) {
            case 1:
                return (
                    <div className='form-wrapper'>
                        <h2>Versandadresse</h2>
                        <form>
                            <div className='flex-row double-input-row-wrapper'>
                                <div className='flex-column with-label-wrapper'>
                                    <p>Vorname*</p>
                                    <input type="text" name="firstName" placeholder="Vorname" value={shippingAddress.firstName || ''} onChange={e => handleChange(e, setShippingAddress)} />
                                </div>
                                <div className='flex-column with-label-wrapper'>
                                    <p>Nachname*</p>
                                    <input type="text" name="lastName" placeholder="Nachname" value={shippingAddress.lastName || ''} onChange={e => handleChange(e, setShippingAddress)} />
                                </div>
                            </div>
                            <div className='flex-row double-input-row-wrapper'>
                                <div className='flex-column with-label-wrapper'>
                                    <p>Straße*</p>
                                    <input type="text" name="street" placeholder="Straße" value={shippingAddress.street || ''} onChange={e => handleChange(e, setShippingAddress)} />
                                </div>
                                <div className='flex-column with-label-wrapper'>
                                    <p>Hausnummer*</p>
                                    <input type="text" name="houseNumber" placeholder="Hausnummer" value={shippingAddress.houseNumber || ''} onChange={e => handleChange(e, setShippingAddress)} />
                                </div>
                            </div>
                            <div className='flex-row double-input-row-wrapper'>
                                <div className='flex-column with-label-wrapper'>
                                    <p>Stadt*</p>
                                    <input type="text" name="city" placeholder="Stadt" value={shippingAddress.city || ''} onChange={e => handleChange(e, setShippingAddress)} />
                                </div>
                                <div className='flex-column with-label-wrapper'>
                                    <p>Postleitzahl*</p>
                                    <input type="text" name="postalCode" placeholder="Postleitzahl" value={shippingAddress.postalCode || ''} onChange={e => handleChange(e, setShippingAddress)} />
                                </div>
                            </div>
                            <div className='flex-column with-label-wrapper'>
                                <p>Land</p>
                                <select name="country" value={shippingAddress.country || ''} onChange={e => handleChange(e, setShippingAddress)}>
                                    {countries.map(country => (
                                        <option key={country} value={country}>{country}</option>
                                    ))}
                                </select>
                            </div>
                            <div className='flex-column with-label-wrapper'>
                                <p>Lieferung</p>
                                <select name="shipmentType"
                                    value={shipmentType}
                                    onChange={e => setShipmentType(e.target.value)}>
                                    <option value="STANDARD">Standard</option>
                                    <option value="EXPRESS">Express +3,00 €</option>
                                </select>
                            </div>
                            <p className='note'>Felder mit * sind erforderlich.</p>
                            <div className='step-navigation'>
                                {errorMessage && <p className="error-message">{errorMessage}</p>}
                                <button type="button" onClick={handleNextStep}>Weiter</button>
                                <button type="button" onClick={() => navigate('/cart')}>Zurück zum Warenkorb</button>
                            </div>
                        </form>
                    </div>
                );
            case 2:
                return (
                    <div className='form-wrapper'>
                        <h2>Rechnungsadresse</h2>
                        <form>
                            {!useShippingAsBilling && (
                                <>
                                    <div className='flex-row double-input-row-wrapper'>
                                        <div className='flex-column with-label-wrapper'>
                                            <p>Vorname*</p>
                                            <input type="text" name="firstName" placeholder="Vorname" value={billingAddress.firstName || ''} onChange={e => handleChange(e, setBillingAddress)} />
                                        </div>
                                        <div className='flex-column with-label-wrapper'>
                                            <p>Nachname*</p>
                                            <input type="text" name="lastName" placeholder="Nachname" value={billingAddress.lastName || ''} onChange={e => handleChange(e, setBillingAddress)} />
                                        </div>
                                    </div>
                                    <div className='flex-row double-input-row-wrapper'>
                                        <div className='flex-column with-label-wrapper'>
                                            <p>Straße*</p>
                                            <input type="text" name="street" placeholder="Straße" value={billingAddress.street || ''} onChange={e => handleChange(e, setBillingAddress)} />
                                        </div>
                                        <div className='flex-column with-label-wrapper'>
                                            <p>Hausnummer*</p>
                                            <input type="text" name="houseNumber" placeholder="Hausnummer" value={billingAddress.houseNumber || ''} onChange={e => handleChange(e, setBillingAddress)} />
                                        </div>
                                    </div>
                                    <div className='flex-row double-input-row-wrapper'>
                                        <div className='flex-column with-label-wrapper'>
                                            <p>Stadt*</p>
                                            <input type="text" name="city" placeholder="Stadt" value={billingAddress.city || ''} onChange={e => handleChange(e, setBillingAddress)} />
                                        </div>
                                        <div className='flex-column with-label-wrapper'>
                                            <p>Postleitzahl*</p>
                                            <input type="text" name="postalCode" placeholder="Postleitzahl" value={billingAddress.postalCode || ''} onChange={e => handleChange(e, setBillingAddress)} />
                                        </div>
                                    </div>
                                    <div className='flex-column with-label-wrapper'>
                                        <p>Land</p>
                                        <select name="country" value={billingAddress.country || ''} onChange={e => handleChange(e, setBillingAddress)}>
                                            {countries.map(country => (
                                                <option key={country} value={country}>{country}</option>
                                            ))}
                                        </select>
                                    </div>
                                    <p className='note'>Felder mit * sind erforderlich.</p>
                                </>
                            )}
                            <label>
                                <input
                                    type="checkbox"
                                    checked={useShippingAsBilling}
                                    onChange={() => setUseShippingAsBilling(!useShippingAsBilling)}
                                />
                                Rechnungsadresse entspricht Versandadresse
                            </label>

                            <div className='step-navigation'>
                                {errorMessage && <p className="error-message">{errorMessage}</p>}
                                <button type="button" onClick={handleNextStep}>Weiter</button>
                                <button type="button" onClick={handlePreviousStep}>Zurück</button>
                            </div>
                        </form>
                    </div>
                );
            case 3:
                return (
                    <div className='form-wrapper'>
                        <h2>Kontaktdaten</h2>
                        <form>
                            <div className='flex-column with-label-wrapper'>
                                <p>E-Mail*</p>
                                <input type="email" name="email" value={auth.user?.profile.email} disabled />
                            </div>
                            <div className='flex-column with-label-wrapper'>
                                <p>Telefonnummer*</p>
                                <input type="text" name="phone" placeholder="Telefonnummer" value={phone || ''} onChange={handlePhoneChange} />
                            </div>
                            <div className='step-navigation'>
                                <button type="button" onClick={handleNextStep}>Weiter</button>
                                <button type="button" onClick={handlePreviousStep}>Zurück</button>
                            </div>
                        </form>
                    </div>
                );
            case 4:
                return (
                    <div className='form-wrapper'>
                        <h2>Bestellübersicht</h2>
                        <div className='order-summary'>
                            <h3>Produkte</h3>
                            {cart.cartItems.map(item => (
                                <div key={item.productId} className='flex-row product-and-price'>
                                    <p>{item.name}</p>
                                    <p>{item.quantity} x {item.price.toFixed(2).replace('.', ',')} €</p>
                                </div>
                            ))}
                            <hr></hr>
                            <div className='flex-row product-and-price total-price'>
                                <p>Lieferung</p>
                                <p>{shipmentType === "EXPRESS" ? 7.99 : 4.99}</p>
                            </div>
                            <div className='flex-row product-and-price total-price'>
                                <p>Bestellsumme</p>
                                <p id="price">{(cart.totalPrice + (shipmentType === "EXPRESS" ? 7.99 : 4.99)).toFixed(2).replace('.', ',')} €</p>
                            </div>
                            <h3>Versandadresse</h3>
                            <p>{`${shippingAddress.firstName} ${shippingAddress.lastName}`}</p>
                            <p>{`${shippingAddress.street} ${shippingAddress.houseNumber}`}</p>
                            <p>{`${shippingAddress.postalCode} ${shippingAddress.city}`}</p>
                            <p>{shippingAddress.country}</p>

                            <h3>Rechnungsadresse</h3>
                            {useShippingAsBilling ? (
                                <>
                                    <p>gleich wie Versandadresse </p>
                                </>
                            ) : (
                                <>
                                    <p>{`${billingAddress.firstName} ${billingAddress.lastName}`}</p>
                                    <p>{`${billingAddress.street} ${billingAddress.houseNumber}`}</p>
                                    <p>{`${billingAddress.postalCode} ${billingAddress.city}`}</p>
                                    <p>{billingAddress.country}</p>
                                </>
                            )}

                            <h3>Kontaktdaten</h3>
                            <p>{auth.user?.profile.email}</p>
                            <p>{phone}</p>
                        </div>
                        <div className='step-navigation'>
                            <button type="button" onClick={handlePlaceOrder}>Bestellung aufgeben und mit Paypal bezahlen</button>
                            <button type="button" onClick={handlePreviousStep}>Zurück</button>
                        </div>
                    </div>
                );
            case 5:
                return (
                    <div className='form-wrapper'>
                        <h2>PayPal Zahlung</h2>
                        <div className='paypal-buttons'>
                            {paymentRedirectUrl ? (
                                <a href={paymentRedirectUrl}>
                                    <button className="paypal-button">Bezahlen mit PayPal</button>
                                </a>
                            ) : (
                                <p>Lade PayPal...</p>
                            )}
                        </div>
                    </div>
                );
            default:
                return null;
        }
    };

    return (
        <div className="checkout">
            {renderStep()}
        </div>
    );
};

export default Checkout;

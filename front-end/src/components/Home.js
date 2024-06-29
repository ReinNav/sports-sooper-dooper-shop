import React, { useEffect, useState } from 'react';
import { Typography, List } from 'antd';
import ProductsApi from './ProductsApi';
const { Title } = Typography;

function Home() {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        const handleProducts = async () => {
            try {
                const response = await ProductsApi.getProducts();
                setProducts(response.data);
            } catch (error) {
                console.log(error);
            }
        };
        handleProducts();
    }, []);

    return (
        <>
                <div style={{ padding: '20px' }}>
                    <Title level={2}>Product List</Title>
                    {products.length > 0 ? ( <List
                        bordered
                        dataSource={products}
                        renderItem={product => (
                            <List.Item>
                                {product.name}
                            </List.Item>
                        )}
                        />
                    ) : <div> No products yet!</div>}
                </div>
        </>
    );
}

export default Home;

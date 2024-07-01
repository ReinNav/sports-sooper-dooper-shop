import React, { useEffect, useState } from 'react';
import { useAuth } from "react-oidc-context";
import { Typography } from 'antd';
import SecuredApi from './SecuredApi';
const { Title } = Typography;

function SecuredPage() {
    const [data, setData] = useState([]);
    const auth = useAuth();

    useEffect(() => {
        const fetchData = async () => {
            if (auth.user) {
                try {
                    const response = await SecuredApi.getSecuredData(auth.user.access_token);
                    setData(response.data);
                } catch (error) {
                    console.log(error);
                }
            }
        };

        fetchData();
    }, [auth.user]);

    return (
                <div style={{ padding: '20px' }}>
                    <Title level={2}>A secured page. With secured data:</Title>
                    <div>{data}</div>
                </div>
    );
}

export default SecuredPage;

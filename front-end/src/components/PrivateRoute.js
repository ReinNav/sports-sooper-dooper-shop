import { useAuth } from "react-oidc-context"
import { Spin, Typography } from 'antd'
const { Title } = Typography

function PrivateRoute({ children }) {
    const auth = useAuth()

    const textAlignStyle = { textAlign: "center" }
    const subTitleStyle = { color: 'grey' }

    if (auth.isLoading) {
        return (
            <div style={textAlignStyle}>
                <Title>Authenticating...</Title>
                <Spin size="large"></Spin>
            </div>
        )
    }

    if (auth.error) {
        return (
        <div style={textAlignStyle}>
                <Title>Oops ...</Title>
                <Title level={2} style={subTitleStyle}>{auth.error.message}</Title>
            </div>    
        )
    }

    if (!auth.isAuthenticated) {
        return (
            <div style={textAlignStyle}>
                    <Title>Oops ...</Title>
                    <Title level={2} style={subTitleStyle}>Looks like you do not have access to this page.</Title>
                </div>    
            )
    }

    return children
}

export default PrivateRoute
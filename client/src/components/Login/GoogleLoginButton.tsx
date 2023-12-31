import styled from 'styled-components';
import Google from '../../assets/icon/icon_google.png';

const GoogleLoginButton: React.FC = () => {
  const handleOAuthClick = async (e: any) => {
    e.preventDefault();
    window.location.href =
      'http://ec2-54-180-127-81.ap-northeast-2.compute.amazonaws.com:8080/oauth2/authorization/google';
  };

  return (
    <OAuthButton onClick={handleOAuthClick}>
      <Img src={Google} alt="Google logo" />
      Login with Google
    </OAuthButton>
  );
};

export default GoogleLoginButton;

const OAuthButton = styled.button`
  width: 200px;
  height: 40px;
  background-color: white;
  border: 1px solid rgb(224, 224, 224);
  cursor: pointer;
  margin: 50px 0px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const Img = styled.img`
  width: 24px;
  height: 24px;
  margin-right: 10px;
`;

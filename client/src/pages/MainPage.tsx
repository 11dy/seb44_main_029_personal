import styled from 'styled-components';
import backgroundImg from '../assets/images/background.jpg';
import Title from '../components/main/Title';

const MainPage = () => {
  return (
    <Layout>
      <Title />
    </Layout>
  );
};

export default MainPage;

const Layout = styled.div`
  width: 100vw;
  height: 100vh;
  padding: 0;
  display: flex;
  align-items: center;
  background-image: url(${backgroundImg});
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
`;

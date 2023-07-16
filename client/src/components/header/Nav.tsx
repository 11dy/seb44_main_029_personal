import styled from 'styled-components';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Logout } from '../../api/api';
import { useMutation } from '@tanstack/react-query';
import { FiAlignJustify, FiHome, FiUser } from 'react-icons/fi';
import { TbCarouselHorizontal, TbLogout, TbLogin } from 'react-icons/tb';

// Nav 컴포넌트
const Nav = ({
  setIsLogInClicked,
  setIsSignUpClicked,
}: {
  setIsLogInClicked: React.Dispatch<React.SetStateAction<boolean>>;
  setIsSignUpClicked: React.Dispatch<React.SetStateAction<boolean>>;
}) => {
  // 호버 여부 상태 관리
  const [isClicked, setIsClick] = useState(false);
  const navigate = useNavigate();

  //임시 jwt토큰 유무 판단용
  const accessToken = localStorage.getItem('accessToken');

  const handleLogoutMutation = useMutation(Logout, {
    onSuccess: () => {
      // 토큰 삭제
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');

      navigate('/');
    },
    onError: (error) => {
      console.error('Logout failed:', error);
    },
  });

  const handleLogOut = () => {
    const confirmLogout = window.confirm('로그아웃 하시겠습니까?');
    if (confirmLogout) {
      handleLogoutMutation.mutate();
    }
  };
  return (
    <Container>
      <NavBtnDiv onMouseLeave={() => setIsClick(false)} isClicked={isClicked}>
        {/* 마우스 호버시 나타나는 메뉴바 */}
        {isClicked ? (
          <>
            <S_FiHome onClick={() => navigate('/')} />
            <S_FiUser onClick={() => navigate('/profile')} />
            <S_TbCarouselHorizontal onClick={() => navigate('/theme')} />
            {/* jwtToken 토큰 유무 분기 */}
            {!accessToken ? (
              <S_TbLogin onClick={() => setIsLogInClicked(true)} />
            ) : (
              <S_TbLogout onClick={handleLogOut} />
            )}
          </>
        ) : (
          <S_FiAlignJustify onClick={() => setIsClick(true)} />
        )}
      </NavBtnDiv>
    </Container>
  );
};

export default Nav;

// 스타일드 컴포넌트 정의
const Container = styled.div`
  z-index: 99;
`;

const NavBtnDiv = styled.div<{ isClicked: boolean }>`
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
  width: 70px;
  cursor: pointer;
  border-radius: 0 0 0 10px;
  transition: transform 0.5s, box-shadow 2s, height 0.5s;
  background-color: ${(props) =>
    props.isClicked && 'rgba(255, 255, 255, 0.5);'};
  //transform related
`;

const S_FiAlignJustify = styled(FiAlignJustify)`
  width: 50px;
  height: auto;
  border-radius: 0 0 0 10px;
  padding: 10px 0px;
  color: white;
  opacity: 0.8;
  //살짝 둥근 배경효과
  transition: background-color 0.2s, padding 0.2s, opacity 0.2s;
  &:hover {
    padding: 10px;
    opacity: 1;
    background-color: rgba(255, 255, 255, 0.8);
  }
  //클릭 시 색상변화
  &:active {
    background-color: #bbddff;
    transition: background-color 0.05s, padding 0.05s;
  }
`;

const S_FiHome = styled(FiHome)`
  width: 30px;
  height: auto;
  border-radius: 0 0 0 10px;
  padding: 20px 0px;
  color: white;
  //살짝 둥근 배경효과
  transition: background-color 0.2s, padding 0.2s;
  &:hover {
    padding: 20px;
    background-color: #dbdbdb;
  }
  //클릭 시 색상변화
  &:active {
    background-color: #bbddff;
    transition: background-color 0.05s, padding 0.05s;
  }
`;

const S_FiUser = styled(FiUser)`
  width: 30px;
  height: auto;
  border-radius: 0 0 0 10px;
  padding: 20px 0px;
  color: white;
  //살짝 둥근 배경효과
  transition: background-color 0.2s, padding 0.2s;
  &:hover {
    padding: 20px;
    background-color: #dbdbdb;
  }
  //클릭 시 색상변화
  &:active {
    background-color: #bbddff;
    transition: background-color 0.05s, padding 0.05s;
  }
`;

const S_TbCarouselHorizontal = styled(TbCarouselHorizontal)`
  width: 30px;
  height: auto;
  border-radius: 0 0 0 10px;
  padding: 20px 0px;
  color: white;
  //살짝 둥근 배경효과
  transition: background-color 0.2s, padding 0.2s;
  &:hover {
    padding: 20px;
    background-color: #dbdbdb;
  }
  //클릭 시 색상변화
  &:active {
    background-color: #bbddff;
    transition: background-color 0.05s, padding 0.05s;
  }
`;

const S_TbLogin = styled(TbLogin)`
  width: 30px;
  height: auto;
  border-radius: 0 0 0 10px;
  padding: 20px 0px;
  color: white;
  //살짝 둥근 배경효과
  transition: background-color 0.2s, padding 0.2s;
  &:hover {
    padding: 20px;
    background-color: #dbdbdb;
  }
  //클릭 시 색상변화
  &:active {
    background-color: #bbddff;
    transition: background-color 0.05s, padding 0.05s;
  }
`;

const S_TbLogout = styled(TbLogout)`
  width: 30px;
  height: auto;
  border-radius: 0 0 0 10px;
  padding: 20px 0px;
  color: white;
  //살짝 둥근 배경효과
  transition: background-color 0.2s, padding 0.2s;
  &:hover {
    padding: 20px;
    background-color: #dbdbdb;
  }
  //클릭 시 색상변화
  &:active {
    background-color: #bbddff;
    transition: background-color 0.05s, padding 0.05s;
  }
`;

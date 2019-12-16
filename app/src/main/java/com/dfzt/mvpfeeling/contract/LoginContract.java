package com.dfzt.mvpfeeling.contract;

import com.dfzt.mvpfeeling.model.entity.LoginBean;

public class LoginContract {
    public interface LoginView {
        String getUserName();
        String getPwd();
        void loginSuccess(LoginBean loginBean); // 登录成功，展示数据
        void loginFail(String failMsg);
    }
    public interface LoginPresenter {
        void login(String name, String pwd); // 业务逻辑
    }
}

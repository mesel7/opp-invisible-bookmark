package com.myapp;

import com.myapp.view.MainView;
import com.myapp.viewmodel.BookViewModel;

public class App {
    public static void main(String[] args) {
        BookViewModel bookViewModel = new BookViewModel();
        MainView mainView = new MainView(bookViewModel);
        mainView.setVisible(true);
    }
}
package a3lena.a3shak.com.menu_library.internal.view;

public interface View_HasStateListenerSupport {
    void addOnAttachStateChangeListener(View_OnAttachStateChangeListener listener);
    void removeOnAttachStateChangeListener(View_OnAttachStateChangeListener listener);
}

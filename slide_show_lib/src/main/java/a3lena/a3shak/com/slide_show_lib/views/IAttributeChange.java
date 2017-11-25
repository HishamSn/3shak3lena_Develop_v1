package a3lena.a3shak.com.slide_show_lib.views;

/**
 * @author S.Shahini
 * @since 12/11/16
 */

public interface IAttributeChange {
    void onIndicatorSizeChange();
    void onSelectedSlideIndicatorChange();
    void onUnselectedSlideIndicatorChange();
    void onDefaultIndicatorsChange();
    void onAnimateIndicatorsChange();
    void onIntervalChange();
    void onLoopSlidesChange();
    void onDefaultBannerChange();
    void onEmptyViewChange();
    void onHideIndicatorsValueChanged();
}

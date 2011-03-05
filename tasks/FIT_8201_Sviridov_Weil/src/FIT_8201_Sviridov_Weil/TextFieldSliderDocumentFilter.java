package FIT_8201_Sviridov_Weil;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Class for JEditField -> JSlider interaction filtering non-numeric input.
 * Adding <code>ChangeListners</code> to <code>JSlider</code> is necessary.
 * 
 * @author alstein
 */
class TextFieldSliderDocumentFilter extends DocumentFilter {

    final JTextField _textfield;
    final JSlider _slider;
    final int _slider_max;
    final int _slider_min;

    /**
     * Constructor which creates <code>DocumentFilter</code> to keep track of
     * given <code>JTextEdit</code> and <code>JSlider</code>
     *
     * @param textfield
     * @param slider
     */
    public TextFieldSliderDocumentFilter(JTextField textfield, JSlider slider) {

        _textfield = textfield;
        _slider = slider;
        _slider_max = slider.getMaximum();
        _slider_min = slider.getMinimum();

        _textfield.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                if (_textfield.getText().length() == 0) {
                    _textfield.setText(Integer.toString(_slider.getValue()));
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });

        _slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                _textfield.setText(Integer.toString(_slider.getValue()));
            }
        });
    }

    /**
     * Method is called on JTextField alteration with removing text. Checks if
     * changes are valid and if not - ignores them
     */
    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {

        String textfield_text = _textfield.getText();
        String result = textfield_text.substring(0, offset)
                + textfield_text.substring(offset + length);
        super.remove(fb, offset, length);

        try {

            Integer value = Integer.parseInt(result);

            if (value != 0) {
                _slider.setValue(value);
            } else {
                _slider.setValue(_slider_min);
            }

        } catch (NumberFormatException ex) {
            return;
        }
    }

    /**
     * Method called on JTextField alteration with typing. Checks if changes are
     * valid and if not - ignores them
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws BadLocationException {

        String textfield_text = _textfield.getText();
        String result = textfield_text.substring(0, offset)
                + text
                + textfield_text.substring(offset + length,
                textfield_text.length());

        try {
            Integer value = Integer.parseInt(result);
            if (value > _slider_max) {
                _slider.setValue(_slider_max);
                return;
            }

            if (value < _slider_min) {
                _slider.setValue(_slider_min);
                return;
            }

            super.replace(fb, offset, length, text, attrs);
            _slider.setValue(value);
        } catch (NumberFormatException ex) {
            return;
        }
    }
}

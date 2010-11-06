import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QPushButton;





public class Dummy
{
    public static void main(String args[])
    {
        QApplication.initialize(args);

        QPushButton hello = new QPushButton("Mi Piacciono i treni");
        hello.resize(120, 40);
        hello.setWindowTitle("WRUUMMMMMM");
        hello.show();

        QApplication.exec();
    }
}
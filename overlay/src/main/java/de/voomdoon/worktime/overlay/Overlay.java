package de.voomdoon.worktime.overlay;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

import de.voomdoon.logging.LogManager;
import de.voomdoon.logging.Logger;
import de.voomdoon.worktime.adapter.file.observer.RawListener;
import de.voomdoon.worktime.adapter.file.observer.RawObserver;
import de.voomdoon.worktime.model.RawDay;
import de.voomdoon.worktime.model.RawSection;
import de.voomdoon.worktime.model.RawWork;
import de.voomdoon.worktime.util.RawCalculator;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class Overlay {

	/**
	 * DOCME add JavaDoc for Overlay
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private class Listener implements RawListener {

		/**
		 * @since 0.1.0
		 */
		@Override
		public void notifySectionEnded(RawSection section, RawDay day, RawWork work) {
			logger.info("notifySectionEnded " + section + "\nwork: " + work);
			workReference.set(work);
		}

		/**
		 * @since 0.1.0
		 */
		@Override
		public void notifySectionStarted(RawSection section, RawDay day, RawWork work) {
			logger.info("notifySectionStarted " + section + "\nwork: " + work);
			workReference.set(work);
		}
	}

	/**
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private class MyPanel extends JPanel implements ActionListener {

		/**
		 * @since 0.1.0
		 */
		private static final long serialVersionUID = -6015243735868896543L;

		/**
		 * @since 0.1.0
		 */
		private RawCalculator calculator = new RawCalculator();

		/**
		 * @since 0.1.0
		 */
		private Timer timer;

		/**
		 * @since 0.1.0
		 */
		public MyPanel() {
			setOpaque(false);
			timer = new Timer(1000, this);
			timer.start();
		}

		/**
		 * @since 0.1.0
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// try {
			// observer.run();
			// } catch (Exception exception) {
			// logger.error("observer: " + exception.getMessage(), exception);
			// }

			repaint();
		}

		/**
		 * @since 0.1.0
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.clearRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(255, 0, 0, 64));
			int fintSize = 32;
			g.setFont(new Font("Arial", Font.BOLD, fintSize));
			int seconds = calculator.getDaySum(workReference.get(), LocalDate.now(), LocalTime.now());
			Duration duration = Duration.ofSeconds(seconds);
			String value = String.format("%02d:%02d", duration.toHours(), duration.toMinutesPart());
			g.drawString("today: " + value, 0, fintSize);
		}
	}

	/**
	 * @since 0.1.0
	 */
	private Listener listener;

	/**
	 * @since 0.1.0
	 */
	private Logger logger = LogManager.getLogger(getClass());

	/**
	 * @since 0.1.0
	 */
	private RawObserver observer;

	/**
	 * @since 0.1.0
	 */
	private AtomicReference<RawWork> workReference = new AtomicReference<>();

	/**
	 * DOCME add JavaDoc for constructor Overlay
	 * 
	 * @param observer
	 * @since 0.1.0
	 */
	public Overlay(RawObserver observer) {
		this.observer = observer;

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JWindow window = new JWindow();
				window.add(new MyPanel());
				window.pack();
				window.setLocationRelativeTo(null);
				window.setBackground(new Color(0, 0, 0, 0));
				window.setAlwaysOnTop(true);
				window.setBounds(window.getGraphicsConfiguration().getBounds());
				window.setVisible(true);

				makeWindowClickThroughOnWindows(window);
			}
		});
	}

	/**
	 * DOCME add JavaDoc for method start
	 * 
	 * @since 0.1.0
	 */
	public void start() {
		listener = new Listener();
		RawWork work = observer.register(listener, 1000);
		logger.debug("work: " + work);
		workReference.set(work);
	}

	/**
	 * DOCME add JavaDoc for method stop
	 * 
	 * @since 0.1.0
	 */
	public void stop() {
		// TODO implement stop
	}

	/**
	 * DOCME add JavaDoc for method makeWindowClickThroughOnWindows
	 * 
	 * @param window
	 * @since 0.1.0
	 */
	private void makeWindowClickThroughOnWindows(JWindow window) {
		HWND hwnd = new HWND();
		hwnd.setPointer(Native.getWindowPointer(window));

		int wl = User32.INSTANCE.GetWindowLong(hwnd, User32.GWL_EXSTYLE);
		wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
		User32.INSTANCE.SetWindowLong(hwnd, User32.GWL_EXSTYLE, wl);
	}
}

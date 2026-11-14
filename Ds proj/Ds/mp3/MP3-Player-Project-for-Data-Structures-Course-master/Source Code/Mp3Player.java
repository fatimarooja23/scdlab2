import javazoom.jl.player.advanced.*;
import java.io.FileInputStream;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mp3Player extends JPanel implements ActionListener {
    static JButton play, pause, next;
    JLabel songname, songlist;
    PlayThread runnable;
    Thread t;
    static List songList;
    static int selectedSong;

    public Mp3Player() {
        selectedSong = 0;
        setPreferredSize(new Dimension(300, 250));
        play = new JButton("Play");
        play.setActionCommand("play");
        next = new JButton("Next");
        next.setActionCommand("next");
        pause = new JButton("Stop");
        pause.setActionCommand("pause");
        pause.setEnabled(false);

        play.addActionListener(this);
        pause.addActionListener(this);
        next.addActionListener(this);

        songname = new JLabel(songList.get(selectedSong));
        songname.setPreferredSize(new Dimension(200, 30));
        songlist = new JLabel(songList.toHTMLString());
        songlist.setPreferredSize(new Dimension(240, 180));

        add(new JLabel("Song Name:"));
        add(songname);
        add(new JLabel(""));
        add(play);
        add(pause);
        add(next);
        add(songlist);
    }

    public void actionPerformed(ActionEvent e) {
        if ("play".equals(e.getActionCommand())) {
            if (runnable == null || !runnable.isPlaying()) {
                runnable = new PlayThread(songList.get(selectedSong));
                t = new Thread(runnable);
                t.start();
            }
            play.setEnabled(false);
            pause.setEnabled(true);
        }

        if ("pause".equals(e.getActionCommand())) {
            if (runnable != null && runnable.isPlaying()) {
                runnable.stop();
            }
            pause.setEnabled(false);
            play.setEnabled(true);
        }

        if ("next".equals(e.getActionCommand())) {
            if (runnable != null && runnable.isPlaying()) {
                runnable.stop();
            }
            selectedSong++;
            selectedSong = selectedSong % songList.size();
            String nextsong = songList.get(selectedSong);
            songname.setText(nextsong);

            runnable = new PlayThread(songList.get(selectedSong));
            t = new Thread(runnable);
            t.start();
            play.setEnabled(false);
            pause.setEnabled(true);
        }
    }

    public static void doneSong() {
        pause.setEnabled(false);
        play.setEnabled(true);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MP3 Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Mp3Player newContentPane = new Mp3Player();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        songList = new List();
        songList.insertItem("barbie_girl.mp3");
        songList.insertItem("call_me_maybe.mp3");
        songList.insertItem("chandelier.mp3");
        songList.insertItem("mmmbop.mp3", 1);
        songList.insertItem("macarena.mp3", 0);
        songList.insertItem("who_let_the_dogs_out.mp3", 1);
        songList.removeItem("mmmbop.mp3");
        songList.insertItem("mmmbop.mp3");

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
}

class PlayThread implements Runnable {
    private String song;
    private AdvancedPlayer player;
    private boolean playing;

    public PlayThread(String song) {
        this.song = song;
        this.playing = false;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void stop() {
        if (player != null) {
            player.close();
            playing = false;
        }
    }

    public void run() {
        try {
            FileInputStream fis = new FileInputStream(
                    "C:\\Users\\Dell\\OneDrive\\Desktop\\Ds\\mp3\\MP3-Player-Project-for-Data-Structures-Course-master\\Necessary Input Files\\" + song
            );
            player = new AdvancedPlayer(fis);
            playing = true;
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            playing = false;
            Mp3Player.doneSong();
        }
    }
}

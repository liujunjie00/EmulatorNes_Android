package com.ritchie.myapplicationmove.sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class SoundMusic {
    private int format = AudioFormat.CHANNEL_OUT_STEREO;
    //private int format = AudioFormat.CHANNEL_IN_MONO;
    private int encoding = AudioFormat.ENCODING_PCM_16BIT;
    private int minSize ;
    private AudioTrack audioTrack;
    private int sampleRateInHz = 22050;
    //private int sampleRateInHz = 44100;
    private Thread musicThread;

    public SoundMusic() {
    }

   /* int format = sfx.isStereo ? AudioFormat.CHANNEL_OUT_STEREO : AudioFormat.CHANNEL_OUT_MONO;
    int encoding = sfx.encoding == SfxProfile.SoundEncoding.PCM8 ?
            AudioFormat.ENCODING_PCM_8BIT : AudioFormat.ENCODING_PCM_16BIT;

            sfx.rate 22050 minSize 10320
    minSize = AudioTrack.getMinBufferSize(sfx.rate, format, encoding);
    track = new AudioTrack(AudioManager.STREAM_MUSIC, sfx.rate, format,
                           encoding, minSize, AudioTrack.MODE_STREAM);*/

    public boolean initSound(){
        if (audioTrack == null){
            minSize = AudioTrack.getMinBufferSize(sampleRateInHz,format,encoding);
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRateInHz,format,encoding,minSize,AudioTrack.MODE_STREAM);
            return true;
        }
        return false;

    }
    public boolean starMusic(short[] bytes,Thread thread,int lenth){
        musicThread = thread;
        audioTrack.flush();
        Log.d("status", "starMusic: "+audioTrack.getState());
        if (lenth > minSize){
            audioTrack.write(bytes,5,minSize);
        }else {
            audioTrack.write(bytes,5,lenth);
        }
        return true;
    }


    public void play() {
        audioTrack.play();
    }

    private void releaseAudioTrack() {
        if (audioTrack != null && audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
            audioTrack.stop();
            audioTrack.release();

        }
        if (musicThread != null) {
           // musicThread.interrupt();
        }
    }

    private void pausePlay() {
        if (audioTrack != null) {
            if (audioTrack.getState() > AudioTrack.STATE_UNINITIALIZED) {
                audioTrack.pause();
            }

        }
        if (musicThread != null) {
            //musicThread.interrupt();
        }
    }

}

package com.ccs.app.musicplayer.model;

import android.arch.lifecycle.MutableLiveData;

import com.ccs.app.musicplayer.model.base.BaseListModel;
import com.ccs.app.musicplayer.model.item.SongItem;

public class PlayerModel extends BaseListModel<SongItem> {

    private MutableLiveData<String> playlistName;

    public MutableLiveData<String> getPlaylistName() {
        if(playlistName == null) playlistName = new MutableLiveData<>();

        return playlistName;
    }

    private MutableLiveData<Integer> currentIndex;

    public MutableLiveData<Integer> getCurrentIndex() {
        if(currentIndex == null) currentIndex = new MutableLiveData<>();

        return currentIndex;
    }

    private MutableLiveData<SongItem> currentSong;

    public MutableLiveData<SongItem> getCurrentSong() {
        if(currentSong == null) currentSong = new MutableLiveData<>();

        return currentSong;
    }

    private MutableLiveData<Integer> duration;

    public MutableLiveData<Integer> getDuration() {
        if(duration == null) duration = new MutableLiveData<>();

        return duration;
    }

    private MutableLiveData<Integer> currentPosition;

    public MutableLiveData<Integer> getCurrentPosition() {
        if(currentPosition == null) currentPosition = new MutableLiveData<>();

        return currentPosition;
    }

    private MutableLiveData<Boolean> playing;

    public MutableLiveData<Boolean> getPlaying() {
        if(playing == null) playing = new MutableLiveData<>();

        return playing;
    }

    private MutableLiveData<Boolean> shuffle;

    public MutableLiveData<Boolean> getShuffle() {
        if(shuffle == null) shuffle = new MutableLiveData<>();

        return shuffle;
    }

    private MutableLiveData<Integer> repeat;

    public MutableLiveData<Integer> getRepeat() {
        if(repeat == null) repeat = new MutableLiveData<>();

        return repeat;
    }

}
